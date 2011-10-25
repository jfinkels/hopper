package perseus.util;

//////////////////////////////////////////////////////////////////
// SetPreferences.java						//
//								//
// by tsai@perseus.tufts.edu					//
// 								//
// servlet for setting Perseus user display preferences.	//
//////////////////////////////////////////////////////////////////
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SetPreferences extends HttpServlet {
  private String prevURL;
  private String query;

  public void doGet(HttpServletRequest request, HttpServletResponse
      response) throws IOException, ServletException {
    prevURL = request.getParameter("url");
    String params = prevURL.replaceFirst(".*[?]", "");
    String[] paramsSplit = params.split("&");
    query = paramsSplit[0].replaceFirst(".*=", "");
    query = query.replaceAll("[+]", " ");

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    this.printForm(out);
  }

  public void printForm(PrintWriter out) {
    out.println("<html>");
    out.println("<head><title>set display preferences</title></head>");
    out.println("<body bgcolor=#ffffff>");
    out.println("<form action=\"" + prevURL + "\" method=GET>");
    out.println("<input type=HIDDEN name=query value=\"" + query + "\">");
    out.println("default version?");
    out.println("<input type=RADIO name=vers value=original checked> Original");
    out.println("<input type=RADIO name=vers value=translation> Translation");
    out.println("<p>");
    out.println("morphology links?");
    out.println("<input type=RADIO name=morph value=1 checked> Yes");
    out.println("<input type=RADIO name=morph value=0> No");
    out.println("<input type=RADIO name=morph value=2> Only ambiguous forms");
    out.println("<input type=RADIO name=morph value=3> Only lexically ambiguous forms");
    out.println("<p>");
    out.println("lookup tool links?");
    out.println("<input type=RADIO name=sor value=1 checked> Search the current collection");
    out.println("<input type=RADIO name=sor value=0> No");
    out.println("<input type=RADIO name=sor value=2> Search all collections");
    out.println("<p>");
    out.println("greek text encoding?");
    out.println("<input type=RADIO name=display value=Latin transliteration checked> Latin transliteration");
    out.println("<input type=RADIO name=display value=Greek transliteration> Greek transliteration");
    out.println("<input type=RADIO name=display value=SMK> GreekKeys");
    out.println("<input type=RADIO name=display value=Sgreek> Sgreek for Windows");
    out.println("<input type=RADIO name=display value=Spionic> SP Ionic");
    out.println("<input type=RADIO name=display value=SuperGreek> SuperGreek");
    out.println("<input type=RADIO name=display value=Beta code> Beta code");
    out.println("<input type=RADIO name=display value=CUTF8> Unicode (UTF-8) with precombined accents");
    out.println("<p>");
    out.println("metrical symbols?");
    out.println("<input type=RADIO name=meter value=Trans checked> Transliteration");
    out.println("<input type=RADIO name=meter value=Font> Show as symbols");
    out.println("<p>");
    out.println("arabic display?");
    out.println("<input type=RADIO name=arabic value=xlit checked> Romanization");
    out.println("<input type=RADIO name=arabic value=buckwalter> Exact transliteration (Buckwalter)");
    out.println("<input type=RADIO name=arabic value=utf8> Arabic letters (Unicode)");
    out.println("<p>");
    out.println("sanskrit display");
    out.println("<input type=RADIO name=sanskrit value=HK checked> Harvard-Kyoto code");
    out.println("<input type=RADIO name=sanskrit value=roman> Usual Romanization");
    out.println("<input type=RADIO name=sanskrit value=devangari> Devangari (Unicode)");
    out.println("<p>");
    out.println("link types?");
    out.println("<input type=RADIO name=linktype value=10 checked> Commentary");
    out.println("<input type=RADIO name=linktype value=20> Cross references");
    out.println("<input type=RADIO name=linktype value=30> Cross references in notes");
    out.println("<input type=RADIO name=linktype value=35> Cross reference in general diction");
    out.println("<input type=RADIO name=linktype value=37> Cross reference in text-specific dictionaries");
    out.println("<input type=RADIO name=linktype value=40> Cross references in indexes");
    out.println("<p>");
    out.println("mark cited passages how?");
    out.println("<input type=RADIO name=lemmap value=1> Italicize whole lemma and place mark");
    out.println("<input type=RADIO name=lemmap value=2> Place mark at end of lemma");
    out.println("<input type=RADIO name=lemmap value=3 checked> Don't mark lemmas");
    out.println("<p>");
    out.println("<input type=SUBMIT name=state value=Submit>");
    out.println("</form>");
    out.println("</body>");
    out.println("</html>");
  }
}
