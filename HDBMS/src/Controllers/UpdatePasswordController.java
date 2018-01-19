package Controllers;

import DatabaseAPI.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.digest.DigestUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatePasswordController
{
	private String role;
	private String username;
	private DatabaseHandler databaseHandler;

	@FXML
	private PasswordField currentPass;

	@FXML
	private PasswordField newPass;

	@FXML
	private PasswordField reNewPass;

	@FXML
	private TextField status;

	// to initialize the necessary variable and interface components
	public void initialize()
	{
		databaseHandler = DatabaseHandler.getInstance();
	}

	void setRole(String role)
	{
		this.role = role;
	}

	void setUsername(String username)
	{
		this.username = username;
	}

	// to update the user's password
	@FXML
	void updatePasswordHandler(ActionEvent event)
	{
		String curPw = currentPass.getText().toString(),
				pw = newPass.getText().toString(),
				rePw = reNewPass.getText().toString();

		if(curPw.equals("") || pw.equals("") || rePw.equals(""))
		{
			status.setAlignment(Pos.CENTER);
			status.setText("Fill All Fields Please");
		}
		else if(!pw.equals(rePw))
		{
			status.setText("Enter the same password twice please");
			newPass.setText("");
			reNewPass.setText("");
		}
		else if(!MainController.isInFormat(pw))
		{
			status.setAlignment(Pos.CENTER_LEFT);
			status.setText("Password must have at least 9 characters [ 1 lowercase letter, 1 uppercase letter, and 1 digit are a MUST ]");
			status.setAlignment(Pos.CENTER);

			newPass.setText("");
			reNewPass.setText("");
		}
		else
		{
			ResultSet rightPass = databaseHandler.getHashedPass(role, username);

			try
			{
				rightPass.first();

				if(DigestUtils.sha384Hex(curPw).equals(rightPass.getString(1)))
				{
					databaseHandler.updatePass(DigestUtils.sha384Hex(pw), username, role);

					status.setText("Password Updated Successfully");

					currentPass.setText("");
					newPass.setText("");
					reNewPass.setText("");
				}
				else
				{
					status.setText("Wrong Current Password");

					currentPass.setText("");
					newPass.setText("");
					reNewPass.setText("");
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

	// to close the stage
	@FXML
	void closeUpdatePasswordHandler(ActionEvent event)
	{
		currentPass.getScene().getWindow().hide();
	}
}
