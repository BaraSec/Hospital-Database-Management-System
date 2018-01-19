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

public class ListPrescriptionsController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;

	@FXML
	private TableView<ObservableList> prescriptionsTable;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
		fillPrescriptionsTable();
	}

	// to full the prescriptions table
	private void fillPrescriptionsTable()
	{
		data = FXCollections.observableArrayList();

		ResultSet prescs = databaseHandler.execQuery("select docID, patID, date, time, description, totalCost from prescriptions");

		TableColumn<ObservableList, String> docName = new TableColumn<>("Doctor's Name");
		docName.setMinWidth(106);
		docName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

		TableColumn<ObservableList, String> patName = new TableColumn<>("Patient's name");
		patName.setMinWidth(106);
		patName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

		TableColumn<ObservableList, String> date = new TableColumn<>("Date issued");
		date.setMinWidth(83);
		date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

		TableColumn<ObservableList, String> time = new TableColumn<>("Time issued");
		time.setMinWidth(77);
		time.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

		TableColumn<ObservableList, String> desc = new TableColumn<>("Prescription");
		desc.setMinWidth(175);
		desc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

		TableColumn<ObservableList, String> cost = new TableColumn<>("Total Cost");
		cost.setMinWidth(74);
		cost.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5).toString()));

		prescriptionsTable.getColumns().addAll(docName, patName, date, time, desc, cost);

		try
		{
			while(prescs.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();

				ResultSet doctor = databaseHandler.execQuery("select name from doctors where is_deleted = 0 and docID = " + prescs.getString(1));

				if(!doctor.isBeforeFirst())
					continue;

				doctor.first();
				row.add(doctor.getString(1));

				ResultSet patient = databaseHandler.execQuery("select name from patients where patID = " + prescs.getString(2));
				patient.first();
				row.add(patient.getString(1));

				for (int i = 3; i <= prescs.getMetaData().getColumnCount(); i++)
					row.add(prescs.getString(i));

				data.add(row);
			}
		}
		catch(SQLException e)
		{
			System.out.println("SQLException at prescController.fillPrescriptionsTable()");

			e.printStackTrace();
		}

		prescriptionsTable.setItems(data);
	}

	// to close the stage
	@FXML
	void closeListPrescriptionsHandler(ActionEvent event) {
		prescriptionsTable.getScene().getWindow().hide();
	}

}
