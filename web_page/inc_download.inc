<blockquote>
<br>
<h2 class="header">Download</h2>
There are 2 ways to get a copy. You can check out sources from CVS on sourceforge and compile them. You will get the most recent version, but it might be broken. Or you are free to download a released package. But be warned, until we reach version 0.1, they will be developer releases. They WILL contain bugs, are not optimized, and can do very bad things.
<ul>
<li>CVS:</li><br>
        CVS system was deprecated (there are still old file there) when Sourceforge installed SVN, which is now the way to get sources.<br><br>

<li>SVN:</li><br>
        SVN system is current versioning system. You neeed to pull down whole tree. When builing with ANT,
        compiled files will be stored into ggc-support\bin folder from where you can directly run it.<br>

	Here is the link to the Sourceforge SVN Page:<br>
	<a href="http://sourceforge.net/svn/?group_id=49749">http://sourceforge.net/cvs/?group_id=49749</a> <br><br>
	For details on how to check out sources see the Sourceforge SVN page.<br><br>
	
<li>Packages:</li><br>
        Packages are in two formats. Windows format which is NullSoft installer (.exe file) (without java) and 
        zip file containing all files packaged. <br><br>

        Windows file will install on your system and is runnable by clicking on right icon. In some cases java 
        install doesn't set right paths, so java executable is not in path. In this case it's required to make
        change to run.cmd file in \bin folder.<br><br>

        Zip file must just be unzipped in folder of your choice and you can run it by invoking either run.cmd or
        run.sh. If you are running Mac or some other system which isn't supported by these two files, please
        let know support (ours) immediately.<br><br>

        Our Update/Startup mechanism added another file (StartupConfig.properties). There is also JAVA_EXE 
        parameter in there which you might need to set (try running first if application is shown disregard 
        this note, if not read further). On Windows you need to use double \\ (if you set path like C:\Windows, 
        correct form would be C:\\Windows, on other system where you use / for path use only one...

        Link to files: <br>
        <a href="http://sourceforge.net/project/showfiles.php?group_id=49749">http://sourceforge.net/project/showfiles.php?group_id=49749</a><br>


</ul>
</blockquote>
