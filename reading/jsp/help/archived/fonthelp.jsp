<html>
  <head>
    <title>Perseus Greek Font display Help (deprecated)</title>
    <link href="/css/hopper.css" type="text/css"
    rel="stylesheet"/>
  </head>
  <body>
    <%@ include file="/includes/index/header.jsp" %>

  <div id="main">
      <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="archives"/>
      </jsp:include>

	<div id="content" class="2column">
	<div id="index_main_col">
				<h3> Greek Font Display Help (deprecated)</h3>
				<b>last revised: 05/31/05</b><br>
				<ol>
			<li><b><a href="#basics">The basics: reading versus entering polytonic Greek.</a></b>
			<li><b><a href="#advantages">Advantages and disadvantages of the Perseus Greek display options.</a></b>
			<li><b><a href="#best">Which polytonic Greek font is best for me?</a></b>
			<li><b><a href="#change">How to change the Greek display.</a></b>
			<li><b><a href="#other">Other notes.</a></b>
		</ol>
				<ol>
			<li><a name="basics"></a><b>The basics: reading versus entering polytonic Greek.<br>
				</b>The font options we provide apply only to the <i>display</i> of Greek texts. Any tool in which you must enter Greek will still display your entry in a Latin font. There are several options for representing the Greek letters (just as for displaying texts), but you will always see the Roman alphabet in the resulting search box. When searching for dictionary headwords, using the Greek morphological analysis, or searching on Greek words in context, you must transliterate your Greek letters and accents based on one of the Perseus Greek options. You cannot, at this time, copy and paste Greek from another program or site, or type it using a Greek font.<font size="-1"> (The Enter Text in Greek keyboard tool permits clicking on the Greek letters and accents, and then converts the result into the appropriate Latin characters based on user settings, but can give erratic results.)<br>
				</font>Most Greek searching errors occur with incorrect transliteration of Greek letters.

			<li><a name="advantages"></a><b>Advantages and disadvantages of the Perseus Greek display options.<br>
				</b>
			<table width="602" border="1" cellspacing="0" cellpadding="0" height="180">
				<tr height="20">
					<td width="130" height="20"><b>Option</b></td>
					<td width="200" height="20"><b>Advantages</b></td>
					<td width="260" height="20"><b>Disadvantages</b></td>
					<td width="260" height="20"><b>Sample</b></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">Latin transliteration</font></td>
					<td width="200"><font size="-1">no special font required<br>
							works on any browser<br>
							default setting</font></td>
					<td width="260"><font size="-1">doesn't look like Greek; can be hard to read</font></td>
					<td width="260"><img src="/img/help/01latintrans.png" alt="" height="13" width="231" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">Greek transliteration</font></td>
					<td width="200"><font size="-1">no special font required<br>
							works on any browser<br>
						</font></td>
					<td width="260"><font size="-1">doesn't look like Greek; can be hard to read<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01greektrans.png" alt="" height="13" width="232" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">GreekKeys</font></td>
					<td width="200"><font size="-1">looks like Greek<br>
						</font></td>
					<td width="260"><font size="-1">requires font installation<br>
							may not display properly on every browser<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01greekkeys.png" alt="" height="17" width="250" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">SGreek for Windows</font></td>
					<td width="200"><font size="-1">looks like Greek<br>
						</font></td>
					<td width="260"><font size="-1">requires font installation<br>
							may not display properly on every browser<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01Sgreek.png" alt="" height="18" width="340" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">SP Ionic</font></td>
					<td width="200"><font size="-1">looks like Greek<br>
						</font></td>
					<td width="260"><font size="-1">requires font installation<br>
							may not display properly on every browser<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01SPionic.png" alt="" height="18" width="246" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">Super Greek</font></td>
					<td width="200"><font size="-1">looks like Greek<br>
						</font></td>
					<td width="260"><font size="-1">requires font installation<br>
							may not display properly on every browser<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01supergreek.png" alt="" height="16" width="259" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">Beta Code</font></td>
					<td width="200"><font size="-1">no special font required<br>
							works on any browser<br>
						</font></td>
					<td width="260"><font size="-1">doesn't look like Greek; can be hard to read<br>
							must change configuration to select this option<br>
						</font></td>
					<td width="260"><img src="/img/help/01betacode.png" alt="" height="16" width="287" border="0"></td>
				</tr>
				<tr>
					<td width="130"><font size="-1">Unicode</font></td>
					<td width="200"><font size="-1">looks like Greek<br>
							most operating systems have fonts pre-installed<br>
							fonts freely available and well documented</font></td>
					<td width="260"><font size="-1">may require font installation<br>
							may not display properly on every browser<br>
							must change configuration to select this option</font></td>
					<td width="260"><img src="/img/help/01unicodeplain.png" alt="" height="15" width="317" border="0"><br>
						<img src="/img/help/01unicode.png" alt="" height="17" width="289" border="0"><br>
						<img src="/img/help/01Unicodewin.png" alt="" height="20" width="251" border="0"></td>
				</tr>
			</table>
			<li><a name="best"></a><b>Which polytonic Greek font is best for me?</b><br>
				<b>Windows (tested on XP, SP2)</b>
			<table width="570" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="150"><b>Polytonic Font</b></td>
					<td width="133"><b>Internet Explorer</b></td>
					<td width="191"><b>Firefox</b></td>
					<td width="84"><b>Netscape</b></td>
				</tr>
				<tr height="16">
					<td width="150" height="16"><font size="-1">GreekKeys</font></td>
					<td width="133" height="16"><font size="-1">works well</font></td>
					<td width="191" height="16"><font size="-1">some letter combos do not display</font></td>
					<td width="84" height="16"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">SGreek</font></td>
					<td width="133"><font size="-1">works well</font></td>
					<td width="191"><font size="-1">works well</font></td>
					<td width="84"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">SP Ionic</font></td>
					<td width="133"><font size="-1">works well</font></td>
					<td width="191"><font size="-1">works well</font></td>
					<td width="84"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Super Greek</font></td>
					<td width="133"><font size="-1">N/A: Mac font</font></td>
					<td width="191"><font size="-1">N/A: Mac font</font></td>
					<td width="84"><font size="-1">N/A: Mac font</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Unicode</font></td>
					<td width="133"><font size="-1">incomplete characters</font></td>
					<td width="191"><font size="-1">works well</font></td>
					<td width="84"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Unicode with precombined</font></td>
					<td width="133"><font size="-1">incomplete characters</font></td>
					<td width="191"><font size="-1">works well</font></td>
					<td width="84"><font size="-1">works well</font></td>
				</tr>
			</table>
			<b>Macintosh (tested on OS X 10.3.6)</b>
			<table width="724" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="150"><b>Polytonic Font</b></td>
					<td width="140"><b>Safari</b></td>
					<td width="140"><b>Internet Explorer</b></td>
					<td width="140"><b>Firefox</b></td>
					<td width="140"><b>Netscape</b></td>
				</tr>
				<tr height="16">
					<td width="150" height="16"><font size="-1">GreekKeys</font></td>
					<td width="140" height="16"><font size="-1">encoding must be Western (Mac OS); spacing issues</font></td>
					<td width="140" height="16"><font size="-1">doesn't work</font></td>
					<td width="140" height="16"><font size="-1">encoding must be set as Western (MacRoman)</font></td>
					<td width="140" height="16"><font size="-1">encoding must be set as Western (MacRoman)</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">SGreek*</font></td>
					<td width="140"><font size="-1">doesn't work*</font></td>
					<td width="140"><font size="-1">works well*</font></td>
					<td width="140"><font size="-1">works well*</font></td>
					<td width="140"><font size="-1">works well*</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">SP Ionic</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Super Greek</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
					<td width="140"><font size="-1">works well</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Unicode</font></td>
					<td width="140"><font size="-1">works; accents displayed apart from characters</font></td>
					<td width="140"><font size="-1">doesn't work</font></td>
					<td width="140"><font size="-1">works; accents displayed apart from characters</font></td>
					<td width="140"><font size="-1">works; accents displayed apart from characters</font></td>
				</tr>
				<tr>
					<td width="150"><font size="-1">Unicode with precombined</font></td>
					<td width="140"><font size="-1">works; some combination characters do not display</font></td>
					<td width="140"><font size="-1">doesn't work</font></td>
					<td width="140"><font size="-1">works; some combination characters do not display</font></td>
					<td width="140"><font size="-1">works; some combination characters do not display</font></td>
				</tr>
			</table>
			<font size="-1">* SGreek is a Windows font, but this filter still works on the Mac. It is not clear if any particular font installation is required or if this is simply an unexpected feature.</font>
			<li><a name="change"></a><b>How to change the Greek display.<br>
				</b>a. If needed, <b>install</b> your chosen font:<br>
				<i>GreekKeys</i>. For display of texts in Perseus, we offer an older version of GreekKeys Athenian free of charge, as does the <a href="http://socrates.berkeley.edu/%7epinax/greekkeys/Atheniandownload.html">GreekKeys support site</a>. Please contact the webmaster for more information. (webmaster@perseus.tufts.edu)<br>
				<i>SGreek. </i>This is a shareware font offered by <a href="http://www.silvermnt.com/fonts.htm">Silver Mountain Software</a>.<br>
				<i>SP Ionic.</i> Available on various sites, including <a href="http://www.monachos.net/other/fonts/get_greek_font.shtml">Monachos.net</a>.
<br>
				<i>SuperGreek. </i>Not currently available. An older version of a font package offered by <a href="http://www.linguistsoftware.com">Linguist Software</a>. A broader WWW search may turn up some older versions of this font, or write to the webmaster for further advice.<br>
				<i>Unicode</i>. For Windows, most current versions of the operating system include Lucida Sans Unicode. (You may use any Unicode font that includes <b>both</b> the Greek letters (the &quot;Basic Greek&quot; Unicode subset) <b>and</b> the accent marks (the &quot;Combining Diacriticals&quot; Unicode subset). If your font does not include both of these subsets, Perseus Greek texts will not display correctly). For the Mac, please consult the <a href="http://socrates.berkeley.edu/%7epinax/greekkeys/GreekKeys.html">GreekKeys support site</a>, which offers excellent GreekKeys Unicode font options.<br>
				<br>
				b. Open a Greek text, either by selecting it from the table of contents or by changing the English version to a Greek version.<br>
				<br>
				c. Follow the &quot;Configure Display&quot; link at the top of the page, typically found in the navigation bar. In the section labeled Greek display, choose your font configuration. (You may also change any other options on this page). Select the &quot;Set Configuration&quot; button at the top of the page and you will be returned to the text page automatically.<br>
				<br>d. If the display is not correct, you may have to manually change some other settings. For instance, the font preferences for &quot;user defined&quot; fonts may have to be changed, or the text encoding may need to be altered. In most cases, with a recent browser and a newer operating system, this will not be necessary. In particular, check the accessibility options: on some configurations you do not want to ignore the page-specified font styles. Most errors occur when the font is not properly installed. If you cannot see the Greek text, please write to the webmaster (webmaster@perseus.tufts.edu) with as much detail about the problem as possible. Please include your operating system version and browser type and version so that we may attempt to replicate the problem here.<br>
				<br>
			
			
			<li><a name="other"></a><b>Other notes: <br>
					Linux with X Windows:</b> You can use any of the TrueType fonts supported for Microsoft Windows. Install the font and configure your X font server to recognize it; we have found that it is essential to set the font's encoding to ISO-8859-1 whether or not that is really correct for the font. Once you have done this, you can use your TrueType font with Perseus or any other site that uses the Perseus font system. We have tested this with RedHat Linux 6.2, 7.0, and 7.1 using Netscape 4.7. We expect that the same method will work with other X Windows implementations on other versions of Unix, but we have not tested them and cannot provide support.
<br>
				<br>
				For further information about Unicode and Greek, see the Stoa Consortium's <a href=http://www.stoa.org/unicode/>Unicode Polytonic Greek for the World Wide Web,</a> by Patrick Rourke; you will need a Unicode font to read it properly.<br>
				<br>
				<font size="-1"><b>A note on fonts in Perseus.</b><br>
				</font><font size="-1">Ancient Greek works in Perseus are stored as beta code, a standard way of describing Greek letters and accents using basic ASCII characters. In order to provide better reading options for the texts we provide, Perseus developed a filter which converts this beta code into other font displays. For many years, browser technology made it a challenge to present more than one font on a WWW page. Perseus purposefully avoided using extensive scripting which might make the site too slow for most of our users; our current font display system is, therefore, imperfect and limited. </font>		</ol>
				<i>document revised</i> May, 2005 LMC 

</div> <!-- index_main_col -->
	</div> <!-- content 2column -->
</div> <!-- main -->

    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>




	






