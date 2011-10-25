<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:dctype="http://purl.org/dc/dcmitype/"
    xmlns:ptext="http://www.perseus.org/meta/ptext.rdfs#"
    xmlns:perseus="http://www.perseus.org/meta/perseus.rdfs#"
    xmlns:persq="http://www.perseus.org/meta/persq.rdfs#"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:tufts="http://www.tufts.edu/"
    exclude-result-prefixes="ptext rdf dcterms dc rdf persq"
    version="1.0">
  <xsl:preserve-space elements="*"/>
  <xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes"/>

  <xsl:include href="langfilter.xsl" />

  <xsl:param name="linenumber" select="4" />
  <xsl:param name="document_id">Perseus%3Atext%3A1999.04.0062"</xsl:param>

  <xsl:param name="query">Perseus:text:1999.01.0001</xsl:param>

  <!-- 
    This parameter contains the authority name of an entity to highlight; it's
    used when the user has followed a link to the text page from within the
    named-entity browser.
  -->
  <xsl:param name="highlight_authname" select="''" />


  <!--
    Parameters for voting. "Form" and "which" specify the form that's being
    voted for, and "which" indicates which occurrence of the form this is
    in the given chunk (is this the first "et", or the second, or...?).
    "Lexquery" specifies the lexicon whose senses are being voted for (if this
    is a sense-vote); "voting_lang" indicates the language of the form that's
    being voted on.
  -->
  <xsl:param name="lexquery" select="''" />
  <xsl:param name="form" select="''" />
  <xsl:param name="which" select="0" />
  <xsl:param name="voting_lang" select="'en'" />

  <!--
    The document ID and subquery of the passage where the voted-on
    word lives.
  -->
  <xsl:param name="source_id" select="''" />
  <xsl:param name="subquery" select="''" />

  <!--
    The primary language of the document we're rendering. Used to
    render Greek fonts if we need to.
  -->
  <xsl:param name="lang" select="'en'" />


  <!--
    For lexicon entries, "sourcework" represents the document containing the
    word that we're looking up, in ABO format, "sourcesub" the subquery.
    This allows us to give special treatment to lexicon citations that point
    back to the work we came from, and perhaps even more special treatment
    to citations that point to the specific passage we came from.
  -->
  <xsl:param name="sourcework" select="''" />
  <xsl:param name="sourcesub" select="0" />

  <!--
    Should we display "[select]" links for this entry? By default, no; but we
    set this to 'true' if we're viewing a lexicon entry in a specific context
    (i.e., if we have values for "source_id" and "subquery").
  -->
  <xsl:param name="allow_voting" select="false" />

  <xsl:template match="/">
    <div class="text_container {$lang}">
     <div class="text">
     <xsl:apply-templates select="ptext:pack|ptext:front|/TEI.2/text/front|/tei.2/text/front|ptext:body|/TEI.2/text/body|ptext:back|/tei.2/text/back|/TEI.2/text/back|/TEI.2/text/group/text"/>
     <xsl:text> </xsl:text>
     </div>
     <!-- The space is necessary here so that we don't run the risk of
	outputting an empty DIV tag, which some browsers may interpret
	poorly, if there are no footnotes -->
     <div class="footnotes en"><xsl:text> </xsl:text>
     <xsl:call-template name="footnotes" />
     </div>
    </div>
  </xsl:template>

  <xsl:template match="rdf:Description"></xsl:template>

<xsl:template match="text[parent::p and not(ancestor::quote[@rend='blockquote'])]">
<blockquote><xsl:apply-templates /></blockquote>
</xsl:template>

<xsl:template match="closer|CLOSER|Closer">
<div align="right"><xsl:apply-templates /></div>
</xsl:template>

<xsl:template match="lb|LB">
  <xsl:choose>
    <xsl:when test="@ed='F1'">
      <!-- Don't put a line break in Shakespeare texts with this -->
    </xsl:when>
    <xsl:otherwise>
      <br/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="znote">
  <xsl:choose>
    <xsl:when test="not(@place)">
AAA<xsl:apply-templates />B
    </xsl:when>
    <xsl:otherwise><xsl:apply-templates /></xsl:otherwise>
  </xsl:choose>

</xsl:template>

  <xsl:key name="fnotes" match="note[@place='foot']|note[@place='unspecified']|note[@place='text']|note[not(@place)]" 
     use="'current'" />

  <xsl:template name="footnotes">
    <xsl:for-each select="key('fnotes', 'current')">
      <xsl:variable name="noteID">
	  <xsl:call-template name="footnoteID" />
      </xsl:variable>

      <!-- Make footnotes from our grammars look slightly nicer -->
      <xsl:if test="position() = 1 and @rend='smyth'">
	  <hr />
      </xsl:if>
      <p id="note{$noteID}">
      <xsl:choose>
	  <xsl:when test="@rend='smyth'">
	      <strong><xsl:value-of select="@n" /><xsl:text> D. </xsl:text></strong>
	  </xsl:when>
	  <xsl:otherwise>
	      <a href="#note-link{$noteID}"><xsl:value-of select="position()"/></a><xsl:text> </xsl:text>
	  </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates />
      </p>
    </xsl:for-each>
  </xsl:template>

    <xsl:template match="gloss">
	<span class="gloss"><xsl:apply-templates /></span>
    </xsl:template>

    <xsl:template match="emph[@rend='ital']">
	<em><xsl:apply-templates /></em>
    </xsl:template>

    <xsl:template match="emph">
	<strong><xsl:apply-templates /></strong>
    </xsl:template>

<xsl:template match="text[ancestor::text]">
<p/>
<div class="embeddedtext">
<xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
</xsl:call-template>
</div>
<br/>
</xsl:template>

  <xsl:template match="entry|entryfree">
    <p>
    <xsl:apply-templates/>
    </p>
  </xsl:template>

  <xsl:template name="correct-sense">
      <xsl:if test="descendant::bibl[@n=concat($sourcework,':',$sourcesub)]">
	  <span style="font-weight: bold; color: red; font-size: large;">*</span> 
      </xsl:if>
  </xsl:template>

  <xsl:template match="sense[@level]">
    <div class="lex_sense lex_sense{@level}">
	<xsl:call-template name="correct-sense" />
      <xsl:if test="not(@n='')">
        <b><xsl:value-of select="@n"/>. 
	    <xsl:call-template name="sense_vote" /></b>
      </xsl:if>
      <xsl:apply-templates select="node()"/>
    </div>
  </xsl:template>

  <xsl:template match="sense[@n=0]">
    <div class="lex_sense">
      <xsl:call-template name="correct-sense" />
      <xsl:call-template name="sense_vote" />
        <xsl:apply-templates />
    </div>
  </xsl:template>

  <xsl:template match="sense">
    <div class="lex_sense">
      <xsl:call-template name="correct-sense" />
        <xsl:if test="not(@n='')">
           <b><xsl:value-of select="@n"/>. 
	<xsl:call-template name="sense_vote" />
	    </b>
        </xsl:if>
        <xsl:apply-templates />
    </div>
  </xsl:template>

  <xsl:template name="sense_vote">
      <xsl:if test="$allow_voting = 'true'">
	  <xsl:element name="a">
	      <xsl:attribute name="style">
		  <xsl:text>font-size: small; color: blue; text-decoration: none;</xsl:text>
	      </xsl:attribute>
	      <xsl:attribute name="href">
		  <xsl:value-of select="concat('submitvote?type=sense&amp;lexquery=',$lexquery,'&amp;doc=',$source_id,'&amp;subquery=',$subquery,'&amp;form=',$form,'&amp;which=',$which,'&amp;sense_id=',@id,'&amp;lang=',$voting_lang)" />
	      </xsl:attribute>
	      [select]
	  </xsl:element>
      </xsl:if>
  </xsl:template>

  <xsl:template match="orth|form/orth">
    <b><xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template></b><xsl:text> </xsl:text>
  </xsl:template>
  
  <xsl:template match="gen">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="usg">
    <b><xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template></b>
  </xsl:template>

  <xsl:template match="head">
    <h4>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang" />
      </xsl:call-template>
    </h4>
  </xsl:template>

  <xsl:template match="head[@rend='braced' or @rend='brace']">
    <b>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang" />
      </xsl:call-template>
    </b>
    <br/>
  </xsl:template>

  <xsl:template match="headLabel">
    <h4>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang" />
      </xsl:call-template>
    </h4>
  </xsl:template>

  <xsl:template match="label">
    <b>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang" />
      </xsl:call-template>
    </b>
  </xsl:template>

  <xsl:template match="docauthor|docAuthor">
    <h5>
    <xsl:apply-templates />
    </h5>
  </xsl:template>

  <xsl:template match="surname">
    <!-- careful of empty surname tags, since some have been sighted! -->
    <span class="surname">
	<xsl:choose>
	    <xsl:when test="text()">
		<xsl:apply-templates />
	    </xsl:when>
	    <xsl:when test="@n">
		<xsl:value-of select="@n" />
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:text> </xsl:text>
	    </xsl:otherwise>
	</xsl:choose>
    </span>
  </xsl:template>

  <xsl:template match="forename">
    <span class="forename">
	<xsl:choose>
	    <xsl:when test="text()">
		<xsl:apply-templates />
	    </xsl:when>
	    <xsl:when test="@n">
		<xsl:value-of select="@n" />
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:text> </xsl:text>
	    </xsl:otherwise>
	</xsl:choose>
    </span>
  </xsl:template>

  <xsl:template match="head[@type=hidden]">
  </xsl:template>

  <xsl:template name="figalign">
    <xsl:variable name="figID">
      <xsl:number level="any" count="figure[@n!='']" from="body"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$figID mod 2 = 0">
        <xsl:text>left</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>right</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="name[@type='place']">
    <span style="background-color: aaaaee;">
    <xsl:choose>
      <xsl:when test="@reg!=''">
        <a target="gazetteer" href="http://maps.yahoo.com/maps_result?name=&amp;csz={substring-before(@reg, ', ')}%2C+{substring-after(@reg, ', ')}">
          <xsl:apply-templates />
        </a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates />
      </xsl:otherwise>
    </xsl:choose>
    </span>
  </xsl:template>


  <!-- This is meant for HEAD tags with multiple tags inside them, like
    the Pindar odes
    -->
  <xsl:template match="head/title">
    <u>
	<xsl:call-template name="language-filter">
	    <xsl:with-param name="lang" select="@lang" />
	</xsl:call-template>
    </u>
    <br />
  </xsl:template>

  <xsl:template match="title">
    <u>
	<xsl:call-template name="language-filter">
	    <xsl:with-param name="lang" select="@lang" />
	</xsl:call-template>
    </u>
  </xsl:template>



  <xsl:template match="name[@key!='']">
    <!--<a target="gazetteer" href="http://www.getty.edu/vow/TGNFullDisplay?find=&amp;place=&amp;nation=&amp;english=Y&amp;subjectid={substring-after(@key, 'tgn,')}">-->
      <xsl:apply-templates />
    <!--</a>-->
  </xsl:template>

  <xsl:template match="figure[ancestor::list[@type='illustrations']]" priority="2">
  	<xsl:variable name="thumbURL">
    	<xsl:call-template name="getThumbURL">
    		<xsl:with-param name="n" select="@n"/>
    	</xsl:call-template>
    </xsl:variable>
      <a target="figure" href="/hopper/image?img={@n}"><img class="thumb_img" src="{$thumbURL}"/></a>
      <br/><br/>
      <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="figure[@n!='' and not(descendant::p)]" priority="1">
    <xsl:variable name="align">
      <xsl:call-template name="figalign"/>
    </xsl:variable>
    <xsl:variable name="thumbURL">
    	<xsl:call-template name="getThumbURL">
    		<xsl:with-param name="n" select="@n"/>
    	</xsl:call-template>
    </xsl:variable>
  	<xsl:choose>
  		<xsl:when test="ancestor::note">
  		<p>
  			<a target="figure" href="/hopper/image?img={@n}"><img class="thumb_img" src="{$thumbURL}"/></a>
  		</p>
  	</xsl:when>
  	<xsl:otherwise>
    <p><table width="120" align="{$align}">
      <tr><td>
              <a target="figure" href="/hopper/image?img={@n}"><img class="thumb_img" src="{$thumbURL}"/></a>
      </td></tr>
      <tr><td><xsl:apply-templates /></td></tr>
    </table></p>
  	</xsl:otherwise>
  	</xsl:choose>
  </xsl:template>


  <xsl:template match="figure[@n!='' and (descendant::p)]" priority="1">
    <xsl:variable name="thumbURL">
    	<xsl:call-template name="getThumbURL">
    		<xsl:with-param name="n" select="@n"/>
    	</xsl:call-template>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="ancestor::table">
        <table align="center">
          <tr><td>
          <a target="figure" href="/hopper/image?img={@n}"><img class="thumb_img" src="{$thumbURL}"/></a>
          </td>
          <td><xsl:apply-templates /></td></tr>
        </table>
      </xsl:when>
      <xsl:otherwise>
        <p>
        <table border="1" width="90%" align="center">
        <tr><td align="center">
        <a target="figure" href="/hopper/image?img={@n}"><img class="thumb_img" src="{$thumbURL}"/></a>
        </td></tr>
        <tr><td><xsl:apply-templates /></td></tr>
        </table></p>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="getThumbURL">
  <xsl:param name="n"/>
  <!-- This is ugly and I know it.  I have spent way too much time trying to display thumbnails in a better way, so this will have to do. -->
  	   <xsl:variable name="archiveNumber">
  	   	<xsl:choose>
  	   		<xsl:when test="starts-with($n, 'Perseus:image:')">
  	   			<xsl:value-of select="substring-after($n, 'Perseus:image:')"/>
  	   		</xsl:when>
  	   		<xsl:otherwise>
  	   			<xsl:value-of select="$n"/>
  	   		</xsl:otherwise></xsl:choose>
  	   </xsl:variable>
  	   <xsl:variable name="directory1">
  	   		<xsl:value-of select="substring($archiveNumber, 1, 4)"/>
  	   </xsl:variable>
  	   <xsl:variable name="directory2">
  	   		<xsl:value-of select="substring($archiveNumber, 6, 2)"/>
  	   </xsl:variable>
  	   <xsl:variable name="directory3">
  	   		<xsl:value-of select="floor(number(substring($archiveNumber, 9, 4)) div 500) + 1"/>
  	   </xsl:variable>

	     <xsl:text>http://images.perseus.tufts.edu/images/thumbs/</xsl:text>
	     <xsl:value-of select="$directory1"/>.<xsl:value-of select="$directory2"/>.<xsl:value-of select="$directory3"/>/<xsl:value-of select="$archiveNumber"/>
  </xsl:template>

  <xsl:template name="getFigureID">
    <xsl:param name="fID"/>
    <xsl:choose>
      <xsl:when test="contains($fID,'.')">
          <xsl:choose>
	   <xsl:when test="string-length(substring-before($fID, '.')) = 4">
	     <xsl:text>0</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length(substring-before($fID, '.')) = 3">
	    <xsl:text>00</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length(substring-before($fID, '.')) = 2">
	    <xsl:text>000</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length(substring-before($fID, '.')) = 1">
	    <xsl:text>0000</xsl:text>
	   </xsl:when>
	 </xsl:choose> 
        <xsl:value-of select="substring-before($fID,'.')"/><xsl:text>_</xsl:text><xsl:value-of
    select="substring-after($fID, '.')"/>    
      </xsl:when>
      <xsl:otherwise>
          <xsl:choose>
	   <xsl:when test="string-length($fID) = 4">
	     <xsl:text>0</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length($fID) = 3">
	    <xsl:text>00</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length($fID) = 2">
	    <xsl:text>000</xsl:text>
	   </xsl:when>
	   <xsl:when test="string-length($fID) = 1">
	    <xsl:text>0000</xsl:text>
	   </xsl:when>
	 </xsl:choose> 
	 <xsl:value-of select="$fID"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="figure[@id!='' and not(descendant::p)]">
    <xsl:variable name="fID"><xsl:value-of select="substring-after(./@id,'fig.')"/></xsl:variable>
    <xsl:variable name="figID"><xsl:call-template name="getFigureID"><xsl:with-param name="fID"><xsl:value-of select="$fID"/></xsl:with-param></xsl:call-template></xsl:variable>
    <xsl:variable name="imID"><xsl:value-of select="concat('Perseus:image:', substring-after($document_id, 'Perseus:text:'))"/><xsl:text>.fig</xsl:text><xsl:value-of select="$figID"/></xsl:variable>
    <xsl:variable name="thumbURL">
    	<xsl:call-template name="getThumbURL">
    		<xsl:with-param name="n" select="$imID"/>
    	</xsl:call-template>
    </xsl:variable>
    <xsl:variable name="align">
      <xsl:call-template name="figalign"/>
    </xsl:variable>
    <p><table width="120" align="{$align}">
      <tr><td>
      <a target="figure" href="/hopper/image?img={$imID}"><img class="thumb_img" src="{$thumbURL}"/></a>
      </td></tr>
      <tr><td><xsl:apply-templates /></td></tr>
    </table></p>
  </xsl:template>

  <xsl:template match="figure[@id!='' and descendant::p]">
    <xsl:variable name="fID"><xsl:value-of select="substring-after(./@id,'fig.')"/></xsl:variable>
    <xsl:variable name="figID"><xsl:call-template name="getFigureID"><xsl:with-param name="fID"><xsl:value-of select="$fID"/></xsl:with-param></xsl:call-template></xsl:variable>
    <xsl:variable name="imID"><xsl:value-of select="concat('Perseus:image:', substring-after($document_id, 'Perseus:text:'))"/><xsl:text>.fig</xsl:text><xsl:value-of select="$figID"/></xsl:variable>
    <xsl:variable name="thumbURL">
    	<xsl:call-template name="getThumbURL">
    		<xsl:with-param name="n" select="$imID"/>
    	</xsl:call-template>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="ancestor::table">
        <table align="center">
          <tr><td>
          <a target="figure" href="/hopper/image?img={$imID}"><img class="thumb_img" src="{$thumbURL}"/></a>
        	</td>
          <td><xsl:apply-templates /></td></tr>
        </table>
      </xsl:when>
      <xsl:otherwise>
        <p>
        <table border="1" width="90%" align="center">
        <tr><td align="center">
        <a target="figure" href="/hopper/image?img={$imID}"><img class="thumb_img" src="{$thumbURL}"/></a>
        </td></tr>
        <tr><td><xsl:apply-templates /></td></tr>
        </table></p>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="figure/head">
    <span class="help_note"><xsl:apply-templates /></span>
  </xsl:template>

  <xsl:template match="etym">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ovar">
    <i><xsl:value-of select="."/></i> 
  </xsl:template>

  <xsl:template match="hi[@rend='center']">
    <center><xsl:value-of select="."/></center>
  </xsl:template>
  
  <xsl:template match="hi[@rend='super']">
    <sup><xsl:value-of select="."/></sup>
  </xsl:template>
  
  <xsl:template match="hi[@rend='bold']">
    <b><xsl:value-of select="."/></b>
  </xsl:template>
	
	<xsl:template match="hi[@rend='caps']">
		<span style="font-variant: small-caps;"><xsl:apply-templates /></span>
	</xsl:template>

  <xsl:template match="hi[@rend='ital' or @rend='italics' or @rend='italic']">
    <xsl:if test="text() or child::*">
    	<i><xsl:apply-templates /></i> 
    </xsl:if>
  </xsl:template>

  <xsl:template match="tr|trans/tr">
    <i><xsl:value-of select="."/></i> 
  </xsl:template>

  <xsl:template match="argument">
    <xsl:apply-templates />
    <hr/>
  </xsl:template>

  <xsl:template match="opener">
    <div align="right">
      <xsl:apply-templates />
    </div>
  </xsl:template>

  <xsl:template match="dateline">
    <xsl:apply-templates /><br/>
  </xsl:template>

  <xsl:template match="salute">
    <xsl:apply-templates /><br/>
  </xsl:template>

  <xsl:template match="p">
      <xsl:apply-templates /><p/>
  </xsl:template>

  <xsl:template match="gap">
    <xsl:choose>
      <xsl:when test="@desc">
        <br/><xsl:value-of select="@desc" />
      </xsl:when>
      <xsl:when test="@reason">
        <br/><span class="english"><xsl:value-of select="@reason" /></span>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text> ... </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="note[@place='inset']">
    <div style="float:right">
    <xsl:apply-templates />&#160;
    </div>
  </xsl:template>

  <xsl:template match="note//note">
    <xsl:apply-templates />&#160;
  </xsl:template>

  <xsl:template match="note[@place='sum']">
    <h4><xsl:apply-templates /></h4>
  </xsl:template>

  <xsl:template name="margalign">
    <xsl:variable name="margID">
      <xsl:number level="any" count="note[@place='marg']" from="body"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$margID mod 2 = 0 or @resp='ed'">
        <xsl:text>left</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>right</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="note[@place='marg']">
    <xsl:variable name="align">
      <xsl:call-template name="margalign"/>
    </xsl:variable>
    <table width="100" align="{$align}"><tr><td bgcolor="#f0f0f0"><h5>
      <span class="sidetext">
      <xsl:apply-templates />
      </span>
    </h5></td></tr></table>
  </xsl:template>

  <xsl:template match="note[@place='inline' and @resp!='' and @rend='credit']">
    <xsl:apply-templates />
    <xsl:text> -- </xsl:text>
    <xsl:value-of select="@resp"/>
  </xsl:template>

  <xsl:template match="item[ancestor::argument and position() != 1]">
	-- <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="item[bibl or list]">
     <dd><xsl:apply-templates /></dd>
  </xsl:template>

  <xsl:template match="item">
    <xsl:choose>
	<xsl:when test="parent::list[@type='illustrations']">
	    <xsl:apply-templates />
	</xsl:when>
	<xsl:otherwise>
	   <li><xsl:apply-templates /></li>
	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="note[@place='inline']">
      <xsl:choose>
	  <xsl:when test="@rend='ag' or @rend='smyth'">
	      <p style="font-size: small; margin: 10px 5%;">

		  <xsl:call-template name="permalink">
		      <xsl:with-param name="smythp" select="(preceding::milestone[@unit='smythp'])[last()]/@n" />

		      <!-- Pass a nonempty parameter for the subsection iff
		      we're currently in a subsection (i.e., we haven't seen
		      any new sections since we last saw a subsection). -->

		      <xsl:with-param name="smythsub">
			  <xsl:if test="(preceding::milestone[@unit='smythp' or @unit='smythsub'])[last()]/@unit='smythsub'">
			      <xsl:value-of select="(preceding::milestone[@unit='smythsub'])[last()]/@n" />
			  </xsl:if>
		      </xsl:with-param>

		      <xsl:with-param name="current_note" select="@n" />
		  </xsl:call-template>

		  <xsl:text> </xsl:text>

		  <span style="font-variant: small-caps;">
		      <xsl:text>Note</xsl:text>
		      <xsl:if test="@n &gt; 1 or (following-sibling::note[@place='inline' and (@rend='ag' or @rend='smyth')][1]/@n) &gt; 1">

			  <xsl:text> </xsl:text>
			  <xsl:value-of select="@n" />
		      </xsl:if>
		      <xsl:text>.--</xsl:text>
		  </span>
		  <xsl:apply-templates />
	      </p>
	  </xsl:when>
	  <xsl:otherwise>
	      <i><xsl:apply-templates /></i>
	  </xsl:otherwise>
      </xsl:choose>
  </xsl:template>
  <!--
  <xsl:template match="note[@place='inline' and @rend='ag']">

  </xsl:template>
  -->


 <!--Used by MPI texts - so not currently used because they are not in the hopper-->
  <xsl:template match="pb[@img]">
	<a target="figure" href="/hopper/image?img={@img}">[<xsl:value-of select="substring-after(@id, 'p.')"/>]</a>
 </xsl:template>

  <xsl:template match="pb">
        <xsl:variable name="figID"><xsl:call-template name="getFigureID"><xsl:with-param name="fID"><xsl:value-of select="./@n"/></xsl:with-param></xsl:call-template></xsl:variable>
        <xsl:variable name="imID"><xsl:value-of select="$document_id"/><xsl:text>.</xsl:text><xsl:value-of select="$figID"/></xsl:variable>
	<xsl:choose>
	  <!-- Want to include page images for the American Collection-->
	  <xsl:when test="@id and starts-with($document_id, 'Perseus:text:2001.05')">
	  	<xsl:variable name="imageID"><xsl:value-of select="concat('Perseus:image:', substring-after($imID, 'Perseus:text:'))"/></xsl:variable>
		[<a target="figure"
        href="/hopper/image?img={$imageID}"><xsl:value-of
        select="./@n"/></a>]   		
	  </xsl:when>
	  <xsl:when test="@id">
	  	<xsl:variable name="pagenum"><xsl:value-of select="substring-after(@id, 'p.')"/></xsl:variable>
		<xsl:if test="$pagenum != ''">
			<span lang="en">[p. <xsl:value-of select="$pagenum"/>]</span>
	  	</xsl:if>
	  </xsl:when>
	</xsl:choose>
  </xsl:template>

  <xsl:template name="footnoteID">
    <xsl:choose>
      <xsl:when test="@id">
        <xsl:value-of select="@id"/>
      </xsl:when>
      <xsl:when test="@n">
        <xsl:value-of select="@n"/>
      </xsl:when>
      <xsl:when test="ancestor::back">
        <xsl:number level="any"  count="note[@place='foot']|note[@place='unspecified']|note[@place='text']|note[not(@place)]" from="back"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:number level="any" count="note[@place='foot']|note[@place='unspecified']|note[@place='text']|note[not(@place)]" from="body"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="note[@place='foot']|note[@place='unspecified']|note[@place='text']|note[not(@place)]">
    <xsl:variable name="identifier">
      <xsl:call-template name="footnoteID"/>
    </xsl:variable>
    <xsl:if test="not(@rend) or @rend != 'smyth'">
	<a id="note-link{$identifier}" href="#note{$identifier}"><sup><xsl:value-of select="count(preceding::note[@place = 'foot' or @place = 'unspecified' or @place = 'text' or not(@place)]) + 1" /></sup></a>
    </xsl:if>
  </xsl:template>

  <xsl:template match="cit">
    <xsl:choose>
      <xsl:when test="quote/l or quote/sp or quote/p or quote/lg">
        <blockquote>
          <xsl:call-template name="block-quote" />
        </blockquote>
      </xsl:when>
      <xsl:otherwise>
	  <xsl:apply-templates />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="block-quote">
    <p><xsl:apply-templates select="quote"/></p>
    <div align="right"><xsl:apply-templates select="bibl"/></div>
  </xsl:template>

  <xsl:template match="list">
	<xsl:choose>
	  <xsl:when test="parent::argument">
		<xsl:apply-templates />
	  </xsl:when>
	  <xsl:when test="@type='ordered'">
	      <ol><xsl:apply-templates /></ol>
	  </xsl:when>
	  <xsl:when test="@type='gloss'">
	      <dl><xsl:apply-templates /></dl>
	  </xsl:when>
	  <xsl:when test="@type='illustrations'">
	      <p><table width="100%" style="text-align:center">
		   <xsl:for-each select="child::item">
	            <tr>
		      <td width="33%">
			<xsl:apply-templates />
		      </td>
		    </tr>
		    </xsl:for-each>
		</table>
		</p>
	  </xsl:when>
	  <xsl:otherwise>
		<ul><xsl:apply-templates /></ul>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:template>

  <xsl:template match="listbibl|listBibl">
    <ul><xsl:apply-templates /></ul>
  </xsl:template>

  <xsl:template match="bibl[parent::cit]">
    <xsl:text> </xsl:text>
    <xsl:call-template name="bibl-link"/>
  </xsl:template>

  <xsl:template match="bibl[parent::listbibl]">
    <li>
      <xsl:call-template name="bibl-link"/>
    </li>
  </xsl:template>

  <xsl:template match="bibl">
    <xsl:call-template name="bibl-link"/>
  </xsl:template>

  <xsl:template name="bibl-link">
    <xsl:choose>
        <xsl:when test="not(@n) and . = ''"></xsl:when>
	<xsl:when test="not(@valid)">
	    <i><xsl:call-template name="language-filter">
              <xsl:with-param name="lang" select="@lang" />
	      <xsl:with-param name="default" select="'en'" />
            </xsl:call-template></i>
	</xsl:when>
	<xsl:when test="@n=concat($sourcework,':',$sourcesub)">
	    <span style="font-size: x-large;"><a href="text?doc={@n}&amp;lang=original" target="_new"><xsl:call-template name="language-filter">
              <xsl:with-param name="lang" select="@lang" />
	      <xsl:with-param name="default" select="'en'" />
            </xsl:call-template></a></span>
	</xsl:when>
      <xsl:when test="@n and $sourcework != 'none' and starts-with(@n, $sourcework)">
	  <b><a href="text?doc={@n}&amp;lang=original" target="_new"><xsl:call-template name="language-filter">
            <xsl:with-param name="lang" select="@lang" />
	    <xsl:with-param name="default" select="'en'" />
          </xsl:call-template></a></b>
      </xsl:when>
      <xsl:when test="@n">
	  <a href="text?doc={@n}&amp;lang=original" target="_new">
	    <xsl:call-template name="language-filter">
            <xsl:with-param name="lang" select="@lang" />
	    <xsl:with-param name="default" select="'en'" />
          </xsl:call-template>
	  </a>
      </xsl:when>
      <xsl:otherwise>
	<xsl:call-template name="language-filter">
          <xsl:with-param name="lang" select="@lang" />
	  <xsl:with-param name="default" select="'en'" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="xref[@n!='']">
    <a href="{@n}"><xsl:apply-templates /></a>
  </xsl:template>

  <xsl:template match="ref[@target!='']">
    <a href="text?doc={$document_id}:id={@target}"><xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template></a>
  </xsl:template>

  <xsl:template match="//ref[@lang!='']">
  	<xsl:call-template name="language-filter">
		<xsl:with-param name="lang" select="@lang"/>
	</xsl:call-template>	
  </xsl:template>
	
	<xsl:template match="xptr[@type='Building' or @type='Coin' or @type='Gem' or @type='Sculpture' or @type='Site' or @type='Vase']">
		<a href="artifact?name={@from}&amp;object={@type}" target="_new"><xsl:value-of select="@n"/></a> <em> [<xsl:value-of select="@type"/>]</em>
	</xsl:template>
	
	<xsl:template match="xptr[@type='Browser']">
		<xsl:variable name="object" select="substring-before(@from,'|')"/>
		<xsl:variable name="remaining" select="substring-after(@from,'|')"/>
		<xsl:variable name="field" select="substring-before($remaining,'|')"/>
		<xsl:variable name="value" select="substring-after($remaining,'|')"/>
		<a href="artifactBrowser?object={$object}&amp;field={$field}&amp;value={$value}" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>
	
	<xsl:template match="xptr[@type='ArtifactSearch']">
		<xsl:variable name="type" select="substring-before(@from,'|')"/>
		<xsl:variable name="query" select="substring-after(@from,'|')"/>
		<a href="artifactSearch?q={$query}&amp;artifact=yes&amp;artifactType={$type}" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>

	<xsl:template match="xptr[@type='ImageSearch']">
		<a href="artifactSearch?q={@from}&amp;image=yes" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>
	
	<xsl:template match="xptr[@type='EnglishClassicsSearch']">
		<a href="searchresults?target=en&amp;collections=Perseus:collection:Greco-Roman&amp;any_words={@from}" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>
	
	<xsl:template match="xptr[@type='GreekClassicsSearch']">
		<a href="searchresults?target=greek&amp;collections=Perseus:collection:Greco-Roman&amp;any_words={@from}&amp;any_words_expand=on" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>
	
	<xsl:template match="xptr[@type='Collection']">
		<a href="collection?collection={@from}" target="_new"><xsl:value-of select="@n"/></a>
	</xsl:template>

 <xsl:template match="quote|q">


   <xsl:choose>
     <xsl:when test="@rend[contains(.,'blockquote')]">
      <blockquote><xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template></blockquote>
      </xsl:when>
     <xsl:otherwise>

       <xsl:choose>
	 <xsl:when test="parent::cit and ancestor::quote">
		<xsl:apply-templates />
	 </xsl:when>
	 <xsl:otherwise>

	   <xsl:choose>
	     <xsl:when test="self::quote">
	       <xsl:text>&#x201C;</xsl:text>
	     </xsl:when>
	     <xsl:otherwise>
	       <xsl:text>&#x2018;</xsl:text>
	     </xsl:otherwise>
	 </xsl:choose>


       <xsl:call-template name="language-filter">
         <xsl:with-param name="lang" select="@lang" />
       </xsl:call-template>
 
       <xsl:choose>
	 <xsl:when test="parent::cit and ancestor::quote">
	 </xsl:when>
	 <xsl:otherwise>
	   <xsl:choose>
	     <xsl:when test="self::quote">
	       <xsl:text>&#x201D;</xsl:text>
	     </xsl:when>
	     <xsl:otherwise>
	       <xsl:text>&#x2019;</xsl:text>
	     </xsl:otherwise>
	   </xsl:choose>


	 </xsl:otherwise>
       </xsl:choose>
  	 </xsl:otherwise>
       </xsl:choose>
    

       </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="castlist|castList">
    <xsl:apply-templates />
  </xsl:template>

<xsl:template match="castGroup">
    <p>
	<xsl:apply-templates />
    </p>
</xsl:template>

  <xsl:template match="castItem">
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="role">
    <xsl:choose>
      <xsl:when test="following-sibling::roleDesc">
        <xsl:apply-templates />
        <xsl:text>, </xsl:text>
      </xsl:when>
      <xsl:when test="not(text()) and not(child::*)"/>
      <xsl:when test="parent::castItem[@type='list']">
        <xsl:apply-templates />
	<xsl:text>, </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates /><br/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="roleDesc">
    <xsl:apply-templates /><br/>
  </xsl:template>

  <xsl:template match="sp">
    <xsl:if test="position()!=1">
      <p/>
    </xsl:if>
    <xsl:if test="@n!='' and speaker">
      <b><xsl:value-of select="@n"/></b><br/>
    </xsl:if>
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="speaker">
    <xsl:if test="parent::sp">
      <p/>
    </xsl:if>
    <b><xsl:value-of select="."/></b><br/>
  </xsl:template>

  <xsl:template match="stage">
    <i><xsl:apply-templates /></i>
    <xsl:choose>
	<xsl:when test="not(parent::p)">
	    <br/>
	</xsl:when>
	<xsl:otherwise>
	    <xsl:text> </xsl:text>
	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="l|L">
    <xsl:variable name="linenumber">
      <xsl:number />
    </xsl:variable>

    <xsl:if test="@n!='tr' and @n mod 5 = 0">
      <span style="float: right" class="linenumber"><span class="english"><xsl:value-of select="@n"/></span></span>
    </xsl:if>

    <xsl:if test="ancestor::lg[@type='pentameter' or @type='altindent'] and $linenumber mod 2 = 0">
      &#160;&#160;&#160;&#160;
    </xsl:if>

    <xsl:if test="@part='F'">
      &#160;&#160;&#160;&#160;--
    </xsl:if>

    <xsl:if test="@rend='indent'">
      &#160;&#160;&#160;&#160;
    </xsl:if>
    <xsl:apply-templates/><br/>
  </xsl:template>

  <!-- this is used by at least the Shakespeare texts -->
  <xsl:template match="lb[@n!='' and @ed='G']">
<!--    <xsl:if test="number(@n)=NaN or @n mod 5 = 0">-->
      <span style="float: right" class="linenumber"><span class="english">(<xsl:value-of select="@n"/>)</span></span>
<!--    </xsl:if>-->
    <br/>
  </xsl:template>

  <xsl:template match="lb[@n!='' and @type='displayNum']">
      <xsl:if test="number(@n)=NaN or @n mod 5 = 0">
          <span style="float: right" class="linenumber"><span class="english"><xsl:value-of select="@n"/></span></span>
      </xsl:if>
    <br/>
  </xsl:template>


  <xsl:template match="lg|lg1">
    <xsl:variable name="linecount" select="1" />
    <xsl:apply-templates/><p/>
  </xsl:template>

  <xsl:template match="caesura">
    &#160;&#160;&#160;&#160;
  </xsl:template>

  <xsl:template match="milestone[@unit='chapter']">
    <b><xsl:value-of select="@n"/>.</b>
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="milestone[@unit='smythp']">

      <xsl:call-template name="permalink">
	  <xsl:with-param name="smythp" select="@n" />
	  <xsl:with-param name="smythsub" select="''" />
	  <xsl:with-param name="current_note" select="''" />
      </xsl:call-template>

      <xsl:text> </xsl:text>
      <b><xsl:value-of select="@n"/>.</b>
      <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="milestone[@unit='smythsub']">
      <xsl:variable name="current_smythsub" select="@n" />

      <xsl:call-template name="permalink">
	  <xsl:with-param name="smythp" select="(preceding::milestone[@unit='smythp'])[last()]/@n" />
	  <xsl:with-param name="smythsub" select="@n" />
	  <xsl:with-param name="current_note" select="''" />
      </xsl:call-template>

      <xsl:text> </xsl:text>
      <b><i><xsl:value-of select="@n"/>.</i></b>
      <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template name="permalink">
      <xsl:param name="smythp"/>
      <xsl:param name="smythsub"/>
      <xsl:param name="current_note"/>

      <xsl:variable name="link">
	  <xsl:text>chapter</xsl:text>
	  <xsl:value-of select="$smythp" />
	  <xsl:value-of select="$smythsub" />
	  <xsl:if test="$current_note != ''">
	      <xsl:text>.note</xsl:text>
	      <xsl:value-of select="$current_note"/>
	  </xsl:if>
      </xsl:variable>

      <a class="permalink" id="{$link}" href="#{$link}">[*]</a>
  </xsl:template>

  <!-- section numbers have to be wrapped in <span class="english"> tags to prevent
       the Greek transcoder from interpreting "184c" as xi. -->
  <xsl:template match="milestone[@unit='section']">
    <xsl:if test="@n!=1">
      [<span class="english"><xsl:value-of select="@n"/></span>]
      <xsl:text> </xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="milestone[@unit='para']">
    <p/>
    <xsl:if test="@ref!=''">
      <span class="english"><xsl:value-of select="@ref"/></span>
    </xsl:if>
    <xsl:if test="parent::l and position()!=1">
      <xsl:text>&#160;&#160;&#160;&#160;</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="milestone[@unit='tale']">
    <h4><xsl:value-of select="@n"/></h4>
  </xsl:template>

  <xsl:template match="milestone[@unit='line' and @ed!='exclude']">
    [<xsl:value-of select="@n"/>]
  </xsl:template>

  <xsl:template match="milestone[@unit='verse']">
    <xsl:if test="@n!=1">
      [<xsl:value-of select="@n"/>]
    </xsl:if>
  </xsl:template>

  <xsl:template match="div1">
    <xsl:choose>
	<xsl:when test="@lang">
	  <xsl:call-template name="language-filter">
	    <xsl:with-param name="lang" select="@lang" />
	  </xsl:call-template>
	</xsl:when>
	<xsl:otherwise>
	    <xsl:apply-templates />
	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="div2[@type='commline']">
    <p>
    <xsl:if test="@n!=1">
      [<xsl:value-of select="@n"/>]
    </xsl:if>
    <xsl:apply-templates />
    </p>
  </xsl:template>

  <xsl:template match="div2|div3|div4">
	<hr/><xsl:apply-templates />
</xsl:template>

  <xsl:template match="lemma">
    <b><u>
      <xsl:call-template name="language-filter">
        <xsl:with-param name="lang" select="@lang" />
      </xsl:call-template>
    </u></b>
  </xsl:template>

  <xsl:template match="foreign">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="orig">
     <xsl:value-of select="@reg" />
  </xsl:template>

  <xsl:template match="pron">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="itype">
    <xsl:call-template name="language-filter">
      <xsl:with-param name="lang" select="@lang" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table">
    <p><table border="1">
      <xsl:apply-templates/>
    </table></p><p/>
  </xsl:template>

<xsl:template match="name[@type='place']|placeName|placename|PlaceName|Placename">
  <xsl:call-template name="render-entity">
      <xsl:with-param name="class" select="'place'" />
  </xsl:call-template>
</xsl:template>

<xsl:template match="name[@type='ship']">
  <xsl:call-template name="render-entity">
      <xsl:with-param name="class" select="'ship'" />
  </xsl:call-template>
</xsl:template>

<xsl:template match="persname|persName|PersName|Persname">
  <xsl:call-template name="render-entity">
      <xsl:with-param name="class" select="'person'" />
  </xsl:call-template>
</xsl:template>

<!--<xsl:template match="date|datestruct|dateStruct">
  <xsl:call-template name="render-entity">
      <xsl:with-param name="class" select="'date'" />
  </xsl:call-template>
</xsl:template>-->

<xsl:template name="render-entity">
    <xsl:param name="class" select="'entity'" />

    <xsl:variable name="thisAuthname" select="@authname" />

	<xsl:choose>
	<xsl:when test="@authname != ''">
    	<xsl:element name="span">
		<xsl:attribute name="class">
	    	<xsl:if test="@authname=$highlight_authname">
			<xsl:text>search_result </xsl:text>
	    	</xsl:if>
	    	<xsl:value-of select="$class" />
		</xsl:attribute>
		<xsl:if test="@authname=$highlight_authname">
	   		<xsl:attribute name="id">
			<xsl:text>match</xsl:text><xsl:value-of select="count(preceding::*[@authname=$highlight_authname]) + 1" />
	    	</xsl:attribute>
		</xsl:if>
        	<a target="_blank" onclick="openPopupWindow(this); return false" href="entityvote?doc={$query}&amp;auth={@authname}&amp;n={count(preceding::*[@authname=$thisAuthname]) + 1}&amp;type={$class}"><xsl:apply-templates /></a>
    	</xsl:element>
    </xsl:when>
    <xsl:otherwise>
    	<xsl:apply-templates />
    </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match="rs|RS|Rs">
  <span class="ref"><xsl:apply-templates /></span>
</xsl:template>

<xsl:template match="orgname|orgName">
  <xsl:apply-templates/>
  <!-- <xsl:call-template name="render-entity">
      <xsl:with-param name="class" select="'org'" />
  </xsl:call-template> -->
</xsl:template>

  <xsl:template match="row">
    <tr>
      <xsl:apply-templates/>
    </tr>
  </xsl:template>

  <xsl:template match="cell">
    <td>
        <xsl:if test="@cols">
	  <xsl:attribute name="colspan"><xsl:value-of select="@cols" /></xsl:attribute>
	</xsl:if>
        <xsl:if test="@rows">
	  <xsl:attribute name="rowspan"><xsl:value-of select="@rows" /></xsl:attribute>
	</xsl:if>
      <xsl:apply-templates/>
    </td>
  </xsl:template>

<!--
  <xsl:template match="text()">
    <xsl:value-of select="."/>
  </xsl:template>
-->

  <!-- The following templates are for the Old Perseus DTD -->

  <xsl:template match="pindhead">
    <h4><xsl:apply-templates /></h4>
  </xsl:template>

  <xsl:template match="winner|race">
    <xsl:apply-templates />,
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="date">
    <xsl:apply-templates />
    <xsl:text></xsl:text>
  </xsl:template>

  <xsl:template match="strophe">
    <p>
      [<xsl:value-of select="@name"/>.]<br/>
      <xsl:apply-templates />
    </p>
  </xsl:template>

  <xsl:template match="section">
    <p>
      [<xsl:value-of select="@n"/>.]
      <xsl:text> </xsl:text>
      <xsl:apply-templates />
    </p>
  </xsl:template>
    
    <xsl:template match="seg">
    <xsl:choose>
    <!-- We don't have a way to display the combining double breve below, unicode &#860, so ignore them for now
    	 because inserting a span element breaks up the morph links, which is bad. -->
    	<xsl:when test="@type='sling'">
    		<xsl:apply-templates />
    	</xsl:when>
    	<xsl:otherwise>
        	<xsl:element name="span">
	    	<xsl:if test="@rend">
				<xsl:attribute name="class"><xsl:value-of select="@rend" /></xsl:attribute>
	    	</xsl:if>
            <xsl:apply-templates />
        	</xsl:element>
        </xsl:otherwise>
    </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
