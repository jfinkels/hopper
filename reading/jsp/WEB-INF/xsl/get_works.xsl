<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">
    <xsl:variable name="projid">
      <xsl:value-of select="//GetWorks/request/textgroup"/>
    </xsl:variable>

    <GetWorks>
      <xsl:apply-templates select="//GetWorks/request"/>
      <works>
        <xsl:apply-templates select="//textgroup[@projid = $projid]"/>      
      </works>
    </GetWorks>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>