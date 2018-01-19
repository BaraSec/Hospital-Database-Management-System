package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController
{
	private static final byte MANAGER_PRIVILEGES = 1;
	private static final byte DOCTOR_PRIVILEGES = 2;
	private static final byte SECRETARY_PRIVILEGES = 3;
	private int currentUserPrivilegesBit;
	private static String role;
	private static String username;
	private DatabaseHandler databaseHandler;

	@FXML
	private Menu AccountMenu;

	@FXML
	private Menu personalMenu;

	@FXML
	private MenuItem updatePassword;

	@FXML
	private MenuItem listMyData;

	@FXML
	private Menu appointmentsMenu;

	@FXML
	private MenuItem listAppointments;

	@FXML
	private MenuItem addAppointment;

	@FXML
	private Menu patientsMenu;

	@FXML
	private MenuItem listPatients;

	@FXML
	private MenuItem addPatient;

	@FXML
	private MenuItem listPatientPrescriptionHistory;

	@FXML
	private Menu prescriptionsMenu;

	@FXML
	private MenuItem listPrescriptions;

	@FXML
	private MenuItem addPrescription;

	@FXML
	private Menu employeesMenu;

	@FXML
	private MenuItem avEpOp;

	@FXML
	private TextField building, IDnum;

	@FXML
	private TextField city;

	@FXML
	private TextField street;

	@FXML
	private TextField uname;

	@FXML
	private TextField dob;

	@FXML
	private TextField phoneNum;

	@FXML
	private TextField salary;

	@FXML
	private TextField gender;

	@FXML
	private TextField name;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();

		name.sceneProperty().addListener((obs, oldScene, newScene) -> {
			Platform.runLater(() -> {
				Stage stage = (Stage) newScene.getWindow();
				stage.setOnCloseRequest(e -> {
					Platform.exit();
					System.exit(0);
				});
			});
		});
	}

	public static String getRole()
	{
		return role;
	}

	public static String getUsername()
	{
		return username;
	}

	void setUsername(String username)
	{
		this.username = username;
	}

	// to set the right privileges for the logged in user
	void setCurrentUserPrivilegesBit(byte currentUserPrivilegesBit)
	{
		this.currentUserPrivilegesBit = currentUserPrivilegesBit;
		applyPrivileges();
		setRole();
	}

	// to apply the right privileges
	private void applyPrivileges()
	{
		if(currentUserPrivilegesBit == MANAGER_PRIVILEGES)
		{
			personalMenu.setVisible(false);
			addAppointment.setVisible(false);
			addPatient.setVisible(false);
			addPrescription.setVisible(false);
		}
		else if(currentUserPrivilegesBit == DOCTOR_PRIVILEGES)
		{
			appointmentsMenu.setVisible(false);
			addPatient.setVisible(false);
			listPrescriptions.setVisible(false);
			employeesMenu.setVisible(false);
		}
		else if(currentUserPrivilegesBit == SECRETARY_PRIVILEGES)
		{
			personalMenu.setVisible(false);
			listPatientPrescriptionHistory.setVisible(false);
			prescriptionsMenu.setVisible(false);
			employeesMenu.setVisible(false);
		}
	}

	// to set the logged in user's Role
	private void setRole()
	{
		if(currentUserPrivilegesBit == MANAGER_PRIVILEGES)
			role = "managers";
		else if(currentUserPrivilegesBit == DOCTOR_PRIVILEGES)
			role = "doctors";
		else if(currentUserPrivilegesBit == SECRETARY_PRIVILEGES)
			role = "secretaries";
	}

	// to view the user's info
	void setMyInfo()
	{
		ResultSet accOwner = databaseHandler.getAccInfo(role, username);

		try
		{
			accOwner.first();
			name.setText(accOwner.getString(3));
			phoneNum.setText(accOwner.getString(6));
			gender.setText(accOwner.getString(4));
			dob.setText(accOwner.getString(7));
			salary.setText(accOwner.getString(5));
			uname.setText(username);
			IDnum.setText(accOwner.getString(10));

			ResultSet address = databaseHandler.execQuery("select * from addresses where adrID = " + accOwner.getInt(2));
			address.first();
			ResultSet cityRes = databaseHandler.execQuery("select name from cities where cID = " + address.getInt(2));
			cityRes.first();

			building.setText(address.getString(4));
			street.setText(address.getString(3));
			city.setText(cityRes.getString(1));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to sign out
	@FXML
	void signOut(ActionEvent event) {
		((Stage) name.getScene().getWindow()).close();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/LoginUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at LoginUI.fxml");
			e.printStackTrace();
		}

		LoginController controller = loader.getController();

		primaryStage.setTitle("Hospital Database Management System Login");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/hdbms.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	// View Handler
	@FXML
	void addAppointmentHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/AddAppointmentUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at AddAppointmentUI.fxml");
			e.printStackTrace();
		}

		AddAppointmentController controller = loader.getController();

		primaryStage.setTitle("Add an appointment");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/appointment.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void addPatientHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/AddPatientUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at AddPatientUI.fxml");
			e.printStackTrace();
		}

		AddPatientController controller = loader.getController();

		primaryStage.setTitle("Add a patient");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/patient.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void addPrescriptionHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/AddPrescriptionUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at AddPrescriptionUI.fxml");
			e.printStackTrace();
		}

		AddPrescriptionController controller = loader.getController();
		controller.setUsername(username);

		primaryStage.setTitle("Add a prescription");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/prescription.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void listAppointmentsHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/ListAppointmentsUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at ListAppointmentsUI.fxml");
			e.printStackTrace();
		}

		ListAppointmentsController controller = loader.getController();

		primaryStage.setTitle("Appointments");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/appointment.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void listMyDataHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/PersonalUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at PersonalUI.fxml");
			e.printStackTrace();
		}

		PersonalController controller = loader.getController();

		primaryStage.setTitle("My Data");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/data.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void listPatientPrescriptionHistoryHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/ListPatient'sPrescriptionsUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at ListPatient'sPrescriptionsUI.fxml");
			e.printStackTrace();
		}

		ListPrescriptionsPatientController controller = loader.getController();

		primaryStage.setTitle("Prescriptions History");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/patient.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void listPatientsHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/ListPatientsUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at ListPatientsUI.fxml");
			e.printStackTrace();
		}

		ListPatientsController controller = loader.getController();

		primaryStage.setTitle("Patients");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/patient.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void listPrescriptionsHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/ListPrescriptionsUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at ListPrescriptionsUI.fxml");
			e.printStackTrace();
		}

		ListPrescriptionsController controller = loader.getController();

		primaryStage.setTitle("Prescriptions");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/prescription.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@FXML
	void openEmployeesWorld(ActionEvent event) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/EmployeesUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at EmployeesUI.fxml");
			e.printStackTrace();
		}

		EmployeesController controller = loader.getController();
		controller.setUsername(username);

		stage.setTitle("Employees World");
		stage.getIcons().add(new Image("file:src/resources/Images/employees.png"));
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	void updatePasswordHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/UpdatePasswordUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at UpdatePasswordUI.fxml");
			e.printStackTrace();
		}

		UpdatePasswordController controller = loader.getController();
		controller.setUsername(username);
		controller.setRole(role);

		primaryStage.setTitle("Update your password");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/password.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	// to check if a string is an 'integer' or not (like: ID, phone number, ..)
	public static boolean isNumeric(String str)
	{
		for (char c : str.toCharArray())
			if (!Character.isDigit(c))
				return false;

		return true;
	}

	// to check if a string is a double or not
	public static boolean isNumericOrDouble(String str)
	{
		if(countDots(str) > 1)
			return false;

		for (char c : str.toCharArray())
			if (!Character.isDigit(c) && c != '.')
				return false;

		return true;
	}

	// to count the number of dots '.' in a string
	private static int countDots(String str)
	{
		int flag = 0;

		for (char c : str.toCharArray())
			if(c == '.')
				flag++;

		return flag;
	}

	// to check if a password meets the minimum requirements
	public static boolean isInFormat(String pw)
	{
		boolean lowFlag = false, capFlag = false, numFlag = false;

		if(pw.length() < 9)
			return false;

		for (char c : pw.toCharArray())
		{
			if(Character.isLowerCase(c))
				lowFlag = true;
			else if(Character.isUpperCase(c))
				capFlag = true;
			else if(Character.isDigit(c))
				numFlag = true;

			if(lowFlag && capFlag && numFlag)
				return true;
		}

		return false;
	}

	// to check if a name is valid
	public static boolean isValidName(String name)
	{
		if(name.length() < 3)
			return false;

		for (char c : name.toCharArray())
			if (!Character.isLetter(c))
				return false;

		return true;
	}

	// to check if a username is valid
	public static boolean isValidUserName(String username)
	{
		if(username.length() < 5 || !hasLetter(username))
			return false;

		for (char c : username.toCharArray())
			if (!Character.isLetterOrDigit(c))
				return false;

		return true;
	}

	// to check if a username (or other string) contains a letter or not
	private static boolean hasLetter(String username)
	{
		for (char c : username.toCharArray())
			if (Character.isLetter(c))
				return true;

		return false;
	}
}
