<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" encoding="utf-8"/>

    <xsl:include href="langfilter.xsl" />

<xsl:key name="incoming-cits" match="incoming/citation" use="linkType" />
<xsl:key name="outgoing-cits" match="outgoing/citation" use="linkType" />

<xsl:template match="citations">
    <div id="citations">
	<!--
	<p>Found <xsl:value-of select="count(//citation)" />
	references related to this page.</p>
	-->
	<!-- (moved this to the widget title bar) -->

	<ul>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'cit'" />
		<xsl:with-param name="description"
				select="'Citations'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'commentary'" />
		<xsl:with-param name="description"
				select="'Commentary references'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'text'" />
		<xsl:with-param name="description"
				select="'Cross-references'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'note'" />
		<xsl:with-param name="description"
				select="'Cross-references in notes'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'lexicon'" />
		<xsl:with-param name="description"
		    select="'Cross-references in general dictionaries'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'specific_lexicon'" />
		<xsl:with-param name="description"
		select="'Cross-references in text-specific dictionaries'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cit-category">
		<xsl:with-param name="key" select="'index'" />
		<xsl:with-param name="description"
				select="'Cross-references in indexes'" />
	    </xsl:call-template>
	</ul>
    </div>
</xsl:template>

<xsl:template name="print-cit-category">
    <xsl:param name="key" select="'specific_lexicon'" />
    <xsl:param name="description" select="'References of unknown type'" />
    
	    <xsl:call-template name="print-cits">
		<xsl:with-param name="node-list"
		    select="key('incoming-cits', $key)" />
		<xsl:with-param name="description"
		    select="concat($description, ' to this page')" />
		<xsl:with-param name="direction" select="'incoming'" />
	    </xsl:call-template>
	    <xsl:call-template name="print-cits">
		<xsl:with-param name="node-list"
		    select="key('outgoing-cits', $key)" />
		<xsl:with-param name="description"
		    select="concat($description, ' from this page')" />
		<xsl:with-param name="direction" select="'outgoing'" />
	    </xsl:call-template>
</xsl:template>

<xsl:template name="print-cits">
    <xsl:param name="node-list" select="''" />
    <xsl:param name="direction" select="'incoming'" />
    <xsl:param name="description" select="'Citations of some sort'" />

    <xsl:if test="count($node-list) &gt; 0">
	<li>
	    <xsl:value-of select="$description" />
		(<xsl:value-of select="count($node-list)" />):
	    <ul>
		<xsl:for-each select="$node-list">
		    <li>
			<xsl:call-template name="print-citation">
			    <xsl:with-param
				    name="direction"
				    select="$direction" />
			</xsl:call-template>
		    </li>
		</xsl:for-each>
	    </ul>
	</li>
    </xsl:if>
</xsl:template>

<xsl:template name="print-citation">
    <xsl:param name="direction" select="'incoming'" />
    <xsl:choose>
	<xsl:when test="$direction = 'outgoing'">
	    <xsl:apply-templates select="destination" />
	</xsl:when>
	<xsl:when test="$direction = 'incoming'">
	    <xsl:apply-templates select="source" />
	</xsl:when>
    </xsl:choose>
</xsl:template>

<xsl:template match="citation[ancestor::*[starts-with(name(), 'in')]]">
    <li>
	<xsl:apply-templates select="source" />
    </li>
</xsl:template>

<xsl:template match="source|destination">
    <xsl:if test="creator">
	<xsl:apply-templates select="creator"/>
	<xsl:text>, </xsl:text>
    </xsl:if>
    <cite>
	<xsl:choose>
	    <xsl:when test="not(header//text())">
		<a href="text?doc={query}">
		    <xsl:apply-templates select="title"/>
		</a>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:apply-templates select="title" />
	    </xsl:otherwise>
	</xsl:choose>
    </cite>
    <xsl:if test="header//text()">
	<xsl:text>, </xsl:text>
	
	<xsl:element name="a">
	    <xsl:attribute name="href">text?doc=<xsl:value-of select="query"/></xsl:attribute>
	    <xsl:attribute name="target">_blank</xsl:attribute>
	    <xsl:apply-templates select="header" />
	</xsl:element>
    </xsl:if>
</xsl:template>

<xsl:template name="print-cit">
    <xsl:param name="direction" select="source"/>
</xsl:template>

<xsl:template match="header">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="description|query|lang|lemma|linkType" />

<xsl:template match="foreign|orth">
    <xsl:call-template name="language-filter">
	<xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
</xsl:template>

</xsl:stylesheet>
