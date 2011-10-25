<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="utf-8"/>

    <xsl:param name="maxErrors" select="10" />

    <xsl:template match="/">
	<html>
	    <head>
		<title>Verification Results</title>
		<link type="text/css" rel="STYLESHEET" href="/css/docstatus.css"/>
		<script type="text/javascript" src="/js/hopper.js"/>
	    </head>
	    <body>
		<h1>Status Report</h1>
		<xsl:apply-templates />
	    </body>
	</html>
    </xsl:template>

    <xsl:template match="corpus">
	<h2><xsl:apply-templates /></h2>
    </xsl:template>

    <xsl:template match="corpusVerification">
	<xsl:apply-templates select="corpus" />
	<h5>Run at <xsl:value-of select="@timestamp" /></h5>

	<table>
	    <tr>
		<th>Document ID</th>
		<th>Work</th>
		<th>Status</th>
		<th>Errors</th>
	    </tr>
	    <xsl:apply-templates select="verificationResult" />
	</table>
    </xsl:template>

    <xsl:template name="childResult">
	<xsl:variable name="errorID">
	    <xsl:value-of select="generate-id()" />
	</xsl:variable>

	<tr>
	    <td class="query"><xsl:value-of select="subquery" /></td>
	    <td class="work"><xsl:call-template name="print-status" /></td>
	    <td class="errorlink"><a href="javascript:toggle('{$errorID}')" id="{$errorID}-link">show</a></td>
	</tr>
	<tr>
	    <td colspan="3">
		<div id="{$errorID}" style="display: none;">
		    <xsl:apply-templates select="errors"/>
		</div>
	    </td>
	</tr>
	<tr>
	    <xsl:call-template name="print-children" />
	</tr>
    </xsl:template>

    <xsl:template match="verificationResult">
	<tr>
	    <td class="query"><xsl:value-of select="@documentID"/></td>
	    <td class="work"><xsl:call-template name="metadata"/></td>
	    <td class="status"><xsl:call-template name="print-status" /></td>
	    <td class="errors">
		<xsl:if test="errors">
		    <xsl:apply-templates select="errors" />
		</xsl:if>
		<xsl:call-template name="print-children" />
	    </td>
	</tr>
    </xsl:template>

    <xsl:template match="errors">
	<xsl:apply-templates />
    </xsl:template>

    <xsl:template match="error">
	<div class="error">
	    <h4><xsl:value-of select="name"/>: <xsl:value-of select="message"/></h4>
	    <p>Stack trace:</p>
	    <xsl:apply-templates select="stackTrace"/>
	</div>
    </xsl:template>

    <xsl:template match="stackTrace">
	<ul>
	    <xsl:apply-templates />
	</ul>
    </xsl:template>

    <xsl:template match="element">
	<li><xsl:apply-templates /></li>
    </xsl:template>

    <xsl:template name="print-status">
	<xsl:choose>
	    <xsl:when test="@status = 0">
		<span class="ok">Okay</span>
	    </xsl:when>
	    <xsl:when test="@status = 1">
		<span class="failed">Failed</span>
	    </xsl:when>
	    <xsl:when test="@status = 2">
		<span class="child_failed">Child Failed</span>
	    </xsl:when>
	    <xsl:otherwise>
		Unknown code: <xsl:value-of select="@value"/>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="metadata">
	<xsl:if test="author">
	    <xsl:value-of select="author" /><xsl:text>, </xsl:text>
	</xsl:if>
	<cite><xsl:value-of select="title" /></cite>
	<xsl:if test="language">
	    <xsl:text> (</xsl:text>
	    <xsl:value-of select="language" />
	    <xsl:text>)</xsl:text>
	</xsl:if>
    </xsl:template>

    <xsl:template name="print-children">
	<xsl:if test="children">
	    <table class="children">
		<xsl:for-each select="children/verificationResult">
		    <xsl:if test="position() &lt; $maxErrors+1">
			<xsl:call-template name="childResult" />
			<xsl:if test="position() = $maxErrors and last() &gt; position()">
			    <tr><td>...<xsl:value-of select="last() - position()" /> more</td></tr>
			</xsl:if>
		    </xsl:if>
		</xsl:for-each>
	    </table>
	</xsl:if>
    </xsl:template>

    <xsl:template match="name|message|author|title|language|documentID|subquery"/>
</xsl:stylesheet>
