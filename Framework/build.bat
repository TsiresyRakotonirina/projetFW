set miditra=cd
set mivoka=cd..
set chemin1=.\Framework\
set package=./etu002015
set name_jar=framework
set chemin2="../Test framework/lib"

javac -d . *.java
jar -cf %name_jar%.jar %package%
xcopy %name_jar%.jar %chemin2%