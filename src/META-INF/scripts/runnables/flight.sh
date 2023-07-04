param1=$1
param2=$2
param3=$3
param4=$4
/usr/lib/jvm/java-11-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 \
-p /home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/slf4j-api-1.7.25.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/rxjava-2.2.8.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/reactive-streams-1.0.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/jna-platform-5.2.0.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/jna-5.2.0.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpcore5-testing-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpcore5-reactive-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpcore5-h2-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpcore5-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpclient5-win-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpclient5-testing-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpclient5-fluent-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpclient5-cache-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/httpclient5-5.1.3.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/commons-codec-1.15.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/httpcomponents-client-5.1.3-bin/lib/commons-cli-1.4.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib2/slf4j-jdk14-1.7.9.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.base.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.controls.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.fxml.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.graphics.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.media.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.swing.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx.web.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/javafx-sdk-17.0.2/lib/javafx-swt.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/lib/jdbc-mysql-8.0/mysql-connector-java-8.0.28.jar\
:/home/zabuzara/Desktop/Damago/12.Module.Java/Workspace/flight-loader/bin \
-m com.ome.flight.model/com.ome.flight.view.FlightApp $param1 $param2 $param3 $param4
