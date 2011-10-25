<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="wikiArticle">
		<xsl:variable name="articlePath"><xsl:text>http://ironmonkey.perseus.tufts.edu/staged/wiki/index.php/Special:Export_HTML?action=submit&amp;curonly=true&amp;pages=</xsl:text><xsl:value-of select="//pages"/></xsl:variable>		
		<xsl:apply-templates select="document($articlePath)//text"/>
	</xsl:template>

	<xsl:template match="text">
		<xsl:apply-templates mode="copy"/>
		<br/><br/>
	</xsl:template>
		
	<xsl:template match="span[./@class='urlexpansion']" mode="copy"/>	
	<xsl:template match="div[./@class='editsection']" mode="copy"/>
	
	<xsl:template match="image" mode="copy">
		<div class="wikiImage">
		<xsl:element name="img">
			<xsl:attribute name="src"><xsl:value-of select="./a"/></xsl:attribute>
		</xsl:element>		
		</div>
	</xsl:template>

	<xsl:template match="@*|node()" mode="copy">
		<xsl:copy>
			<xsl:apply-templates select="@*" mode="copy"/>
			<xsl:apply-templates mode="copy"/>
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
