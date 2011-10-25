"""
This class tries to extract person-names from the REG attributes of persName
tags. As of 11/04, the syntax we look for is the something like

<match_method>:surname,forename(,forename)+[:<score (?)>]

We try to assemble an authority name and display name from everything after
the first colon (and before the second, if there is a second one). If the
method used to store person-information changes, parts of process_persName()
will probably need to be updated.
"""

from perseus.document import Chunk, Query

from perseus.ie import Location
from perseus.ie.entity import Person, HibernateEntityManager, EntityOccurrence
from perseus.ie.entity import EntityFrequency

from java.io import BufferedReader, InputStreamReader
from java.util import HashMap

import sys

import re

from NewEntityExtractor import NewEntityExtractor

class NewPersonExtractor(NewEntityExtractor):

    def __init__(self):
	NewEntityExtractor.__init__(self)

	self.person_re = re.compile(".*?:(?P<name>[^:]*)(?::.*)?")

    def getTaskName(self):
	return "extract-people"

    def process_persName(self, element):
	#print "Trying to process persname: %s" % element.getTextContent()
	reg = element.getAttribute("reg")
	if reg == "":
	    print "Empty reg attribute for %s! Skipping..." % \
		element.getTextContent()
	    return None
	match_result = self.person_re.match(reg)
	name_string = match_result.group("name")

	name_tokens = name_string.split(",")
	surname = name_tokens[0]
	forenames = filter(None, \
			[n for n in name_tokens[1:] if n != "nomatch"])

	person = Person()
	for f in forenames:
	    if len(f.strip()) > 0: person.addName(person.FORENAME, f)
	person.addName(person.SURNAME, surname)

	print "%s -> %s" % (element.getTextContent(), person.getDisplayName())
	return person

    def get_entity_classes(self):
	return [Person]

if __name__ == "__main__":
    conv = NewPersonExtractor()
    if len(sys.argv) > 1:
	for i in range(1, len(sys.argv)):
	    conv.processAnything(sys.argv[i])
    else:
	conv.processCorpus()
