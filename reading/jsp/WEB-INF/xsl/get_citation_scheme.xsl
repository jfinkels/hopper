<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
	  <xsl:variable name="edition"><xsl:value-of select="//GetCitationScheme/request/edition"/></xsl:variable>
	  <GetCitationScheme>
	    <xsl:apply-templates select="//GetCitationScheme/request"/>
	    <xsl:apply-templates select="//editioncomments[@projid = $edition]"/>
	    <xsl:apply-templates select="//translationcomments[@projid = $edition]"/>
	  </GetCitationScheme>
	</xsl:template>

	<xsl:template match="editioncomments">
	  <xsl:apply-templates select="./online/citation"/>
	</xsl:template>

	<xsl:template match="translationcomments">
	  <xsl:apply-templates select="./online/citation"/>
	</xsl:template>

	<xsl:template match="*">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
