<%@page pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>


<%
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
%>
		<a class=<%=tab.equals(tabActive) ? "tab_active" : "tab" %> href="/hopper/<%=url.replaceAll("\\$1",tab)%>"><%=dispName%></a>
<%
}
%>

