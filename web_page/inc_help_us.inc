
<blockquote>


<h2 class="header">Help Us (and your-self)</h2>

We can make this software better only with your help. We (developers) all have diabetes so we are trying to 
make software that we can use, if you give us your ideas and comments we can make this software to fit your 
needs. If you are developer yourself you can also help in development process. So here are tasks you can do.

<h3>Testing</h3>
We need testers that will regullary test application and determine if there are any bugs that are lurking and 
to find if some processes could be done better. There are several ways you can report your observations, there 
are several trackers available, forums and lists.


<br><br>

<h3 class="header">Support for different platforms</h3>
Since application is written in java it should work on all platforms (with installed Java 1.5 or higher), but 
some of our functionalities require that we change application (library) a little for each different 
platform. Changes are usually small and require short amount of time, but until we implement it and test it on
each platform so long it doesn't work. Our Update/Startup mechanism requires to create startup script for 
application to be created on-the-fly (in case libraries have changed, or were added), so we need to add this for
each platform. If your platform is unsuported (you will get message on start), let us know (me specifically on 
andy(at)atech-software.com) and I will make adjustment in minutes (most of unix-like oses can use Sh script, 
so we don't need to make big changes).


<br><br>

<h3 class="header">Development</h3>
If you are java developer and are willing to help with development, let us know. You just need to subscribe to 
sourceforge and you can get full source code from SVN. You will also need to get code for atech-tools (project is 
also on sourceforge) and you can start. If you wish to contribute your changed code let us know, and we will give 
you check-in rights.

<br><br>

<h3 class="header">Translation</h3>
Do you wish to have GGC in your own language... Nothing easier than that, download application and install it. 
In file ggc-lang-X.X.jar (bin directory) you will find following two files (GGC_en.properties and 
GGC_Nutrition_en.properties). Extract them and change en to two letter code for your country. You can start 
translating this two files. When you have translated something (you don't need to translate everything, but when 
you have at least 50% translated, let us know and we will add it to application. Next language pack will have
your changes in it. Please let us know you will start translation in forum 'Translation'. You will be contacted
by one of our developers, who will send you some tools that will help you with translations.



<br><br>

<h3 class="header">Add support for your meter (pump)</h3>
If you wish to have your hardware/device supported please contact us, and we will send you detailed information 
on what we need from you. <br>In short we need following: you need to have original software for your meter/pump, 
cable or any other interface that device needs for communication with PC. You need to install your software and 
make it work (you will get detailed instructions in your User Manual). Then you need to install Serial Monitor
software (<a href="http://www.hhdsoftware.com/sermon.html">http://www.hhdsoftware.com/sermon.html</a>). This is 
not free software, it's commercial one, but it has trial download. Trial is 14 days, so you have 14 days to 
retrive data. In your meter software you will probably have several operations available, we need only logs 
for following (you need to create separate logs for each operation): read data (*1), read data from date to date (*4), 
clear meter (*3), get meter info(*2).  As you can see we tagged operation with * and number. We need at least 
number 1 (for reading data), but if we want to have full support we will need also all other information. If you
plan to do this let us know in forum 'Hardware/Device Support'. After your device is added (process takes some 
time...) you will also need to test it for us...




</blockquote>
