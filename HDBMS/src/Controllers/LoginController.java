package Controllers;

import DatabaseAPI.DatabaseHandler;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController
{
	private static final byte NOUSER_PRIVILEGES = 0;
	private DatabaseHandler databaseHandler;

	@FXML
	private JFXTextField username;
	@FXML
	private JFXPasswordField password;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
	}

	// to login
	@FXML
	private void handleLoginButtonAction(ActionEvent event)
	{
		String hashedPass = DigestUtils.sha384Hex(password.getText().trim());
		byte privBit = NOUSER_PRIVILEGES;

		try
		{
			privBit = (byte)databaseHandler.getUser(username.getText().trim(), hashedPass);
		}
		catch (SQLException e)
		{
			System.out.println("MySql DB username Error. 'handleLoginButtonAction()'.");
			e.printStackTrace();
		}

		if(privBit == NOUSER_PRIVILEGES)
		{
			username.getStyleClass().add("wrong-credentials");
			password.getStyleClass().add("wrong-credentials");
		}
		else
			createMainWindows(privBit, username.getText().trim());
	}

	// to create the Main program's stage
	private void createMainWindows(byte privBit, String username)
	{
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/UI/MainUI.fxml"));
		Parent root = null;

		try
		{
			root = loader.load();
		}
		catch (IOException e)
		{
			System.out.println("Exception at MainUI.fxml");
			e.printStackTrace();
		}

		MainController controller = loader.getController();
		controller.setCurrentUserPrivilegesBit(privBit);
		controller.setUsername(username);
		controller.setMyInfo();

		primaryStage.setTitle("Hospital Database Management System");
		primaryStage.getIcons().add(new Image("file:src/resources/Images/hdbms.png"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		closeStage();
		primaryStage.show();
	}

	// to exit the system
	@FXML
	private void handleCancelButtonAction(ActionEvent event)
	{
		System.exit(0);
	}

	// to close the stage
	private void closeStage()
	{
		((Stage) username.getScene().getWindow()).close();
	}
}