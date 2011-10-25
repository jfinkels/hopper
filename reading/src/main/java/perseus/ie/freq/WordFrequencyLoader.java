package perseus.ie.freq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.Document;
import perseus.document.Query;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.Token;
import perseus.ie.entity.Entity;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.Lemmatizer;
import perseus.util.HibernateUtil;

/**
 * Loads word frequencies for a document or corpus. Currently does <em>not</em>
 * load forms--only lemmas..
 */
@SuppressWarnings("serial")
public class WordFrequencyLoader extends RecentlyModifiedCorpusProcessor {
	private Logger logger = Logger.getLogger(getClass());
	private Chunk currentChunk;
	private Semaphore freqSem;
	private Language docLanguage;
	//even though we only have one type of TokenStrategy - lemma - keep this map so if form  is
	//ever used, there will be less to change to the code
	private Map<TokenStrategy,Map<String,ChunkFrequency>> chunkFrequencies;

	private Map<Entity,EntityDocumentFrequency> lemmaFrequencies =
		new HashMap<Entity,EntityDocumentFrequency>();

		private enum TokenStrategy {
			/*
	FORM {
	    protected List<? extends ChunkFrequency> doProcessing(Token token) {
		ChunkFrequency freq = new FormChunkFrequency(
			token.getOriginalText(), token.getLanguage());
		freq.count();
		return Collections.singletonList(freq);
	    }
	},
			 */
			LEMMA {
				protected List<? extends ChunkFrequency> doProcessing(Token token) {
					if (!token.getLanguage().getHasMorphData()) {
						return Collections.emptyList();
					}
					List<Lemma> lemmas = Lemmatizer.getLemmas(
							token.getOriginalText(),
							token.getLanguage());

					List<ChunkFrequency> freqs = new ArrayList<ChunkFrequency>();
					for (Lemma lemma : lemmas) {
						EntityTuple tuple = new EntityTuple(lemma);
						tuple.count(lemmas.size());
						freqs.add(tuple);
					}

					return freqs;
				}
			};

			protected abstract List<? extends ChunkFrequency> doProcessing(
					Token token);
		}

		public WordFrequencyLoader() {
			this(null);
		}

		public WordFrequencyLoader(Semaphore freqSem) {
			super();
			setOption(SUBDOC_METHOD, ONE_DOC);
			if (freqSem == null) {
				this.freqSem = new Semaphore(1);
			} else {
				this.freqSem = freqSem;
			}
			chunkFrequencies = new HashMap<TokenStrategy,Map<String,ChunkFrequency>>();
		}
		
		public String getTaskName() {
			return "word-frequencies";
		}

		public void startDocument(Chunk documentChunk) {
			docLanguage = documentChunk.getMetadata().getLanguage();
			lemmaFrequencies.clear();
			HibernateFrequencyDAO dao = new HibernateFrequencyDAO(DocumentFrequency.class);
			try {
				freqSem.acquire();
			} catch (InterruptedException e) {
				logger.trace("Thread interrupted");
			}
			dao.deleteByDocument(documentChunk.getDocumentID(), getClasses());
			freqSem.release();
		}

		private void updateDocumentFrequencies(Chunk chunk, EntityTuple frequency) {
			Entity entity = frequency.getEntity();

			if (!lemmaFrequencies.containsKey(entity)) {
				lemmaFrequencies.put(entity, new EntityDocumentFrequency(entity, chunk.getDocumentID()));
			}

			lemmaFrequencies.get(entity).add(frequency);
		}

		public void endDocument(Chunk documentChunk) {
			String docID = documentChunk.getDocumentID();

			HibernateFrequencyDAO dao = new HibernateFrequencyDAO(DocumentFrequency.class);
			logger.info("Saving counts for " + docID);

			int totalLemmas = lemmaFrequencies.values().size();
			for (EntityDocumentFrequency edf : lemmaFrequencies.values()) {
				int maxFreq = edf.getMaxFrequency();
				double termFreq = ((double) maxFreq) / totalLemmas;
				edf.setTermFreq(termFreq);

				/*
				 * otherwise term freq is:
				 * 1 + (Math.log(maxFreq) / Math.log(2))
				 */
			}

			try {
				freqSem.acquire();
			} catch (InterruptedException e) {
				logger.trace("Thread interrupted");
			}
			dao.beginTransaction();

			dao.updateDocumentFrequencies(
					new ArrayList<DocumentFrequency>(lemmaFrequencies.values()),
					documentChunk.getDocumentID(),
					getClasses());
			
			// This clears the session's persistence context, getting rid of
			// stray objects (like Parses) that may be hanging around--if we
			// don't do this while processing an entire corpus, we'll eventually
			// run out of memory
			dao.endTransaction();
			HibernateUtil.closeSession();
			freqSem.release();
			
			super.endDocument(documentChunk);
		}

		public void startChunk(Chunk documentChunk) {
			currentChunk = documentChunk;
			chunkFrequencies.clear();
		}

		public void endChunk(Chunk documentChunk) {
			logger.debug("Inserting new freqs: " + documentChunk);
			HibernateFrequencyDAO dao = new HibernateFrequencyDAO(EntityTuple.class);
			List<ChunkFrequency> chunkFreqs = new ArrayList<ChunkFrequency>();

			for (TokenStrategy strategy : chunkFrequencies.keySet()) {
				Collection<ChunkFrequency> freqValues = chunkFrequencies.get(strategy).values();
				chunkFreqs.addAll(freqValues);
				int totalLemmas = freqValues.size();
				for (ChunkFrequency freq : freqValues) {
					int maxFreq = freq.getMaxFrequency();
					double termFreq = ((double) maxFreq) / totalLemmas;
					freq.setTermFreq(termFreq);
					/*
					 * otherwise term freq is:
					 * 1 + (Math.log(maxFreq) / Math.log(2))
					 */
					// don't aggregate forms for now
					if (EntityTuple.class.isAssignableFrom(freq.getClass())) {
						updateDocumentFrequencies(documentChunk, (EntityTuple) freq);
					}
				}
			}

			try {
				freqSem.acquire();
			} catch (InterruptedException e) {
				logger.trace("Thread interrupted");
			}
			dao.beginTransaction();
			dao.updateChunkFrequencies(chunkFreqs, documentChunk, getClasses());
			dao.endTransaction();
			freqSem.release();
		}

		private List<Class<? extends Entity>> getClasses() {
			List<Class<? extends Entity>> classes =
				new ArrayList<Class<? extends Entity>>();
			classes.add(Lemma.class);
			return classes;
		}

		public void processToken(Token token) {
			if (token.getType() != Token.Type.WORD) return;
			//only process words that are of the same language as the document
			if (token.getLanguage() != docLanguage) return;

			for (TokenStrategy strategy : TokenStrategy.values()) {				
				List<? extends ChunkFrequency> results = strategy.doProcessing(token);
				
				Map<String,ChunkFrequency> chunkFreqs;
				if (chunkFrequencies.containsKey(strategy)) {
					chunkFreqs = chunkFrequencies.get(strategy);
				} else {
					chunkFreqs = new TreeMap<String,ChunkFrequency>();
				}
				
				for (ChunkFrequency freq : results) {
					freq.setChunk(currentChunk);

					String form = freq.getForm();
					if (chunkFreqs.containsKey(form)) {
						chunkFreqs.get(form).add(freq);
					} else {
						chunkFreqs.put(form, freq);
					}
				}
				
				chunkFrequencies.put(strategy, chunkFreqs);
			}
		}

		public boolean shouldProcessDocument(Chunk documentChunk) {
			return (super.shouldProcessDocument(documentChunk) &&
					documentChunk.getMetadata().getLanguage().getHasMorphData());
		}

		public static void main(String[] args) {
			WordFrequencyLoader wfl = new WordFrequencyLoader();

			String[] effectiveArgs = args;
			Options options = new Options()
			.addOption("f", "force", false, "force loading, even if file unchanged")
			.addOption(OptionBuilder.withLongOpt("threads")
					.withDescription("set number of threads to use")
					.hasArg()
					.withArgName("NUMBER")
					.create());

			CommandLineParser parser = new PosixParser();
			boolean hasForce = false;
			//use the minimum # of cpu's available to this machine, but allow user to change #
			int THREADS = Runtime.getRuntime().availableProcessors();

			try {
				CommandLine cl = parser.parse(options, args);

				if (cl.hasOption("force") || cl.hasOption("f")) {
					wfl.setIgnoreModificationDate(true);
					hasForce = true;
				}
				if (cl.hasOption("threads")) {
					String option = cl.getOptionValue("threads");
					if (option != null && !option.equals("")) {
						THREADS = Integer.parseInt(option);
					}
				}
				effectiveArgs = cl.getArgs();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (effectiveArgs.length > 0) {
				for (String arg : effectiveArgs) {
					wfl.processAnything(arg);
				}
			} else {
				ExecutorService exec = Executors.newFixedThreadPool(THREADS);
				List<String> documentIDs = Document.getTexts();
				Collections.shuffle(documentIDs);
				Semaphore freqSem = new Semaphore(1);
				for (String documentID : documentIDs) {
					Query documentQuery = new Query(documentID);
					WordFrequencyLoader loader = new WordFrequencyLoader(freqSem);
					loader.setIgnoreModificationDate(hasForce);
					loader.setQuery(documentQuery);
					exec.execute(loader);
				}
				exec.shutdown();
			}
		}
}
