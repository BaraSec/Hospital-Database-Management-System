package Controllers;

import DatabaseAPI.DatabaseHandler;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.apache.commons.codec.digest.DigestUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EmployeesController
{
	private DatabaseHandler databaseHandler;
	private ObservableList<ObservableList> data;
	private String currentRole;
	private String currentEID;
	private static String loggedInUsername;

	@FXML
	private TextField totalSecrsN;

	@FXML
	private TextField totalMngrsN;

	@FXML
	private TextField totalDocsN;

	@FXML
	private TextField totalEmpsN, totalPatsN;

	@FXML
	private TextField eID, aID, uID;

	@FXML
	private Button deleteAccount;

	@FXML
	private TextField status;

	@FXML
	private TextField aName;

	@FXML
	private TableView<ObservableList> employeesTable;

	@FXML
	private Button updateInfo;

	@FXML
	private ChoiceBox<String> aGender, eType;

	@FXML
	private JFXDatePicker aDOB;

	@FXML
	private TextField aUname;

	@FXML
	private ChoiceBox<String> aRole;

	@FXML
	private TextField aSalary;

	@FXML
	private TextField aPhoneNumber;

	@FXML
	private ChoiceBox<String> aCity;

	@FXML
	private TextField aBuilding;

	@FXML
	private TextField aStreet;

	@FXML
	private Button findEmployee;

	@FXML
	private TextField uName;

	@FXML
	private TextField uGender;

	@FXML
	private JFXDatePicker uDOB;

	@FXML
	private TextField uUname;

	@FXML
	private TextField uRole;

	@FXML
	private TextField uSalary;

	@FXML
	private TextField uPhoneNumber;

	@FXML
	private ChoiceBox<String> uCity;

	@FXML
	private TextField uBuilding;

	@FXML
	private TextField uStreet;

	@FXML
	private PasswordField aPassword;

	@FXML
	private PasswordField aREPassword;

	@FXML
	private PasswordField uREPassword;

	@FXML
	private PasswordField uPassword;

	void setUsername(String loggedInUsername)
	{
		this.loggedInUsername = loggedInUsername;
	}

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();

		fillEmployeesTable();
		initializeGenderBox();
		initializeCityBox();
		initializeRoleBox();
		initializeSecRoleBox();
		fillTotals();

		aDOB.setValue(LocalDate.now());

		resetUfields();
	}

	// to fill the number of employees, etc textFields
	private void fillTotals()
	{
		int[] nums = databaseHandler.getNofEmployees();

		totalEmpsN.setText(nums[3] + "");
		totalMngrsN.setText(nums[0] + "");
		totalDocsN.setText(nums[1] + "");
		totalSecrsN.setText(nums[2] + "");
		totalPatsN.setText(databaseHandler.getNofPatients() + "");
	}

	// to fill the employees table
	private void fillEmployeesTable()
	{
		data = FXCollections.observableArrayList();

		employeesTable.getItems().clear();
		employeesTable.getColumns().clear();

		ResultSet[] employees = new ResultSet[3];
		employees[0] = databaseHandler.execQuery("select manID, ID, name, username, gender, salary, phoneNum, dateOfBirth, adrID from managers where is_deleted = 0");
		employees[1] = databaseHandler.execQuery("select docID, ID, name, username, gender, salary, phoneNum, dateOfBirth, adrID from doctors where is_deleted = 0");
		employees[2] = databaseHandler.execQuery("select secID, ID, name, username, gender, salary, phoneNum, dateOfBirth, adrID from secretaries where is_deleted = 0");

		String[] roles = {"Manager", "Doctor", "Secretary"};

		try
		{
			TableColumn<ObservableList, String> eID = new TableColumn<>("eID");
			eID.setMinWidth(48);
			eID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));

			TableColumn<ObservableList, String> ID = new TableColumn<>("ID");
			ID.setMinWidth(102);
			ID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));

			TableColumn<ObservableList, String> name = new TableColumn<>("Name");
			name.setMinWidth(132);
			name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));

			TableColumn<ObservableList, String> username = new TableColumn<>("Username");
			username.setMinWidth(85);
			username.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));

			TableColumn<ObservableList, String> gender = new TableColumn<>("Gender");
			gender.setMinWidth(81);
			gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4).toString()));

			TableColumn<ObservableList, String> role = new TableColumn<>("Role");
			role.setMinWidth(75);
			role.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5).toString()));

			TableColumn<ObservableList, String> salary = new TableColumn<>("Salary");
			salary.setMinWidth(77);
			salary.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6).toString()));

			TableColumn<ObservableList, String> sim = new TableColumn<>("Phone Number");
			sim.setMinWidth(107);
			sim.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7).toString()));

			TableColumn<ObservableList, String> dob = new TableColumn<>("Date of birth");
			dob.setMinWidth(104);
			dob.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(8).toString()));

			TableColumn<ObservableList, String> city = new TableColumn<>("City");
			city.setMinWidth(91);
			city.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(9).toString()));

			TableColumn<ObservableList, String> street = new TableColumn<>("Street");
			street.setMinWidth(80);
			street.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(10).toString()));

			TableColumn<ObservableList, String> building = new TableColumn<>("Building");
			building.setMinWidth(96);
			building.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(11).toString()));

			employeesTable.getColumns().addAll(eID, ID, name, username, gender, role, salary, sim, dob, city, street, building);

			for(int i = 0; i < employees.length; i++)
			{
				while(employees[i].next())
				{
					ObservableList<String> row = FXCollections.observableArrayList();

					for(int j = 1; j <= employees[i].getMetaData().getColumnCount() - 1; j++)
					{
						row.add(employees[i].getString(j));

						if(j == 5)
							row.add(roles[i]);
					}

					ResultSet address = databaseHandler.execQuery("select cID, street, building from addresses where adrID = " + employees[i].getString(employees[i].getMetaData().getColumnCount()));
					address.first();

					ResultSet cityName = databaseHandler.execQuery("select name from cities where cID = " + address.getString(1));
					cityName.first();

					row.add(cityName.getString(1));

					for(int j = 2; j <= address.getMetaData().getColumnCount(); j++)
					{
						row.add(address.getString(j));
					}

					data.add(row);
				}

				employeesTable.setItems(data);
			}
		}
		catch (SQLException e)
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("MySql DB Error. Report this please!");
			status.setAlignment(Pos.CENTER);

			e.printStackTrace();
		}
	}

	// to add an employee
	@FXML
	void addEmployee(ActionEvent event)
	{
		String name = aName.getText().toString().trim(),
				username = aUname.getText().toString().trim(),
				street = aStreet.getText().toString().trim(),
				ID = aID.getText().toString().trim(),
				sim = aPhoneNumber.getText().toString().trim(),
				building = aBuilding.getText().toString().trim(),
				salary = aSalary.getText().toString().trim(),
				password = aPassword.getText().toString(),
				rePassword = aREPassword.getText().toString();

		LocalDate dob = aDOB.getValue();

		String gender = aGender.getSelectionModel().getSelectedItem(),
				role = aRole.getSelectionModel().getSelectedItem(),
				city = aCity.getSelectionModel().getSelectedItem();

		if(name.equals("") || username.equals("") || street.equals("") || ID.equals("") || sim.equals("") || building.equals("") || salary.equals("") || password.equals("") || rePassword.equals("") ||
				gender.equals("Gender") || role.equals("Role") || city.equals("City"))
			status.setText("Fill all fields to add an employee please");
		else if(!MainController.isValidName(name))
		{
			status.setText("Enter a valid name please");
			aName.setText("");
		}
		else if(!MainController.isValidUserName(username))
		{
			status.setText("Enter a valid username please");
			aUname.setText("");
		}
		else if(databaseHandler.isUsernameFound(username))
		{
			status.setText("Enter a unique username please");
			aUname.setText("");
		}
		else if(!password.equals(rePassword))
		{
			status.setText("Enter the same password twice please");
			aPassword.setText("");
			aREPassword.setText("");
		}
		else if(!MainController.isInFormat(password))
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("Password must have at least 9 characters [ 1 lowercase letter, 1 uppercase letter, and 1 digit are a MUST ]");
			status.setAlignment(Pos.CENTER);

			aPassword.setText("");
			aREPassword.setText("");
		}
		else if(!MainController.isNumeric(ID) || ID.length() < 7)
		{
			status.setText("Enter a correct ID please");
			aID.setText("");
		}
		else if(!MainController.isNumeric(sim) || ID.length() < 7)
		{
			status.setText("Enter a correct phone number please");
			aPhoneNumber.setText("");
		}
		else if(dob.compareTo(LocalDate.now()) > 0)
		{
			status.setText("Enter a correct birth date please");
			aDOB.setValue(LocalDate.now());
		}
		else if(!MainController.isNumericOrDouble(salary))
		{
			status.setText("Salary must have a valid positive numeric value");
			aSalary.setText("");
		}
		else
		{
			int addressID = databaseHandler.addAddress(street, building, databaseHandler.getCityID(city));
			databaseHandler.addEmployee(addressID, name, gender, salary, sim, dob, username, DigestUtils.sha384Hex(password), ID, role);

			fillEmployeesTable();
			fillTotals();

			status.setText("Employee has been added successfully");
			aName.setText("");
			aUname.setText("");
			aStreet.setText("");
			aID.setText("");
			aPhoneNumber.setText("");
			aBuilding.setText("");
			aSalary.setText("");
			aPassword.setText("");
			aREPassword.setText("");
			aDOB.setValue(LocalDate.now());
			aGender.getSelectionModel().clearSelection();
			aGender.setValue("Gender");
			aRole.getSelectionModel().clearSelection();
			aRole.setValue("Role");
			aCity.getSelectionModel().clearSelection();
			aCity.setValue("City");
		}
	}

	// to find an employee
	@FXML
	void findEmployee(ActionEvent event)
	{
		currentEID = eID.getText().toString().trim();
		currentRole = eType.getSelectionModel().getSelectedItem();

		if(currentEID.equals("") || currentRole.equals("Role"))
		{
			status.setText("Fill all fields to find an employee please");

			resetUfields();
		}

		else if(!MainController.isNumeric(currentEID))
		{
			status.setText("ID must be a positive integer please");
			eID.setText("");

			resetUfields();
		}
		else
		{
			ResultSet info = databaseHandler.getEmployeeFullInfo(currentEID, currentRole);

			try
			{
				if(!info.isBeforeFirst())
				{
					status.setText("No " + currentRole + " with ID " + currentEID + " has been found!");
					eID.setText("");
					eType.getSelectionModel().clearSelection();
					eType.setValue("Role");

					resetUfields();
				}
				else
				{
					updateInfo.setDisable(false);
					deleteAccount.setDisable(false);

					eID.setText("");
					eType.getSelectionModel().clearSelection();
					eType.setValue("Role");

					info.first();

					ResultSet address = databaseHandler.execQuery("select cID, street, building from addresses where adrID = " + info.getString(2));
					address.first();

					ResultSet cityName = databaseHandler.execQuery("select name from cities where cID = " + address.getString(1));
					cityName.first();

					initializeSecCityBox();

					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					dtf = dtf.withLocale(Locale.US);

					uName.setDisable(false);
					uUname.setDisable(false);
					uStreet.setDisable(false);
					uID.setDisable(false);
					uPhoneNumber.setDisable(false);
					uBuilding.setDisable(false);
					uSalary.setDisable(false);
					uPassword.setDisable(false);
					uREPassword.setDisable(false);
					uGender.setDisable(false);
					uRole.setDisable(false);
					uCity.setDisable(false);
					uDOB.setDisable(false);

					uName.setText(info.getString(3));
					uGender.setText(info.getString(4));
					uSalary.setText(info.getString(5));
					uPhoneNumber.setText(info.getString(6));
					uDOB.setValue(LocalDate.parse(info.getString(7), dtf));
					uUname.setText(info.getString(8));
					uID.setText(info.getString(10));
					uCity.setValue(cityName.getString(1));
					uStreet.setText(address.getString(2));
					uBuilding.setText(address.getString(3));
					uRole.setText(currentRole.toString());

					status.setText("Employee info has been loaded successfully");
				}
			}
			catch (SQLException e)
			{
				status.setAlignment(Pos.CENTER_LEFT);
				status.setText("MySql DB Error. Report this please!");
				status.setAlignment(Pos.CENTER);

				e.printStackTrace();
			}
		}
	}

	// to update an employee's info
	@FXML
	void updateInfo(ActionEvent event)
	{
		String pass = uPassword.getText().toString(),
				rePass = uREPassword.getText().toString(),
				name = uName.getText().toString().trim(),
				username = uUname.getText().toString().trim(),
				street = uStreet.getText().toString().trim(),
				ID = uID.getText().toString().trim(),
				sim = uPhoneNumber.getText().toString().trim(),
				building = uBuilding.getText().toString().trim(),
				salary = uSalary.getText().toString().trim();

		String city = uCity.getSelectionModel().getSelectedItem();

		LocalDate dob = uDOB.getValue();

		if(name.equals("") || username.equals("") || street.equals("") || ID.equals("") || sim.equals("") || building.equals("") || salary.equals("") || city.equals("City"))
			status.setText("Fill all fields to update the employee's info please");
		else if(!MainController.isValidName(name))
		{
			status.setText("Enter a valid name please");
			uName.setText("");
		}
		else if(!MainController.isValidUserName(username))
		{
			status.setText("Enter a valid username please");
			uUname.setText("");
		}
		else if(databaseHandler.isUsernameFound(username) && !databaseHandler.getEmployeeUsername(currentEID, currentRole).equals(username))
		{
			status.setText("Enter a unique new username please");
			aUname.setText("");
		}
		else if(!pass.equals(rePass))
		{
			status.setText("Enter the same new password twice please");
			uPassword.setText("");
			uREPassword.setText("");
		}
		else if(!pass.equals("") && !MainController.isInFormat(pass))
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("Password must have at least 9 characters [ 1 lowercase letter, 1 uppercase letter, and 1 digit are a MUST ]");
			status.setAlignment(Pos.CENTER);

			uPassword.setText("");
			uREPassword.setText("");
		}
		else if(!MainController.isNumeric(ID) || ID.length() < 7)
		{
			status.setText("Enter a correct ID please");
			uID.setText("");
		}
		else if(!MainController.isNumeric(sim) || ID.length() < 7)
		{
			status.setText("Enter a correct phone number please");
			uPhoneNumber.setText("");
		}
		else if(dob.compareTo(LocalDate.now()) > 0)
		{
			status.setText("Enter a correct birth date please");
			uDOB.setValue(LocalDate.now());
		}
		else if(!MainController.isNumericOrDouble(salary))
		{
			status.setText("Salary must have a valid positive numeric value");
			uSalary.setText("");
		}
		else
		{
			int addressID = databaseHandler.addAddress(street, building, databaseHandler.getCityID(city));
			databaseHandler.updateEmployeeInfo(addressID, name, salary, sim, dob, username, pass, ID, currentRole, currentEID);

			fillEmployeesTable();

			status.setText("Employee's info has been updated successfully");
			eID.setText("");
			eType.getSelectionModel().clearSelection();
			eType.setValue("Role");

			resetUfields();
		}
	}

	// to reset the necessary fields' values
	private void resetUfields()
	{
		updateInfo.setDisable(true);
		deleteAccount.setDisable(true);

		uName.setText("");
		uUname.setText("");
		uStreet.setText("");
		uID.setText("");
		uPhoneNumber.setText("");
		uBuilding.setText("");
		uSalary.setText("");
		uPassword.setText("");
		uREPassword.setText("");
		uDOB.setValue(null);
		uGender.setText("");
		uRole.setText("");
		uCity.getSelectionModel().clearSelection();
		uCity.setValue("City");
		uName.setDisable(true);
		uUname.setDisable(true);
		uStreet.setDisable(true);
		uID.setDisable(true);
		uPhoneNumber.setDisable(true);
		uBuilding.setDisable(true);
		uSalary.setDisable(true);
		uPassword.setDisable(true);
		uREPassword.setDisable(true);
		uDOB.setValue(null);
		uDOB.setDisable(true);
		uGender.setDisable(true);
		uRole.setDisable(true);
		uCity.setDisable(true);
	}

	// to delete an account
	@FXML
	void deleteAccount(ActionEvent event)
	{
		databaseHandler.deleteEmployee(currentRole, currentEID);
		fillEmployeesTable();
		fillTotals();

		status.setText("Account has been deleted successfully");
		eID.setText("");
		eType.getSelectionModel().clearSelection();
		eType.setValue("Role");

		resetUfields();

		if(currentRole.equals("Manager") && databaseHandler.getUserID("managers", loggedInUsername) == -1)
			System.exit(0);
	}

	// to initialize the gender's choice box
	private void initializeGenderBox()
	{
		ObservableList<String> genders = FXCollections.observableArrayList();

		genders.addAll("Male", "Female");

		aGender.setItems(genders);
		aGender.setValue("Gender");
	}

	// to initialize the city's choice box
	private void initializeCityBox()
	{
		ObservableList<String> cities = FXCollections.observableArrayList();
		ResultSet data = databaseHandler.execQuery("select name from cities");

		try
		{
			while(data.next())
				cities.add(data.getString(1));
		}
		catch(SQLException e)
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("MySql DB Error. Report this please!");
			status.setAlignment(Pos.CENTER);

			e.printStackTrace();
		}

		aCity.setItems(cities);
		aCity.setValue("City");
	}

	// to initialize the second city's choice box
	private void initializeSecCityBox()
	{
		ObservableList<String> cities = FXCollections.observableArrayList();
		ResultSet data = databaseHandler.execQuery("select name from cities");

		try
		{
			while(data.next())
				cities.add(data.getString(1));
		}
		catch(SQLException e)
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("MySql DB Error. Report this please!");
			status.setAlignment(Pos.CENTER);

			e.printStackTrace();
		}

		uCity.setItems(cities);
		uCity.setValue("City");
	}

	// to initialize the role's choice box
	private void initializeRoleBox()
	{
		ObservableList<String> roles = FXCollections.observableArrayList();

		roles.addAll("Manager", "Doctor", "Secretary");

		aRole.setItems(roles);
		aRole.setValue("Role");
	}

	// to initialize the second role's choice box
	private void initializeSecRoleBox()
	{
		ObservableList<String> roles = FXCollections.observableArrayList();

		roles.addAll("Manager", "Doctor", "Secretary");

		eType.setItems(roles);
		eType.setValue("Role");
	}

	// to close the stage
	@FXML
	void closeEmployeesWorld(ActionEvent event)
	{
		employeesTable.getScene().getWindow().hide();
	}
}
