<?xml version="1.0" encoding="utf-8"?>
<analyses>
<#list lemmaParses as parse>
	${parse.toXML(prefs)}
</#list>
</analyses>