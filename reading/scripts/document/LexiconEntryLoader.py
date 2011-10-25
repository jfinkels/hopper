from java.util import HashSet

from perseus.document import Metadata, CorpusProcessor, Query
from perseus.morph import HibernateLemmaDAO, Lemma
from perseus.util import LanguageCode, HibernateUtil, Timekeeper

import sys.argv
import re
import time

class LexiconEntryLoader(CorpusProcessor):

    def __init__(self):
	self.dao = HibernateLemmaDAO()
	self.cur_lang = None
	self.entry_count = 0
	self.keeper = Timekeeper()

	self.headword_expr = re.compile("(.*?)(\d+)$")
	self.no_lengths_expr = re.compile("[_^]")

    def shouldProcessDocument(self, doc_chunk):
	meta = doc_chunk.metadata

	return meta.defaultChunk == "entry"

    def startDocument(self, doc_chunk):
	self.cur_lang = doc_chunk.metadata.get(Metadata.SUBJECT_LANGUAGE_KEY)
	self.cur_lang_id = LanguageCode.getLanguageID(self.cur_lang)
	self.entry_count = 0
	self.last_time = time.time()

    def endDocument(self, doc_chunk):
	print self.entry_count, "entries processed for document"
	self.dao.endTransaction()

    def processChunk(self, chunk):
	if chunk.type != "entry": return
	self.dao.beginTransaction()
	self.keeper.record("Beginning chunk %s" % chunk.value)
	
	headword = self.no_lengths_expr.sub("", chunk.value)
	seq_num = 1

	match = self.headword_expr.search(headword)
	if match != None:
	    headword = match.group(1)
	    seq_num = int(match.group(2))

	self.keeper.record("Split headword")
	
	matches = self.dao.getMatchingLemmas(headword, seq_num, self.cur_lang)
	self.keeper.record("Got matching lemmas")

	if len(matches) == 0:
	    print "CREATING:", headword
	    lemma = Lemma()
	    lemma.headword = headword
	    lemma.sequenceNumber = seq_num
	    lemma.languageID = self.cur_lang_id

	    lemma.shortDefinition = None # worry about this later
	    lemma.lexiconQueries = HashSet()

	    lemma.authorityName = "%s%d(%s)" % \
				    (headword, seq_num, self.cur_lang)
	    lemma.displayName = headword
	    lemma.sortableString = "%s%s" % (headword, seq_num)

	    self.dao.insertLemma(lemma)
	    matches.add(lemma)

	for m in matches:
	    m.getLexiconChunks().add(chunk)
	    self.keeper.record("Added chunk")
	    self.dao.updateLemma(m)
	    self.keeper.record("Updated lemma")

	self.entry_count += 1
	if self.entry_count % 30 == 0:
	    new_time = time.time()

	    print "[%6d] %s / %d / %s : %.5f" % \
		(self.entry_count, headword, seq_num, self.cur_lang, \
		    new_time - self.last_time),
	    self.last_time = new_time
	    print "->", ", ".join([l.headword for l in matches])

	    HibernateUtil.getSession().flush()
	    HibernateUtil.getSession().clear()
	    #self.dao.beginTransaction()
	    self.keeper.record("FLUSHED session")

	    #print self.keeper.getResults()
	    self.keeper.stop()
	    self.keeper = Timekeeper()

if __name__ == "__main__":
    loader = LexiconEntryLoader()
    if len(sys.argv) > 1:
	for doc in sys.argv[1:]:
	    loader.processDocument(doc)
    else:
	loader.processCorpus()
