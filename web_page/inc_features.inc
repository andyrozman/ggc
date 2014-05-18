<blockquote>
<br>
<h2 class="header">Features</h2>


In last few years application has gone through several major changes, especially data management side. 
We added Hibernate Framework, which now gives us support for all major java supported databases. Also 
user interface was made little neater (support for different skins with SkinLF framework) and 
user-friendlier. <br><br>

<b>Currently Implemented Features:</b>
<ul>
	<li>Entering You can enter:</li>
	<ul>
		<li>Blood Glucose (BG) Values (in both formats)</li>
		<li>2 different Insulins (Basal, Bolus)</li>
		<li>Carbohydrates intake</li>
		<li>Activity level</li>
        <li>Urine tests</li>
		<li>Comments</li>
		<li>Adding food data (From Db)</li>
		<li>Adding food data (by description)</li>
		<li>Bolus Helper to determine Bolus dose</li>
        <li>Simple Ratio Helper</li>
	</ul>
	<li>Store your data in:</li>
	<ul>
		<li>With help of Hibernate (see <a href="http://www.hibernate.org/">http://www.hibernate.org/</a>) we now support wide 
                variety of databases. 
        <li>We have one "internal" database, which can be used out-of-the-box (we use <a href="http://www.h2database.com/">H2</a> database for that)</li>
		<li>Backup data and restore (full only)</li>
		<li>Database versioning</li>
	</ul>
	<li>Graphs:</li>
	<ul>
		<li>One Day (shows a BG curve, avg BG, insulin and BU intake)</li>
		<li>Course (shows you your values over a period of time)</li>
		<li>Spread (shows data over a period of time, but plots the days over each other, so you can find accumulations)</li>
		<li>Frequency (shows kind of a histogram for your BG values)</li>
		<li>We are now using JFreeChart library for graphs (through ATech Graph Framework)</li>
	</ul>
	<li>Nutrition database:</li>
	<ul>
		<li>We have now tree nutrition/food databases. </li>
		<li>USDA database. This is static database of nutrition data of US department of Agriculture.</li>
		<li>User database. To add foods that are not USDA</li>
		<li>Meals database. You can group USDA/User/Meal entries into meals</li>
	</ul>
	<li>Print support:</li>
	<ul>
		<li>We can now create PDFs with Simple and Extended reports. From within PDF Reader, we can
                then print-out our data.</li>
		<li>Simple report. Gathers together data for BGs, Insulins and CHs and displays them.</li>
		<li>Extended report. It shows every entry as it was written, with fully detailed data.</li>
		<li>Printing of Food Menus (with different options)</li>
	</ul>
	<li>Approximate your current HbA1c</li>
	<li>Customize whole application</li>
        <li>Three sub-projects were created: Meter Tool, Pump Tool and CGMS Tool (see page Sub-Projects)</li>
        <li>Help framework integrated (Some help pages written)</li>
    <li>USB Image of GGC</li>
    <li>Application update: Client side is working, we need to implement server side</li>
	<li>Tools integration:</li>
	<ul>
		<li>Meter Tool integration (v2.0.3) - See projects page for details.</li>
        <li>Pump Tool integration (v1.3.4) - See projects page for details.</li>
        <li>CGMS Tool integration (v1.0.3) - See projects page for details.</li>
	</ul>
	
</ul>

<b>Versioning:</b>
Our application is still in 0.x version, but that will change, when some of major features are 
implemented, we will go to version 1.0. This will probably happen with version 0.5, if all items
marked with [1.0] will be finished, then first release, with them, will be 1.0.<br><br> 


<b>Planned Features:</b>
<ul>


<li><i>Version 0.6 [4Q/2014]:</i></li>
<ul>
    <li>Stocks</li>
    <li>Update system - Server side [1.0]</li>
    <li>Write help pages for English (full help) [1.0] </li>
    <li>Db Tool [1.0]</li>
    <li>Support for multiple persons</li>
    <li>USB devices integration (via HID interface)</li>
    <li>Internal PDF Viewer</li>
    <li>moving from Rxtx to java-simple-serial-connector (not 100% decided)</li> 
    <li>moving to development with Maven (internal change)</li>
</ul>



<li><i>Version 0.7 or later:</i></li>
<ul>
    <li>Extend nutrition databases functionality</li>
    <li>Add Remote Food database</li>
	<li>Editor for Graph (v2)</li>
	<li>Restore data (partitial/update)</li>
	<li>Appointment and Doctors handling</li>
    <li>Activity manager</li>
    <li>Support for multiple instances of GGC and their communication</li>
    <li>Internet data (links for pages, forums, blogs - informational)</li>
    <li>Import of data from SiDiary Desktop application. SiDiary has its sister applications running 
        on many small devices (PocketPC, Palms, SmartPhones, Java Phones), and it has import for a lot of 
        Meters and also some pumps...</li>
</ul>


</blockquote>