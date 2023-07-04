package com.ome.flight.contoller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.HashCodes;
import com.ome.flight.tools.ParentController;
import com.ome.flight.tools.UncheckedSQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Tab; 
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class RootController extends ParentController<TabPane> {	
	static private Connection connection;
	static private boolean isAuthenticated;
	static private boolean logoutClicked;
	private final LoaderController loaderController;
	

	/**
	 * Initializes a new instance
	 * @param node
	 */
	public RootController (final TabPane node) {
		super(node);

		final VBox authenticationPane = (VBox) this.getNode().getTabs().get(0).getContent();
		final LoginController authenticationController = new LoginController(this, authenticationPane);
		this.getChildren().put("authentication", authenticationController); 
			
		final BorderPane loaderPane = (BorderPane) this.getNode().getTabs().get(1).getContent();
		this.loaderController = new LoaderController(this, loaderPane);
		this.getChildren().put("loader", this.loaderController); 
		
		final TabPane tablesPane = (TabPane) this.getNode().getTabs().get(2).getContent();
		final TablesController tablesController = new TablesController(this, tablesPane); 
		this.getChildren().put("tables", tablesController); 
		
		this.getNode().getTabs().get(1).getGraphic().setVisible(false);
		this.getNode().getTabs().get(2).getGraphic().setVisible(false);
		this.getNode().getTabs().get(3).getGraphic().setVisible(false);
		this.getNode().getTabs().get(1).setDisable(true);
		this.getNode().getTabs().get(2).setDisable(true);
		this.getNode().getTabs().get(3).setDisable(true);

		this.getNode().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {			
			@Override
			public void changed (ObservableValue<? extends Tab> currentTab, Tab tab1, Tab tab2) {
				if (currentTab.getValue().getId().equals("loginTab") || currentTab.getValue().getId().equals("reloadTab")) {
					final int tabIndex = node.getSelectionModel().getSelectedIndex();
					if (tabIndex-1 < 0 && !logoutClicked) 
						node.getSelectionModel().select(tabIndex+1);
					if (tabIndex+1 > node.getTabs().size()-1)
						node.getSelectionModel().select(tabIndex-1);
				}
				if (!currentTab.getValue().getId().equals("reloadTab")) {
					tab1.getGraphic().getStyleClass().add("notActive");
					currentTab.getValue().getGraphic().getStyleClass().remove("notActive");
				} else {
					currentTab.getValue().getGraphic().getStyleClass().remove("notActive");
				}
			}
		});
		
		final Label loginLabel = (Label) this.getNode().getTabs().get(0).getGraphic();
		loginLabel.setOnMouseClicked(event -> {
			if (isAuthenticated)
				this.logout();
		});
	}

	
	/**
	 * Logouts the and disconnect database and authentication
	 */
	private void logout () {
		this.getNode().getTabs().get(0).setDisable(false);
		this.getNode().getTabs().get(1).setDisable(true);
		this.getNode().getTabs().get(2).setDisable(true);
		this.getNode().getTabs().get(3).setDisable(true);
		this.getNode().getTabs().get(1).getGraphic().setVisible(false);
		this.getNode().getTabs().get(2).getGraphic().setVisible(false);
		this.getNode().getTabs().get(3).getGraphic().setVisible(false);
		this.getNode().getTabs().get(0).getGraphic().getStyleClass().remove("canBeHovered");
		this.getNode().getTabs().get(1).getGraphic().getStyleClass().remove("canBeHovered");
		this.getNode().getTabs().get(2).getGraphic().getStyleClass().remove("canBeHovered");
		this.getNode().getTabs().get(3).getGraphic().getStyleClass().remove("canBeHovered");
		final Label loginTabLabel = (Label) ((Tab) this.getNode().getTabs().get(0)).getGraphic();
		logoutClicked = true;
		this.getNode().getSelectionModel().select(0);
		logoutClicked = false;
		loginTabLabel.setText("Login");
		loginTabLabel.getStyleClass().remove("logout");
		connection = null;
		isAuthenticated = false;
	}
	
	
	/**
	 * After successfully authenticated
	 * will be active Tabs
	 */
	private void authenticated () {
		final Label loginTabLabel = (Label) ((Tab) this.getNode().getTabs().get(0)).getGraphic();
		loginTabLabel.setText("Logout");
		loginTabLabel.getStyleClass().add("logout");
		this.getNode().getSelectionModel().select(1);
		isAuthenticated = true;
	}
	
	
	/**
	 * Returns the authenticated
	 * @return the authenticated
	 */
	static public boolean isAuthenticated () {
		return isAuthenticated;
	}
	
	
	/**
	 * Returns database connection
	 * @return database connection
	 */
	static public Connection getConnection() {
		return connection;
	}
	
	
	/**
	 * Connect program to database in headless mode
	 */
	static public void connect () {
		final MysqlDataSource dataSource = new MysqlDataSource();
		try {
			dataSource.setURL("jdbc:mysql://localhost:3306/");
			dataSource.setDatabaseName("flight");
			dataSource.setUser("flight");
			dataSource.setPassword(HashCodes.sha2HashText(512, "k9A_0hW5x=+LzU1c9+P#y7E3"));
			connection = dataSource.getConnection();
			if (connection.isValid(0)) {
				isAuthenticated = true;
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Creates and returns a new data source for the sql databases. Most flexible
	 * way to bootstrap database communications, but most seldom used in Internet
	 * examples as it is used in server apps (Java EE)!
	 * @return the data source created
	 */
	protected DataSource newSqlConfig (final String username, final String password) {
		final MysqlDataSource dataSource = new MysqlDataSource();
		try {
			dataSource.setURL("jdbc:mysql://localhost:3306/");
			dataSource.setUser(username);
			dataSource.setPassword(password);
			connection = dataSource.getConnection();
			if (connection.isValid(0)) {
				((TablesController) this.getChildren().get("tables")).setDatabaseName("flight");
				this.authenticated();
				this.getNode().getTabs().get(1).setDisable(false);
				this.getNode().getTabs().get(2).setDisable(false);
				this.getNode().getTabs().get(3).setDisable(false);
				this.getNode().getTabs().get(1).getGraphic().setVisible(true);
				this.getNode().getTabs().get(2).getGraphic().setVisible(true);
				this.getNode().getTabs().get(3).getGraphic().setVisible(true);
				this.getNode().getTabs().get(0).getGraphic().getStyleClass().add("canBeHovered");
				this.getNode().getTabs().get(1).getGraphic().getStyleClass().add("canBeHovered");
				this.getNode().getTabs().get(2).getGraphic().getStyleClass().add("canBeHovered");
				this.getNode().getTabs().get(3).getGraphic().getStyleClass().add("canBeHovered");
				this.getNode().getTabs().get(1).getGraphic().requestFocus();
			}
			
		} catch (final SQLException e) {
			final VBox authenticationPane = (VBox) this.getNode().getTabs().get(0).getContent();
			final Label infoLabel = (Label) authenticationPane.getChildren().get(0);
			infoLabel.setText("Invalid username or password, please check your login information and try again.");
		}
		return dataSource;
	}
	
	
	/**
	 * Closes the database connection
	 */
	public void closeConnection () {
		try {
			connection.close();
		} catch (final SQLException e) {
			throw new UncheckedSQLException(e); 
		}
	}
	

	/**
	 * Returns the loaderController
	 * @return the loaderController
	 */
	public LoaderController loaderController () {
		return this.loaderController;
	}
}