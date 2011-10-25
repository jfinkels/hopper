
from java.io import *
from perseus.document import *
import sys

if len(sys.argv) != 4:
    print "Usage: transcode_xml [filename] [language] [output encoding]"
else:
    
    filename = sys.argv[1]
    default_lang = sys.argv[2]
    output_format = sys.argv[3]
    
    input_file = File(filename)
    tokenizer = TokenizingParser(input_file, default_lang)
    
    token_list = tokenizer.getOutputFromXML()
    
    renderer = Renderer(default_lang)
    renderer.addTokenFilter(GreekTranscoderTokenFilter(output_format))

    print renderer.render(token_list)
