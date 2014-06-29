if {%1}=={} java -jar c:\\agent_java_windows_model.jar
if "%1"=="report_state" java -jar c:\\agent_java_windows_model.jar %1 %2=%3
if "%1"=="recycle_results" java -jar c:\\agent_java_windows_model.jar %1 %2=%3