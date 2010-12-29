<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<helpset version="1.0">
	<title>  Help</title>
 	<maps>
  		<homeID>top</homeID>
  		<mapref location="en/Map.jhm"/>
 	</maps>
 	<view>
  		<name>TOC</name>
  		<label>TOC</label>
  		<type>javax.help.TOCView</type>
  		<data>en/GGCTOC.xml</data>
 	</view>
 	<view>
  		<name>Index</name>
  		<label>Index</label>
  		<type>javax.help.IndexView</type>
  		<data>en/GGCIndex.xml</data>
 	</view>
 	<view>
  		<name>Search</name>
  		<label>Search</label>
  		<type>javax.help.SearchView</type>
  		<data engine="com.sun.java.help.search.DefaultSearchEngine">en/JavaHelpSearch</data>
 	</view>
</helpset>
