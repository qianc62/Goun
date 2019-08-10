@ECHO OFF

REM Start the application:  RealPro_Prog_DSyntS_Building_App

REM ----------------------------------------
REM Set the Java virtual machine
REM ----------------------------------------
set JVM=java


REM ----------------------------------------
REM Set your class paths ...
REM ----------------------------------------
set CLASSPATH=.;..\..\lib;..\..\lib\rpw.jar;..\..\lib\rpw-lkb.jar;..\..\lib\xalan.jar;..\..\lib\xml-apis.jar;..\..\lib\xercesImpl.jar


REM ----------------------------------------
REM Run the Java application
REM ----------------------------------------
%JVM%  -classpath  %CLASSPATH%  RealPro_Prog_DSyntS_Building_App 


pause