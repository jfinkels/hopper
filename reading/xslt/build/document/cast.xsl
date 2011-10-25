<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
		xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
		xmlns:dcterms="http://purl.org/dc/terms/"
		xmlns:perseus="http://www.perseus.org/meta/perseus.rdfs#"
		exclude-result-prefixes="dc dcterms perseus rdf"
		version="1.0">
    <xsl:import href="citation.xsl" />

    <xsl:strip-space elements="*" />
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"
		encoding="utf-8" />

    <xsl:param name="document_id" select="''" />

	
    <!--
perseus:speaker //castList//role
    -->

    <xsl:template match="castList">
	<metadata>
	    <document id="{$document_id}">
		<xsl:apply-templates />
	    </document>
	</metadata>
    </xsl:template>

    <!-- Ignore refsDecl elements concerning subdocuments; they've already
	 been dealt with.
     -->
    <xsl:template match="role">
	<perseus:Speaker>
	    <xsl:copy-of select="." />
	</perseus:Speaker>
    </xsl:template>
</xsl:stylesheet>


