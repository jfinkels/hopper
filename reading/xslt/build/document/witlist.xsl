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
perseus:witness //witList//witness
    -->

    <xsl:template match="witList">
	<metadata>
	    <document id="{$document_id}">
		<xsl:apply-templates />
	    </document>
	</metadata>
    </xsl:template>

    <!-- Ignore refsDecl elements concerning subdocuments; they've already
	 been dealt with.
     -->
    <xsl:template match="witness">
	<perseus:Witness valueid="{@sigil}">
	    <xsl:copy-of select="child::*|text()" />
	    <xsl:if test="@included">
		<!-- ...what should we do with these? Other than come up with
		     a more robust metadata format?
		-->
		INCL[<xsl:value-of select="@included" />]
	    </xsl:if>
	</perseus:Witness>
    </xsl:template>
</xsl:stylesheet>

