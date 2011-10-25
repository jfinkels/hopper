/**
 * Provides access to all available "resources" for a given chunk of text.
 * A "resource" here denotes other chunks that have some relation to the
 * chunk in question: currently these include translations, commentaries,
 * summaries and commentaries-on-summaries. Each type is specified by an
 * instance of the enum class <kbd>ResourceType</kbd>, each of which contains
 * methods for accessing and formatting the type of resource it represents.
 * <p>
 * This class cannot itself be instantiated; the only method you'll probably
 * ever want to call is the static method <kbd>getResources()</kbd>, which
 * returns a <kbd>Map</kbd> containing all known ResourceTypes (as keys) and
 * all matching resources for each type, as a set of chunks. A version of
 * <kbd>getResources()</kbd> that takes a ResourceType as a parameter and
 * returns a Set of matching chunks is also provided.
 */

package perseus.document;

import static perseus.document.Metadata.ABO_KEY;
import static perseus.document.Metadata.ARITY_KEY;
import static perseus.document.Metadata.CONTRIBUTOR_KEY;
import static perseus.document.Metadata.CREATOR_KEY;
import static perseus.document.Metadata.DATE_COPYRIGHTED_KEY;
import static perseus.document.Metadata.INTRODUCTION_KEY;
import static perseus.document.Metadata.LANGUAGE_KEY;
import static perseus.document.Metadata.SUMMARY_COMMENTARY_KEY;
import static perseus.document.Metadata.SUMMARY_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateTableOfContentsDAO;
import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.language.Language;
import perseus.util.StringUtil;

public class Resource {

	private static Logger logger = Logger.getLogger(Resource.class);

	/**
	 * An enumerated type representing the different available categories of
	 * resources. Each instance includes various methods that determine what
	 * resources of said category are available for a given query, among other
	 * things.
	 */
	public enum ResourceType {
		TRANSLATION {
			protected String title(Chunk chunk) {
				return Language.forCode(chunk.getEffectiveLanguage()).getName();
			}

			protected String[] keysToFormat(Metadata metadata) {		
				return new String[] { CONTRIBUTOR_KEY, DATE_COPYRIGHTED_KEY };
			}

			protected Set<Query> getQueries(Query textQuery) {
				String abo;
				if (textQuery.getMetadata().has(SUMMARY_KEY)) {
					abo = textQuery.getMetadata().get(SUMMARY_KEY);
				} else {		    
					abo = textQuery.getMetadata().get(ABO_KEY);
				}

				if (abo == null) return Collections.emptySet();

				MetadataDAO dao = new SQLMetadataDAO();

				Set<Query> queries = new HashSet<Query>();
				List<Query> documents = dao.getDocuments(Metadata.ABO_KEY, null, abo);
				for (Query document : documents) {
					if (!document.getInnermostDocumentID().equals(
							textQuery.getInnermostDocumentID())) {
						queries.add(
								document.appendSubquery(textQuery.getNonABOQuery()));
					}
				}

				return queries;
			}	    
		},

		COMMENTARY {
			protected String title(Chunk chunk) {
				return "Notes";
			}

			protected String[] keysToFormat(Metadata metadata) {
				return new String[] {
						metadata.has(CONTRIBUTOR_KEY) ? CONTRIBUTOR_KEY :
							(metadata.has(CREATOR_KEY) &&
									"secondary".equals(metadata.get(ARITY_KEY)) ?
											CREATOR_KEY : null),

											DATE_COPYRIGHTED_KEY
				};
			}

			protected Set<Query> getQueries(Query textQuery) {
				Set<Commentary> documentCommentaries =
					Commentary.getCommentariesFor(textQuery);

				Set<Query> output = new HashSet<Query>();
				for (Commentary commentary : documentCommentaries) {
					Query commentaryQuery = commentary.transformTextQuery(textQuery);
					output.add(commentaryQuery);
				}

				return output;
			}

			public Set<Chunk> getChunks(Chunk textChunk) {
				if (textChunk.getMetadata().has(Metadata.SUMMARY_KEY)) {
					// If the user is looking at a summary, don't provide
					// notes, which will only refer to chunks somewhere inside
					// the summary
					return Collections.emptySet();
				}

				return super.getChunks(textChunk);
			}
		},

		SUMMARY {
			protected String title(Chunk chunk) {
				return "Summary";
			}

			protected String[] keysToFormat(Metadata metadata) {
				return new String[] {
						LANGUAGE_KEY, CONTRIBUTOR_KEY, DATE_COPYRIGHTED_KEY
				};
			}

			protected Set<Query> getQueries(Query textQuery) {
				String targetDoc = textQuery.getMetadata().get(ABO_KEY);
				if (targetDoc == null) return Collections.emptySet();

				Set<Query> documents = new TreeSet<Query>();

				MetadataDAO dao = new SQLMetadataDAO();
				List<Query> documentsFound =
					dao.getDocuments(SUMMARY_KEY, null, targetDoc);

				for (Query doc : documentsFound) {
					documents.add(doc);
					//documents.add(doc.appendSubquery(textQuery.getNonABOQuery()));
				}
				return documents;
			}

			public Chunk resolveQuery(Query query, Chunk originalChunk)
			throws InvalidQueryException {
				while (query.isWholeText()) {
					try {
						Chunk chunk = query.getChunk();
						return chunk;
					} catch (InvalidQueryException iqe) {
						query = query.getContainingQuery();
					}
				}
				throw new InvalidQueryException(query);
			}
		},

		SUMMARY_COMMENTARY {
			protected String title(Chunk chunk) {
				return "Summary Notes";
			}

			protected String[] keysToFormat(Metadata metadata) {
				return new String[] {
						LANGUAGE_KEY, CONTRIBUTOR_KEY, DATE_COPYRIGHTED_KEY
				};
			}

			protected Set<Query> getQueries(Query textQuery) {
				String targetDoc = textQuery.getMetadata().get(ABO_KEY);
				if (targetDoc == null) return Collections.emptySet();

				Set<Query> documents = new TreeSet<Query>();

				MetadataDAO dao = new SQLMetadataDAO();
				List<Query> documentsFound =
					dao.getDocuments(SUMMARY_COMMENTARY_KEY, null, targetDoc);

				for (Query doc : documentsFound) {
					documents.add(doc.appendSubquery(textQuery.getNonABOQuery()));
				}
				return documents;
			}

			public Chunk resolveQuery(Query query, Chunk originalChunk)
			throws InvalidQueryException {
				while (!query.isWholeText()) {
					try {
						Chunk chunk = query.getChunk();

						return chunk;
					} catch (InvalidQueryException iqe) {
						query = query.getContainingQuery();
					}
				}

				throw new InvalidQueryException(query);
			}
		},
		
		INTRODUCTION {
			@Override
			protected Set<Query> getQueries(Query textQuery) {
				String targetDoc = textQuery.getMetadata().get(ABO_KEY);
				if (targetDoc == null) return Collections.emptySet();

				Set<Query> documents = new TreeSet<Query>();

				MetadataDAO dao = new SQLMetadataDAO();
				List<Query> documentsFound =
					dao.getDocuments(INTRODUCTION_KEY, null, targetDoc);

				for (Query doc : documentsFound) {
					logger.info("doc is "+doc);
					documents.add(doc);
				}
				return documents;
			}

			@Override
			protected String[] keysToFormat(Metadata metadata) {
				return new String[] { CONTRIBUTOR_KEY, DATE_COPYRIGHTED_KEY };
			}

			@Override
			protected String title(Chunk chunk) {
				return "Introduction";
			}
			
			public Chunk resolveQuery(Query query, Chunk originalChunk)
			throws InvalidQueryException {
				while (query.isWholeText()) {
					try {
						Chunk chunk = query.getChunk();
						return chunk;
					} catch (InvalidQueryException iqe) {
						query = query.getContainingQuery();
					}
				}
				throw new InvalidQueryException(query);
			}
		};

		protected abstract String title(Chunk chunk);
		protected abstract String[] keysToFormat(Metadata metadata);

		public String format(Chunk chunk) {
			StringBuilder output = new StringBuilder();
			output.append(title(chunk));

			Metadata metadata = chunk.getMetadata();
			List<String> values = new ArrayList<String>();
			for (String key : keysToFormat(metadata)) {
				if (metadata.has(key)) {
					if (key.equals(LANGUAGE_KEY)) {
						values.add(Language.forCode(
								chunk.getEffectiveLanguage()).getName());
					} else {
						values.add(metadata.get(key));
					}
				}
			}

			if (!values.isEmpty()) {
				output.append(" (").append(StringUtil.join(values, ", ")).append(")");
			}
			return output.toString();
		}

		protected abstract Set<Query> getQueries(Query textQuery);

		public Chunk resolveQuery(Query query, Chunk originalChunk)
		throws InvalidQueryException {
			if (!originalChunk.getMetadata().has(SUMMARY_KEY)) {
				return new HibernateTableOfContentsDAO().getChunkByQuery(null, query);
			}

			// Special case: if we asked for a "translation" of a summary
			// of a book, don't fetch the entire book--fetch the first
			// chunk that it contains, which should be a chapter.
			ChunkDAO dao = new HibernateChunkDAO();
			Chunk matchingChunk = dao.getByQuery(query);
			if (matchingChunk != null) {
				return dao.getFirstContainedChunk(dao.getByQuery(query));
			}

			throw new InvalidQueryException(query);
		}

		public Set<Chunk> getChunks(Chunk textChunk) {
			Set<Chunk> output = new HashSet<Chunk>();

			for (Query query : getQueries(textChunk.getQuery())) {
				try {
					//		    Chunk chunk = tocDAO.getChunkByQuery(null, query);
					Chunk chunk = resolveQuery(query, textChunk);
					output.add(chunk);
				} catch (InvalidQueryException iqe) {
					// oh well; we lose
				}
			}

			// Make sure we don't have a copy of the original chunk...
			output.remove(textChunk);
			return output;
		}
	}

	private Resource() {}

	/**
	 * Returns an EnumMap containing every known resource for `chunk` and, for
	 * each resource, a set of chunks.
	 *
	 * @param chunk the chunk to return resources for
	 * @return a Map pointing resource-types to sets of chunks
	 */
	public static Map<ResourceType,Set<Chunk>> getResources(Chunk chunk) {
		Map<ResourceType,Set<Chunk>> chunks =
			new EnumMap<ResourceType,Set<Chunk>>(ResourceType.class);
		for (ResourceType type : ResourceType.values()) {
			chunks.put(type, type.getChunks(chunk));
		}

		return chunks;
	}

	/**
	 * Returns all known resources for `chunk` of type `type`.
	 *
	 * @param chunk the chunk to return resources for
	 * @param type the ResourceType to return
	 * @return a set of all resources of the given type for `chunk`
	 */
	public static Set<Chunk> getResources(Chunk chunk, ResourceType type) {
		return type.getChunks(chunk);
	}
}
