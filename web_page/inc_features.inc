<blockquote>
<br>
<h2 class="header">Features</h2>


In last few years application has gone through several major changes, especially data management side. 
We added Hibernate Framework, which now gives us support for all major java supported databases. Also 
user interface was made little neater (support for different skins with SkinLF framework) and 
user-friendlier. <br><br>

<b>Currently Implemented Features:</b>
<ul>
	<li>You can enter:</li>
	<ul>
		<li>Blood Glucose (BG) Values (in both formats)</li>
		<li>2 different Insulins (Basal, Bolus)</li>
		<li>Carbhydrates intake</li>
		<li>Activity level</li>
                <li>Urine tests</li>
		<li>Comments</li>
	</ul>
	<li>Store your data in:</li>
	<ul>
		<li>With help of Hibernate (see <a href="http://www.hibernate.org/">http://www.hibernate.org/</a> we now support wide 
                wariety of databases. 
                <li>We have one "internal" database, which can be used out-of-the-box (we use H2 database for that)</li>
	</ul>
	<li>Plots:</li>
	<ul>
		<li>One Day (shows a BG curve, avg BG, insulin and BU intake)</li>
		<li>Course (shows you your values over a period of time)</li>
		<li>Spread (shows data over a period of time, but plots the days over each other, so you can find accumulations)</li>
		<li>Frequency (shows kind of a histogram for your BG values)</li>
	</ul>
	<li>Nutrition database:</li>
	<ul>
		<li>We have now tree nutritional/food databases. </li>
		<li>USDA database. This is static database of nutrition data of US department of Agriculture.</li>
		<li>User database. To add foods taht are not USDA</li>
		<li>Meals database. You can group USDA/User/Meal entries into meals</li>
	</ul>
	<li>Print support:</li>
	<ul>
		<li>We can now create PDFs with Simple and Extended reports. From within Acrobat Reader, we can
                then print-out our data.</li>
		<li>Simple report. Gathers together data for BGs, Insulins and CHs and displayes them.</li>
		<li>Extended report. It shows every entry as it was writen, with fully deatailed data.</li>
	</ul>
	<li>Approximate your current HbA1c</li>
	<li>Customize whole application</li>
        <li>Two sub-projects were created: Meter Tool and Pump Tool (see page Sub-Projects)</li>
        <li>Help framework integrated (no help pages are yet written)</li>
</ul>
<b>Planned Features:</b>
<ul>
<li><i>In next version (0.3):</i></li>
<ul>
	<li>Backup data and restore (non-GUI)</li>
	<li>Db Versioning</li>
	<li>Rewrite Readme</li>
	<li>Error handling (and logging)</li>
	<li>Add some stuff in Daily</li>
        <li>Application update - Client side</li>
        <li>Startup add support for libraries with native parts</li>
        <li>USB Image of GGC</li>
</ul>

<li><i>Version (0.4) - (end of 2008):</i></li>
<ul>
	<li>Restore data (full)</li>
	<li>Rewrite "plots" with new library (support for printing and better display)</li>
        <li>Integration of Meter Tool</li>
        <li>Basic Pump support (for manual input of data)</li>
        <li>Integration of Pump Tool</li>
	<li>Add ratio for smart insulin usage recommendations (good for pump users)</li>
        <li>Update system - Server side</li>
        <li>Write help pages for English</li>
</ul>


<li><i>Version (0.5):</i></li>
<ul>
        <li>Support for multiple persons</li>
	<li>Restore data (partitial/update)</li>
        <li>Appointment and Doctors handling</li>
        <li>Support for multiple instances of GGC and their communication</li>
        <li>Internet data (links for pages, forums, blogs - informational)</li>
        <li>Import of data from SiDiary Desktop application. SiDiary has its sister applications running 
        on many small devices (PocketPC, Palms, SmartPhones, Java Phones), and it has import for a lot of 
        Meters and also some pumps...</li>
</ul>


</blockquote>