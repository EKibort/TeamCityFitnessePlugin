REM SET TEAMCITY_HOME=C:\TeamCity
call ant
copy .\dist\Fitnesse.zip "%TEAMCITY_DATA_PATH%\plugins\"  /Y
call %TEAMCITY_HOME%\buildAgent\bin\agent.bat stop force
CHOICE /C:AB /D:A /T:2 >NUL
call %TEAMCITY_HOME%\buildAgent\bin\agent.bat start