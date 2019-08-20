@ECHO OFF


REM ----------------------------------------------------------------------
REM Script for compiling all Java files in the current directory
REM
REM Usage:  make
REM ----------------------------------------------------------------------

REM ----------------------------------------
REM Set the Java virtual machine compiler
REM ----------------------------------------
set JVC=javac


REM ----------------------------------------
REM Set your class paths ...
REM ----------------------------------------
set CLASSPATH=.;..\..\lib;..\..\lib\rpw.jar;..\..\lib\rpw-lkb.jar;..\..\lib\xalan.jar;..\..\lib\xml-apis.jar;..\..\lib\xercesImpl.jar


REM ----------------------------------------
REM Compile your Java applications ...
REM ----------------------------------------

%JVC%  -classpath %CLASSPATH%  *.java 


pause