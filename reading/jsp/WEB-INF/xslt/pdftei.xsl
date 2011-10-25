<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ptext="ptext" xmlns:rdf="rdf" xmlns:dcterms="dcterms"
 xmlns:dc="Dublin Core" xmlns:rdfs="rdfs" xmlns:persq="persq"
    version="1.0">
  <xsl:strip-space elements="*"/>
  <xsl:preserve-space elements="p l"/>
  <xsl:output method="text" encoding="utf-8" indent="no"/>

  <xsl:param name="linenumber" select="4" />

  <xsl:variable name="language" select="/TEI.2/teiHeader/profileDesc/langUsage/language[0]" />

  <xsl:variable name="linenums" select="count(preceding::l[@n])" />
  <xsl:template match="/">
\documentclass[letterpaper,11pt]{report}
\usepackage[utf8]{inputenc}
\usepackage[greek,english]{babel}
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="teiHeader">
      <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="fileDesc">
      <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="titleStmt">
\title{<xsl:value-of select="title" />}
\author{<xsl:value-of select="author" />}
  </xsl:template>

  <xsl:template match="title|extent|publicationStmt|sourceDesc|encodingDesc|profileDesc|notesStmt|revisionDesc"></xsl:template>

  <xsl:template match="text">
      <xsl:choose>
	  <xsl:when test="group or not (descendant::text)">
\begin{document}
\maketitle
<xsl:apply-templates />
\end{document}
	  </xsl:when>
	  <xsl:when test="descendant::head">
	      \chapter*{<xsl:value-of select="descendant::head[1]" />}
	      <xsl:apply-templates />
	  </xsl:when>
	  <xsl:when test="@n">
	      \chapter*{<xsl:value-of select="@n" />}

	      <xsl:apply-templates />
	  </xsl:when>
	  <xsl:otherwise>
	      <xsl:apply-templates />
	  </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template match="body">
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="div1[@type='book']|div1[@type='Book']|div1[@type='letter']|div1[@type='Letter']">
\newpage
\section*{<xsl:value-of select="@type" /><xsl:text> </xsl:text><xsl:value-of select="@n" />}

<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="div0">
      <xsl:call-template name="print-heads" />
      <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="div1">
<xsl:call-template name="print-heads" />
<xsl:apply-templates />
  </xsl:template>

  <xsl:template name="print-heads">
      <xsl:for-each select="head">
\section*{<xsl:apply-templates />}

      </xsl:for-each>

  </xsl:template>

  <xsl:template match="div2[@type='section']">
\textbf{<xsl:value-of select="@n" />}. <xsl:text/>
<xsl:apply-templates />
  </xsl:template>
  <xsl:template match="div2">
<xsl:call-template name="print-heads" />
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="div3">
<xsl:call-template name="print-heads" />
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="milestone[@unit='para']">
  <xsl:if test="../l">
\\ \\
  </xsl:if>
  <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="p">
<xsl:apply-templates />
<xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="milestone[@unit='card']"><xsl:if test="count(preceding-sibling::l) &gt; 0">\\ \\ <xsl:text/>
</xsl:if></xsl:template>

  <xsl:template match="milestone[@unit='chapter']">\textbf{<xsl:value-of select="@n" />}.<xsl:text> </xsl:text></xsl:template>

  <xsl:template match="milestone[@unit='section']"><xsl:if test="@n!=1"> [<xsl:value-of select="@n" />] </xsl:if></xsl:template>

  <xsl:template match="head"></xsl:template>

  <xsl:template match="note">\footnote{<xsl:apply-templates />}</xsl:template>

  <xsl:template match="cit/bibl">
      \begin{flushright}
      <xsl:apply-templates />
      \end{flushright}
  </xsl:template>

  <xsl:template match="bibl"><xsl:apply-templates /></xsl:template>

  <xsl:template match="quote[@type='verse']|quote[@type='oracle']">
\begin{verse}
<xsl:apply-templates />
\end{verse}
  </xsl:template>

  <xsl:template match="cit">
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="quote">
\begin{quote}
<xsl:if test="title">
    <xsl:if test="title/@lang='greek'">\foreignlanguage{greek}{</xsl:if>
\textbf{<xsl:value-of select="title" />} \\
    <xsl:if test="title/@lang='greek'">}</xsl:if>
</xsl:if>
<xsl:apply-templates />
\end{quote}
  </xsl:template>

  <xsl:template match="l">
<xsl:if test="not(ancestor::lg or preceding::l)">\begin{verse}</xsl:if>
<xsl:if test="@n and @n!='tr'">
<xsl:variable name="linenums" select="count(preceding::l[@n and @n!='tr'])" />\hspace{-1in}<xsl:value-of select="@n" />\hspace{1in}<xsl:text />
</xsl:if>
<xsl:apply-templates /> \\
<xsl:if test="@rend='indent'">
    \indent<xsl:text/>
</xsl:if>
<xsl:if test="not(ancestor::lg) and not(following::l[1])">
    \end{verse}<xsl:text/>
</xsl:if>
  </xsl:template>

  <xsl:template match="lg">
      <xsl:apply-templates />
\\ \\
  </xsl:template>

  <xsl:template match="lb|LB">
      <xsl:text>\\</xsl:text>
  </xsl:template>

  <xsl:template match="sp">
<xsl:text>\par</xsl:text>
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="speaker">
      <xsl:if test="../sp"><xsl:text>
\par </xsl:text></xsl:if>
\textbf{<xsl:apply-templates />} \\
  </xsl:template>

  <xsl:template match="q"><xsl:text>`</xsl:text><xsl:apply-templates /><xsl:text>'</xsl:text></xsl:template>

  <xsl:template match="foreign">
<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="castList">
\textbf{Cast.}
\begin{itemize}
      <xsl:for-each select="castItem">
	  \item <xsl:value-of select="role"/><xsl:if test="roleDesc"><xsl:value-of select="roleDesc" /></xsl:if>
      </xsl:for-each>
\end{itemize}
  </xsl:template>

  <xsl:template match="closer">
\begin{flushright}
<xsl:apply-templates />
\end{flushright}
  </xsl:template>

  <xsl:template match="g">\foreignlanguage{greek}{<xsl:apply-templates />}</xsl:template>

</xsl:stylesheet>
