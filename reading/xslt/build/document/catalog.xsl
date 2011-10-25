<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:other="http://www.perseus.org/meta/perseus.rdfs#"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
		xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
		xmlns:dcterms="http://purl.org/dc/terms/"
		xmlns:perseus="http://www.perseus.org/meta/perseus.rdfs#"
		xmlns:persq="http://www.perseus.org/meta/persq.rdfs#"
		xmlns:tufts="http://www.tufts.edu/"
		exclude-result-prefixes="dc dcterms perseus rdf persq tufts"
		version="1.0">

    <xsl:import href="citation.xsl" />
    
    <xsl:output method="xml" indent="yes" encoding="utf-8"
		omit-xml-declaration="yes" />


    <!-- 
	We use the "other:" namespace below to capture elements in the same
	namespace in our RDF catalogs. Simply writing "sort" won't match
	anything; we need to search for "sort" in the specific namespace
	in which the catalogs have it.
    -->

    <xsl:template match="rdf:RDF">
	<metadata>
	    <xsl:apply-templates />
	</metadata>
    </xsl:template>

    <xsl:template match="rdf:Description">
	<document id="{@rdf:about}">
	    <xsl:apply-templates />
	</document>
    </xsl:template>
    
    <xsl:template match="dcterms:created">
    <xsl:call-template name="create-element">
    	<xsl:with-param name="nodename" select="'dc:Date_Created'" />
    </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="dcterms:issued">
    <xsl:call-template name="create-element">
    	<xsl:with-param name="nodename" select="'dc:Date_Issued'" />
    </xsl:call-template>
    </xsl:template>

    <xsl:template match="dcterms:alternative">
	<xsl:call-template name="create-element">
	    <xsl:with-param name="nodename" select="'dc:Title_Alternative'" />
	    <xsl:with-param name="lang_attr" select="@xml_lang" />
	</xsl:call-template>
    </xsl:template>

    <xsl:template match="dcterms:language">
	<xsl:call-template name="create-element">
	    <xsl:with-param name="nodename"
			    select="'dc:Subject_Language'" />
	</xsl:call-template>
    </xsl:template>

    <xsl:template match="dcterms:isLexiconTo[@rdf:resource]">
	<xsl:call-template name="create-element">
	    <xsl:with-param name="nodename"
			    select="'dc:Relation_IsLexiconTo'" />
	    <xsl:with-param name="valueid_attr" select="@rdf:resource" />
	</xsl:call-template>
    </xsl:template>

    <!-- IsCommentaryOn tags come in pairs, one indicating the target work
	 and another indicating the commentary scheme, which should be
	 combined into one tag by the time they're put into the database.
	 Match the one indicating the target work, then try to look for
	 the one with the scheme.
     -->
    <xsl:template match="dcterms:isCommentaryOn[@rdf:resource]">
	<xsl:element name="dc:Relation_IsCommentaryOn">
	    <xsl:if test="@rdf:resource">
		<xsl:attribute name="valueid">
		    <xsl:value-of select="@rdf:resource" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of
		select="preceding-sibling::dcterms:isCommentaryOn |
			following-sibling::dcterms:isCommentaryOn" />
	    <xsl:value-of select="." />
	</xsl:element>
    </xsl:template>

    <xsl:template match="dcterms:isCommentaryOnSummary[@rdf:resource]">
	<xsl:element name="perseus:IsCommentaryOnSummary">
	    <xsl:if test="@rdf:resource">
		<xsl:attribute name="valueid">
		    <xsl:value-of select="@rdf:resource" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of
		select="preceding-sibling::dcterms:isCommentaryOnSummary |
			following-sibling::dcterms:isCommentaryOnSummary" />
	    <xsl:value-of select="." />
	</xsl:element>
    </xsl:template>
   	
    <xsl:template match="dc:subject[persq:arity]">
	<dc:Type schema="perseus:Arity">
	    <xsl:value-of select="persq:arity" />
	</dc:Type>
    </xsl:template>

    <xsl:template match="dc:type[persq:form]">
	<dc:Type schema="perseus:Form">
	    <xsl:value-of select="persq:form" />
	</dc:Type>
    </xsl:template>

    <xsl:template match="other:sort">
	<perseus:Sort>
	    <xsl:value-of select="." />
	</perseus:Sort>
    </xsl:template>

    <xsl:template match="dcterms:available">
	<dc:Date_Available><xsl:value-of select="." /></dc:Date_Available>
    </xsl:template>

    <xsl:template match="dcterms:isVersionOf">
	<dc:Relation_IsVersionOf valueid="{@rdf:resource}" />
    </xsl:template>
    
    <xsl:template match="dcterms:isIntroductionTo">
	<dc:Relation_IsIntroductionTo valueid="{@rdf:resource}" />
    </xsl:template>

    <xsl:template match="dcterms:isPartOf">
	<dc:Relation_IsPartOf valueid="{@rdf:resource}" />
    </xsl:template>

    <xsl:template match="other:text">
	<perseus:SourceText><xsl:value-of select="." /></perseus:SourceText>
    </xsl:template>

    <xsl:template match="other:pages">
	<perseus:SourcePages><xsl:value-of select="." /></perseus:SourcePages>
    </xsl:template>

    <xsl:template match="other:figures">
	<perseus:SourceFigures><xsl:value-of select="." /></perseus:SourceFigures>
    </xsl:template>

    <xsl:template match="other:status">
	<perseus:Status><xsl:value-of select="." /></perseus:Status>
    </xsl:template>

    <xsl:template match="dc:source[@rdf:resource]">
	<dc:Source valueid="{@rdf:resource}" />
    </xsl:template>

    <!-- Ignore dc:descriptions with resource attributes for now - they
	 exist for collections, and seem to point to a document ID that
	 serves as a description of the collection. The old hopper doesn't
	 seem to use them either. -->
    <xsl:template match="dc:description[not(@rdf:resource)]">
	<xsl:element name="dc:Description">
	    <!--
	    <xsl:if test="@rdf:resource">
		<xsl:attribute name="valueid">
		    <xsl:value-of select="@rdf:resource" />
		</xsl:attribute>
	    </xsl:if>
	    -->
	    <xsl:if test="@xml:lang">
		<xsl:attribute name="lang">
		    <xsl:value-of select="@xml:lang" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of select="." />
	</xsl:element>
    </xsl:template>

    <xsl:template match="persq:stylesheet">
	<perseus:Layout_Stylesheet>
	    <xsl:value-of select="." />
	</perseus:Layout_Stylesheet>
    </xsl:template>

    <xsl:template match="persq:template">
	<perseus:Layout_Template>
	    <xsl:value-of select="." />
	</perseus:Layout_Template>
    </xsl:template>

    <xsl:template name="create-element">
	<xsl:param name="node" select="." />
	<xsl:param name="nodename" select="'FRED'" />
	<xsl:param name="valueid_attr" select="''" />
	<xsl:param name="schema_attr" select="''" />
	<xsl:param name="lang_attr" select="''" />

	<xsl:element name="{$nodename}">
	    <xsl:if test="$valueid_attr">
		<xsl:attribute name="valueid">
		    <xsl:value-of select="$valueid_attr" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:if test="$schema_attr">
		<xsl:attribute name="schema">
		    <xsl:value-of select="$schema_attr" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:if test="$lang_attr">
		<xsl:attribute name="lang">
		    <xsl:value-of select="$lang_attr" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of select="text()" />
	</xsl:element>
    </xsl:template>

    <xsl:template match="dc:title">
	<xsl:element name="dc:Title">
	    <xsl:if test="@xml:lang">
		<xsl:attribute name="lang">
		    <xsl:value-of select="@xml:lang" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of select="." />
	</xsl:element>
    </xsl:template>

    <xsl:template match="dc:creator">
	<xsl:element name="dc:Creator">
	    <xsl:if test="@xml:lang">
		<xsl:attribute name="lang">
		    <xsl:value-of select="@xml:lang" />
		</xsl:attribute>
	    </xsl:if>
	    <xsl:value-of select="." />
	</xsl:element>
    </xsl:template>

    <xsl:template match="dc:subject[persq:corpusType]">
	<perseus:corpusType>
	    <xsl:value-of select="persq:corpusType" />
	</perseus:corpusType>
    </xsl:template>

    <xsl:template match="perseus:view">
	<perseus:view valueid="{@rdf:resource}">
	    <xsl:value-of select="." />
	</perseus:view>
    </xsl:template>

    <xsl:template match="perseus:isSummaryOf">
	<perseus:isSummaryOf valueid="{@rdf:resource}">
	    <xsl:value-of select="." />
	</perseus:isSummaryOf>
    </xsl:template>
       
    <xsl:template match="other:citation | perseus:citation">
	<perseus:Citation><xsl:value-of select="." /></perseus:Citation>
    </xsl:template>

    <!--
    <xsl:template match="dc:type">
	<dc:Type><xsl:value-of select="." /></dc:Type>
    </xsl:template>

    <xsl:template match="dc:subject">
	<dc:Subject><xsl:value-of select="." /></dc:Subject>
    </xsl:template>

    <xsl:template match="dc:description">
	<dc:Description><xsl:value-of select="." /></dc:Description>
    </xsl:template>
    -->

    <!--
	Some elements can go in as is. Check for them here.
    -->
    <xsl:template match="node()|@*">
	<xsl:if test="not(name() = 'dc:title'
		    or name() = 'dc:creator'
		    or name() = 'dc:Rights'
		    or name() = 'dc:type'
		    or name() = 'dc:language'
		    or name() = 'dc:subject'
		    or name() = 'perseus:ViewTitle'
		    or (name() = 'dcterms:isCommentaryOn'
			and not(@rdf:resource))
		    or (name() = 'dcterms:isCommentaryOnSummary'
			and not(@rdf:resource))
		    or name() = '')">
	    <xsl:message>Unknown node! [<xsl:value-of select="name()"/>]  = <xsl:value-of select="." /></xsl:message>
	</xsl:if>
	<xsl:if test="name()">
	    <xsl:copy-of select="." />
	</xsl:if>
    </xsl:template>
</xsl:stylesheet>
