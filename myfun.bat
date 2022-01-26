@echo off
rem echo Prende file in input solo dalla cartella C:\Users\loryc\IdeaProjects\martone-criscuolo_es5_scg\test
set argC=0
for %%x in (%*) do Set /A argC+=1

echo %argC%

if %argC% NEQ 1 (
	echo "Devi fornire 1 parametro (path/to/file.txt)"
	
) else (
set path1=%1
rem set name2=C:\Users\loryc\IdeaProjects\martone-criscuolo_es5_scg\temp\%2.exe

rem Estraggo dal path il nome del file con estensione
for %%F in ( %path1% ) do set name2=%%~nxF

rem Rimuovo l'estesione dal nome del file
set name2=%name2:~0,-4%
echo Per il momento lancia questo comando dalla cartella C:\Users\loryc\IdeaProjects\martone-criscuolo_es5_scg
call java11 -Dfile.encoding=windows-1252 -jar C:\Users\loryc\IdeaProjects\martone-criscuolo_es5_scg\out\artifacts\Martone_Criscuolo_es5_scg_jar\Martone-Criscuolo_es5_scg.jar %path1%
gcc C:\Users\loryc\IdeaProjects\martone-criscuolo_es5_scg\test_files\c_out\%name2%.c -o %name2%.exe
)