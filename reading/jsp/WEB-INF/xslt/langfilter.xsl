<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template name="language-filter">
    <xsl:param name="lang" />
    <xsl:param name="default" select="''" />

    <xsl:choose>
      <xsl:when test="$lang='la'">
        <span class="la"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='lat'">
        <span class="la"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='latin'">
        <span class="la"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='gk'">
        <span class="greek"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='greek'">
        <span class="greek"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='el'">
        <span class="greek"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='it'">
        <span class="it"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='ar'">
        <span class="ar"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$lang='en'">
        <span class="en"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:when test="$default != ''">
	<span class="{$default}"><xsl:apply-templates /></span>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates /> 
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
