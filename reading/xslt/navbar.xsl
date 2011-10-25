<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes" />

    <xsl:param name="document_id" select="''" />
    <xsl:param name="subquery" select="''" />
    <xsl:param name="startOffset" select="//chunk[1]/@start" />
    <xsl:param name="endOffset" select="//chunk[1]/@end" />

    <xsl:variable name="curQuery">
	  <xsl:choose>
	      <xsl:when test="not($subquery = '')">
		  <xsl:value-of select="concat($document_id, ':', $subquery)" />
	      </xsl:when>
	      <xsl:otherwise>
		  <xsl:value-of select="$document_id" />
	      </xsl:otherwise>
	  </xsl:choose>
    </xsl:variable>

    <xsl:variable name="isEntryBased" select="//chunk[@type='entry']" />
    <xsl:variable name="hasSubtexts" select="//chunk[@type='text']" />

    <xsl:param name="smoothWidths" select="'false'" />
    <xsl:param name="selectedType" select="''" />

    <xsl:template match="contents">
	<div id="text_navbars">
	    <xsl:call-template name="printNavbar" />
	    <xsl:for-each select="//chunk[
			    (@start &lt;= $startOffset and @end >= $endOffset)
			    and child::chunk]">
		<xsl:sort select="@start" order="ascending"
			    data-type="number" />
		<xsl:call-template name="printNavbar" />
	    </xsl:for-each>
	</div>
    </xsl:template>

    <xsl:template name="printNavbar">
	<xsl:variable name="totalSize" select="sum(chunk/@end) - sum(chunk/@start)" />

	<xsl:variable name="widthAvailable" select="90" />
	<xsl:variable name="minWidth" select="1 div $widthAvailable" />

	<xsl:variable name="avgSize" select="$totalSize div count(chunk)" />

	<xsl:variable name="linesNeeded"
	    select="floor((count(chunk) - 1) div $widthAvailable)+1" />
	<xsl:variable name="ratio"
	    select="$widthAvailable div ($totalSize div $linesNeeded)" />

	<div class="navbar">
	    <span class="type_header">
	    <xsl:value-of select="chunk[1]/@type" />:
	    </span>

	    <div class="bar">
		<xsl:for-each select="chunk">
		    <xsl:call-template name="printChunk">
			<xsl:with-param name="effectiveWidth"
			    select="(@end - @start) * $ratio" />
		    </xsl:call-template>
		</xsl:for-each>
	    </div>
	</div>
    </xsl:template>

    <xsl:template name="getType">
	<xsl:choose>
	    <xsl:when test="@type='card'">
		<xsl:text>line</xsl:text>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="@type" />
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="printChunk">
	<xsl:param name="effectiveWidth" select="10" />

	<xsl:variable name="displayRef">
	    <xsl:call-template name="getInnermostRef">
		<xsl:with-param name="chunk" select="." />
	    </xsl:call-template>
	</xsl:variable>

	<xsl:variable name="href" select="concat('?doc=',$displayRef)" />
	<xsl:variable name="title">
	    <xsl:choose>
		<xsl:when test="head//text()">
		    <xsl:apply-templates select="head" />
		</xsl:when>
		<xsl:otherwise>
		    <xsl:call-template name="getType" />
		    <xsl:text> </xsl:text>
		    <xsl:value-of select="@n" />
		</xsl:otherwise>
	    </xsl:choose>
	</xsl:variable>

	<xsl:variable name="idxPosition">
	    <xsl:call-template name="getIndexPosition">
		<xsl:with-param name="index" select="position()" />
	    </xsl:call-template>
	</xsl:variable>

	<xsl:variable name="workingWidth">
	    <xsl:call-template name="getWidth">
		<xsl:with-param name="width" select="$effectiveWidth" />
	    </xsl:call-template>
	</xsl:variable>
	<xsl:variable name="actualWidth" select="format-number($workingWidth,'###.####')" />

	<xsl:choose>
	    <xsl:when test="@current or descendant::chunk[@start = $startOffset and @end = $endOffset] or (@start = $startOffset and @end = $endOffset)">
		<a class="current {$idxPosition}" style="width: {$actualWidth}%;" href="{$href}" title="{$title}" onmouseover="showTitle(this);" onmouseout="clearTitle();"><span class="noshow"><xsl:apply-templates select="head"/></span></a>
	    </xsl:when>
	    <xsl:otherwise>
		<a class="{$idxPosition}" style="width: {$actualWidth}%;" href="{$href}" title="{$title}" onmouseover="showTitle(this);" onmouseout="clearTitle();"><span class="noshow"><xsl:call-template name="printHead"/></span></a>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="getWidth">
	<xsl:param name="width" select="1.0" />
	<xsl:choose>
	    <xsl:when test="$smoothWidths = 'true' and ($width &lt; 1.0)">1.0</xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="$width" />
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="getIndexPosition">
	<xsl:param name="index" select="0" />
	<xsl:choose>
	    <xsl:when test="$index mod 2 = 0">even</xsl:when>
	    <xsl:otherwise>odd</xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="getInnermostRef">
	<xsl:param name="chunk" select="0" />

	<xsl:choose>
	    <xsl:when test="($chunk/@start = $startOffset and
			    $chunk/@end = $endOffset) or
			$isEntryBased or $hasSubtexts or @type = $selectedType">
		<xsl:value-of select="$chunk/@ref" />
	    </xsl:when>
	    <xsl:when test="$chunk/chunk">
		<xsl:call-template name="getInnermostRef">
		    <xsl:with-param name="chunk" select="$chunk/chunk[1]" />
		</xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="$chunk/@ref" />
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="printHead">
	<xsl:choose>
	    <xsl:when test="head//text()">
		<xsl:apply-templates select="head" />
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:text>-</xsl:text>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template match="note" />
</xsl:stylesheet>
