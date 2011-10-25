from perseus.document import CorpusProcessor, DOMOffsetAdder, Query
from perseus.ie.entity import *
from perseus.ie import Location

from org.w3c.dom import *

import sys
import entity_utils

from NewEntityExtractor import NewEntityExtractor

class NewPlaceExtractor(NewEntityExtractor):

    def __init__(self):
	NewEntityExtractor.__init__(self)

    def getTaskName(self):
	return "extract-places"

    def process_name(self, name_elt):
	"""
	Some texts, like the hand-tagged version of Herodotus in Ehglish, may
	have <name type="place"> tags instead of <placeName> tags. Happily,
	attributes of either tag should be identical.
	"""
	type = name_elt.getAttribute("type")
	if type != "place":
	    return None
	else:
	    return self.process_placeName(name_elt)
    def process_placeName(self, place_elt):

	key = place_elt.getAttribute("key").split(";")[0]
	if key != "":
	    place = self.manager.getEntityByAuthName(key)
	    if place != None:
		#print "Looked up a place with key %s: %s" % \
		    #(key, place.getDisplayName())
		return place
	
	display_name = place_elt.getAttribute("n")
	reg = place_elt.getAttribute("reg")
	child_text = place_elt.getTextContent()

	for v in [display_name, reg, child_text]:
	    matches = self.manager.getMatchingEntities(v, Place)
	    if len(matches) > 0:
		return matches[0]
		#return self.create_occurrence(matches[0], place_elt)
	print "Couldn't match place '%s'. :(" % place_elt.getTextContent()
	return None

    def get_entity_classes(self):
	return [Place]

if __name__ == "__main__":
    npe = NewPlaceExtractor()
    if len(sys.argv) > 1:
	for i in range(1, len(sys.argv)):
	    npe.processAnything(sys.argv[i])
    else:
	npe.processCorpus()
