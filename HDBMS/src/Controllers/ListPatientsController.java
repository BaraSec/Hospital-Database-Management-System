package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListPatientsController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;

	@FXML
	private TableView<ObservableList> patientsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		fillPatientsTable();
	}

	// to fill the patients table
	public void fillPatientsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet patients = databaseHandler.execQuery("select adrID, patID, ID, name, gender, phoneNum, dateOfBirth from patients");

		try
		{
			TableColumn<ObservableList, String> pID = new TableColumn<>("pID");
			pID.setMinWidth(30);
			pID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> ID = new TableColumn<>("ID");
			ID.setMinWidth(90);
			ID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> name = new TableColumn<>("Name");
			name.setMinWidth(120);
			name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> gender = new TableColumn<>("Gender");
			gender.setMinWidth(55);
			gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			TableColumn<ObservableList, String> sim = new TableColumn<>("Phone Number");
			sim.setMinWidth(120);
			sim.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

			TableColumn<ObservableList, String> dob = new TableColumn<>("Date of birth");
			dob.setMinWidth(60);
			dob.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5).toString()));

			TableColumn<ObservableList, String> city = new TableColumn<>("City");
			city.setMinWidth(110);
			city.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6).toString()));

			TableColumn<ObservableList, String> street = new TableColumn<>("Street");
			street.setMinWidth(110);
			street.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7).toString()));

			TableColumn<ObservableList, String> building = new TableColumn<>("Building");
			building.setMinWidth(102);
			building.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7).toString()));

			patientsTable.getColumns().addAll(ID, pID, name, gender, sim, dob, city, street, building);

			while(patients.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				for(int i = 2; i <= patients.getMetaData().getColumnCount(); i++)
					row.add(patients.getString(i));

				ResultSet address = databaseHandler.execQuery("select cID, street, building from addresses where adrID = " + patients.getString(1));
				address.first();

				ResultSet cityName = databaseHandler.execQuery("select name from cities where cID = " + address.getString(1));
				cityName.first();

				row.add(cityName.getString(1));

				for(int i = 2; i <= address.getMetaData().getColumnCount(); i++)
				{
					row.add(address.getString(i));
				}

				data.add(row);
			}

			patientsTable.setItems(data);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to close the stage
	@FXML
	void closeListPatientsHandler(ActionEvent event) {
		patientsTable.getScene().getWindow().hide();

	}

}
