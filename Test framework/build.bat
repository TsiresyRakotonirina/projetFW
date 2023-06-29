set temp=temp
set web_inf="%temp%/WEB-INF"
set classes=%web_inf%/classes
set lib=%web_inf%/lib

mkdir %temp%
mkdir %web_inf%
mkdir %classes%
mkdir %lib%
javac -cp "./lib/framework.jar" -d %classes% *.java
xcopy  "./lib" %web_inf%"/lib" /E
xcopy web.xml %web_inf%
xcopy *.jsp %temp%
cd %temp%
jar -cf projetTest.war .
cd ..

@REM mkdir temp
@REM cd temp
@REM mkdir WEB_INF
@REM cd WEB_INF
@REM mkdir classes
@REM mkdir lib
@REM cd ../../..
@REM javac -cp "./lib/framework.jar" -d "./temp/WEB_INF/classes" *.java
@REM xcopy  "./lib" %web_inf%"/lib" /E
@REM xcopy web.xml %web_inf%
@REM xcopy *.jsp %temp%
@REM cd %temp%
@REM jar -cf projetTest.war .
@REM cd ../c 