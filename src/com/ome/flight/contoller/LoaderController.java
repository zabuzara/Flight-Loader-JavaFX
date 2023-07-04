package com.ome.flight.contoller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;
import com.ome.flight.model.FlightLoader;
import com.ome.flight.model.FlightLoader.SearchBy;
import com.ome.flight.tools.ChildController;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.JSON;
import com.ome.flight.tools.ParentController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class LoaderController extends ChildController<TabPane, BorderPane> {
	static private boolean[] activesBtn = {false,false,true}; 
	static private long lineCount = 0;
	private final Tab loaderTab;
	private final FlightLoader flightLoader;

	
	/**
	 * Initializes a new instance
	 * @param parent
	 * @param node
	 */
	public LoaderController (final ParentController<TabPane> parent, final BorderPane node) {
		super(parent, node);
		this.flightLoader = new FlightLoader(false, false, 5, SearchBy.BOTH);
 
		this.loaderTab = (Tab) parent.getNode().getTabs().get(1);
		final BorderPane outerPane = (BorderPane) this.loaderTab.getContent();
		final VBox innerTopPane = (VBox) outerPane.getTop();
		final ScrollPane innerScrollCenterPane = (ScrollPane) outerPane.getCenter();
		final VBox innerCenterPane = (VBox) innerScrollCenterPane.getContent();
		final HBox iconHbox = (HBox) innerTopPane.getChildren().get(0);
		
		final ToggleButton arrivalButton = (ToggleButton) iconHbox.getChildren().get(0);
		arrivalButton.getStyleClass().remove("active");
		final ToggleButton departureButton = (ToggleButton) iconHbox.getChildren().get(1);
		departureButton.getStyleClass().remove("active");
		final ToggleButton bothButton = (ToggleButton) iconHbox.getChildren().get(2);
		bothButton.getStyleClass().remove("active");
		final TextField intervalField = (TextField) iconHbox.getChildren().get(4);
		intervalField.setPromptText("current "+(this.flightLoader.getIntervalTime()/1000)+"s");
		final Button setButton = (Button) iconHbox.getChildren().get(3);

		final HBox linkHbox = (HBox) innerTopPane.getChildren().get(1);
		final Label linkDisplayValue = (Label) linkHbox.getChildren().get(1);
		final HBox tokeHbox = (HBox) innerTopPane.getChildren().get(2);
		final Label tokenDisplayValue = (Label) tokeHbox.getChildren().get(1);
		final HBox timeHbox = (HBox) innerTopPane.getChildren().get(3);
		final Label timeDisplayValue = (Label) timeHbox.getChildren().get(1);
		final HBox statusHbox = (HBox) innerTopPane.getChildren().get(4);
		final Label statusDisplayValue = (Label) statusHbox.getChildren().get(1);
		
		DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now());
		final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.u, hh:mm:ss", Locale.GERMANY);	
		linkDisplayValue.setText(flightLoader.getAPI_URL());
		timeDisplayValue.setText(this.flightLoader.getStartLoadingTime().format(dateTimeFormat));
		this.flightLoader.eventListeners().add(text -> Platform.runLater(() -> {
			if (RootController.isAuthenticated()) {
				statusDisplayValue.setText(this.flightLoader.getMessage());
				if (text.length() > 0) {
					final Map<String,Object> map = JSON.parse(text);
					tokenDisplayValue.getStyleClass().remove("errorMessage");
					if (flightLoader.getToken().length() != 24)
						tokenDisplayValue.getStyleClass().add("errorMessage");
					tokenDisplayValue.setText(flightLoader.getToken());
					timeDisplayValue.setText(this.flightLoader.getStartLoadingTime().format(dateTimeFormat));
					final Label lineLabel = new Label((++lineCount)+":\t"+map.toString());
					lineLabel.setStyle("-fx-text-fill: black;-fx-font-size:14;");
					innerCenterPane.getChildren().add(lineLabel);
					innerCenterPane.heightProperty().addListener(observable -> innerScrollCenterPane.setVvalue(1D));
				}
			}
		}));
		
		switch (this.flightLoader.getSearchingType()) {
			case ARRIVAL: activesBtn = new boolean[]{true,false,false}; break;
			case DEPARTURE: activesBtn = new boolean[]{false,true,false}; break;
			case BOTH: activesBtn = new boolean[]{false,false,true}; break;
			default: break;
		}
		this.updateActiveBtns(arrivalButton, departureButton, bothButton, activesBtn);

		arrivalButton.setOnAction(event -> {
			this.updateActiveBtns(arrivalButton, departureButton, bothButton, true, false, false);
			this.searchBy(FlightLoader.SearchBy.ARRIVAL);
		});
		departureButton.setOnAction(event -> {
			this.updateActiveBtns(arrivalButton, departureButton, bothButton, false, true, false);
			this.searchBy(FlightLoader.SearchBy.DEPARTURE);
		});
		bothButton.setOnAction(event -> {
			this.updateActiveBtns(arrivalButton, departureButton, bothButton, false, false, true);
			this.searchBy(FlightLoader.SearchBy.BOTH);
		});
		
		setButton.setDisable(true);
		setButton.setOnAction(event -> {
			this.flightLoader.setIntervalTime(Integer.parseInt(intervalField.getText()));	
			intervalField.setPromptText("current "+intervalField.getText()+"s");
			intervalField.setText("");
			innerCenterPane.requestFocus();
		});
		
		intervalField.textProperty().addListener((Observable, oldValue, newValue) -> {
			try {
				final int parsedInput = Integer.parseInt(newValue);
				if (parsedInput >= 1)
					setButton.setDisable(false);
				else
					setButton.setDisable(true);
			} catch (NumberFormatException e) {
				setButton.setDisable(true);
			}
		});
	}
	
	
	/**
	 * Updates selected button in Loader Tab
	 * @param btn1
	 * @param btn2
	 * @param btn3
	 * @param values
	 */
	private void updateActiveBtns (final ToggleButton btn1, final ToggleButton btn2, final ToggleButton btn3, final boolean... values) {
		activesBtn = new boolean[]{values[0], values[1], values[2]};
		btn1.setSelected(activesBtn[0]);
		btn2.setSelected(activesBtn[1]);
		btn3.setSelected(activesBtn[2]);
	}
	
	
	/**
	 * Sets the loader searchBy property
	 * @param searchBy
	 */
	private void searchBy (final SearchBy searchBy) {
		this.flightLoader.setSearchingType(searchBy);
	}
	
	
	/**
	 * Creates a new LoginTab
	 * @param title
	 * @return a new LoginTab
	 */
	static public Tab newLoaderTab (final String title) {
		final Label loaderTabLabel = new Label(title);
		loaderTabLabel.getStyleClass().add("notActive");
		final Tab loaderTab = new Tab();
		loaderTab.setId("loaderTab");
		loaderTab.setGraphic(loaderTabLabel);
		loaderTab.getGraphic().getStyleClass().add("loaderTabLabel");
				
		final Label apiLinkLabel = new Label("API provider: ");
		final Label apiTokeLabel = new Label("Used token: ");
		final Label apiLoadingTimeLabel = new Label("Start time: ");
		final Label apiLoadingStatus = new Label("Status: ");
		apiLinkLabel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		apiTokeLabel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		apiLoadingTimeLabel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		apiLoadingStatus.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		apiLinkLabel.setPrefWidth(140);
		apiTokeLabel.setPrefWidth(140);
		apiLoadingTimeLabel.setPrefWidth(140);
		apiLoadingStatus.setPrefWidth(140);
		apiLinkLabel.setStyle("-fx-text-fill: #0C0050;");
		apiTokeLabel.setStyle("-fx-text-fill: #0C0050;");
		apiLoadingTimeLabel.setStyle("-fx-text-fill: #0C0050;");
		apiLoadingStatus.setStyle("-fx-text-fill: #0C0050;");
		
		final Label apiLinkValue = new Label();
		final Label apiTokenValue = new Label();
		final Label apiLoadingTimeValue = new Label();
		final Label statusLabel = new Label();
		
		final ToggleButton arrivalBtn = new ToggleButton("Arrival");
		final ToggleButton departureBtn = new ToggleButton("Departure");
		final ToggleButton bothBtn = new ToggleButton("Both");
		arrivalBtn.getStyleClass().add("loaderButton");
		departureBtn.getStyleClass().add("loaderButton");
		bothBtn.getStyleClass().add("loaderButton");
		
		final Label intervalLabel = new Label("Interval(s)");
		final TextField intervalField = new TextField("");
		final Button intervalBtn = new Button("Set");
		
		final HBox iconHbox = new HBox(arrivalBtn, departureBtn, bothBtn, intervalBtn, intervalField, intervalLabel);
		iconHbox.setSpacing(3);
		iconHbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		final HBox linkHbox = new HBox(apiLinkLabel, apiLinkValue);
		final HBox tokenHbox = new HBox(apiTokeLabel, apiTokenValue);
		final HBox timeHbox = new HBox(apiLoadingTimeLabel, apiLoadingTimeValue);
		final HBox messageHBox = new HBox(apiLoadingStatus, statusLabel);
		
		final VBox innerTopPane = new VBox(iconHbox, linkHbox, tokenHbox, timeHbox, messageHBox);
		innerTopPane.setPadding(new Insets(20, 5, 20, 5));
		innerTopPane.setPrefWidth(Integer.MAX_VALUE);
		innerTopPane.setStyle("-fx-border-width:0 0 1 0; -fx-border-color:black;");

		final VBox loaderInnerPane = new VBox();
		loaderInnerPane.getStyleClass().add("loaderInnerPane");

		final ScrollPane centerScrollPane = new ScrollPane(loaderInnerPane);
		centerScrollPane.setPrefWidth(Integer.MAX_VALUE);
		centerScrollPane.setPrefHeight(Integer.MAX_VALUE);
		centerScrollPane.setVvalue(centerScrollPane.getVmax()/2);
		centerScrollPane.setHvalue(centerScrollPane.getHmax()/2);
		centerScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		centerScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		final BorderPane loaderOuterPane = new BorderPane();
		loaderOuterPane.getStyleClass().add("loaderOuterPane");
		loaderOuterPane.setPrefWidth(Integer.MAX_VALUE);
		loaderOuterPane.setTop(innerTopPane);
		loaderOuterPane.setCenter(centerScrollPane);
		
		loaderTab.setContent(loaderOuterPane);
		loaderTab.setClosable(false);
		return loaderTab;
	}
}