
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
<b>Current version:</b> 1.0.7<br>
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
        <li>Lifescan - OT Ultra, OT Ultra2, OT Ultra Smart (partitial), OT Easy/Mini </li>
    </ul>
</ul>
<br>
<b><i>Planned devices: </i></b>
<ul>
    <li>Abbott - Some of them
</ul>




<br>

<h3 class="header">Pump Tool</h3>

Pump Tool is now in development, 1st step is already done, but still most needs to be done. In brackets there 
is version of GGC in which functionality will be available (it may become available sooner, but it's not likely).
Ready in brackets mean that application already has functionality, but since it depends on some other 
functionality it wasn't activated yet.<br>
<b>Current version:</b> 0.2.1<br>
<br><br>


<b><i>Tasks that are already done:</i></b>
<ul>
    <li>Manual adding of pump data (data that will in future be read from pump)</li>
    <li>Adding of additional data (bg measurements, OH intake, Activity...)</li>
</ul>


<br><br>
<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>Base framework for pumps - 70% done [0.5]</li>
    <li>Communication framework for pumps (interface that all pumps will use) [later]</li>
    <li>List of pumps (most of them not all) [0.5 ready]</li>
    <li>Configuration and graphical interface for integration into GGC [0.5 ready]</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output [0.5 ready]
        <li>GGC Db Output (for manual import) [0.5 ready]
        <li>GGC Output (GUI integration) [0.5 ready]
    </ul>
    <li>Graphs (0.6)</li>
    <li>Specific pump models implementations:</li>
    <ul>
        <li>Roche/Disetronic (2009)
        <li>MiniMed (2009)
        <li>Sooil Dana (?) [at this time this pump has no PC communication]
        <li>Animas (unknown) [we have one developer working just on this]
        <li>Smith's Cosmo (?)
        <li>Omni Pod (?)
    </ul>
</ul>


<br>

<h3 class="header">CGMS Tool</h3>

CGMS tool is still in planning phase. So all things are still on TO-DO list. In brackets there is version of GGC
in which functionality will be available (it may become available sooner, but it's not likely).
<br><br>
<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>Base framework for CGMS (0.5)</li>
    <li>Communication framework for CGMS (interface that all CGMS will use)</li>
    <li>List of CGMS (0.6)</li>
    <li>Configuration and graphical interface for integration into GGC (0.6)</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output (0.6)
        <li>GGC Db Output (for manual import) (0.6)
        <li>GGC Output (GUI integration) (0.6)
    </ul>
    <li>Graphs (0.6)</li>
    <li>Specific CGMS models implementations:</li>
    <ul>
        <li>MiniMed (2009)
        <li>Dexcom (?)
        <li>Abbott (?)
    </ul>
</ul>


<br>

<h3 class="header">Translation Tool</h3>
This is separate project from GGC, but it will also be used for GGC. Since GGC has evolved into different sub-projects
we now have several language files and almost impossible task to update them. Current instruction on how to do
translation are not intended for normal (end) users, since they require quite extensive knowledge of source control 
and development tools. Because of this, we created this sub-project, which will now, that we have somehow working 
version of GGC available, get little bit of priority. We plan to have working version by end of summer (somewhere in 
September). <br>

https://sourceforge.net/projects/ggc/files/Translation%20Tool/TranslationTool_1.2.zip/download
https://sourceforge.net/projects/ggc/files/Translation%20Tool/GGC_Base_1.zip/download
https://sourceforge.net/projects/ggc/files/Translation%20Tool/GGC_Lang_1.zip/download		


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
