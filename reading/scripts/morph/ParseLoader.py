import sys.argv
import re

from java.lang import StringBuilder

from perseus.morph import HibernateLemmaDAO, HibernateParseDAO, Parse
from perseus.util import HibernateUtil

from org.xml.sax import InputSource
from org.xml.sax.ext import DefaultHandler2
from org.xml.sax.helpers import XMLReaderFactory

#from java.util.zip import GZipInputStream
from java.io import FileInputStream

class ParseLoader:
    def load(self, filename, language):
	parser = XMLReaderFactory.createXMLReader()
	
	#if (filename.endsWith(".gz")):
	    #input_src = InputSource(GZipInputStream(FileInputStream(filename)))
	#else:
	input_src = InputSource(filename)
	
	parser.contentHandler = ParseHandler(language)
	parser.parse(input_src)

	HibernateUtil.commitTransaction()
	HibernateUtil.closeSession()

class ParseHandler(DefaultHandler2):
    def __init__(self, language):
	self.features = {}
	self.anal_count = 0
	self.language = language

	self.cur_feature = None
	self.cur_value = None

	self.lemma_dao = HibernateLemmaDAO()
	self.parse_dao = HibernateParseDAO()

	self.parse_dao.beginTransaction()
	print "Deleting existing parses in this language"
	self.parse_dao.deleteByLanguage(self.language)
	print "Parsing!"

	self.bare_form_expr = re.compile("[()\\/*=|+']")

	self.parse_count = 0

	self.lemma_cache = {}

    def startElement(self, ns_uri, sname, qname, attributes):
	if qname == "analysis":
	    self.features = {}
	    self.anal_count += 1
	elif qname == "analyses":
	    pass
	else:
	    self.cur_feature = qname.lower()
	    self.cur_value = ""
    
    def endElement(self, ns_uri, sname, qname):
	feats = self.features

	if qname == "analysis":
	    if not(feats.has_key("form") and feats.has_key("lemma")):
		pass # bad parse!
	    else:
		form = feats["form"]
		del feats["form"]

		headword = feats["lemma"].replace("-", "").replace("#", "")
		del feats["lemma"]

		form_w_quant = feats.get("orth", form)
		if feats.has_key("orth"): del feats["orth"]

		bare_form = self.bare_form_expr.sub("", form)
		
		parse = Parse(self.language, form)
		parse.expandedForm = form_w_quant
		parse.bareForm = bare_form

		for feature, value in feats.items():
		    parse.setFeature(feature, value)
		#parse.features = feats

		match = None
		if self.lemma_cache.has_key(headword):
		    match = self.lemma_cache[headword]
		else:
		    matches = self.lemma_dao.getMatchingLemmas(\
			headword, self.language)
		    if len(matches) > 0:
			match = matches[0]
			self.lemma_cache[headword] = match
		    else:
			print "No lemma found matching %s (form %s)" % (headword, form)
		if match != None:
		    parse.lemma = match
		    self.parse_dao.save(parse)

		if len(self.lemma_cache) > 10000:
		    self.lemma_cache.clear()

		self.parse_count += 1
		if self.parse_count % 1000 == 0:
		    print "[%9d] %s %s" % (self.parse_count, headword, parse)
		    self.parse_dao.endTransaction()
		    self.parse_dao.beginTransaction()
	else:
	    feats[self.cur_feature] = self.cur_value
    
    def characters(self, chars, start, length):
	if self.cur_value != None:
	    self.cur_value += "".join(chars[start:start+length])

if __name__ == "__main__":
    if len(sys.argv) != 3:
	print "Usage: ParseLoader <XML file> <language>"
    else:
	loader = ParseLoader()
	loader.load(sys.argv[1], sys.argv[2])
