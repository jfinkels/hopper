<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
	  <GetPassage>
	    <xsl:apply-templates select="//GetPassage/request"/>
	    <TEIFrag>
	      <xsl:apply-templates select="//TEI.2"/>
	    </TEIFrag>
	  </GetPassage>
	</xsl:template>

	<xsl:template match="TEI.2">
	  <xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="*">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
