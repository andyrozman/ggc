<blockquote>
<br>
<h2 class="header">Features</h2>


In last few years application has gone through several major changes, especially data management side. 
We added Hibernate Framework, which now gives us support for all major java supported databases. Also 
user interface was made little neater (support for different skins with SkinLF framework) and 
user-friendlier. <br><br>

<b>Currently Implemented Features:</b>
<ul>
	<li><b>Data entry</b></li>
	<ul>
	    <li>We support 2 different data entries: via Pen/Injection forms or Pump forms
		<li>Blood Glucose (BG) Values (in both formats)</li>
		<li>2 different Insulin types (Basal, Bolus)</li>
		<li>Carbohydrates intake</li>
		<li>Activity level</li>
        <li>Urine</li>
		<li>Comments</li>
		<li>Adding food data (From Db)</li>
		<li>Adding food data (by description)</li>
		<li>Bolus Helper to determine Bolus dose</li>
        <li>Simple Ratio Helper</li>
	</ul>
	<li><b>Data storage</b></li>
	<ul>
		<li>With help of Hibernate 3.1.3 (see <a href="http://www.hibernate.org/">http://www.hibernate.org/</a>) we now support wide
                variety of databases. 
        <li>We have one "internal" database, which can be used out-of-the-box (we use <a href="http://www.h2database.com/">H2</a> database for that)</li>
		<li>Support for full backup and restore</li>
		<li>Database versioning</li>
	</ul>
	<li><b>Graphs</b></li>
	<ul>
		<li>One Day (shows a BG curve, avg BG, insulin and CH intake)</li>
		<li>Course (shows you your values over a period of time)</li>
		<li>Spread (shows data over a period of time, but plots the days over each other, so you can find accumulations)</li>
		<li>Frequency (shows kind of a histogram for your BG values)</li>
		<li>We are now using JFreeChart library for graphs (through ATech Graph Framework)</li>
	</ul>

	<li><b>Nutrition database</b></li>
	<ul>
		<li>We have now 3 nutrition/food databases. </li>
		<li>USDA database. This is static database of nutrition data of US department of Agriculture.</li>
		<li>User database. To add foods that are not part of USDA</li>
		<li>Meals database. You can group USDA/User/Meal entries into meals</li>
	</ul>

	<li><b>Reports</b></li>
	<ul>
	    <li>Our reporting is currently limited to creating PDF reports (we use iText library)
	    <li>When report is created we cn view it with either external PDF viewer or with
	        internal one.
		<li>Pen/Injecton: We have Simple and Extended reports available</li>
		<li>Nutrition: We have printing of food menus (options) used
		<li>Pump: Here we have Extended reports, Daily Timesheet report and some other minor reports which can help with diabetes management.
	</ul>

	<li>Approximate your current HbA1c</li>

	<li><b>Support for Diabetes devices</b></li>
	<ul>
		<li>Meter Tool integration - See projects page for details.</li>
        <li>Pump Tool integration - See projects page for details.</li>
        <li>CGMS Tool integration - See projects page for details.</li>
	</ul>


	<li><b>Other features</b></li>
	<ul>
		<li>Customization of application</li>
        <li>Help framework integrated (Some help pages written)</li>
        <li>USB Image of GGC</li>
    </ul>
</ul>

<b>Versioning:</b>
Our application is still in 0.x version, but that will change, when some of major features are 
implemented, we will go to version 1.0. This will probably happen with version 0.7, if all items
marked with [1.0] will be finished, then first release, with them, will be 1.0.<br><br> 


<b>Planned Features:</b>
<ul>


<li><i>Version 0.7 [4Q/2015]:</i></li>
<ul>
    <li>Stocks</li>
    <li>Update system [1.0]</li>
    <li>Write help pages for English (full help) [1.0] </li>
    <li>Db Tool [1.0]</li>
    <li>Support for multiple persons</li>
    <li>USB devices integration (via HID interface)</li>
</ul>



<li><i>Version 0.8 or later:</i></li>
<ul>
    <li>Extend nutrition databases functionality</li>
    <li>Add Remote Food database</li>
	<li>Editor for Graph (v3)</li>
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