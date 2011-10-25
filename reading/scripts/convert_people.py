"""
Usage:

jython convert_people.py [table_data_filename]

This script is intended to convert person-names from the old Perl byte-offsets
to the new Java pseudo-byte-offsets. It expects a tab-delimited file in which
each row corresponds to an entry from the persnames table (such a file can be
obtained by dumping the output of the command

mysql --batch --skip-column-names sor -e 'select * from persnames \
    order by id asc, cit asc, byte_offset asc'

to a file, the name of which should be supplied on the command line. If none
is supplied, we try to use a file named "persnames_data.txt".

Output goes to stdout by default, noise and error messages to stderr.

The script is of limited use, granted, but here it is.
"""

from perseus.document import *
import sys

if (len(sys.argv) > 1):
    filename = sys.argv[1]
else:
    filename = "persnames_data.txt"

def update_match_counts(query):
    s = str(query)

    if match_counts.has_key(s):
	count = match_counts[s]
    else:
	count = 0

    match_counts[s] = count + 1
    return count

sys.stderr.write("Using file: %s\n" % filename)
data_file = open(filename, 'r')

match_counts = {}
offset_cache = {}
bad_docs = {}

num_processed = 0

while 1:
    line = data_file.readline()
    if len(line) == 0:
	break
    line = line.strip()
    #print line

    fields = line.split('\t')
    # fields:
    # ---
    # surname
    # id
    # byte_offset
    # cit
    # query
    # loc
    # rolename
    # forename1
    # forename2
    # forename3
    # forename4
    # namelink
    # addname
    # genname
    # forenames
    doc_id = fields[1]
    byte_offset = fields[2]
    subquery = fields[3]

    #print "%s -> %s" % (doc_id, subquery)
    if subquery != "\\n":
	q = Query(doc_id, subquery)
    else:
	q = Query(doc_id)

    try:
	if offset_cache.has_key(q):
	    sb = offset_cache[q]
	else:
	    c = q.getTextlessChunk()
	    sb = c.getStartOffset()
	    offset_cache[q] = sb

	match_count = update_match_counts(q)
	new_pos = sb + match_count
	#sys.stderr.write("%s: %d -> %d+%d\n" % \
	    #(str(q), byte_offset, sb, match_count))
	
	fields[2] = str(new_pos)
	# break out the "forenames" field into individual fields
	fields[-1] = fields[-1].replace(",", "\t")
	print "\t".join(fields)
    except InvalidQueryException:
	bad_docs[q.getDocumentID()] = 1
	sys.stderr.write("Oh no! Invalid query: %s\n" % str(q))
    
    num_processed += 1
    if num_processed % 1000 == 0:
	sys.stderr.write("%d\n" % num_processed)

sys.stderr.write("The following documents had problems:\n")
for b in bad_docs.keys():
    sys.stderr.write("\t* %s\n" % b)
