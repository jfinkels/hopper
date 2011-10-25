<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <xsl:import href="tei.xsl"/>
  
  <xsl:template match="orig">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="expan">
    <xsl:value-of select="@abbr"/>
  </xsl:template>

  <xsl:template match="pb">
    <p><E>[<a href="http://www.chlt.org/sandbox/saga/fridthjofssaga-1/page.{@n}.php?size=960x1280"><xsl:value-of select="@n"/></a>]</E></p>
  </xsl:template>

</xsl:stylesheet>