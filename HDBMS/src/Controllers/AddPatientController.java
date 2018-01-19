package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddPatientController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<String> genders;
	private ObservableList<String> cities;

	@FXML
	private ChoiceBox<String> genderBox;

	@FXML
	private DatePicker dob;

	@FXML
	private TextField name, status, IDnum;

	@FXML
	private TextField phoneNumber;

	@FXML
	private ChoiceBox<String> cityBox;

	@FXML
	private TextField street;

	@FXML
	private TextField building;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		initializeGenderBox();
		initializeCityBox();
		dob.setValue(LocalDate.now());
	}

	// to add a patient
	@FXML
	void addPatientHandler(ActionEvent event)
	{
		String patName = name.getText().toString().trim(),
				patGender = genderBox.getSelectionModel().getSelectedItem(),
				patSim = phoneNumber.getText().toString().trim(),
				patStreet = street.getText().toString().trim(),
				patBuilding = building.getText().toString().trim(),
				ID = IDnum.getText().toString().trim(),
				patCity = cityBox.getSelectionModel().getSelectedItem();
		LocalDate patDOB = dob.getValue();

		if(patName.equals("") || patGender.equals("Gender") || patSim.equals("") || patStreet.equals("") || patBuilding.equals("") || patCity.equals("City") || ID.equals(""))
			status.setText("Fill all fields please");
		else if(!MainController.isValidName(patName))
		{
			status.setText("Enter a valid name please");
			name.setText("");
		}
		else if(!MainController.isNumeric(ID) || ID.length() < 7)
		{
			status.setText("Enter a correct ID please");
			IDnum.setText("");
		}
		else if(patDOB.compareTo(LocalDate.now()) > 0)
		{
			status.setText("Enter a correct birth date please");
			dob.setValue(LocalDate.now());
		}
		else if(!MainController.isNumeric(patSim) || patSim.length() < 7)
		{
			status.setText("Enter a correct phone number please");
			phoneNumber.setText("");
		}
		else
		{
			int addressID = databaseHandler.addAddress(patStreet, patBuilding, databaseHandler.getCityID(patCity));
			databaseHandler.addPatient(addressID, patName, patGender, patSim, patDOB, ID);

			status.setText("Patient has been added");
			name.setText("");
			IDnum.setText("");
			genderBox.getSelectionModel().clearSelection();
			genderBox.setValue("Gender");
			dob.setValue(LocalDate.now());
			phoneNumber.setText("");
			street.setText("");
			building.setText("");
			cityBox.getSelectionModel().clearSelection();
			cityBox.setValue("City");
		}
	}

	// to initialize the gender's choice box
	private void initializeGenderBox()
	{
		genders = FXCollections.observableArrayList();

		genders.addAll("Male", "Female");

		genderBox.setItems(genders);
		genderBox.setValue("Gender");
	}

	// to initialize the city's choice box
	private void initializeCityBox()
	{
		cities = FXCollections.observableArrayList();
		ResultSet data = databaseHandler.execQuery("select name from cities");

		try
		{
			while(data.next())
				cities.add(data.getString(1));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		cityBox.setItems(cities);
		cityBox.setValue("City");
	}

	// to close the stage
	@FXML
	void cancelAddPatientHandler(ActionEvent event) {
		name.getScene().getWindow().hide();
	}
}
