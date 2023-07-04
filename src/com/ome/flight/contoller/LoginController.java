package com.ome.flight.contoller;

import com.ome.flight.tools.ChildController;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.ParentController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class LoginController extends ChildController<TabPane, VBox> {
	private final Tab loginTab;
	private final Label infoLabel;
	
	/**
	 * Initializes a new instance
	 * @param parent
	 * @param child
	 */
	public LoginController (final ParentController<TabPane> parent, final VBox child) {
		super(parent, child);

		this.loginTab = (Tab) parent.getNode().getTabs().get(0);
		this.infoLabel = (Label) ((VBox) loginTab.getContent()).getChildren().get(0);
		final GridPane innerLoginPane = (GridPane) ((VBox) loginTab.getContent()).getChildren().get(1);
		final TextField username = (TextField) innerLoginPane.getChildren().get(1);
		final TextField password = (TextField) innerLoginPane.getChildren().get(3);
		final Button loginButton = (Button) innerLoginPane.getChildren().get(4);
		loginButton.setOnAction(event -> {
			this.authenticate(username.getText(), password.getText());
			username.setText("");
			password.setText("");
		});
	}
	
	
	/**
	 * Check authentication and initialize database connection
	 * @param username database username
	 * @param password database password
	 */
	private void authenticate (final String username, final String password) {
		if (!username.isEmpty() & !password.isEmpty()) {
			((RootController) this.getParent()).newSqlConfig(username, password);
		} else {
			this.infoLabel.setText("Username or password field is empty, please write your login information and try again.");
		}
	}
		
	
	/**
	 * Creates a new LoginTab
	 * @param title
	 * @return a new LoginTab
	 */
	static public Tab newAuthenticationTab (final String title) {
		final Label loginTabLabel = new Label(title);
		final Tab loginTab = new Tab();
		final String infoText = "login to to flight-loader, please write your username and password to login.";
		final Label infoLabel = new Label(infoText);
		final Label usernameLabel = new Label("Username");
		final Label passwordLabel = new Label("Password");
		final TextField usernameTextField = new TextField();
		final TextField passwordPasswordField = new PasswordField();
		final Button loginSubmitButton = new Button("Login");
		final Label authorInfo = new Label("Author: \u00A9Omid Malekzadeh Eshtajarani 2022");
	
		loginTab.setId("loginTab");
		loginTab.setGraphic(loginTabLabel);
		loginTab.getGraphic().getStyleClass().add("loginTabLabel");
		
		infoLabel.getStyleClass().setAll("infoLabel");
		usernameLabel.getStyleClass().setAll("usernameLabel");
		usernameTextField.getStyleClass().add("usernameTextField");
		passwordLabel.getStyleClass().setAll("passwordLabel");
		passwordPasswordField.getStyleClass().add("passwordPasswordField");
		loginSubmitButton.getStyleClass().add("loginSubmitButton");
		authorInfo.getStyleClass().add("loginAuthorInfo");
		
		final VBox loginOuterPane = new VBox();
		final GridPane loginInnerPane = new GridPane();
		loginOuterPane.getStyleClass().add("loginOuterPane");
		loginInnerPane.getStyleClass().add("loginInnerPane");

		loginInnerPane.add(usernameLabel, 0, 2);
		loginInnerPane.add(usernameTextField, 1, 2);
		loginInnerPane.add(passwordLabel, 0, 3);
		loginInnerPane.add(passwordPasswordField, 1, 3);
		loginInnerPane.add(loginSubmitButton, 1, 4);
		loginInnerPane.setPrefWidth(Integer.MAX_VALUE);

		loginOuterPane.getChildren().add(infoLabel);
		loginOuterPane.getChildren().add(loginInnerPane);
		loginOuterPane.getChildren().add(authorInfo);
		loginOuterPane.setPrefWidth(Integer.MAX_VALUE);
		
		loginTab.setContent(loginOuterPane);
		loginTab.setClosable(false);
		return loginTab;
	}
}
