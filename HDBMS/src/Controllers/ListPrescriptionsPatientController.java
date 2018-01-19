package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListPrescriptionsPatientController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;

	@FXML
	private TableView<ObservableList> prescriptionsPatientTable;

	@FXML
	private TextField patID, status;

	@FXML
	private TableView<ObservableList> patientsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();

		fillPatientsTable();
	}

	// to full the patients table
	public void fillPatientsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet patients = databaseHandler.execQuery("select patID, ID, name, gender, phoneNum, dateOfBirth from patients");

		try
		{
			TableColumn<ObservableList, String> pID = new TableColumn<>("pID");
			pID.setMinWidth(20);
			pID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> ID = new TableColumn<>("ID");
			ID.setMinWidth(105);
			ID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> name = new TableColumn<>("Name");
			name.setMinWidth(120);
			name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> gender = new TableColumn<>("Gender");
			gender.setMinWidth(70);
			gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			TableColumn<ObservableList, String> sim = new TableColumn<>("Phone Number");
			sim.setMinWidth(120);
			sim.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

			TableColumn<ObservableList, String> dob = new TableColumn<>("Date of birth");
			dob.setMinWidth(75);
			dob.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5).toString()));

			patientsTable.getColumns().addAll(pID, ID, name,gender, sim, dob);

			while(patients.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				for(int i = 1; i <= patients.getMetaData().getColumnCount(); i++)
					row.add(patients.getString(i));

				data.add(row);
			}

			patientsTable.setItems(data);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// TableView-Button handler
	@FXML
	void listPrescriptionsPatient(ActionEvent event)
	{
		String ID = patID.getText().toString();

		if(!ID.equals(""))
		{
			if(!MainController.isNumeric(ID))
			{
				status.setText("Enter a positive integer please");
				patID.setText("");
				prescriptionsPatientTable.getItems().clear();
				prescriptionsPatientTable.getColumns().clear();
			}
			else
			{
				if(!databaseHandler.isPatientFound(ID))
				{
					patID.setText("");
					prescriptionsPatientTable.getItems().clear();
					prescriptionsPatientTable.getColumns().clear();
					status.setText("No associated patient with ID: '" + ID + "' has been found");
				}
				else
				{
					status.setText("Data Loaded Successfully");
					patID.setText("");
					prescriptionsPatientTable.getItems().clear();
					prescriptionsPatientTable.getColumns().clear();

					fillPrescriptionsPatientTable(ID);
				}
			}
		}
		else
		{
			status.setText("Enter a patient ID please");

			prescriptionsPatientTable.getColumns().clear();
			prescriptionsPatientTable.getItems().clear();;
		}
	}

	// to fill the prescriptions' history for the desired patient
	private void fillPrescriptionsPatientTable(String patientID)
	{
		data = FXCollections.observableArrayList();

		ResultSet history = databaseHandler.execQuery("select docID, date, time, description, totalCost from prescriptions where patID = " + patientID);

		TableColumn<ObservableList, String> docName = new TableColumn<>("Doctor's Name");
		docName.setMinWidth(50);
		docName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

		TableColumn<ObservableList, String> date = new TableColumn<>("Date Issued");
		date.setMinWidth(120);
		date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

		TableColumn<ObservableList, String> time = new TableColumn<>("Time issued");
		time.setMinWidth(55);
		time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

		TableColumn<ObservableList, String> presc = new TableColumn<>("Prescription");
		presc.setMinWidth(160);
		presc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

		TableColumn<ObservableList, String> cost = new TableColumn<>("Total Cost");
		cost.setMinWidth(60);
		cost.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

		prescriptionsPatientTable.getColumns().addAll(docName, date, time, presc, cost);

		try
		{
			while(history.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet doctor = databaseHandler.execQuery("select name from doctors where is_deleted = 0 and docID = " + history.getString(1));

				if(!doctor.isBeforeFirst())
					continue;

				doctor.first();
				row.add(doctor.getString(1));
				for (int i = 2; i <= history.getMetaData().getColumnCount(); i++)
					row.add(history.getString(i));
				data.add(row);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		prescriptionsPatientTable.setItems(data);
	}

	// to close the stage
	@FXML
	void closeListPrescriptionsPatientHandler(ActionEvent event) {
		patID.getScene().getWindow().hide();
	}
}
