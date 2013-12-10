This plugin helps with integration of FitNesse test and TeamCity.

### Build

You should have installed TeamCity, JAVA, ANT
run ant in root directory
get compiled Fitnesse.zip from dist folder


### Instalation

copy Fitnesse.zip to plugins directory (for windows %USERPROFILE%\.BuildServer\plugins) and resart TeamCity server

### Using

Add "Fitnesse runer" build step 

Parameters:
    Fitnesse: full path to fitnesse.jar 
    Port: port for fitnesse 8081 (should be free)
    Test or suite relative url: url to suite or test with '?suite' or '?test' <FitNesse.SuiteAcceptanceTests.SuiteResponderTests?suite>

If you have any questions feel free to ask me  

    Eduard Kibort



[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/EKibort/teamcityfitnesseplugin/trend.png)](https://bitdeli.com/free "Bitdeli Badge")