from perseus.document import *
from perseus.morph import *

#This gives you a list of words that when you click on them nothing comes up.
doc = "Perseus:text:1999.05.0007:document=5"
query = Query(doc)
chunk = query.getChunk()
unparseable = Unparseable.getUnparseable(chunk)
for unparsed in unparseable:
    print unparseable.toString()

