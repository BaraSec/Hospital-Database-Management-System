package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddPrescriptionController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;
	private String username;
	private DateTimeFormatter dtf;

	@FXML
	private TextField patID;

	@FXML
	private TextArea description;

	@FXML
	private TextField totalCost, status;

	@FXML
	private TableView<ObservableList> patientsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		fillPatientsTable();
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	// to fill the patients table
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
			ID.setMinWidth(60);
			ID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> name = new TableColumn<>("Name");
			name.setMinWidth(90);
			name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> gender = new TableColumn<>("Gender");
			gender.setMinWidth(40);
			gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			TableColumn<ObservableList, String> sim = new TableColumn<>("Phone Number");
			sim.setMinWidth(90);
			sim.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

			TableColumn<ObservableList, String> dob = new TableColumn<>("Date of birth");
			dob.setMinWidth(40);
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

	// to add a prescription
	@FXML
	void addPrescriptionHandler(ActionEvent event)
	{
		String ID = patID.getText().toString().trim(),
				desc = description.getText().toString().trim(),
				cost = totalCost.getText().toString().trim(),
				docID = databaseHandler.getUserID("doctors", username) + "";

		if(ID.equals("") || desc.equals("") || cost.equals(""))
			status.setText("Fill all fields please");
		else if(!MainController.isNumeric(ID))
		{
			status.setText("ID isn't in correct format");
			patID.setText("");
		}
		else if(!MainController.isNumericOrDouble(cost))
		{
			status.setText("Cost must be a valid numeric value");
			totalCost.setText("");
		}
		else
		{
			if(!databaseHandler.isPatientFound(ID))
			{
				status.setText("Patient with this ID doesn't exist");
				patID.setText("");
			}
			else
			{
				LocalDateTime now = LocalDateTime.now();

				databaseHandler.addPrescription(docID, ID, dtf.format(now).substring(0, 10), dtf.format(now).substring(11, 19), desc, cost);

				status.setText("Prescription has been added");
				patID.setText("");
				description.setText("");
				totalCost.setText("");
			}
		}

	}

	// to close the stage
	@FXML
	void cancelAddPrescriptionHandler(ActionEvent event) {
		patID.getScene().getWindow().hide();
	}
}
