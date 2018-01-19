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

public class PersonalController
{
	private String role;
	private String username;
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;
	private int ID;

	@FXML
	private TableView<ObservableList> myAppointmentsTable;

	@FXML
	private TableView<ObservableList> myPatientsTable;

	@FXML
	private TableView<ObservableList> myPrescriptionsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();

		role = MainController.getRole();
		username = MainController.getUsername();
		setID();
		fillTables();
	}

	public void setID()
	{
		ID = databaseHandler.getUserID(role, username);
	}

	// to fill the TableViews
	public void fillTables()
	{
		fillMyAppointmentsTable();
		fillMyPatientsTable();
		fillMyPrescriptionsTable();
	}

	// to fill the Appointments TableView
	public void fillMyAppointmentsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet appointments = databaseHandler.execQuery("select patID, time, date from appointments where docID = " + ID);

		try
		{
			TableColumn<ObservableList, String> patName = new TableColumn<>("Patient's name");
			patName.setMinWidth(240);
			patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> date = new TableColumn<>("Date");
			date.setMinWidth(150);
			date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> time = new TableColumn<>("Time");
			time.setMinWidth(150);
			time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			myAppointmentsTable.getColumns().addAll(patName, date, time);

			while(appointments.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet name = databaseHandler.execQuery("select name from patients where patID = " + appointments.getString(1));
				name.first();
				row.add(name.getString(1));

				for(int i = 2; i <= appointments.getMetaData().getColumnCount(); i++)
					row.add(appointments.getString(i));

				data.add(row);
			}

			myAppointmentsTable.setItems(data);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to fill the patients TableView
	public void fillMyPatientsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet patients = databaseHandler.execQuery("select DISTINCT patID from appointments where docID = " + ID);

		try
		{
			TableColumn<ObservableList, String> patName = new TableColumn<>("Name");
			patName.setMinWidth(170);
			patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> gender = new TableColumn<>("Gender");
			gender.setMinWidth(100);
			gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> sim = new TableColumn<>("Phone number");
			sim.setMinWidth(150);
			sim.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> dob = new TableColumn<>("Date of birth");
			dob.setMinWidth(120);
			dob.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			myPatientsTable.getColumns().addAll(patName, gender, sim, dob);

			while(patients.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet patData = databaseHandler.execQuery("select name, gender, phoneNum, dateOfBirth from patients where patID = " + patients.getString(1));
				patData.first();

				for(int i = 1; i <= patData.getMetaData().getColumnCount(); i++)
					row.add(patData.getString(i));

				data.add(row);
			}

			myPatientsTable.setItems(data);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to fill the prescriptions TableView
	public void fillMyPrescriptionsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet prescData = databaseHandler.execQuery("select patID, date, time, description, totalCost from prescriptions where docID = " + ID);

		try
		{
			TableColumn<ObservableList, String> patName = new TableColumn<>("Patient's name");
			patName.setMinWidth(120);
			patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> date = new TableColumn<>("Date issued");
			date.setMinWidth(80);
			date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> time = new TableColumn<>("Time issued");
			time.setMinWidth(80);
			time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> presc = new TableColumn<>("Prescription");
			presc.setMinWidth(180);
			presc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			TableColumn<ObservableList, String> cost = new TableColumn<>("Total Cost");
			cost.setMinWidth(60);
			cost.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

			myPrescriptionsTable.getColumns().addAll(patName, date, time, presc, cost);

			while(prescData.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet name = databaseHandler.execQuery("select name from patients where patID = " + prescData.getString(1));
				name.first();
				row.add(name.getString(1));

				for(int i = 2; i <= prescData.getMetaData().getColumnCount(); i++)
					row.add(prescData.getString(i));

				data.add(row);
			}

			myPrescriptionsTable.setItems(data);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to close the stage
	@FXML
	void closePersonalHandler(ActionEvent event) {
		myAppointmentsTable.getScene().getWindow().hide();

	}
}