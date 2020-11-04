@echo off

rmdir /s /q out
del /q *.exe

if exist lib\ goto MAIN_BUILD

mkdir lib

:MAIN_BUILD

set PATH_TO_FX="C:\Users\harry\openjfx-15.0.1_windows-x64_bin-sdk\javafx-sdk-15.0.1\lib"
set PATH_TO_FX_MODS="c:\Users\harry\openjfx-15.0.1_windows-x64_bin-jmods\javafx-jmods-15.0.1"

dir /s /b src\main\java\*.java > sources.txt & javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -d out @sources.txt & del sources.txt
copy src\main\resources\*.fxml out\ & copy src\main\resources\styles.css out\

"%JAVA_HOME%\bin\jar" --create --file=lib\kabfractals.jar --main-class=org.culpan.kabfractals.Main -C out .

"%JAVA_HOME%\bin\jpackage" --type exe -i lib --main-jar kabfractals.jar -n KabFractals --module-path %PATH_TO_FX_MODS% --add-modules javafx.controls,javafx.fxml --main-class org.culpan.kabfractals.Main --win-menu --win-shortcut --win-dir-chooser

attrib -R KabFractals-1.0.exe

