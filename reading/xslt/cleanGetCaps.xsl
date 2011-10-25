<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0">
        <xsl:output method="xml" indent="yes"/>

	<xsl:template match="/">
	  <GetCapabilities>
	    <request/>
	  <xsl:apply-templates/>
	  </GetCapabilities>
	</xsl:template>

	<xsl:template match="textgroup">
	  <xsl:variable name="projid"><xsl:value-of select="substring-before(substring-after(./work/@projid,','), ',')"/></xsl:variable>
	  <xsl:variable name="groupname"><xsl:value-of select="./@projid"/></xsl:variable>
	  <xsl:variable name="nspace">
	    <xsl:call-template name="getNSpace">
	      <xsl:with-param name="lang"><xsl:value-of select="./work[1]/@work"/></xsl:with-param>
	    </xsl:call-template>
	  </xsl:variable>

	  <xsl:element name="textgroup">
	    <xsl:attribute name="projid"><xsl:value-of select="$nspace"/><xsl:text>:tlg</xsl:text><xsl:value-of select="$projid"/></xsl:attribute>
	    <xsl:element name="groupname">
	      <xsl:attribute name="lang"><xsl:text>eng</xsl:text></xsl:attribute>
	      <xsl:value-of select="$groupname"/>
	    </xsl:element>	
            <xsl:apply-templates/>
	  </xsl:element>
	</xsl:template>

	<xsl:template match="work">
	  <xsl:variable name="projid"><xsl:value-of select="substring-after(substring-after(./@projid, ','), ',')"/></xsl:variable>
	  <xsl:variable name="lang">
	    <xsl:call-template name="getWorkLang">
	      <xsl:with-param name="lang"><xsl:value-of select="./@work"/></xsl:with-param>
	    </xsl:call-template>
	  </xsl:variable>
	  <xsl:variable name="nspace">
	     <xsl:call-template name="getNSpace">
	       <xsl:with-param name="lang"><xsl:value-of select="./@work"/></xsl:with-param>
	     </xsl:call-template>
          </xsl:variable>
	  
	  <xsl:element name="work">
	    <xsl:attribute name="projid"><xsl:value-of select="$nspace"/><xsl:text>:tlg</xsl:text><xsl:value-of select="$projid"/></xsl:attribute>
	    <xsl:attribute name="lang"><xsl:value-of select="$lang"/></xsl:attribute>
	    <xsl:apply-templates/>
	  </xsl:element>
	</xsl:template>

	<xsl:template match="translationcomments">
	  <xsl:variable name="lang">
	    <xsl:call-template name="getTransLang"/>
	  </xsl:variable>
	  <xsl:variable name="projid">
	    <xsl:call-template name="convertProjID">
	      <xsl:with-param name="projid"><xsl:value-of select="./@projid"/></xsl:with-param>
	    </xsl:call-template>
	  </xsl:variable>
	  
	  <xsl:element name="translationcomments">
	    <xsl:attribute name="label"><xsl:value-of select="./@label"/></xsl:attribute>
	    <xsl:attribute name="projid">
		<xsl:value-of select="$projid"/>
	    </xsl:attribute>
	    <xsl:attribute name="lang"><xsl:value-of select="$lang"/></xsl:attribute>
	    <xsl:apply-templates/>
	  </xsl:element>
	</xsl:template>

	<xsl:template match="editioncomments">
	  <xsl:variable name="projid">
	    <xsl:call-template name="convertProjID">
	      <xsl:with-param name="projid"><xsl:value-of select="./@projid"/></xsl:with-param>
	    </xsl:call-template>
	  </xsl:variable>

	  <xsl:element name="editioncomments">
	    <xsl:attribute name="label"><xsl:value-of select="./@label"/></xsl:attribute>
	    <xsl:attribute name="projid">
		<xsl:value-of select="$projid"/>
	    </xsl:attribute>
	    <xsl:apply-templates/>
	  </xsl:element>
	</xsl:template>

	<xsl:template match="title[@lang != '']">
	  <xsl:variable name="lang">
	    <xsl:call-template name="getTitleLang"/>
	  </xsl:variable>

	  <xsl:element name="title">
	    <xsl:attribute name="lang"><xsl:value-of select="$lang"/></xsl:attribute>
	    <xsl:apply-templates/>
	  </xsl:element>
	</xsl:template>

	<xsl:template name="getTitleLang">
	  <xsl:choose>
	    <xsl:when test="./@lang = 'en'">
	      <xsl:text>eng</xsl:text>
	    </xsl:when>
	    <xsl:when test="./@lang = 'la'">
	      <xsl:text>lat</xsl:text>
	    </xsl:when>
	  </xsl:choose>
	</xsl:template>
	
	<xsl:template name="getNSpace">
	  <xsl:param name="lang"/>
	  <xsl:variable name="workLang">
	  <xsl:call-template name="getWorkLang">
	    <xsl:with-param name="lang"><xsl:value-of select="$lang"/></xsl:with-param>
	  </xsl:call-template>
	  </xsl:variable>

	  <xsl:choose>
	    <xsl:when test="$workLang = 'grc-beta'">
	      <xsl:text>greekLit</xsl:text>
	    </xsl:when>
	    <xsl:when test="$workLang = 'lat'">
	      <xsl:text>latinLit</xsl:text>
	    </xsl:when>
	  </xsl:choose>
	</xsl:template>

	<xsl:template name="convertProjID">
	  <xsl:param name="projid"/>
	  <xsl:variable name="one"><xsl:value-of
	  select="substring-before($projid, '.')"/></xsl:variable>
	  <xsl:variable name="two"><xsl:value-of
	  select="substring-before(substring-after($projid, '.'), '.')"/></xsl:variable>
	  <xsl:variable name="three"><xsl:value-of
	  select="substring-after(substring-after($projid,  '.'), '.')"/></xsl:variable>
	  <xsl:variable name="separator"><xsl:text>_</xsl:text></xsl:variable>
	  <xsl:value-of select="$one"/><xsl:value-of select="$separator"/><xsl:value-of
	  select="$two"/><xsl:value-of
	  select="$separator"/><xsl:value-of select="$three"/>
	</xsl:template>

	<xsl:template name="getTransLang">
	  <xsl:variable name="lang"><xsl:value-of
	  select="substring-before(substring-after(./online/@local,
	  '_'), '.')"/></xsl:variable>
	  
	  <xsl:choose>
	    <xsl:when test="$lang = 'eng'">
	      <xsl:text>eng</xsl:text>
	    </xsl:when>
	    <xsl:when test="$lang = 'lat'">
	      <xsl:text>lat</xsl:text>
	    </xsl:when>
	    <xsl:when test="$lang = 'gk'">
	      <xsl:text>grc</xsl:text>
	    </xsl:when>
	  </xsl:choose>
	</xsl:template>

	<xsl:template name="getWorkLang">
	  <xsl:param name="lang"/>
	  <xsl:choose>
	    <xsl:when test="$lang = 'greek'">
	      <xsl:text>grc-beta</xsl:text>
	    </xsl:when>
	    <xsl:when test="$lang = 'en, greek'">
	      <xsl:text>grc-beta</xsl:text>
	    </xsl:when>
	    <xsl:when test="$lang = 'en, la'">
	      <xsl:text>lat</xsl:text>
	    </xsl:when>
	  </xsl:choose>
	</xsl:template>

   	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
  
</xsl:stylesheet>