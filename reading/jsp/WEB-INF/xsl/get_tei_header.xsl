<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">
    <GetTEIHeader>
      <xsl:apply-templates select="//GetTEIHeader/request"/>
      <xsl:apply-templates select="//teiHeader"/>
    </GetTEIHeader>
  </xsl:template>

  <xsl:template match="*">
	<xsl:copy>
	  <xsl:copy-of select="@*"/>
	  <xsl:apply-templates/>
	</xsl:copy>
  </xsl:template>
	
</xsl:stylesheet>