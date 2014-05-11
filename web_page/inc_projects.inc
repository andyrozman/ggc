
<blockquote>


<h2 class="header">Sub-Projects</h2>
From version 0.2.6 of GGC we decided to remove Meters support and create separate project out of it. Old code 
was developed by developer that is no longer on team and it was writen in such way that adding of any new 
meters would be a disaster. All Sub-projects are now developed independently from main source code. After 
creation of Meter Tool we decided on another project called Pump Tool, which is especially for pumpers. You 
can run application without these tools (sub-projects) and you will in version 0.3, but when tools become 
available and integrated into main application, you will just need to run an update and tools will be 
installed. If you don't wish to use them, you can disable them, but they will still be there. Main reason that
this two projects were created is, that there are quite a lot of different devices (meters and pumps) available 
and when they are added to code, version of main application stays the same, you just get a new version of
tool. <br><br>
We decided to add another sub-project: CGMS Tool. In some cases we get CGMS data together with pump data, but in 
most cases we have different device that receives this data.

<br><br>
Here is where you come in. Adding support for device is tricky thing, since most of companies that make this 
devices are not willing to disclose their protocols for communication (there are some exceptions to the rule, 
those two beeing Ascensia and Lifescan). We can either implement device, by having protocol, or we can get
list of communication commands and responses received from "spy" programs. Please visit page 
<a href="index.php?show=help_us" class="menu">Help Us</a> to see how can you help us, getting your device 
supported. 

<br><br>

<h3 class="header">Meter Tool</h3>

Meter Tool implementation is now finished. All that remains is adding support for more Meter Devices.<br>
<b>Current version:</b> 2.0.3<br>
<br><br>
<b><i>Tasks that are already done:</i></b>
<ul>
    <li>Base framework for meters</li>
    <li>Communication framework for meters (interface that all meters will use)</li>
    <li>List of meters (most of them, not all)</li>
    <li>Configuration </li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output</li>
        <li>GGC Db Output (for manual import)</li>
        <li>GGC Output (GUI integration)</li>
    </ul>
    <li>Specific meter models (companies) implementations:</li>
    <ul>
        <li>Ascensia/Bayer - Contour, Countour Link, Dex, Elite, Breeze </li>
        <li>Roche/Accu-Chek - All devices supported through Smart Pix device</li>
        <li>Lifescan - OT Ultra, <strike>OT Ultra2</strike>, OT Ultra Smart (partitial), OT Easy/Mini </li>
    </ul>
</ul>
<br>
<b><i>Planned devices: </i></b>
<ul>
    <li>Abbott - Some of them</li>
    <li>Ascensia: Contour USB</li>
    <li>Arkray: Glucocard X-mini plus</li>
    <li>Calla Light</li>
    <li>OT: Verio, Smart</li>
</ul>





<br>

<h3 class="header">Pump Tool</h3>

Pump Tool development is mostly finished. Some of functionalities still need to be completed, but most of stuff
works. We still need to add support for more devices.<br> 
<b>Current version:</b> 1.3.4<br>
<br><br>


<b><i>Tasks that are already done:</i></b>
<ul>
    <li>Manual adding of pump data (data that will in future be read from pump)</li>
    <li>Adding of additional data (BG measurements, OH intake, Activity...)</li>
    <li>Base framework for pumps</li>
    <li>Communication framework for pumps (interface that all pumps will use) [later]</li>
    <li>List of pumps (most of them not all)</li>
    <li>Configuration and graphical interface for integration into GGC</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output
        <li>GGC Db Output (for manual import)
        <li>GGC Output (GUI integration)
    </ul>
    <li>Specific pump models implementations:</li>
    <ul>
        <li>Roche/Disetronic: Spirit, Combo
        <li>Sooil: Dana III
    </ul>
</ul>


<br><br>
<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>Graphs (0.6) (waiting on Graphs v2 with Editor)</li>
</ul>
<br>
<b><i>Planned devices: </i></b>
<ul>
    <li>MiniMed (2015)
    <li>Animas (unknown) [we have one developer working just on this]
    <li>Smith's Cosmo (?)
    <li>Omni Pod (?)
</ul>

<br>

<h3 class="header">CGMS Tool</h3>

CGMS tool is still in implementation phase. Base framework is finished, and some imports are already working,
but we still need to do some implementation and also same analysis on the problem.<br>
<b>Current version:</b> 1.0.3<br>
<br><br>

<b><i>Tasks that are already done:</i></b>
<ul>
    <li>Base framework for CGMS</li>
    <li>Communication framework for CGMS (interface that all CGMS will use)</li>
    <li>Configuration and graphical interface for integration into GGC</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output
        <li>GGC Db Output (for manual import)
        <li>GGC Output (GUI integration)
    </ul>
</ul>

<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>List of CGMS (0.6)</li>
    <li>Graphs (0.6) (waiting on Graphs v2 with Editor)</li>
    <li>Specific CGMS models implementations:</li>
    <ul>
        <li>MiniMed (2015)
        <li>Dexcom (?, we currently suppport only import through export file from DM3 app)
        <li>Abbott (?)
    </ul>
</ul>


<br>

<h3 class="header">Translation Tool</h3>
This is separate project from GGC, but it will also be used for GGC. Since GGC has evolved into different sub-projects
we now have several language files and almost impossible task to update them. This tool will help with this translation.
At first you need to install Java JRE 1.5 (or higher), and then you need to read following documentation:
<ul>
	<li><a href="docs/tt/Readme_v1.2.txt">Readme (for 1.2)</a>. Just read section 3.</li>
	<li><a href="docs/tt/TranslationTool_GGC_v1.2.txt">GGC Readme (for 1.2)</a>
</ul> 
<br>
You need to install following three packages and then you are ready to go (only latest versions are available here, if 
you need older version you will need to visit our files section on sourceforge.net):
<ul>
	<li><a href="https://sourceforge.net/projects/ggc/files/Translation%20Tool/TranslationTool_1.2.2.zip/download">Translation Tool 1.2.2</a></li>
	<li><a href="https://sourceforge.net/projects/ggc/files/Translation%20Tool/GGC_Base_1.zip/download">GGC Base Pack v1</a></li>
	<li><a href="https://sourceforge.net/projects/ggc/files/Translation%20Tool/GGC_Lang_1.zip/download">GGC Lang Pack v1 (for GGC 0.4.17)</a></li>
	<li><a href="https://sourceforge.net/projects/ggc/files/Translation%20Tool/GGC_Lang_2.zip/download">GGC Lang Pack v2 (for GGC 0.4.20)</a></li>
</ul> 
<br>
Currently supported languages: English (100%), German (~50%), Slovenian (90%), French (~70%).<br>
If you are willing to translate GGC into your language, please contact us.
<br>

 
<h3 class="header">Db Tool</h3>
This is also one of separate tools, but this one is intended for all end users, who want to use GGC with database
other than internal. This will help you setup GGC with your favorite database, with little problems (you will need
to create database and users manually, but tool will help you with setting up GGC with your running database, as well
as with install of tables and startup data.<br>
Usage of external database can bring some good advantages. First we have a low level type of backup in case that 
something goes wrong and as second we can use data entered (or read from devices) with our own software or even some
worksheet software for creating nifty reports.<br>
Tool is planned to be "shipped" together with next major release.<br>

<br>
</blockquote>
