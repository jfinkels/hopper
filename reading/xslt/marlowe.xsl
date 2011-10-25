<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <xsl:import href="tei.xsl"/>
  <xsl:preserve-space elements="*"/>

  <!-- We need the following speaker rule
       to pick up app tags: the default template 
       just renders the value. -->

 <xsl:template match="speaker">
    <xsl:if test="parent::sp">
      <p/>
    </xsl:if>
    <b><xsl:apply-templates/></b><br/>
  </xsl:template>


  <!-- We override the default footnotes
       template here and replace notes with
       critical notes -->
  <xsl:template name="footnotes">
    <hr/>
    <xsl:apply-templates select="//app" mode="appcrit"/>
  </xsl:template>

  <xsl:template name="appID">
    <xsl:number level="any" count="app"/>
  </xsl:template>

  <xsl:template match="app">
      <xsl:variable name="identifier">
        <xsl:call-template name="appID"/>
      </xsl:variable>
      <a href="#note{$identifier}">
        <xsl:apply-templates select="lem" />
      </a>
  </xsl:template>

  <xsl:template match="app" mode="footnote">
      <xsl:apply-templates select="lem"/>
      <xsl:variable name="identifier">
        <xsl:call-template name="appID"/>
      </xsl:variable>
      <a href="#note{$identifier}">
        <sup><xsl:value-of select="$identifier"/></sup>
      </a>
  </xsl:template>
  
  <xsl:template match="app" mode="appcrit">
    <div class="textual_note">
      <a name="note{position()}" />
      <xsl:text> </xsl:text>
      <xsl:value-of select="lem"/>
      <xsl:text>] </xsl:text>
      <ul class="rdg">
      <xsl:for-each select="rdg">
        <li>
        <xsl:apply-templates select="." mode="appcrit" />
        </li>
      </xsl:for-each>
    </ul>
    </div>
  </xsl:template>

  <xsl:template match="lem">
    <span class="lem"><xsl:value-of select="."/></span>
  </xsl:template>
  
  <xsl:template match="rdg" mode="appcrit">
    <span class="rdg"><xsl:value-of select="."/></span>
      <xsl:text> </xsl:text>
      <span class="witlist"><xsl:value-of select="@wit"/></span>
  </xsl:template>
  
  <xsl:template match="name">
    <cite><xsl:apply-templates/></cite>
  </xsl:template>
</xsl:stylesheet>
