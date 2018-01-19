package Controllers;

import DatabaseAPI.DatabaseHandler;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddAppointmentController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;
	private ObservableList<String> doctors;

	@FXML
	private TextField patID, status;

	@FXML
	private ChoiceBox<String> docBox;

	@FXML
	private JFXDatePicker date;

	@FXML
	private JFXTimePicker time;

	@FXML
	private TableView<ObservableList> patientsTable, appointmentsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		initializeDocsBox();
		fillPatientsTable();
		fillAppointmentsTable();

		date.setValue(LocalDate.now());
		time.setEditable(false);
		time.setValue(LocalTime.now());
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
			gender.setMinWidth(30);
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

	// to fill the appointments table
	public void fillAppointmentsTable()
	{
		appointmentsTable.getItems().clear();
		appointmentsTable.getColumns().clear();

		data = FXCollections.observableArrayList();

		ResultSet appoints = databaseHandler.execQuery("select docID, patID, date, time from appointments");

		TableColumn<ObservableList, String> docName = new TableColumn<>("Doctor's Name");
		docName.setMinWidth(140);
		docName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

		TableColumn<ObservableList, String> patName = new TableColumn<>("Patient's name");
		patName.setMinWidth(140);
		patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

		TableColumn<ObservableList, String> date = new TableColumn<>("Date");
		date.setMinWidth(85);
		date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

		TableColumn<ObservableList, String> time = new TableColumn<>("Time");
		time.setMinWidth(80);
		time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

		appointmentsTable.getColumns().addAll(docName, patName, date, time);

		try
		{
			while(appoints.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet doctor = databaseHandler.execQuery("select name from doctors where is_deleted = 0 and docID = " + appoints.getString(1));

				if(!doctor.isBeforeFirst())
					continue;

				doctor.first();
				row.add(doctor.getString(1));

				ResultSet patient = databaseHandler.execQuery("select name from patients where patID = " + appoints.getString(2));
				patient.first();
				row.add(patient.getString(1));

				for (int i = 3; i <= appoints.getMetaData().getColumnCount(); i++)
					row.add(appoints.getString(i));

				data.add(row);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		appointmentsTable.setItems(data);
	}

	// to add an appointment
	@FXML
	void addAppointmentHandler(ActionEvent event)
	{
		String ID = patID.getText().toString().trim();
		String doc = docBox.getSelectionModel().getSelectedItem();
		String docID = doc.substring(0, doc.indexOf('|')).trim();
		LocalDate appDate = date.getValue();
		LocalTime appTime = time.getValue();

		if(ID.equals("") || doc.equals("ID | Name"))
			status.setText("Fill all fields please");
		else if(!MainController.isNumeric(ID))
		{
			status.setText("ID isn't in correct format");
			patID.setText("");
		}
		else if(appDate.compareTo(LocalDate.now()) < 0)
		{
			status.setText("Date can't be from the past");
			date.setValue(LocalDate.now());
		}
		else if(appDate.equals(LocalDate.now().toString()) && appTime.toString().compareTo(LocalTime.now().toString().substring(0, LocalTime.now().toString().lastIndexOf(':'))) < 0)
		{
			status.setText("Time can't be from the past");
			time.setValue(LocalTime.now());
		}
		else
		{
			if(!databaseHandler.isPatientFound(ID))
			{
				status.setText("Patient with this ID doesn't exist");
				patID.setText("");
			}
			else if(databaseHandler.isTimeTaken(docID, appDate, appTime))
				status.setText("Appointment is already set in this time");
			else
			{
				databaseHandler.addAppointment(docID, ID, appDate, appTime);
				fillAppointmentsTable();

				status.setText("Appointment has been added");
				patID.setText("");
				date.setValue(LocalDate.now());
				time.setValue(LocalTime.now());
				docBox.getSelectionModel().clearSelection();
				docBox.setValue("ID | Name");
			}
		}
	}

	// to initialize doctors' choice box
	private void initializeDocsBox()
	{
		doctors = FXCollections.observableArrayList();
		ResultSet data = databaseHandler.execQuery("select docID, name from doctors where is_deleted = 0");

		try
		{
			while(data.next())
				doctors.add(data.getString(1) + " | " + data.getString(2));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		docBox.setItems(doctors);
		docBox.setValue("ID | Name");
	}

	@FXML
	void cancelAddAppointmentHander(ActionEvent event) {
		patID.getScene().getWindow().hide();
	}

}
