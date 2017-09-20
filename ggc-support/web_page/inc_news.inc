<blockquote>
<br>
<h2 class="head">News</h2>







    <table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
        <tr class="newsheader">
            <td width="150">2017-07-25</td>
            <td>Translations using Crowdsource Translations - Crowdin</td>
        </tr>
        <tr>
            <td colspan="2">
                Since Crowdsourced Translation are new hit lately, we decided to put our project
                up on crowdin, so that you could more easily help to translate the project.<br><br>

                We put all files up there, so you can start working on translations if you would
                like to have application available in your language and are prepared to put in
                some work. I have enabled some of the languages, and if your language is not one
                of enabled ones, please contact me, and I will enable your language.<br><br>

                Here is invite link, please click and follow instructions.
                <a href="https://crowdin.com/project/gnu-gluco-control-core/invite">Crowdin GGC Invite</a><br><br>

                We are also working on integration of our own translation tool into application, but that will have
                to wait, for few more months.
                <br><br>
                Andy<br>
                Project Manager/Lead Developer
        </tr>
    </table>

    <br><br>


<!--
    <table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
        <tr class="newsheader">
            <td width="150">2016-06-29</td>
            <td>Release 0.8.0</td>
        </tr>
        <tr>
            <td colspan="2">
                It's been long time again... my "real" life is throwing me for loops lately, so GGC has to suffer
                because of it.<BR>
                This release contains mostly bug fixes and we added two new meter families: Menarini and Arkray. There
                are about 10 new devices there, they are not tested, so please be careful with it and let us know if
                something doesn't work.<BR>
                Most of development, was done on Doctor's Appointments and Inventory handling. But since both this
                changes also require changes to database, some of work was also done on Update functionality, which
                is requirement for new two functionalities, so you will have to wait for all 3 of them to be there
                (I hope it will come out in 0.9).<BR>




                If you find some problems please report and I will try to make a fix release if needed.<br>
                <br>
                Next release is planned for end of year 2017.
                <br>
                Any help from any of you is greatly appreciated... I try to reply to each mail I get...
        </tr>
    </table>

    <br><br>
-->

    <table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
        <tr class="newsheader">
            <td width="150">2016-06-29</td>
            <td>Release 0.7.0</td>
        </tr>
        <tr>
            <td colspan="2">
                It's been long time, again... since the last release... Unfortunately my "real" life got in the way
                of GGC development again.<br>
                I know I promised that Minimed support comes with next version, but I didn't have time do a lot of
                development and testing with Minimed. I can report, that basic support is done, but everything else
                is untested. You can test it yourself if you want, but like I said it might not work... If you do the
                testing, please send us the log files, so that we can anlyze it and see if it works, or if it works
                correctly. I just got news that it might not work anymore on never Linux (it seems that USB 3.0 support
                broke support for Carelink USB).<br>
                Changes in this version:<br>
                - I changed menus a little, different grouping<br>
                - added graphs from Pen to Pump (this is still work in progress, but at least some things should be working)<br>
                - added/changed reports, they now contain also CGMS data (Daily Time Sheet report for Pump)<br>
                - added some daily/weekly graphs for CGMS<br>
                - minor change for Contour USB meters (it seems that library we are using has a bug, so reading of data
                  takes a long time (about 2 min for all entries)... We are looking to remedy the problem ASAP).<br>
                  We added development version
                  of library to our project, so that we can test this and possibly find a solution in near future.<br>
                <br>
                If you find some problems please report and I will try to make a fix release if needed.<br>
                <br>
                Next release is planned for end of year 2016. I will try to get final version of Minimed
                support in project, but it all depends if we can solve Carelink USB problem. There should also
                be some new meters added (Menarini and some Arkray models).
                <br>
                Any help from any of you is greatly appreciated... I try to reply to each mail I get...
        </tr>
    </table>

    <br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2015-10-08</td>
<td>Release 0.6.1</td>
</tr>
<tr>
<td colspan="2">
You are probably wondering if everything is OK, since new release is already here...<br>And here comes
the explanation...<br>I planned new release at end of year (I usually try to make 2 releases in year, but
mostly it happens that there is only one), so I made plan for features and I divided them into simple and hard
tasks... Simple are tasks that are now in this release, this includes several bug fixes, basal statistics, fixed graphs
for pump (now we have finally all data displayed... sorry no CGMS in Pump Daily Graph for now) and finally
implementation of Contour USB (and Contour Next Family) meters, which are our first USB Hid Meters supported (this was also main
reason for this release). <br>
Now because of this release, it might happen that next release won't be at end of this year, but sometime in
start of next year (probably february). My plan for next (0.7) release is to prepare support for Minimed Pump. While
    there are some other tasks to be done, this is my main goal for next release, so when I have this support ready
    (for ComLink cable and CareLink USB device and for start support will be done only for Pump part, so sorry no
    CGMS data yet (coming in next release)) you can expect next release. If this is ready by end of year, then you
    will get release by then, if I encounter some additional problems, then next release will be little later (with help
    of decoding-carelink project (thanks Ben West) I managed to decode most of data, so everything that I need to do
    is incorporate this results, with my current Minimed implementation and extend implementation to Carelink USB device
    (so far I only worked with old ComLink cable). I hope this will be done in next few weeks/months.
    <br>
    This release is update only. Which means that if you are new user you need to install full 0.6.0 and then overwrite
    its files, with this new upgrade. Same for existing users... just overwrite files with files in this package.
</tr>
</table>

<br><br>




<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2015-08-01</td>
<td>Release 0.6</td>
</tr>
<tr>
<td colspan="2">
Finally release is here... It took my something longer than expected, but there is one additonal
functionality added...<br>
I am afraid to say that this release was only tested by me (nobody from testing team had time
to check this) with my hardware. Because of this, I ask everyone of you who has hardware that was
implemented to test this and let me know if something doesn't work. In all 3 implementation we
work with dates, and in some cases this dates need to be converted (TimeZones and Daylight Saving time),
so please take few minutes time and check it out.<br>
New functionalites include:<br>
- Dexcom G4<br>
- Animas 1200 and higher, including Vibe (also Dexcom/CGMS support)<br>
- Insulet Omnipod (importing of .ibf backup file)<br>
- added few reports for Pumps (check Daily Timesheet)<br>
- some fixes here and there...<br>
<br>
If you find some problems please report and I will try to make a fix release if needed.<br>
<br>
Next release is planned for end of year 2015. If everything works OK, we should have some support for
Minimed Pumps. I am working on it with full speed, so I hope it will be finished by end of year
release (it should download at least Pump data, CGMS data download is planned for later time).
<br>
Any help from any of you is greatly appreciated... I try to reply to each mail I get...
</tr>
</table>

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2015-04-29</td>
<td>About next release 0.6 and Help Needed</td>
</tr>
<tr>
<td colspan="2">
I am sorry to say that as you can see, release in January didn't happen... I kept working on Animas integration, which started getting complicated in CGMS integration, but now there is finally working version there. 0.6 will be sent to Testers by end of week, and then after two weeks it should come out...<br>
I have more free time now, which means that next version should be interesting... but now we come to reason for this message... <br>
<br>
<b>WE NEED HELP</b><br>
1. TRANSLATION. Since we are gaining some new users from all around the world, it would be nice, if we could
translate GGC into some more languages. We have currently just 3 additional languages (German, Slovene and French),
it would be nice to have some more... I created Translation Tool, which will make translating very easy, you just
need to put some time in... <br>
2. GRAPHS. We have support for quite a few devices, now we just need to display (visualize) this data.
We have Reports to print out this data, but Graphs are still our weak point. GGC has Graph Framework, which
requires developer to implement each Graph separately, which can be quite a problem, which is reason why I started
working on Graph Editor (it should be able to create Graph definition (as Xml), which would be displayed by
special Viewer. This would give us option to make graphs very simply, but it would also give User option to
define it's own), but other stuff was more important so it's still in beginning phase. I would need someone
to either develop this, or help me with development (by testing mostly and doing research into library).
We are using most widely used Graph library for Java called JFreeChart (currently version 1.0.13).<br><br>
If you feel you could help us in either of this points (or even by testing), please contact me:
<a href="mailto:andy.rozman@gmail.com">andy.rozman@gmail.com</a></td>
</tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2014-12-29</td>
<td>About next release 0.6 (planned for Yule 2014)</td>
</tr>
<tr>
<td colspan="2">
Our next release will come out little late. It was planned for end of December, but Animas implementation
is taking more time than planned... Release 0.6 will come out in start of 2015 (probably at end of January
or start of February).<br>
</td>
</tr>
</table>

<br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2014-09-27</td>
<td>GGC 0.5.0.3 (release 2)</td>
</tr>
<tr>
<td colspan="2">
Our latest release had one file missing. We helped several GGC users by sending them missing
file. But for all others, here is release with all files. We are really sorry for this problem.<br><br>
But some good news, development of GGC is now back on track and we plan regular updates
(twice a year, probably in June and in December).<br><br>
Next release will contain support for Dexcom G4 CGMS, internal PDF viewer for Reports (Printing) and 
perhaps one or two other changes and/or fixes, it should be out by end of December.  
<br>
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2014-05-18</td>
<td>GGC 0.5.0.3</td>
</tr>
<tr>
<td colspan="2">
It's been quite a while since our last release. I am really sorry but development team is 
currently limited only to me and I had some personal problems, which moved new release (0.5)
farther and farther away. Now that everything is setlled and I hope to have a little more time
to work on GGC. <br><br>
I wanted to introduce our new installer for GGC (IzPack) with this release, but I am still
having some problems with so, this release is still in old way, buit I hope to make an interim 
release with some changes and with new installer, but this won't happen before September.<br>
As first I would like to thank all <b>Testers</b>, who helped us to real testing of this application
(first one). We found some "problems" and all relevant were fixed.<br><br>
<b>What is new in this release...</b><br>
We moved to Rxtx 2.2, which was required since last version 
had problem with 64-bit operating systems (Linux and Windows too). There were all testing fixes, 
and we extended communications with some of devices.<br><br>
<b>And the future...</b><br>
While this release doesn't have so many features, we did get a step
further to add support for MiniMed devices, which is currently most used Pump on the market. 
We still have problem with decoding data. which is reason why it's not in this release, but I 
hope it will be in next (end of year relase not in interim). We also started working on support
for USB devices (so far we supported only Serial devices), which means that we will soon start
adding support for new USB Meter devices. If you are willing to support this, we need all 
help we can get: either programmers, testers, translators, documentation writers... If you 
are owner of Animas pump, we have some code ready for testing and we could add support for 
it soon.<br><br>
For more detailed info what is planned, look at <b><i>Features</i></b> tab.
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2011-08-06 12:45</td>
<td><b>Need help - Testers</td>
</tr>
<tr>
<td colspan="2">
Now, we need your help... We are just before release of GGC 0.5, and we would need some testers. You 
don't have to be "professionals" (professional testers in software development companies), 
you just need to be willing to put some time aside (about day or two, every few months, usually 
about month before release). We currently have about 0-2 releases per year, but in next few months, 
we plan to release our new update system, which would probably bring this number to 2-4 per year.<br><br>

As I said we need few people willing to go through all functionalities, play with your
devices and determine if GGC works ok. We don't have test plan written yet, but at this point 
administrator will point you to functionalities that were added (or changed), and which need to be tested.<br><br> 
<b>If (and I hope we do)</b> we manage to establish team of testers (at least few of them), we (developers 
and release admin) will write testing manual, with all test cases, which will be then expanded with each 
new functionality...<br><br>
We will need GGC Core testers and GGC Hardware testers. Core testers will mainly look at main 
application and plugins from user view (if everything works ok, if there are errors, etc.), while 
Hardware testers will look at different hardware (probably their own) and compare transfered data with data
that is stored in GGC after transfer.<br><br>
In 0.5 we changed way data is transfered from meter devices (we added support to framework, which 
allows to transfer data, that is not only BG, but also other data (for "special" meters like 
XCeed, Aviva Combo,...)), which is why release is so late. We are now testing stuff, but we need 
an outsider (not developer), to look things over.<br><br>
If you are willing to help GGC community, please contact me at: <a href="mailto:andy.rozman@gmail.com?subject=GGC Testing">andy.rozman@gmail.com</a> or 
<a href="mailto:ggc@atech-software.com?subject=GGC Testing">ggc@atech-software.com</a> (please put in Subject words "GGC Testing".<br><br>
Andy<br>
administrator<br>  
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2010-08-07 12:45</td>
<td>Translation Tool - new version</td>
</tr>
<tr>
<td colspan="2">
New version of translation tool (1.2.2) and also new version of GGC Lang pack. We plan next 
release of GGC in next month, so this Lang pack is intended for 0.4.20 version.
See section <a href="index.php?show=projects">Projects</a> for more info.
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2010-04-08 21:45</td>
<td>Translation Tool</td>
</tr>
<tr>
<td colspan="2">
After long wait, here is finally tool that will help translate our application into other languages.
See section <a href="index.php?show=projects">Projects</a> for more info.
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2009-11-08 21:45</td>
<td>GGC Update 0.4.10.1</td>
</tr>
<tr>
<td colspan="2">
As one of users noticed, there was minor/major problem in configuration. There was problem with 
configuration of Unit for Blood Glucose. It wouldn't be a problem for old users, but it is/was for 
for new users. This is now fixed. If you are old user (and don't plan to change BG unit) you don't 
need to apply it. 
</td>
</tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2009-10-31 22:45</td>
<td>GGC Update 0.4.10</td>
</tr>
<tr>
<td colspan="2">
I am again only one developing so this update is little late. We plan to have another release by end 
of year, but since I am only developer at this time (we need more people, so if you know java or have
ideas, please contact us), development is just slowly progressing...<br>
We fixed some bugs in this update, and implemented application mode (either pump or pen/injection), 
which changes toolbar and display different statistics (not working yet for pump mode). A lot of work 
was done on Pump Tool, we have added some devices (Dana fully, Roche partitialy) and also changed 
some stuff on manual entry (TBRs, Food entries, fixed Bolus Helper). 
</td>
</tr>
</table> 

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2009-10-31 22:45</td>
<td>Translation Tool update</td>
</tr>
<tr>
<td colspan="2">
Translation Tool is in works, we are currently working on next "internal" version. It should be finished
and ready for all end-users who wish to translate our software in their own language soon. Plan is to 
release it sometime in November or in start of December.
</td>
</tr>
</table> 

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2009-06-18 14:00</td>
<td>Next release update</td>
</tr>
<tr>
<td colspan="2">
Since we didn't reach any of "special" goals we decided to postpone 0.5 release till end of year 
(probably till December). This doesn't mean that you won't get any updates, but there will be just
minor updates (0.4.x), which don't require database changes.<br>
Next minor update is planned for start of July and it will contain much revised Pump Tool with possible
support for reading data from some devices.<br>
We are most actively working now on Pump Tool and we also started Translation Tool subproject which 
is intended for people willing to translate this software. So far translation is messy and not 
really intended for non-developers, but this will change with this new tool, which will come out
arround September 2009 and will simplify translation.
</td>
</tr>
</table> 


<br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" summary="" class="container">
<tr class="newsheader">
<td width="150">2009-05-10 14:00</td>
<td>Translations update</td>
</tr>
<tr>
<td colspan="2">
We created special document for "How To Translate GGC". See Documentation.
</td>
</tr>
</table> 


<br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2009-04-02 21:00</td>
<td>GGC 0.4.3 Update</td>
</tr>
<tr>
<td colspan="2">
Update makes changes to HbA1c Dialog, Printing system and Startup system, also there has been change to 
Nutrition part (we now have dynamic loading of data, so startup is faster).
</td>
</tr>
</table> 


<br><br>




<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2009-02-14 11:00</td>
<td>About versioning of GGC</td>
</tr>
<tr>
<td colspan="2">
As you can see, we are still in 0.x versions. We now had two releases, which both work, but yet we are still in 
so called pre-versions (bellow 1.0). <br>
Problem is that we "tagged" several features, we need/want to have implemented before we put out "real" 1.0 
release. If everything goes alright this will happen with next release (0.5)...
</td>
</tr>
</table> 


<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2009-02-14 10:00</td>
<td>Release 0.4 OUT</td>
</tr>
<tr>
<td colspan="2">
We have finally brought you next GGC release (this time we are just over a month late...). New version features
changes in main gui, additional food support (we support free-text food entries), Bolus Helper (and Simple Ratios
for that), new graph framework, Meter Tool finsihed (and new devices added), development of Pump Tool started 
(Manual adding of data only, so far). <br>
There were also several changes to this web page, added files to Documentation, changed Screenshots, Downloads, 
Features. 
</td></tr>
</table> 



<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-12-24 22:44</td>
<td>Release 0.4 comming out soon</td>
</tr>
<tr>
<td colspan="2">
After first public release it was decided, that there will be one major release every six months, 
at least until we finish all main functionalities. Next release will come out in January 2009,
with some major changes and lots of minor ones.<br>
As you can see, we are still in 0.x versions. When all tagged (required) functionalities are finished, 
we will go to version 1.0 (this might happen with release 0.5 or maybe 0.6).  
</td></tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-12-24 18:00</td>
<td>Best wishes for next year</td>
</tr>
<tr>
<td colspan="2">
Merry Christmas (Happy Hanukkah) and Happy New Year 2009 for all Diabetics, their loved ones and all other people.<br>
GGC Development Team
</td></tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-12-01 4:44</td>
<td>New developers</td>
</tr>
<tr>
<td colspan="2">
In last few months, we gathered several new developers, which will help us with development
of several parts of GGC, so please welcome them.<br>
So welcome Aleksander Balaban (support for Meter OT UltraSmart and other stuff), Magnus Odell (Support for
Animas pump and other stuff) and Tom S. (translation to French and some other stuff).
</td></tr>
</table> 

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-08-04 22:44</td>
<td>Release 0.3 out</td>
</tr>
<tr>
<td colspan="2">
And its finally here... Our first public release. As promissed, we added two more features to all previously planned ones. 
Meter Tool is now released (supported are currently just Ascensia/Bayer) and full Backup and Restore is possible. More about
all current and planned features on page 'Features'.
</td></tr>
</table> 

<br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-07-06 18:44</td>
<td>Release by end of month July 2008</td>
</tr>
<tr>
<td colspan="2">
We planned to have application ready by 4th of July, but we are still behind our release plan. This, vacations, RL (Real life)
keep interfering with our release. We have just minimal stuff to do, so release WILL BE out by end of month. We wanted to 
implement Restore Db function, so that updates will be possible in the future. We intend to implement special update service,
but this might be months away. 
</td></tr>
</table> 

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-05-30 10:44</td>
<td>Release postponed (for one month)</td>
</tr>
<tr>
<td colspan="2">
Release was planned for 4th of June 2008, but due to some problems we are forced to postpone it for a month. To repay you 
for waiting another month, we decided to add two features, which were planned for next release (which ones is surprise). 
</td></tr>
</table>

<br><br>



<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-04-20 16:44</td>
<td>Update to web page</td>
</tr>
<tr>
<td colspan="2">
Major update of this web page. All pages were changed, and added were 'Sub-Projects' and 'Help Us' pages.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2008-02-10 19:50</td>
<td>New Preview release</td>
</tr>
<tr>
<td colspan="2">
First public release (0.2.9.6). This is just preview (Alpha) version for first major release, planned in near 
future. (4th April 2008)
</td></tr>
</table>

<br><br>


<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2006-03-01 09:22</td>
<td>New developer</td>
</tr>
<tr>
<td colspan="2">
New developer rumbi joined the team and is taking testing and linux specific development.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2005-04-04 10:44</td>
<td>New developer</td>
</tr>
<tr>
<td colspan="2">
New developer andyrozman joined the team and is taking over main development of project.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2002-08-01 19:51</td>
<td>New project homepage</td>
</tr>
<tr>
<td colspan="2">
We have a brand new homepage. Check it out.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2002-08-01 19:50</td>
<td>First alpha released</td>
</tr>
<tr>
<td colspan="2">
Today, we released a first public alpha version.<br><br>
It is a developer release but you are welcome to test it.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2002-08-01 15:17</td>
<td>Homepage is on its way</td>
</tr>
<tr>
<td colspan="2">
A brand new homepage with screenshots, information ans stuff like that, is on its way.<br><br>
Check back to see it.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2002-07-16 16:07</td>
<td>sources now in cvs</td>
</tr>
<tr>
<td colspan="2">
Today I put the current development sources into cvs. Check them out if you want, but don't expect too much for now. It's still a very early alpha.
</td></tr>
</table>

<br><br>

<table cellpadding="3" cellspacing="0" align="center" width="100%" class="container">
<tr class="newsheader">
<td width="150">2002-03-25 23:06</td>
<td>Project started</td>
</tr>
<tr>
<td colspan="2">
Today we opened the project.
</td></tr>
</table>

<br><br>

<blockquote>
