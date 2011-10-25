package org.apache.jsp.includes.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class navigation_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\n');
      out.write('\n');
      out.write('\n');

String tabNames = request.getParameter("tabNames");
String dispNames = request.getParameter("dispNames");
String tabActive = request.getParameter("tabActive");
String url = request.getParameter("url");


String[] tabs = tabNames.split(",");
String[] disp = new String[tabs.length];
if(dispNames == null){		
	for(int i=0; i < tabs.length; i++){
		String tab = tabs[i];
		String end = tab.substring(1);
		String begin = tab.substring(0,1);
		begin = begin.toUpperCase();
		String displayTab = begin + end;
		disp[i] = displayTab;
	}
}else{
	disp = dispNames.split(",");
}
for(int i=0; i < tabs.length; i++){
	String tab = tabs[i];
	String dispName = disp[i];

      out.write("\n\t\t<a class=");
      out.print(tab.equals(tabActive) ? "tab_active" : "tab" );
      out.write(" href=\"/hopper/");
      out.print(url.replaceAll("\\$1",tab));
      out.write('"');
      out.write('>');
      out.print(dispName);
      out.write("</a>\n");

}

      out.write('\n');
      out.write('\n');
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
