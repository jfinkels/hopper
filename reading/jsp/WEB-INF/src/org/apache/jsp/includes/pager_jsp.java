package org.apache.jsp.includes;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class pager_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n<div class=\"pager\">\n");


int hitCount = Integer.parseInt(request.getParameter("hitCount")); //numHits
int hitsPerPage = Integer.parseInt(request.getParameter("hitsPerPage"));
int firstHit = Integer.parseInt(request.getParameter("firstHit")); //currentHit
int threshold = Integer.parseInt(request.getParameter("threshold"));
String sourceURL = request.getParameter("sourceURL");

String transformedURL;

if ((hitCount-1) / hitsPerPage > 0) {
 	int currentPage = firstHit / hitsPerPage;
    int lastPage = (hitCount-1) / hitsPerPage;

 	if (currentPage > 0) {
 		transformedURL = sourceURL.replaceAll("\\$1", "0");

      out.write("\n    \t<a href=\"");
      out.print( transformedURL );
      out.write("\"><img src='/img/westend.gif' border='0'/></a>\n");

    	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(firstHit-hitsPerPage));

      out.write("\n    \t<a href=\"");
      out.print( transformedURL );
      out.write("\"><img src='/img/west.gif' border='0'/></a>\n");

 	}
  	for (int thisPage = 0; thisPage <= lastPage; thisPage++) {
		int distance;
		if (thisPage < currentPage) {
	    	distance = currentPage - thisPage;
		} else {
	    	distance = thisPage - currentPage;
		}
		
		if (distance == 0) {
	    	
      out.write(' ');
      out.print( thisPage+1 );
      out.write(' ');

		} else if (distance <= threshold) {
			transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(thisPage * hitsPerPage));

      out.write("\t\t\t<a href=\"");
      out.print( transformedURL );
      out.write('"');
      out.write('>');
      out.print( thisPage+1 );
      out.write("</a>");

		} else if (distance == threshold+1) {
	    	
      out.write(" ... ");

		}
    }
    if (currentPage < lastPage) {
    	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(firstHit+hitsPerPage));

      out.write("\n    \t<a href=\"");
      out.print( transformedURL );
      out.write("\"><img src='/img/east.gif' border='0'/></a>\n");
 
     	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(lastPage*hitsPerPage));

      out.write("\n    \t<a href=\"");
      out.print( transformedURL );
      out.write("\"><img src='/img/eastend.gif' border='0'/></a>\n");

    }
}

      out.write("\n</div>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
