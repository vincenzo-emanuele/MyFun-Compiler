@echo off
echo Prende file in input solo dalla cartella C:\Users\emanu\IdeaProjects\martone-criscuolo_es5_scg\test
set argC=0
for %%x in (%*) do Set /A argC+=1

echo %argC%

if %argC% NEQ 2 (
	echo "Devi fornire 2 parametri (nome file di input myFun e nome output file exe)"

) else (
set path1=test %1.fun
rem set name2=C:\Users\emanu\IdeaProjects\martone-criscuolo_es5_scg\temp\%2.exe
set name2=%2.exe
echo Per il momento lancia questo comando dalla cartella C:\Users\emanu\IdeaProjects\martone-criscuolo_es5_scg
call java -Dfile.encoding=windows-1252 -jar C:\Users\emanu\IdeaProjects\martone-criscuolo_es5_scg\out\artifacts\Martone_Criscuolo_es5_scg_jar\Martone-Criscuolo_es5_scg.jar %path1%
gcc C:\Users\emanu\IdeaProjects\martone-criscuolo_es5_scg\temp\test.c -o %name2%
)