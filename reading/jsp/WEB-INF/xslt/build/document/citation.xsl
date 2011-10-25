<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
		xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
		xmlns:dcterms="http://purl.org/dc/terms/"
		xmlns:perseus="http://www.perseus.org/meta/perseus.rdfs#"
		exclude-result-prefixes="dc dcterms perseus rdf"
		version="1.0">
    <xsl:strip-space elements="*" />
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"
		encoding="utf-8" />

    <xsl:template match="refsDecl">
	<xsl:variable name="refText">
	    <xsl:call-template name="getRefs" />
	</xsl:variable>
	<xsl:variable name="lcRefText">
	    <xsl:value-of select="translate($refText,
				    'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
				    'abcdefghijklmnopqrstuvwxyz')" />
	</xsl:variable>

	<xsl:element name="perseus:Citation">
	    <xsl:attribute name="n">
		<xsl:value-of select="count(preceding::refsDecl)" />
	    </xsl:attribute>
	    <xsl:value-of select="normalize-space($lcRefText)" />
	</xsl:element>
    </xsl:template>

    <xsl:template name="getRefs">
	<xsl:choose>
	    <xsl:when test="state">
		<xsl:call-template name="getRefsHelper">
		    <xsl:with-param name="node" select="state[1]" />
		</xsl:call-template>
	    </xsl:when>
	    <xsl:when test="step">
		<xsl:call-template name="getRefsHelper">
		    <xsl:with-param name="node" select="step[1]" />
		</xsl:call-template>
	    </xsl:when>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="getRefsHelper">
	<xsl:param name="runningRefs" select="''" />
	<xsl:param name="node" select="null" />

	<xsl:variable name="refText">
	    <xsl:call-template name="getRefText">
		<xsl:with-param name="node" select="$node" />
	    </xsl:call-template>
	</xsl:variable>

	<xsl:choose>
	    <xsl:when test="count($node/following-sibling::*) = 0">
		<xsl:choose>
		    <xsl:when test="$runningRefs != ''">
			<xsl:value-of select="concat($runningRefs, ':', $refText)" />
		    </xsl:when>
		    <xsl:otherwise>
			<xsl:value-of select="normalize-space($refText)" />
		    </xsl:otherwise>
		</xsl:choose>
	    </xsl:when>
	    <xsl:when test="string-length($runningRefs) &gt; 0">
		<xsl:call-template name="getRefsHelper">
		    <xsl:with-param name="runningRefs"
			select="concat($runningRefs,':',$refText)" />
		    <xsl:with-param name="node" select="$node/following-sibling::*[1]" />
		</xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:call-template name="getRefsHelper">
		    <xsl:with-param name="runningRefs" select="$refText" />
		    <xsl:with-param name="node" select="$node/following-sibling::*[1]" />
		</xsl:call-template>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="getRefText">
	<xsl:param name="node" select="''" />

	<xsl:variable name="unit">
	    <xsl:call-template name="getUnit">
		<xsl:with-param name="node" select="$node" />
	    </xsl:call-template>
	</xsl:variable>

	<xsl:variable name="chunkedUnit">
	    <xsl:call-template name="concat-ifnotnull">
		<xsl:with-param name="test" select="contains($node/@n, 'chunk')" />
		<xsl:with-param name="base" select="$unit" />
		<xsl:with-param name="suf" select="'*'" />
	    </xsl:call-template>
	</xsl:variable>
	<xsl:variable name="persistUnit">
	    <xsl:call-template name="concat-ifnotnull">
		<xsl:with-param name="test" select="contains($node/@n, 'persist')" />
		<xsl:with-param name="base" select="$chunkedUnit" />
		<xsl:with-param name="suf" select="'!'" />
	    </xsl:call-template>
	</xsl:variable>
	<xsl:variable name="contextUnit">
	    <xsl:call-template name="concat-ifnotnull">
		<xsl:with-param name="test" select="contains($node/@n, 'context')" />
		<xsl:with-param name="base" select="$persistUnit" />
		<xsl:with-param name="suf" select="'+'" />
	    </xsl:call-template>
	</xsl:variable>

	<xsl:value-of select="$contextUnit" />
    </xsl:template>

    <xsl:template name="getUnit">
	<xsl:param name="node" select="null" />

	<xsl:choose>
	    <xsl:when test="$node/@refunit">
		<xsl:value-of select="$node/@refunit" />
	    </xsl:when>
	    <xsl:when test="$node/@ed and $node/@unit">
		<xsl:value-of select="$node/@ed" /> <xsl:value-of select="$node/@unit" />
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="$node/@unit" />
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="concat-ifnotnull">
	<xsl:param name="test" select="false" />
	<xsl:param name="base" select="''" />
	<xsl:param name="suf" select="$test" />

	<xsl:choose>
	    <xsl:when test="$test">
		<xsl:value-of select="concat($base, $suf)" />
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="$base" />
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

</xsl:stylesheet>
