<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		version="1.0">
    
    <xsl:output method="xml" indent="yes" encoding="utf-8"
		omit-xml-declaration="yes" />
    
    <xsl:param name="current_query" select="''" />
    <xsl:param name="default_link_type" select="'text'" />

    <xsl:template match="/">
	<citations>
	    <xsl:apply-templates />
	</citations>
    </xsl:template>

    <xsl:template match="//cit[bibl[@n]]">
	<xsl:call-template name="create-citation">
	    <xsl:with-param name="destination_query" select="bibl/@n" />

	    <!--
		Mark links inside <cit> tags as such, but only if we're looking
		at a generic text and not a lexicon or commentary (in which
		case the "from lexicon/commentary" category trumps the
		"cit-tag" category).
	    -->
	    <xsl:if test="$default_link_type = 'text'">
		<xsl:with-param name="link-type" select="'cit'" />
	    </xsl:if>
	</xsl:call-template>
    </xsl:template>

    <!--
	This will match bibls that aren't actually part of citations, of which
	there are lots.
    -->
    <xsl:template match="bibl[@n and not(ancestor::cit)]">
	<xsl:call-template name="create-citation">
	    <xsl:with-param name="destination_query" select="@n" />
	</xsl:call-template>
    </xsl:template>

    <xsl:template name="create-citation">
	<xsl:param name="destination_query" select="''" />
	<xsl:param name="link-type" select="$default_link_type" />

	<citation>
	    <source><xsl:value-of select="$current_query" /></source>
	    <destination><xsl:value-of select="$destination_query" /></destination>
	    <linkType>
		<xsl:choose>
		    <xsl:when test="ancestor::note">note</xsl:when>
		    <xsl:when test="ancestor::*[@type='index']">index</xsl:when>
		    <xsl:otherwise><xsl:value-of select="$link-type" /></xsl:otherwise>
		</xsl:choose>
	    </linkType>
	</citation>
    </xsl:template>

    <xsl:template match="quote[parent::cit or following::bibl[@n]]">
	<xsl:value-of select="." />
    </xsl:template>

    <xsl:template match="oRef">
	<oRef />
    </xsl:template>
    <xsl:template match="gap">
	<gap />
    </xsl:template>

    <xsl:template match="text()" />
</xsl:stylesheet>
