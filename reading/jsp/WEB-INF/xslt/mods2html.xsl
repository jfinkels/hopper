<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:mods="http://www.loc.gov/mods/v3"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    exclude-result-prefixes="xlink mods xsl">

<!--
    To get this to work, I had to change "xmlns=blah" to "xmlns:mods=blah"
    in the MODS XMl file. No other changes were necessary, aside from
    cleaning up the random textits and emphs.
-->

<xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" />

<xsl:param name="sort_by" select="'name'" />

<xsl:template match="mods:modsCollection">
    <ul class="publications">
	<xsl:choose>
	    <xsl:when test="$sort_by = 'date'">
		<xsl:apply-templates select="mods:mods">
		    <xsl:sort select="mods:originInfo/mods:dateIssued"
				order="descending" />
		</xsl:apply-templates>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:apply-templates select="mods:mods">
		    <xsl:sort select="mods:name/mods:namePart[@type='family']" />
		    <xsl:sort select="mods:name/mods:namePart[@type='given'][1]" />
		</xsl:apply-templates>
	    </xsl:otherwise>
	</xsl:choose>
    </ul>
</xsl:template>

<xsl:template match="mods:mods">
    <li id="pub-{@ID}">
	<xsl:call-template name="printAuthors" />
	<xsl:text> </xsl:text>

	<xsl:call-template name="printTitle" />
	<xsl:text> </xsl:text>

	<xsl:if test="mods:relatedItem">
	    <xsl:call-template name="printSourceInfo" />
	</xsl:if>

	<xsl:call-template name="printLinks" />
    </li>
    <xsl:text>
</xsl:text>
</xsl:template>

<xsl:template name="printAuthors">
    <xsl:for-each select="mods:name">
	<xsl:call-template name="printName" />
	<xsl:choose>
	    <xsl:when test="position() = last()">
		<xsl:text>.</xsl:text>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:text>, </xsl:text>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:for-each>
</xsl:template>

<xsl:template name="printTitle">
    <xsl:apply-templates select="mods:titleInfo" />
    <xsl:text>.</xsl:text>
</xsl:template>

<xsl:template name="printSourceInfo">
    <xsl:text>In </xsl:text>
    <xsl:for-each select="mods:relatedItem[@type='host']">
	<cite><xsl:apply-templates select="mods:titleInfo" /></cite>

	<xsl:if test="mods:part/mods:detail or mods:part/mods:extent">
	    <xsl:text>, </xsl:text>
	    <xsl:call-template name="printPart" />
	</xsl:if>

	<xsl:choose>
	    <xsl:when test="mods:originInfo/mods:place">
		<xsl:text>, </xsl:text>
		<xsl:apply-templates select="mods:originInfo/mods:place" />

		<xsl:if test="mods:originInfo/mods:publisher">
		    <xsl:text>: </xsl:text>
		    <xsl:apply-templates select="mods:originInfo/mods:publisher" />
		</xsl:if>
	    </xsl:when>
	    <xsl:when test="mods:originInfo/mods:publisher">
		<xsl:text>, </xsl:text>
		<xsl:apply-templates select="mods:originInfo/mods:publisher" />
	    </xsl:when>
	</xsl:choose>

	<xsl:if test="mods:part/mods:date">
	    <xsl:text>, </xsl:text>
	    <xsl:apply-templates select="mods:part/mods:date" />
	</xsl:if>

	<xsl:if test="position() != last()">
	    <xsl:text>, </xsl:text>
	</xsl:if>
    </xsl:for-each>
    <xsl:text>.</xsl:text>
</xsl:template>

<xsl:template name="printName">
    <xsl:for-each select="mods:namePart">
	<xsl:apply-templates />
	<xsl:if test="position() != last()">
	    <xsl:if test="string-length(.) = 1">
		<!-- Assume that one-character names represent initials;
		     add a period except for the last namePart, which will have
		     one put after it anyway -->
		<xsl:text>.</xsl:text>
	    </xsl:if>
	    <xsl:text> </xsl:text>
	</xsl:if>
    </xsl:for-each>
</xsl:template>

<xsl:template name="printLinks">
    <xsl:if test="mods:abstract/@xlink:href">
	<xsl:text> (</xsl:text>
	<a href="{mods:abstract/@xlink:href}">Abstract</a>
	<xsl:text>)</xsl:text>
    </xsl:if>

    <xsl:if test="mods:location/mods:url">
	<xsl:text> (</xsl:text>
	<a href="{mods:location/mods:url}">Full text</a>
	<xsl:text>)</xsl:text>
    </xsl:if>

    <xsl:if test="mods:relatedItem/mods:location/mods:url">
	<xsl:text> (</xsl:text>
	<a href="{mods:relatedItem/mods:location/mods:url}">Full text</a>
	<xsl:text>)</xsl:text>
    </xsl:if>
</xsl:template>

<xsl:template match="mods:titleInfo">
    <xsl:apply-templates select="mods:title" />
    <xsl:if test="mods:subTitle">
	<xsl:text>: </xsl:text>
	<xsl:apply-templates select="mods:subTitle" />
    </xsl:if>
</xsl:template>

<xsl:template name="printPart">
    <xsl:for-each select="mods:part">
	<xsl:choose>
	    <xsl:when test="mods:detail[@type='volume']">
		<xsl:apply-templates select="mods:detail[@type='volume']" />
		<xsl:if test="mods:detail[@type='number']">
		    <xsl:text>(</xsl:text>
		    <xsl:apply-templates select="mods:detail[@type='number']" />
		    <xsl:text>)</xsl:text>
		</xsl:if>
		<xsl:apply-templates select="child::*[@type='page']" />
	    </xsl:when>
	    <xsl:when test="mods:detail[@type='page']">
		<xsl:text>page </xsl:text>
		<xsl:apply-templates select="mods:detail[@type='page']" />
	    </xsl:when>
	    <xsl:when test="mods:extent[@unit='page']">
		<xsl:text>pages </xsl:text>
		<xsl:apply-templates select="mods:extent[@unit='page']" />
	    </xsl:when>
	    <xsl:otherwise>
		<!-- ...this shouldn't happen... -->
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:for-each>
</xsl:template>

<xsl:template match="mods:extent">
    <xsl:apply-templates select="mods:start" />
    <xsl:text>-</xsl:text>
    <xsl:apply-templates select="mods:end" />
</xsl:template>

<xsl:template match="cite|mods:cite">
    <cite><xsl:apply-templates /></cite>
</xsl:template>

<xsl:template match="cite|mods:em">
    <em><xsl:apply-templates /></em>
</xsl:template>

</xsl:stylesheet>
