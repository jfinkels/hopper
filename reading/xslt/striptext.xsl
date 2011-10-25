<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ptext="ptext"
                xmlns="http://www.w3.org/TR/xhtml1/strict">

<xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
     <div>
     <xsl:apply-templates select="ptext:pack|ptext:front|/TEI.2/text/front|/tei.2/text/front|ptext:body|/TEI.2/text/body|ptext:back|/tei.2/text/back|/TEI.2/text/back|/TEI.2/text/group/text"/>
     </div>
  </xsl:template>

<xsl:template match="body">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="text">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="group">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="l">
    <xsl:apply-templates /><xsl:text> </xsl:text>
</xsl:template>

<xsl:template match="name">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="div|div0|div1|div2|div3|div4|div5|div6">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
</xsl:template>

<xsl:template match="speaker">
</xsl:template>

<xsl:template match="cit">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="q">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="quote">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
    <xsl:text> </xsl:text>
</xsl:template>

<!-- If we don't add whitespace here, we may end up with the words on either
    side of the tag smooshed together -->
<xsl:template match="br">
    <xsl:text> </xsl:text>
</xsl:template>
<xsl:template match="note">
    <xsl:text> </xsl:text>
</xsl:template>
<xsl:template match="milestone">
    <xsl:text> </xsl:text>
</xsl:template>
<xsl:template match="head">
    <xsl:text> </xsl:text>
</xsl:template>
<xsl:template match="bibl">
    <xsl:text> </xsl:text>
</xsl:template>

<xsl:template match="p">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
</xsl:template>

  <xsl:template match="foreign">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

<xsl:template match="castList"></xsl:template>
<xsl:template match="stage"></xsl:template>
<xsl:template match="title"></xsl:template>

  <xsl:template name="language-filter">
    <xsl:param name="lang" />
    <xsl:choose>
      <xsl:when test="$lang='la'">
        <L><xsl:apply-templates /></L>
      </xsl:when>
      <xsl:when test="$lang='lat'">
        <L><xsl:apply-templates /></L>
      </xsl:when>
      <xsl:when test="$lang='latin'">
        <L><xsl:apply-templates /></L>
      </xsl:when>
      <xsl:when test="$lang='gk'">
        <G><xsl:apply-templates /></G>
      </xsl:when>
      <xsl:when test="$lang='greek'">
        <G><xsl:apply-templates /></G>
      </xsl:when>
      <xsl:when test="$lang='el'">
        <G><xsl:apply-templates /></G>
      </xsl:when>
      <xsl:when test="$lang='it'">
        <IT><xsl:apply-templates /></IT>
      </xsl:when>
      <xsl:when test="$lang='ar'">
        <AR><xsl:apply-templates /></AR>
      </xsl:when>
      <xsl:when test="$lang='en'">
        <E><xsl:apply-templates /></E>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates /> 
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


</xsl:stylesheet>
