@ECHO OFF

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
%JVM%  -classpath  %CLASSPATH%  RealPro_XML_File_App  dsynts.xml


pause