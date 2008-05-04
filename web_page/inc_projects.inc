
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
Here is where you come in. Adding support for device is tricky thing, since most of companies that make this 
devices are not willing to disclose their protocols for communication (there are some exceptions to the rule, 
those two beeing Ascensia and Lifescan). We can either implement device, by having protocol, or we can get
list of communication commands and responses received from "spy" programs. Please visit page 
<a href="index.php?show=help_us" class="menu">Help Us</a> to see how can you help us, getting your device 
supported. 

<br><br>

<h3 class="header">Meter Tool</h3>

Meter tool implementation is on the way. Planning phase was finished in January and we started on development. 
Meter tool will be used in version 0.4 of GGC (by end of year) or maybe even before.
<br><br>
<b><i>Tasks that are already done:</i></b>
<ul>
    <li>Base framework for meters</li>
    <li>Communication framework for meters (interface that all meters will use)</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output
        <li>GGC Db Output (for manual import)
    </ul>
</ul>
<br>
<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>List of meters (most of them, not all) (0.4)</li>
    <li>Configuration (0.4)
    <li>Graphical interface for integration into GGC (0.4)</li>
    <li>Output types:</li>
    <ul>
        <li>GGC Output (GUI integration) (0.4)
    </ul>
    <li>Specific meter models (companies) implementations:</li>
    <ul>
    <li>Ascensia/Bayer - Contour (in development), Dex, Elite, Breeze (September 2008)
    <li>Lifescan - most of meters (January 2009)
    <li>All other meters are not planned at this point, but if you wish to use your meter let us know...
    </ul>
</ul>




<br>

<h3 class="header">Pump Tool</h3>

Pump tool is still in planning phase. So all things are still on TO-DO list. In brackets there is version of GGC
in which functionality will be available (it may become available sooner, but it's not likely).
<br><br>
<b><i>Tasks that need to be done:</i></b>
<ul>
    <li>Base framework for pumps (0.4)</li>
    <li>Communication framework for pumps (interface that all pumps will use) (later)</li>
    <li>Manual adding of pump data (data that will in future be read from pump) (0.5)</li>
    <li>Adding of additional data (bg measurements, OH intake, Activity...) (0.5)</li>
    <li>List of pumps (most of them not all) (0.4)</li>
    <li>Configuration and graphical interface for integration into GGC (0.5)</li>
    <li>Output types:</li>
    <ul>
        <li>Console and file output (0.5)
        <li>GGC Db Output (for manual import) (0.5)
        <li>GGC Output (GUI integration) (0.5)
    </ul>
    <li>Graphs (0.6)</li>
    <li>Specific pump models implementations:</li>
    <ul>
        <li>MiniMed (2009)
        <li>Sooil Dana (2010)
        <li>Roche/Disetronic (2010)
        <li>Animas (?)
        <li>Smith's Cosmo (?)
        <li>Omni Pod (?)
    </ul>
</ul>


</blockquote>
