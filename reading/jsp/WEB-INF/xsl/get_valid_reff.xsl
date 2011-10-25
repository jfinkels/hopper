<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
	  <GetValidReff>
	    <xsl:apply-templates select="//contents/request"/>
	    <reff>
	      <xsl:apply-templates select="//chunk"/>
	    </reff>
	  </GetValidReff>
	</xsl:template>

	<xsl:template match="chunk">	  
	  <xsl:element name="ref">
	    <xsl:attribute name="element">?</xsl:attribute>
	    <xsl:attribute name="n"><xsl:value-of select="./@n"/></xsl:attribute>
	  </xsl:element>
	</xsl:template>

	<xsl:template match="*">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
