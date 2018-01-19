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

public class ListAppointmentsController
{

	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;

	@FXML
	private TableView<ObservableList> listAppointmentsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		fillListAppointmentsTable();
	}

	// to fill the appointments table
	public void fillListAppointmentsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet appoints = databaseHandler.execQuery("select docID, patID, date, time from appointments");

		TableColumn<ObservableList, String> docName = new TableColumn<>("Doctor's Name");
		docName.setMinWidth(178);
		docName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

		TableColumn<ObservableList, String> patName = new TableColumn<>("Patient's name");
		patName.setMinWidth(181);
		patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

		TableColumn<ObservableList, String> date = new TableColumn<>("Date");
		date.setMinWidth(103);
		date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

		TableColumn<ObservableList, String> time = new TableColumn<>("Time");
		time.setMinWidth(77);
		time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

		listAppointmentsTable.getColumns().addAll(docName, patName, date, time);

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

		listAppointmentsTable.setItems(data);
	}

	// to close the stage
	@FXML
	void closeListAppointmentsHandler(ActionEvent event)
	{
		listAppointmentsTable.getScene().getWindow().hide();

	}
}
