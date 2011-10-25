<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8"/>

  <xsl:template match="/">
    <html>
    <head>
    <script language="javascript" src="/js/hopper.js"></script>
    </head>
    <body>
    <xsl:apply-templates select="contents/hierarchy"/>
    </body>
    </html>
  </xsl:template>

  <xsl:template match="hierarchy">

    <xsl:apply-templates select="chunk"/>

  </xsl:template>

  <xsl:template match="chunk[child::chunk]">
    <xsl:variable name="chunkID">
      <xsl:value-of select="generate-id()" />
    </xsl:variable>
    <xsl:variable name="indentPixels">
      <xsl:value-of select="18 * (count(ancestor::chunk) + 1)" />
    </xsl:variable>
    <table><tr>
    <td width="{$indentPixels}">&#160;</td>
    <td width="18" align="center">&#160;</td>
    <td>
      <a href="javascript:toggleExpand('{$chunkID}');"><img border="0" src="/img/east.gif" name="img_{$chunkID}" /></a>
    <xsl:variable name="href">
      <xsl:value-of select="ref"/>
    </xsl:variable>
    <a href="text?doc={$href}"><xsl:value-of select="head" /></a>
    </td>
    </tr></table>

    <div id="{$chunkID}" style="display: none">
    <xsl:apply-templates select="chunk"/>
    </div>

  </xsl:template>

  <xsl:template match="chunk">
    <xsl:variable name="indentPixels">
      <xsl:value-of select="18 * (count(ancestor::chunk) + 1)" />
    </xsl:variable>
    <table><tr>
    <td width="{$indentPixels}">&#160;</td>
    <td width="18" align="center">&#160;</td>
    <td>
    <xsl:variable name="href">
      <xsl:value-of select="ref"/>
    </xsl:variable>
    <a href="text?doc={$href}">
	<xsl:if test="@isParent">
	    <img border="0" src="/img/east.gif" />
	</xsl:if>
	<xsl:value-of select="head" />
    </a>
    </td>
    </tr></table>
  </xsl:template>

</xsl:stylesheet>

  
