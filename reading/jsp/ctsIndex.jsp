<html>
  <head>
    <title>Perseus - CTS Service</title>
  </head>
  <body>
<h1>Perseus Canonical Text Services</h1>
<p>This is the base URL for a server
supporting version 1.1 of the CTS protocol.
</p>

<p>
</p>

<h2>This implementation of the CTS protocol</h2>
<p>In addition to the request parameters defined by the CTS 1.1
protocol, this server accepts an optional parameter named
<code>config</code> identifying
a configuration file.  This is an XML document with two elements
identifying the CTS TextInventory to use, and a directory
in the local file system where TEI-conformant XML files are kept.
The <code>local</code> attribute in the TextInventory should
give file names that can be  concatenated with name of this
directory.

</p>
<p>
By adding further configuration files pointing to a different
TextInventory and possibly (but not necessarily) pointing to
a different directory for TEI files, you effectively
implement a new CTS service with a base URL of the form
<br/>
<code>http://yourmachine/path/to/servlet/CTS?config=yourconfigfile.xml&amp;</code>

</p>

<p>If no <code>config</code> parameter is included, the
server uses the file named <code>config.xml</code>.
In this case, the base URL of your CTS service is
<br/>
<code>http://yourmachine/path/to/servlet/CTS?</code>

</p>


<h2>Requests defined by the CTS 1.1 protocol</h2>

<p>
<form method="get" action="CTS">

<table>

  <tr><td>configuration file:</td><td><input name="config"
  size="10"/></td></tr>
  <tr/>
<tr><td>request:</td>
<td><select name="request">
  <option value="GetCapabilities"
  selected="selected">GetCapabilities</option>

      <option value="GetWorks">GetWorks</option>
      <option value="GetTEIHeader">GetTEIHeader</option>
  <option value="GetValidReff">GetValidReff</option>	
  <option value="GetPassage">GetPassage</option>
      <option value="DownloadText">DownloadText</option>
      <option value="GetCitationScheme">GetCitationScheme</option>
      <option value="GetFirstRef">GetFirstRef</option>
      <option value="GetPrevNext">GetPrevNext</option>
      <option value="GetPassagePlus">GetPassagePlus</option>
</select>
</td></tr>

<tr><td>textgroup:</td><td> <input name="textgroup" size="12"/></td>
<td> work: </td><td><input name="work" size="12"/></td></tr>
<tr><td>edition:</td><td> <input name="edition" size="12"/></td>
<td>or translation:</td><td><input name="translation"
size="12"/></td></tr>
<td> exemplar:</td><td> <input name="exemplar" size="12"/></td></tr>
<tr></tr>

<tr><td>ref:</td><td><input name="ref" size="12"/></td>
<td> or  range:</td><td><input name="range" size="12"/></td></tr>

<td>collection:</td><td><input name="collection" size="12"/></td></tr>

<tr>
  <td>level for valid reff.:</td>
  <td><input name="level" size="4"/></td>
  </tr>

<tr>
  <td>
# of units of surrounding context:</td><td><input name="contextsize"
  size="4"/></d></tr>

</table>
<input type="submit" value="Get request"/>
</form>
</p>
	 <!-- Google Analytics --> 
	<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>