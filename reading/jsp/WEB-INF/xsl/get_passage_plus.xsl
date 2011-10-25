<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="/">
	  <xsl:variable name="edition"><xsl:value-of select="//GetPassagePlus/request/edition"/></xsl:variable>
	  <GetPassagePlus>
	    <xsl:apply-templates select="//GetPassagePlus/request"/>
	    <teiHeader/>	    
	    <TEIFrag>
	      <xsl:apply-templates select="//TEI.2/text/body/*"/>
	    </TEIFrag>
	    <xsl:apply-templates select="//prevnext"/>
	    <xsl:apply-templates select="//editioncomments[@projid = $edition]"/>
	    <xsl:apply-templates select="//translationcomments[@projid = $edition]"/>
	  </GetPassagePlus>
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

	<xsl:template match="comment()|processing-instruction()">
		<xsl:copy/>
	</xsl:template>

</xsl:stylesheet>
