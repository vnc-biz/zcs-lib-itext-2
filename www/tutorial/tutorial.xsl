<?xml version="1.0" encoding="UTF-8"?>

<!-- $Header$ -->
<!-- author: Bruno Lowagie -->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns:site="http://www.lowagie.com/iText/site"
	exclude-result-prefixes="site html" >

<xsl:output method="html" doctype-public="-//W3C//DTD HTML 4.0 Transitional//EN" indent="no" media-type="text/html" />

<!-- Amazon related stuff -->

<xsl:template name="amazonasin">
<xsl:param name="asins"/>
<script type="text/javascript"><![CDATA[<!--
document.write('<iframe src="http://rcm.amazon.com/e/cm?t=itisacatalofwebp&o=1&p=8&l=as1&asins=]]><xsl:value-of select="$asins" /><![CDATA[&fc1=000000&lc1=0000ff&bc1=&lt1=_blank&IS2=1&bg1=ffffff&f=ifr" width="120" height="240" scrolling="no" marginwidth="0" marginheight="0" frameborder="0" align="Center"></iframe>');
//-->]]></script>
<xsl:element name="a">
	<xsl:attribute name="href">http://www.amazon.co.uk/exec/obidos/ASIN/<xsl:value-of select="substring($asins, 0, 11)" />/catloogjecom-21</xsl:attribute>
	<xsl:attribute name="class">amazonlinks</xsl:attribute>
	amazon.co.uk-link
</xsl:element><br />
</xsl:template>

<xsl:template name="amazonkeyword">
<xsl:param name="keyword"/>
<script type="text/javascript"><![CDATA[<!--
document.write('<iframe src="http://rcm.amazon.com/e/cm?t=itisacatalofwebp&o=1&p=10&l=st1&mode=books&search=]]><xsl:value-of select="$keyword" /><![CDATA[&=1&fc1=&lc1=&lt1=&bg1=&f=ifr" width="120" height="460" border="0" frameborder="0" style="border:none;" scrolling="no" marginwidth="0" marginheight="0"></iframe>');
//-->]]></script>
</xsl:template>

<!-- Keeping the html as is -->

<xsl:template match="html:*">
	<xsl:copy>
		<xsl:apply-templates select="*|text()|@*" />
	</xsl:copy>
</xsl:template>

<xsl:template match="@*">
	<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
</xsl:template>

<!-- examples -->

<xsl:param name="root" select="/site:page/site:metadata/site:tree/@root" />
<xsl:param name="branch" select="/site:page/site:metadata/site:tree/@branch" />

<xsl:template match="site:source">
  <xsl:param name="class" select="@class" />
  <div id="example">
  <xsl:for-each select="/site:page/site:examples/site:example">
  	<xsl:if test="$class=./site:java/@src">
  	  Example: java
  	  <xsl:element name="a">
  		<xsl:attribute name="href">..<xsl:value-of select="$root" />/examples/com/lowagie/examples<xsl:value-of select="$branch" />/<xsl:value-of select="site:java/@src" />.java</xsl:attribute>
  		com.lowagie.examples<xsl:value-of select="translate($branch, '/', '.')" />.<xsl:value-of select="site:java/@src" />
    </xsl:element>
    <xsl:if test="count(site:argument)!=0" >
      <xsl:for-each select="site:argument"><xsl:value-of select="string(' ')" /><xsl:value-of select="." /></xsl:for-each>
    </xsl:if><br />
    <xsl:value-of select="site:description/." />: see
	<xsl:for-each select="site:result">
	  <xsl:value-of select="string(' ')" />
	  <xsl:element name="a">
      	<xsl:attribute name="href"><xsl:value-of select="." /></xsl:attribute>
  		<xsl:value-of select="." />
  	  </xsl:element>
    </xsl:for-each><br />
    <xsl:if test="count(site:externalresource)!=0" >
      External resources for this example:
      <xsl:for-each select="site:externalresource">
      <xsl:value-of select="string(' ')" />
      <xsl:element name="a">
      <xsl:attribute name="href"><xsl:value-of select="." /></xsl:attribute>
      <xsl:value-of select="." />
      </xsl:element>
      </xsl:for-each><br />
    </xsl:if>
  	<xsl:if test="count(site:extrajar)>0">
	  Extra jars needed in your CLASSPATH:
  	  <xsl:for-each select="./site:extrajar"><xsl:value-of select="string(' ')" /><xsl:value-of select="." /></xsl:for-each><br />
	</xsl:if>
    </xsl:if>
  </xsl:for-each>
  </div>
</xsl:template>

<xsl:template match="site:examples">
  <xsl:for-each select="site:example">
  	<div class="example">
  	<xsl:element name="a">
  		<xsl:attribute name="class">source</xsl:attribute>
  		<xsl:attribute name="href">..<xsl:value-of select="$root" />/examples/com/lowagie/examples<xsl:value-of select="$branch" />/<xsl:value-of select="site:java/@src" />.java</xsl:attribute>
  		<xsl:value-of select="site:java/@src" />
    </xsl:element><br />
  	<div class="description"><xsl:value-of select="site:description/." /></div>
  	<xsl:if test="count(site:extrajar)>0">
	  <div class="small">Extra jars needed:</div>
  	  <ul><xsl:for-each select="./site:extrajar">
	    <li><xsl:value-of select="." /></li>
      </xsl:for-each></ul>
	</xsl:if>
    <xsl:if test="count(site:argument)!=0" >
      <div class="small">Argument(s):</div>
      <ul><xsl:for-each select="site:argument">
  		<li><xsl:value-of select="." /></li>
      </xsl:for-each></ul>
    </xsl:if>
    <xsl:if test="count(site:externalresource)!=0" >
      <div class="small">Input:</div>
      <ul><xsl:for-each select="site:externalresource">
  		<li><xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="." /></xsl:attribute><xsl:value-of select="." /></xsl:element></li>
      </xsl:for-each></ul>
    </xsl:if>
 	<div class="small">Output:</div>
 	<ul><xsl:for-each select="site:result">
      <li><xsl:element name="a">
      	<xsl:attribute name="href"><xsl:value-of select="." /></xsl:attribute>
  		<xsl:value-of select="." />
  	  </xsl:element></li>
    </xsl:for-each></ul>
    </div>
  </xsl:for-each>
</xsl:template>

<!-- the sections -->

<xsl:template match="site:chapter">
	<xsl:for-each select="site:section">
		<xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute></xsl:element>
		<div class="title">
			<xsl:value-of select="site:sectiontitle" />:
		</div>
		<xsl:apply-templates select="html:div" />
		<a class="top" href="#top">Go to top of the page</a>
	</xsl:for-each>
	<div xmlns="http://www.w3.org/1999/xhtml" id="footer">
	Page Updated: <xsl:value-of select="substring(site:metadata/site:updated, 8, 19)" /><br />
	Copyright &#169; 1999-2004
	<xsl:for-each select="/site:page/site:metadata/site:author"><xsl:value-of select="." /><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>
	</div>
</xsl:template>

<!-- the actual page -->

<xsl:template match="site:page">
<html>

<head>
	<xsl:element name="title">iText Tutorial: <xsl:value-of select="site:metadata/site:title" />
	</xsl:element>
	<xsl:element name="meta">
		<xsl:attribute name="name">Description</xsl:attribute>
		<xsl:attribute name="content"><xsl:value-of select="site:metadata/site:summary" /></xsl:attribute>
	</xsl:element>
	<xsl:element name="meta">
		<xsl:attribute name="name">Keywords</xsl:attribute>
		<xsl:attribute name="content"><xsl:value-of select="site:metadata/site:keywords" /></xsl:attribute>
	</xsl:element>
	<xsl:element name="link">
		<xsl:attribute name="rel">stylesheet</xsl:attribute>
		<xsl:attribute name="href">.<xsl:value-of select="$root" />/style.css</xsl:attribute>
		<xsl:attribute name="type">text/css</xsl:attribute>
	</xsl:element>
</head>

<body>
	<a name="top" class="logo" href="http://www.lowagie.com/iText"><img src="http://www.lowagie.com/iText/images/logo.gif" border="0" alt="iText" /></a>
	<h1>Tutorial</h1>
    <h2><xsl:value-of select="site:metadata/site:title" /></h2>
    
<xsl:element name="div">
	<xsl:attribute name="id">content</xsl:attribute>
    
    <xsl:element name="div">
		<xsl:attribute name="id">sidebar</xsl:attribute>
		<xsl:element name="a">
		  <xsl:attribute name="class">toc</xsl:attribute>
          <xsl:attribute name="href">.<xsl:value-of select="$root" /></xsl:attribute>
		  Table of Contents
	    </xsl:element>
		<div class="sidetitle">Sections:</div>
		<ul>
		<xsl:for-each select="site:chapter/site:section">
			<li><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="@name" /></xsl:attribute>
				<xsl:value-of select="site:sectiontitle" />
			</xsl:element></li>
		</xsl:for-each>
		</ul><br /><br />
		<div class="sidetitle">Examples:</div>
		<xsl:apply-templates select="site:examples" />
	</xsl:element>
    
    <xsl:element name="div">
    	<xsl:attribute name="id">main</xsl:attribute>
    	<xsl:apply-templates select="site:chapter" />
    </xsl:element>
    
</xsl:element>

<div class="commercial">

<br />

<!-- Google -->

<script type="text/javascript"><![CDATA[<!--
google_ad_client = "pub-0340380473790570";
google_ad_width = 120;
google_ad_height = 600;
google_ad_format = "120x600_as";
google_ad_channel ="1357857646";
google_ad_type = "text_image";
google_color_border = "FFFFFF";
google_color_bg = "FFFFFF";
google_color_link = "1B09BD";
google_color_url = "100670";
google_color_text = "707070";
//-->]]></script>
<script type="text/javascript"
  src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>

<br />
<br />

<!-- Amazon -->

<div class="subtitle">Amazon books:</div>
<xsl:for-each select="/site:page/site:metadata/site:amazonbooks/site:book">
	<xsl:call-template name="amazonasin"><xsl:with-param name="asins"><xsl:value-of select="string(@asin)" /></xsl:with-param></xsl:call-template><br />
</xsl:for-each>
<xsl:for-each select="/site:page/site:metadata/site:amazonbooks/site:keyword">
	<xsl:call-template name="amazonkeyword"><xsl:with-param name="keyword"><xsl:value-of select="." /></xsl:with-param></xsl:call-template><br />
</xsl:for-each>

</div>

</body>
</html>

</xsl:template>

</xsl:stylesheet>
