<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <xsl:import href="tei.xsl"/>

  <!-- We need the following speaker rule
       to pick up app tags: the default template 
       just renders the value. -->

  <xsl:template match="date">
    <div>
    <xsl:text>Date: </xsl:text><xsl:value-of select="." />
    </div>
  </xsl:template>

  <xsl:template match="placeName">
    <div>
    <xsl:text>Location: </xsl:text><xsl:value-of select="." />
    </div>
  </xsl:template>

  <xsl:template name="footnotes">
    <hr/>
    <xsl:apply-templates select="//app" mode="appcrit"/>
  </xsl:template>

  <xsl:template name="appID">
    <xsl:number level="any" count="app"/>
  </xsl:template>

  <xsl:template match="app">
      <xsl:variable name="identifier">
        <xsl:call-template name="appID"/>
      </xsl:variable>
      <xsl:apply-templates select="lem" />
      <a href="#note{$identifier}"><sup>
        <xsl:value-of select="$identifier"/>
        </sup>
     </a>
  </xsl:template>

  <xsl:template match="app" mode="footnote">
      <xsl:apply-templates select="lem"/>
      <xsl:variable name="identifier">
        <xsl:call-template name="appID"/>
      </xsl:variable>
      <a href="#note{$identifier}">
        <sup><xsl:value-of select="$identifier"/></sup>
      </a>
  </xsl:template>
  
  <xsl:template match="app" mode="appcrit">
    <span class="papy_app_crit">
      <a name="note{position()}" />
      <xsl:value-of select="preceding::lb[1]/@n"/>
      <xsl:text> </xsl:text>

      <xsl:apply-templates select="rdg" mode="appcrit" />
    </span>
  </xsl:template>

  <xsl:template match="lem">
    <span class="lem"><xsl:value-of select="."/></span>
  </xsl:template>
  
  <xsl:template match="rdg[@type='auth']" mode="appcrit">
    <xsl:if test="position() &gt; 1">
      <xsl:text> : </xsl:text>
    </xsl:if>
      <E>corr. from </E>
      <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="rdg[@type='orth']" mode="appcrit">
      <xsl:apply-templates />
      <E> Pap.</E>
  </xsl:template>

  <xsl:template match="rdg[@type='BL']" mode="appcrit">
      <E>BL <xsl:value-of select="following::wit"/> : </E>
      <xsl:apply-templates /> <E> orig. ed.</E>
  </xsl:template>

  <xsl:template match="rdg[@type='ed']" mode="appcrit">
      <E>subs. ed. <xsl:value-of select="following::wit"/> : </E>
      <xsl:apply-templates /> <E> orig. ed.</E>
  </xsl:template>

  <xsl:template match="expan">
    <xsl:text>_lpar;</xsl:text>
    <xsl:apply-templates />
    <xsl:text>_rpar;</xsl:text>
  </xsl:template>

  <xsl:template match="gap[@desc='vestig']">
     <E>vestig</E>
  </xsl:template>

  <xsl:template match="gap[@extent]">
    <xsl:choose>
      <xsl:when test="@extent='?'">
        <E>vac.</E>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="gap-dots">
          <xsl:with-param name="dots" select="@extent"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="gap-dots">
    <xsl:param name="dots"/>
    <xsl:if test="$dots &gt; 1">
       <xsl:call-template name="gap-dots">
         <xsl:with-param name="dots" select="$dots - 1"/>
       </xsl:call-template>
    </xsl:if>
    <xsl:text>.</xsl:text>
  </xsl:template>

  <xsl:template match="lb">
    <xsl:variable name="linenumber">
      <xsl:value-of select="@n"/>
    </xsl:variable>
    <br/>
    <span class="papy_line_num">
      <xsl:if test="number(@n)=NaN or @n mod 5 = 0">
        <xsl:value-of select="@n"/>
      </xsl:if>
      &#160;
    </span>
  </xsl:template>
  
  <xsl:template match="milestone[@unit='3']">
    <div><b><E><xsl:value-of select="@n"/></E></b></div>
  </xsl:template>

  <xsl:template match="milestone[@unit='4']">
    <div><b><E><xsl:value-of select="@n"/></E></b></div>
  </xsl:template>

</xsl:stylesheet>
