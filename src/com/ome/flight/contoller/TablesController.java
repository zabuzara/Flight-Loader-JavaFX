package com.ome.flight.contoller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ome.flight.tools.ChildController;
import com.ome.flight.tools.Copyright;
import com.ome.flight.tools.ParentController;
import com.ome.flight.tools.RelationalDatabases;
import com.ome.flight.tools.UncheckedSQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DefaultStringConverter;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class TablesController extends ChildController<TabPane,TabPane> {
	private String databaseName =  null;
	private List<Tab> loadedTables = null;
	
	
	/**
	 * Inizializes a new instance
	 * @param parent
	 * @param node
	 */
	public TablesController (final ParentController<TabPane> parent,final TabPane node) {
		super(parent, node);
		
		final Button reloadButton = (Button) parent.getNode().getTabs().get(3).getGraphic();
		reloadButton.setOnMousePressed(event -> {
			if (RootController.isAuthenticated()) {
				reloadButton.setText("Wait");
			}
		});
		reloadButton.setOnMouseReleased(event -> {
			if (RootController.isAuthenticated()) {
				((Label) parent.getNode().getTabs().get(2).getGraphic()).requestFocus();
				parent.getNode().getSelectionModel().select(2);
				this.refreshTables();
				reloadButton.setText("Reload");
			}
		});
	}
	
	/**
	 * Refresh the database view
	 */
	private void refreshTables () {
		this.loadTables();
	}

	
	/**
	 * Sets the databaseName
	 * @param databaseName
	 */
	public void setDatabaseName (final String databaseName) {
		this.databaseName = databaseName;
		if (!this.databaseName.isEmpty()) {
			this.loadTables();
		}
	}
	
	
	/**
	 * Returns the databaseName
	 * @return the databaseName
	 */
	public String getDatabaseName () {
		return this.databaseName;
	}
	
	
	/**
	 * Returns the tablesTab
	 * @return the tablesTab
	 */
	public TabPane getTablesTab() {
		final TabPane loadedTabPane = this.getNode();
		loadedTabPane.getTabs().addAll(loadedTables);	
		return loadedTabPane;
	}
	
	
	/**
	 * Loads the tables
	 */
	private void loadTables () {
		try {
			final PreparedStatement statement = RootController.getConnection().prepareStatement("SHOW TABLES FROM "+this.getDatabaseName()); 
			if (statement.execute()) {
				final ResultSet result = statement.getResultSet();
				final List<Map<String,Object>> tablesMap = RelationalDatabases.toRowMaps(result, false);
				final List<Object> tablesList = new ArrayList<>();
				tablesList.clear();
				this.getNode().getTabs().clear();
				for (Map<String,Object> tableMap : tablesMap) {
					tablesList.addAll(tableMap.values());
				}

				for (Object value : tablesList) {
					this.getNode().getTabs().add(this.newSubTableTab(value));
	
					this.getNode().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
						@Override
						public void changed (ObservableValue<? extends Tab> currentTab, Tab tab1, Tab tab2) {
							if (currentTab != null &&
								currentTab.getValue() != null && 
								currentTab.getValue().getTabPane() != null &&
								currentTab.getValue().getTabPane().getTabs() != null &&
								currentTab.getValue().getTabPane().getTabs().size() > 0) {
								currentTab.getValue().getTabPane().getTabs().forEach(t -> {
									t.getGraphic().getStyleClass().remove("notActive");
									t.getGraphic().getStyleClass().add("notActive");
								});
								currentTab.getValue().getGraphic().getStyleClass().remove("notActive");
							}
						}
					});
				}	
				this.getNode().getTabs().forEach(t -> t.getGraphic().getStyleClass().add("notActive"));
				this.getNode().getTabs().get(0).getGraphic().getStyleClass().remove("notActive");
			}
		} catch (final SQLException e) {
			throw new UncheckedSQLException(e);
		}
	}
	
	
	/**
	 * Creates a new subTableTab
	 * @param tableName
	 * @return a new subTableTab
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Tab newSubTableTab (final Object tableName) {
		final BorderPane tableOuterPane = new BorderPane();
		tableOuterPane.getStyleClass().add("tableOuterPane");
		final Label authorInfo = new Label("Author:  \u00A9Omid Malekzadeh Eshtajarani 2022");
		authorInfo.getStyleClass().setAll("tableAuthorInfo");
		final Label tabLabel = new Label((String) tableName);
		final Tab tableTab = new Tab();
		tableTab.setClosable(false);
		tableTab.setId("tableTab");
		tableTab.setGraphic(tabLabel);
		tableTab.getGraphic().getStyleClass().add("tableTabLabel");
		final TableView<Map<Integer,String>> table = new TableView<>();
	
		table.getStyleClass().add("table");
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.setEditable(false);
		try {
			final PreparedStatement statement = RootController.getConnection().prepareStatement("SELECT * FROM "+this.getDatabaseName()+"."+tabLabel.getText()); 
			if (statement.execute()) {
				final ResultSet result = statement.getResultSet();
				final TableColumn<Map<Integer,String>,String>[] tableColumns =  new TableColumn[statement.getMetaData().getColumnCount()];
				for (int index=1;  index<=statement.getMetaData().getColumnCount(); ++index) {
			        final String name = statement.getMetaData().getColumnName(index);
					final TableColumn<Map<Integer,String>,String> tableColumn  = new TableColumn<>(name);					
					tableColumn.setCellValueFactory(new MapValueFactory(index));
			        tableColumn.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()));
			        tableColumn.setPrefWidth(200);
			        tableColumns[index-1] = tableColumn;
				}
		        table.getColumns().addAll(tableColumns);
		        while(result.next()) {
			        final Map<Integer,String> dataRow = new HashMap<>();
					 for (int columnIndex = 1; columnIndex <= statement.getMetaData().getColumnCount(); columnIndex++) {
			            final String value = result.getString(columnIndex);
			            dataRow.put(columnIndex, value);
			        }
			    	table.getItems().add(dataRow);	
			    }
			}
		} catch (final SQLException e) {
			Logger.getGlobal().log(Level.INFO, e.getMessage());
		} 	
		tableOuterPane.setCenter(table);
		tableTab.setContent(tableOuterPane);
		return tableTab;
	}
	
	/**
	 * Creates a new tableTab
	 * @param title
	 * @return a new tableTab
	 */
	static public Tab newTableTab (final String title) {
		final Label tablesTabLabel = new Label(title);
		final TabPane tablesTabPane = new TabPane();
		tablesTabPane.setId("tablesTabPane");
		final Tab tablesTab = new Tab();
		tablesTab.setId("tablesTab");
		tablesTab.setGraphic(tablesTabLabel);
		tablesTab.getGraphic().getStyleClass().add("tablesTabLabel");
		tablesTab.getGraphic().getStyleClass().add("notActive");
		tablesTab.setClosable(false);
		tablesTab.setDisable(false);
		tablesTab.setContent(tablesTabPane);
		return tablesTab;
	}
	
	
	/**
	 * Creates a new reloadTab
	 * @param title
	 * @return a new reloadTab
	 */
	static public Tab newReloadTab (final String title) {
		final Button reloadTabLabel = new Button(title);
		final Tab reloadTab = new Tab("");
		reloadTab.setId("reloadTab");
		reloadTab.setGraphic(reloadTabLabel);
		reloadTab.getGraphic().getStyleClass().add("reloadTabButton");
		reloadTab.getGraphic().getStyleClass().add("notActive");
		reloadTab.setClosable(false);
		reloadTab.setDisable(false);
		return reloadTab;
	}
}