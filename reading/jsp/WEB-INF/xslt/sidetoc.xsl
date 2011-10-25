<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes" />

  <xsl:include href="langfilter.xsl" />

  <xsl:param name="document_id" select="''" />
  <xsl:param name="subquery" select="''" />
  <xsl:param name="scheme" select="''" />
  <xsl:param name="tocSubquery" select="$subquery" />
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

  <xsl:variable name="curTocQuery">
      <xsl:choose>
	  <xsl:when test="not($tocSubquery = '')">
	      <xsl:value-of select="concat($document_id, ':', $tocSubquery)" />
	  </xsl:when>
	  <xsl:otherwise>
	      <xsl:value-of select="$document_id" />
	  </xsl:otherwise>
      </xsl:choose>
  </xsl:variable>

  <xsl:variable name="isEntryBased" select="count(//chunk[@type='entry']) > 0"/>

  <xsl:template match="/">
      <xsl:apply-templates select="contents"/>
  </xsl:template>

  <xsl:template match="contents">
    <div id="side_toc">
	<xsl:apply-templates select="chunk"/>
	<xsl:text> </xsl:text>
    </div>
  </xsl:template>

  <xsl:template match="chunk[child::chunk]">
    <xsl:variable name="chunkID">
      <xsl:value-of select="generate-id()" />
    </xsl:variable>
    <xsl:variable name="indentLevel">
      <xsl:value-of select="1 * count(ancestor::chunk)" />
    </xsl:variable>
    <xsl:variable name="href">
      <xsl:value-of select="@ref"/>
    </xsl:variable>
    <xsl:variable name="query">
      <xsl:value-of select="concat(@ref, ':')" />
    </xsl:variable>
    <!-- If we match the current query against the @ref attribute alone,
	 we could infer that the subquery book=10:line=125 "starts with"
	 not only book=10 but also with book=1, which we don't want.
	 To prevent cases like this, we match against book=1: and book=10:
	 instead.
     -->

    <div class="sidetoc mbot ind{$indentLevel}">
      <a href="javascript:toggleExpand('{$chunkID}');">
    <xsl:choose>
	<xsl:when test="@current or descendant::chunk[@start = $startOffset and @end = $endOffset] or (@start=$startOffset and @end=$endOffset)">
            <img border="0" src="/img/south.gif" id="img_{$chunkID}" alt="V" />
        </xsl:when>
        <xsl:otherwise>
            <img border="0" src="/img/east.gif" id="img_{$chunkID}" alt=">" />
        </xsl:otherwise>
    </xsl:choose>
      </a>
      <xsl:choose>
	  <xsl:when test="$isEntryBased and @type != 'entry'">
	      <a href="javascript:toggleExpand('{$chunkID}');"><xsl:apply-templates select="head"/></a>
	      <!--<a href="?doc={$curQuery}&amp;toc={$href}"><xsl:apply-templates select="head" /></a> -->
	  </xsl:when>
	  <!-- A poor way of determining whether or not this chunk is at the
	    current chunk level -->
	  <xsl:when test="$scheme != @type">
	      <a href="javascript:toggleExpand('{$chunkID}');"><xsl:apply-templates select="head" /></a>
	  </xsl:when>
	  <xsl:otherwise>
	      <a href="?doc={$href}"><xsl:apply-templates select="head" /></a>
	  </xsl:otherwise>
      </xsl:choose>

    <xsl:choose>
	<xsl:when test="@current or descendant::chunk[@start = $startOffset and @end = $endOffset] or (@start=$startOffset and @end=$endOffset)">
          <div id="{$chunkID}" class="sidetoc mtop ind1">
          <xsl:apply-templates select="chunk"/>
          </div>
        </xsl:when>
        <xsl:otherwise>
          <div id="{$chunkID}" class="sidetoc mtop ind1" style="display: none;">
          <xsl:apply-templates select="chunk"/>
          </div>
        </xsl:otherwise>
    </xsl:choose>   
    </div>

  </xsl:template>

  <xsl:template match="chunk">
    <xsl:variable name="indentLevel">
      <xsl:value-of select="1 * count(ancestor::chunk)" />
    </xsl:variable>
    <xsl:variable name="href">
      <xsl:value-of select="@ref" />
    </xsl:variable>
    <xsl:variable name="chunkID">
      <xsl:value-of select="generate-id()" />
    </xsl:variable>

    <div id="{$chunkID}" class="sidetoc mbot ind{$indentLevel}">
      <xsl:choose>
      <xsl:when test="@type = 'root'">
         <a href="?doc={$href}">
                <img border="0" src="/img/east.gif" id="img_{$chunkID}" alt=">"/>
	      </a>
	      <a href="?doc={$href}"><xsl:apply-templates select="head" /></a>   
      </xsl:when>
      <xsl:when test="$isEntryBased and @type != 'entry'">
	      <a href="?doc={$curQuery}&amp;toc={$href}">
                <img border="0" src="/img/east.gif" id="img_{$chunkID}" alt=">"/>
	      </a>
	      <a href="?doc={$curQuery}&amp;toc={$href}"><xsl:apply-templates select="head" /></a>
	  </xsl:when>
	  <xsl:when test="@isParent">
	    <a href="?doc={$href}">
	    <img border="0" src="/img/east.gif" id="img_{$chunkID}" alt=">"/>
	    </a>
	    <a href="?doc={$href}"><xsl:apply-templates select="head" /></a>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:if test="@isParent">
		<a href="?doc={$href}">
		    <img border="0" src="/img/east.gif" id="img_{$chunkID}" alt=">"/>
		</a>
	    </xsl:if>
	    <a href="?doc={$href}"><xsl:apply-templates select="head" /></a>
	</xsl:otherwise>
      </xsl:choose>
    </div>
  </xsl:template>

  <xsl:template match="head[@lang!='']">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>  

  <xsl:template match="head">
    <xsl:choose>
	<xsl:when test="text() or child::*">
	    <xsl:apply-templates />
	</xsl:when>
	<xsl:otherwise>
	    <xsl:value-of select="parent::chunk[1]/@type" />
	    <xsl:text> </xsl:text>
	    <xsl:value-of select="parent::chunk[1]/@n" />
	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="note" />

  <xsl:template match="orth[@lang!='']">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>  

  <xsl:template match="foreign[@lang!='']">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>  

</xsl:stylesheet>
