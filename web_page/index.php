<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<meta name="Author" content="Dieter Schultschik">
<meta name="Description" content="GNU Gluco Control Homepage">
<meta name="Keywords" content="GNU Gluco Control, Diabetes, Health, Blood glucose, medical, insulin">
<link href="style.css" rel="stylesheet" type="text/css">

<title>GGC - GNU Gluco Control</title>
</head>
<body bgcolor="#E6E5FF">


<table cellpadding="0" cellspacing="0" align="center" width="750" class="container">
<tr>
	<td><img src="./logo.jpg" border="0" width="750" height="150" alt="GGC - GNU Gluco Control"></td>
</tr>
<tr>
	<td height="25" align="center" valign="middle" class="menu">
	<a href="index.php?show=news" class="menu">News</a> | 
	<a href="index.php?show=features" class="menu">Features</a> |
	<a href="index.php?show=doc" class="menu">Documentation</a> |
	<a href="index.php?show=screenshots" class="menu">Screenshots</a> |
	<a href="index.php?show=projects" class="menu">Sub-Projects</a> |
	<a href="index.php?show=download" class="menu">Download</a> |
	<a href="index.php?show=support" class="menu">Support</a>
	</td>
</tr>
<tr>
	<td>
	<table cellpadding="15" border="0" align="center" valign="top" width="100%"> 
	<td>
	<!-- content goes here -->
	<?
	if ($show == "") {
		$show = "news";        //default to news
	}
	
	$incfile = sprintf("./inc_%s.inc",$show);
	include $incfile;
	
	?>
	<!-- content ends here -->
	</td>
	</td>
	</table>
</tr>
<tr>
	<td align="center">
	This site is hosted on:<br>
	<a href="http://sourceforge.net">
	<img src="http://sourceforge.net/sflogo.php?group_id=49749&amp;type=5" width="210" height="62" border="0" alt="SourceForge Logo">
	</a>
	</td>
</tr>
</table>

<hr>

	<table border="0" width="100%">
	    <tr>
		<td align="left">
		    schultd at chello dot at
		</td>
		<td align="right">
		    <a href="http://stud3.tuwien.ac.at/~e0125258/">schultd</a>
		</td>
	    </tr>
	</table>
	
</body>
</html>
