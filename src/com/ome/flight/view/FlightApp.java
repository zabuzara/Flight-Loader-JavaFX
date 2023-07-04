package com.ome.flight.view;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import com.ome.flight.contoller.LoginController;
import com.ome.flight.contoller.LoaderController;
import com.ome.flight.contoller.RootController;
import com.ome.flight.contoller.TablesController;
import com.ome.flight.model.FlightLoader;
import com.ome.flight.model.FlightLoader.SearchBy;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.CustomStageStyle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class FlightApp extends Application {
	static public enum Argument {
		HEAD_LESS("--headless"),
		INTERVAL("--interval"),
		DEBUG("--debug"),
		SEARCH("--search"),
		HELP("--help");

		private String argument;
		
		private Argument (final String argument) {
			this.argument = argument;
		}
		
		public String argument () {
			return this.argument;
		}
		
		static public Argument get (final String value) {
			for (final Argument arg : Argument.values())
				if (arg.argument().equals(value))
					return arg;
			return null;
		}
	}
	static private final String WINDOW_TITLE = "Flight-Loader";
	static private final double WINDOW_WIDTH;
	static private final double WINDOW_HEIGHT;
	static private final double WINDOW_X;
	static private final double WINDOW_Y;
	static private final boolean START_IN_FULL_SCREEN_MODE = false;
	static {
		/**
		 * Get Screen size 
		 */
		ObservableList<Screen> SCREEN_SIZES = Screen.getScreens();
	    Rectangle2D SCREEN_SIZE = SCREEN_SIZES.get(0).getBounds();
	    /**
	     * Set Application widnow properties
	     * width & height, x & y
	     */
		WINDOW_WIDTH = SCREEN_SIZE.getWidth() / 2;
		WINDOW_HEIGHT = SCREEN_SIZE.getHeight() / 2;
		WINDOW_X = WINDOW_WIDTH - (WINDOW_WIDTH / 2);
		WINDOW_Y = WINDOW_HEIGHT - (WINDOW_HEIGHT / 2);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start (final Stage stage) throws Exception {
		final String iconPath = this.getClass().getResource("/META-INF/flight.png").toExternalForm();
		final String cssPath = this.getClass().getResource("/META-INF/style.css").toString();

		/**
		 * Initialize GUI components 
		 */
		final BorderPane rootPane = this.newRootPane();	
		final Scene sceneGraph = new Scene(rootPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		sceneGraph.getStylesheets().add(cssPath);
		
		/**
		 * Initialize the RootController
		 */
		new RootController((TabPane) rootPane.getCenter());
		
		/**
		 * Configure the Stage
		 */ 
		final ToolBar windowTopBar = new CustomStageStyle(sceneGraph, WINDOW_TITLE, iconPath, 50); 
		rootPane.setTop(windowTopBar);
		stage.getIcons().add(new Image(iconPath));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle(WINDOW_TITLE);
		stage.setScene(sceneGraph); 
		stage.setX(WINDOW_X);
		stage.setY(WINDOW_Y);
		stage.setFullScreen(START_IN_FULL_SCREEN_MODE);
		stage.setFullScreenExitHint("You cant't ESCAPE unless you press Q");
		stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
		stage.addEventHandler(KeyEvent.KEY_PRESSED,  (event) -> {
		    KeyCode keyCode = event.getCode();
		    switch(keyCode) {
		    	case F11:
		    		stage.setFullScreen(!stage.isFullScreen());
		    		break;
	    		default:break;
		    }
		});
		stage.show();
		stage.setOnCloseRequest(event -> {
			Platform.exit();
			System.exit(0);
		});
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop () throws Exception {
		Platform.exit();
		System.exit(0);
	}
	
	
	/**
	 * Creates a new RootPane
	 * @return a new RootPane type BorderPane
	 */
	private BorderPane newRootPane () {
		final BorderPane rootPane = new BorderPane();
		final Tab loginTab = LoginController.newAuthenticationTab("Login");		
		final Tab loaderTab = LoaderController.newLoaderTab("Loader");		
		final Tab tablesTab = TablesController.newTableTab("Database View");
		final Tab realodTab = TablesController.newReloadTab("Reload database");
		final TabPane centerPane = new TabPane(loginTab, loaderTab, tablesTab, realodTab);
		centerPane.setId("rootPane");
		rootPane.getStyleClass().add("rootPane");
		rootPane.setCenter(centerPane);
		return rootPane;
	}
	
	
	/**
	 * Displays help and exits with code 0
	 * @param option
	 */
	static private void displayHelp (final int option) {
		final char nl = '\n';
		final char t = '\t';
		final StringBuilder sb = new StringBuilder();
		if (option != 1)
			sb.append(nl).append("!!! Invalid parameter !!!").append(nl).append(nl);
		sb.append("available-commands:").append(nl);
		sb.append("       ").append(Argument.HELP.argument).append(":").append(t).append(t).append("Displays available arguments").append(nl);
		sb.append("                        ").append("you can use also -h").append(nl).append(nl);
		sb.append("       ").append(Argument.HEAD_LESS.argument).append(":").append(t).append("Starts the program without GUI.").append(nl);
		sb.append("                        ").append("If you don't write this option, program starts with GUI.").append(nl).append(nl);
		sb.append(t).append(Argument.INTERVAL.argument).append(":").append(t).append("Sets the loading interval time in seconds").append(nl);
		sb.append("                        ").append("Command example: --interval=10, < min value of interval is 5 seconds >").append(nl);
		sb.append("                        ").append("program loads every 10 seconds.").append(nl);
		sb.append("                        ").append("default: --interval=5").append(nl).append(nl);
		sb.append("           ").append(Argument.DEBUG.argument).append(":").append(t).append("Enables the debug for print in console").append(nl);
		sb.append("                        ").append("if you ignore this option, app starts without debug mode").append(nl).append(nl);
		sb.append("          ").append(Argument.SEARCH.argument).append(":").append(t).append("Sets the seaching type for loading from API").append(nl);
		sb.append("                        ").append("Command example:").append(nl);
		sb.append("                        ").append("--search=both:").append(t).append(t).append("loads flights for germany airports both arrival and departure.").append(nl);
		sb.append("                        ").append("--search=arrival:").append(t).append("loads flights for germany airports as arrival.").append(nl);
		sb.append("                        ").append("--search=departure:").append(t).append("loads flights for germany airports as departure.").append(nl);
		sb.append("                        ").append("default: --search=both").append(nl).append(nl);
		sb.append("Author: ©Omid Malekzadeh Eshtajarani-2022").append(nl);
		System.out.println(sb);
		System.exit(0);
	}
	
	/**
	 * Application entry point
	 * @param arguments
	 */
	static public void main (final String[] arguments) throws Exception {
		if (arguments.length == 1 && ((Argument.get(arguments[0]) != null && Argument.get(arguments[0]) == Argument.HELP) || arguments[0].equals("-h"))) displayHelp(1);
		
		final Map<Argument,Object> argumentsMap = new LinkedHashMap<>();
		for (final String argument : arguments) {
			String param = argument.indexOf("=") > 0 ? argument.substring(0,argument.indexOf("=")).trim() : argument.trim() ;
			String value = argument.indexOf("=") > 0 ? argument.substring(argument.indexOf("=")+1).trim() : "" ;
			if (Argument.get(param.trim()) == null) displayHelp(0);
			if (Argument.get(param.trim()) == Argument.INTERVAL) {
				if (value.length() == 0 || !value.matches("[0-9]{1,}") || (value.matches("[0-9]{1,}") && Integer.parseInt(value.trim()) < 5)) displayHelp(0);
				argumentsMap.put(Argument.get(param.trim()), Integer.parseInt(value.trim()));	
			} else if (Argument.get(param.trim()) == Argument.SEARCH) {
				if (value.length() == 0 || !value.matches("both|arrival|departure")) displayHelp(0);
				argumentsMap.put(Argument.get(param.trim()), value.toUpperCase());	
			} else {
				if (argument.indexOf("=") > 0 && value.length() == 0) displayHelp(0);
				argumentsMap.put(Argument.get(param.trim()), true);	
			}
		}
		if (arguments.length == 0 || !argumentsMap.containsKey(Argument.HEAD_LESS)) {
			launch(arguments);
		} else {
			final boolean headlessActive = argumentsMap.containsKey(Argument.HEAD_LESS);
			final boolean debugActive = argumentsMap.containsKey(Argument.DEBUG);
			final boolean intervalActive = argumentsMap.containsKey(Argument.INTERVAL);
			final boolean searchTypeActive = argumentsMap.containsKey(Argument.SEARCH);
			final int interval = intervalActive ? ((int) argumentsMap.get(Argument.INTERVAL)) : 5;
			SearchBy searchBy = SearchBy.BOTH;
			if (searchTypeActive)
				for (final SearchBy sb : SearchBy.values())
					if (sb.name().equals(argumentsMap.get(Argument.SEARCH)))
						searchBy = sb;
			new FlightLoader(headlessActive, debugActive, interval, searchBy);
			RootController.connect();
			System.out.println("Application: Automatic-Flight-Loader (AFL)");
			System.out.println("Loading start on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
			System.out.println("[ Mode: headless ], [ Debug: "+(debugActive ? "on" : "off")+ " ], [ Search: "+(searchBy == SearchBy.BOTH ? "departure/arrival" : searchBy.toString().toLowerCase())+" ], [ Interval: every "+interval+"s ]");
			System.out.println("Hint: CTRL+C to exit");
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    @Override
				public void run () {
			    	try {
						RootController.getConnection().close();
						System.out.println("Loading end on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
						System.out.println("Author: ©Omid Malekzadeh Eshtajarani-2022\n");
			    	} catch (final SQLException e) {
						e.printStackTrace();
					}
			    }
			 });
		}
	}
}