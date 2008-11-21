@echo off
:: ##############################################################################
:: # Use this batch file for launching Jacksum easily from the command line
:: # just by typing "jacksum". You can remove this comment block if you like.
:: #
:: # You need to edit the path below (C:\Program Files ...) so jacksum.jar
:: # can be found!
:: # Make also sure, that this batch file is reachable by the
:: # operating system environment variable called PATH.
:: #
:: # The %* modifier is a unique modifier that represents all arguments
:: # passed in a batch file (e. g. %1 %2 %3 %4 %5 ...%255), but it may
:: # not work on all Windows platforms. In this case just use the old syntax:
:: # java -jar "C:\Program Files\Jacksum\jacksum.jar" %1 %2 %3 %4 %5 %6 %7 %8 %9
:: ##############################################################################

java -jar ..\jacksum.jar %*
