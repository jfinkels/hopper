from perseus.document import CorpusProcessor

from perseus.ie.entity import Date, DateRange, CivilWarDateParser

import sys
import entity_utils

from NewEntityExtractor import NewEntityExtractor

from java.lang import NumberFormatException

class NewDateExtractor(NewEntityExtractor):

    def __init__(self):
	NewEntityExtractor.__init__(self)
	self.parser = CivilWarDateParser()
	self.bad_dates = []
    
    def getTaskName(self):
	return "extract-dates"

    def process_date(self, element):
	date_text = element.getAttribute("value")
	if date_text == "":
	    children = entity_utils.children_as_list(element)
	    date_text = "".join([c.getNodeValue() for c in children \
				    if c.getNodeType() == c.TEXT_NODE])
	
	if date_text is None or date_text == "":
	    raise IllegalArgumentException

	try:
	    return self.parser.parse(date_text)
	except NumberFormatException:
	    print "Problem parsing date: %s" % date_text
	    self.bad_dates.append((date_text, "date"))
	    return None

    def process_dateStruct(self, element):
	date_value = element.getAttribute("value")
	if date_value != "":
	    try:
		return self.parser.parse(date_value)
	    except NumberFormatException:
		print "Problem parsing date-struct: %s" % date_value
		self.bad_dates.append((date_value, "dateStruct_value"))
	
	values = {"year": 0, "month": 0, "day": 0}

	for c in filter(lambda x: x.getNodeType() == x.ELEMENT_NODE, \
			entity_utils.children_as_list(element)):
	    name = c.getLocalName()
	    if values.has_key(name): values[name] = c.getAttribute("reg")

	return Date(values["year"], values["month"], values["day"])

    def process_dateRange(self, element):
	start = element.getAttribute("from")
	end = element.getAttribute("to")
	try:
	    return DateRange(self.parser.parse(start), self.parser.parse(end))
	except NumberFormatException:
	    print "Problem parsing date-range: %s, %s" % (start, end)
	    self.bad_dates.append(((start, end), "dateRange"))
	    return None

    def get_entity_classes(self):
	return [Date, DateRange]

if __name__ == "__main__":
    nde = NewDateExtractor()
    if len(sys.argv) > 1:
	for i in range(1, len(sys.argv)):
	    nde.processAnything(sys.argv[i])
    else:
	nde.processCorpus()
    print "The following date strings couldn't be parsed:"
    for bd in nde.bad_dates: print bd
