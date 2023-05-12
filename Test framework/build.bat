set temp=temp
set web_inf="%temp%/WEB-INF"
set classes=%web_inf%/classes

mkdir %temp%
mkdir %web_inf%
mkdir %classes%
mkdir %web_inf%/lib
javac -cp "./lib/framework.jar" -d %classes% *.java
xcopy  "./lib" %web_inf%"/lib" /E
xcopy web.xml %web_inf%
xcopy *.jsp %temp%
cd %temp%
jar -cf projetTest.war .
cd ../c 