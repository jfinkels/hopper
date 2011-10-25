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
Current practices for translating TEI into Dublin Core/Perseus metadata:
dc:title /teiHeader/fileDesc/titleStmt/title
dc:creator /teiHeader/fileDesc/titleStmt/author
dc:contributor /teiHeader/fileDesc/titleStmt/editor
perseus:citation This comes from the encodingDesc/refsDecl element. There are two varieties: step-based and state-based.
dc:language //langUsage/language[1][@id]
Language is also guessed from filenames: "_eng", "_gk", "_lat". (Deprecated)
perseus:funder //funder
dc:source Two varieties: //sourceDesc//idno for ISBNs and OCLC numbers, and //sourceDesc otherwise (free text)
perseus:method //editorialDecl/correction/p
perseus Correction Level //editorialDesc/correction
perseus:segmentation //editorialDecl/segmentation/p   To reflect the changes/addition of breaks in the text

Three elements look beyond the TEI header:

perseus:speaker //castList//role
perseus:witness //witList//witness
dcterms:haspart //figure

(We're ignoring these.)
    -->

    <xsl:template match="teiHeader">
	<metadata>
	    <!-- Some documents, like Cicero, have different referencing
		 schemes for different parts of the text. Output the
		 information for each document separately.
	    -->
	    <xsl:for-each select="//refsDecl[@n]">
		<xsl:call-template name="print-subdoc">
		    <xsl:with-param name="subdoc" select="@n" />
		</xsl:call-template>
	    </xsl:for-each>
	    <document id="{$document_id}">
		<xsl:apply-templates />
	    </document>
	</metadata>
    </xsl:template>

    <xsl:template name="print-subdoc">
	<xsl:param name="subdoc" select="''" />

	<xsl:variable name="other-subdocs"
		      select="substring-after($subdoc, ';')" />
	<xsl:variable name="next-subdoc">
	    <xsl:choose>
		<xsl:when test="string-length($other-subdocs) &gt; 0">
		    <xsl:value-of select="substring-before($subdoc, ';')" />
		</xsl:when>
		<xsl:otherwise>
		    <xsl:value-of select="$subdoc" />
		</xsl:otherwise>
	    </xsl:choose>
	</xsl:variable>


	<xsl:if test="string-length($next-subdoc) &gt; 0">
	    <document id="{$document_id}:{$next-subdoc}">
		<xsl:apply-templates select="." />
	    </document>
	    <xsl:call-template name="print-subdoc">
		<xsl:with-param name="subdoc" select="$other-subdocs" />
	    </xsl:call-template>
	</xsl:if>
    </xsl:template>
    <!-- Ignore refsDecl elements concerning subdocuments; they've already
	 been dealt with.
     -->
    <xsl:template match="encodingDesc">
	<xsl:apply-templates select="child::*[not(name() = 'refsDecl' and @n)]" />
    </xsl:template>

    <xsl:template match="teiHeader/fileDesc/titleStmt/title">
	<dc:Title n="{count(preceding::title)}">
	    <xsl:call-template name="space_descending_text" />
	</dc:Title>
    </xsl:template>

    <xsl:template match="teiHeader/fileDesc/titleStmt/author">
	<!-- <dc:creator n="{count(preceding::author)}"><xsl:value-of select="normalize-space(.)"/></dc:creator> -->
	<dc:Creator n="{count(preceding::author)}">
	    <xsl:call-template name="space_descending_text" />
	</dc:Creator>
    </xsl:template>

    <xsl:template match="teiHeader/fileDesc/titleStmt/editor">
	<dc:Contributor n="{count(preceding::editor)}">
	    <xsl:call-template name="space_descending_text" />
	</dc:Contributor>
    </xsl:template>

    <xsl:template match="editor">
	<dc:Contributor n="{count(preceding::editor)}">
	    <xsl:call-template name="space_descending_text" />
	</dc:Contributor>
    </xsl:template>

    <xsl:template name="space_descending_text">
	<xsl:for-each select="descendant::text()">
	    <xsl:value-of select="normalize-space(.)"/>
	    <xsl:if test="position() != last()">
		<xsl:text> </xsl:text>
	    </xsl:if>
	</xsl:for-each>
    </xsl:template>

    <xsl:template match="//langUsage">
	<dc:Language><xsl:value-of select="language[1]/@id" /></dc:Language>
    </xsl:template>

    <xsl:template match="//funder">
	<xsl:element name="perseus:Funder">
	    <xsl:if test="@n">
		<xsl:attribute name="id">
		    <xsl:value-of select="@n" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of select="normalize-space(.)" />
	</xsl:element>
    </xsl:template>

    <xsl:template name="printIdno">
	<xsl:variable name="type">
	    <xsl:call-template name="getType" />
	</xsl:variable>
	<xsl:variable name="contents">
	    <xsl:value-of select="normalize-space(.)" />
	</xsl:variable>
	<dc:Source rdf:resource="Perseus:bib:{$type},{$contents}"
		n="{count(preceding::idno)}" />
    </xsl:template>

    <xsl:template match="//sourceDesc">
	<xsl:variable name="sourceText">
	    <xsl:call-template name="printSourceText" />
	</xsl:variable>
	<xsl:if test="string-length(normalize-space($sourceText)) &gt; 0">
	    <dc:Source><xsl:value-of select="$sourceText" /></dc:Source>
	</xsl:if>
	<xsl:for-each select="descendant::idno">
	    <xsl:call-template name="printIdno" />
	</xsl:for-each>

	<xsl:if test="not(//teiHeader/fileDesc/titleStmt/editor)">
	    <xsl:apply-templates select="descendant::editor" />
	</xsl:if>

	<xsl:apply-templates />
    </xsl:template>

    <xsl:template match="//monogr/imprint/date">
	<dc:Date_Copyrighted>
	    <xsl:value-of select="." />
	</dc:Date_Copyrighted>
    </xsl:template>

    <xsl:template name="getType">
	<!-- Normalize the type to lower-case, as that's what the Perl script
	     seems to do. -->
	<xsl:choose>
	    <xsl:when test="@type">
		<xsl:value-of select="translate(@type,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />
	    </xsl:when>
	    <xsl:otherwise>oclc</xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="printSourceText">
	<!-- Attempt to format the source description nicely and ignore
	     values that'll be caught by the //idno template -->
	<xsl:for-each select="descendant::*[name() != 'idno']/text()">
	    <xsl:variable name="normalized" select="normalize-space(.)" />
	    <xsl:value-of select="$normalized" />
	    <!-- Print a period after each text node, unless the current node
		 ends in a period -->
	    <xsl:if test="not(contains(substring($normalized, string-length($normalized)), '.'))">
		<xsl:text>.</xsl:text>
	    </xsl:if>
	    <xsl:if test="position() != last()">
		<xsl:text> </xsl:text>
	    </xsl:if>
	</xsl:for-each>
    </xsl:template>

    <xsl:template match="correction">
	<perseus:CorrectionLevel><xsl:value-of select="normalize-space(@status)"/></perseus:CorrectionLevel>
	<xsl:for-each select="p">
	    <perseus:Method><xsl:value-of select="normalize-space(.)"/></perseus:Method>
	</xsl:for-each>
    </xsl:template>
    
     <xsl:template match="segmentation">
	<xsl:for-each select="p">
	    <perseus:Segmentation><xsl:value-of select="normalize-space(.)"/></perseus:Segmentation>
	</xsl:for-each>
    </xsl:template>

    <!-- For the time being, we won't match the following three templates,
	 because we're restricting our parsing to the header of the document.
    -->
    <xsl:template match="//castList">
	<xsl:for-each select="descendant::role">
	    <xsl:choose>
		<xsl:when test="@id">
		    <perseus:Speaker n="{position()-1}" id="{@id}"><xsl:value-of select="normalize-space(.)"/></perseus:Speaker>
		</xsl:when>
		<xsl:otherwise>
		    <perseus:Speaker n="{position()-1}"><xsl:value-of select="normalize-space(.)"/></perseus:Speaker>
		</xsl:otherwise>
	    </xsl:choose>
	</xsl:for-each>
    </xsl:template>

    <xsl:template match="//witList//witness">
	<perseus:Witness id="{@sigil}"><xsl:value-of select="normalize-space(.)"/></perseus:Witness>
    </xsl:template>

    <xsl:template match="//figure[@n]">
	<dc:Relation.HasPart id="{@n}"><xsl:value-of select="normalize-space(.)"/></dc:Relation.HasPart>
    </xsl:template>

    <xsl:template match="text()"></xsl:template>
</xsl:stylesheet>
