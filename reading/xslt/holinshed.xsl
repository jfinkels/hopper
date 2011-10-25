<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
  
  <xsl:import href="tei.xsl"/>
  <!--
  <xsl:output
    method="html"
    encoding="utf-8"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    indent="yes"/>
  -->
  <!-- Heads -->
  
  <xsl:template match="head" mode="toc">
    <li><xsl:value-of select="."/></li>
  </xsl:template>
  
  <xsl:template match="//div/head|//div3/head|//div4/head">
    <a name="{generate-id()}"/> <h1 class="div head"><xsl:apply-templates/></h1>
  </xsl:template>

  <xsl:template match="//div1[@type='book']/head">
    <a name="{generate-id()}"/>
    <h1 class="book head"><xsl:value-of select="."/></h1>
  </xsl:template>
  
  <xsl:template match="//div2[@type='chapter']/head">
    <a name="{generate-id()}"/>
    <h2 class="chapter head"><xsl:value-of select="."/></h2>
  </xsl:template>

  <xsl:template match="//list/head">
    <h1 class="list head"><xsl:apply-templates/></h1>
  </xsl:template>
  
  <!-- Notes -->

  <xsl:template match="note" mode="toc">
    <li><a href="#{generate-id()}"><xsl:value-of select="."/></a></li>
  </xsl:template>
  
  <xsl:template match="note[@place='marg']">
    <xsl:choose>
      <xsl:when test="child::*[1][self::date]">
        <span class="note margin rubric">
          <a name="{generate-id()}"></a>
          <xsl:apply-templates/>
        </span>
      </xsl:when>
      <xsl:otherwise>
        <span class="note margin">
          <a name="{generate-id()}"></a>
          <xsl:apply-templates/>
        </span>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- Dates -->

  <xsl:template match="date">
    <span class="date"><a name="{generate-id()}"></a><xsl:apply-templates/></span>
  </xsl:template>
  
  <xsl:template match="date" mode="toc">
    <li>
      <a href="#{generate-id()}">
        <xsl:value-of select="@value"/>
      </a>
    </li>
  </xsl:template>
  
  <!-- Lists -->
  
  <xsl:template match="item">
    <li><xsl:apply-templates/></li>
  </xsl:template>
  
  <xsl:template match="list[@type='ordered']">
    <ol>
      <xsl:apply-templates/>
    </ol>
  </xsl:template>
  
  <xsl:template match="list">
    <ul>
      <xsl:apply-templates/>
    </ul>
  </xsl:template>
  
  <xsl:template match="milestone">
    <a name="{generate-id()}"></a>
  </xsl:template>
  
  <!-- Quotes -->

  <xsl:template match="quote[@rend='block']">
    <blockquote>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang"/>
      </xsl:call-template>
    </blockquote>
  </xsl:template>

  <xsl:template match="lg">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang"/>
    </xsl:call-template>
  </xsl:template>

</xsl:stylesheet>
