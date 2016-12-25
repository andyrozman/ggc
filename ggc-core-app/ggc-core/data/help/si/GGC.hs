<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<helpset version="1.0">
	<title>  Pomoč</title>
 	<maps>
  		<homeID>top</homeID>
  		<mapref location="Map.jhm"/>
 	</maps>
 	<view>
  		<name>TOC</name>
  		<label>Kazalo</label>
  		<type>javax.help.TOCView</type>
  		<data>GGCTOC.xml</data>
 	</view>
 	<view>
  		<name>Index</name>
  		<label>Indeks</label>
  		<type>javax.help.IndexView</type>
  		<data>GGCIndex.xml</data>
 	</view>
 	<view>
  		<name>Search</name>
  		<label>Iskanje</label>
  		<type>javax.help.SearchView</type>
  		<data engine="com.sun.java.help.search.DefaultSearchEngine">JavaHelpSearch</data>
 	</view>
</helpset>
