module com.ome.flight.model {
	requires java.logging;
	requires java.net.http;
	requires javafx.graphics;
	requires java.sql;
	requires mysql.connector.java;
	requires java.management;
	requires javafx.controls;
	requires java.desktop;

    opens com.ome.flight.view;
	exports com.ome.flight.model;
	exports com.ome.flight.tools;
}