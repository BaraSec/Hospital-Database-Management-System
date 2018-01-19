package DatabaseAPI;

import org.apache.commons.codec.digest.DigestUtils;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public final class DatabaseHandler
{
	private static DatabaseHandler handler = null;
	private static Connection connection = null;
	private static Statement stmt = null;
	private static PreparedStatement pStmt = null;
	private static final String DB_URL = "jdbc:mysql://localhost/hdbms";
	private static final String username = "";
	private static final String password = "";

	private DatabaseHandler()
	{
		createConnection();
	}

	// to get an instance
	public static DatabaseHandler getInstance()
	{
		if(handler == null)
			handler = new DatabaseHandler();
		return handler;
	}

	// to create a connection
	private void createConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DB_URL, username, password);
		}
		catch (Exception e)
		{
			System.out.println("MySql DB Connection Error. 'createConnection()'.");
			System.exit(0);
		}
	}

	// to execute a specific query
	public ResultSet execQuery(String query)
	{
		ResultSet result;
		try
		{
			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
		}
		catch (SQLException ex)
		{
			System.out.println("Exception at 'execQuery()':dataHandler :" + ex.getLocalizedMessage());
			return null;
		}
		finally
		{
		}

		return result;
	}

	// to get a user's type/role
	public int getUser(String user, String pw) throws SQLException
	{
		pStmt = connection.prepareStatement("select COUNT(*) from managers where username = ? and hashedPass = ? and is_deleted = 0");
		pStmt.setString(1, user);
		pStmt.setString(2, pw);
		ResultSet managersAcc = pStmt.executeQuery();
		managersAcc.first();

		pStmt = connection.prepareStatement("select COUNT(*) from doctors where username = ? and hashedPass = ? and is_deleted = 0");
		pStmt.setString(1, user);
		pStmt.setString(2, pw);
		ResultSet doctorsAcc = pStmt.executeQuery();
		doctorsAcc.first();

		pStmt = connection.prepareStatement("select COUNT(*) from secretaries where username = ? and hashedPass = ? and is_deleted = 0");
		pStmt.setString(1, user);
		pStmt.setString(2, pw);
		ResultSet secretariesAcc = pStmt.executeQuery();
		secretariesAcc.first();

		if(managersAcc.getInt(1) == 1)
			return 1;
		else if(doctorsAcc.getInt(1) == 1)
			return 2;
		else if(secretariesAcc.getInt(1) == 1)
			return 3;
		else
			return 0;
	}

	// to get the hashe password of a user (using Username)
	public ResultSet getHashedPass(String role, String username)
	{
		try
		{
			pStmt = connection.prepareStatement("select hashedPass from " + role + " where username = ? and is_deleted = 0");
			pStmt.setString(1, username);
			return pStmt.executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// to get a user's eID
	public int getUserID(String role, String username)
	{
		String idName = role.substring(0, 3) + "ID";
		try
		{
			pStmt = connection.prepareStatement("select " + idName + " from " + role + " where username = ? and is_deleted = 0");
			pStmt.setString(1, username);
			ResultSet result = pStmt.executeQuery();
			result.first();

			return result.getInt(1);
		}
		catch (SQLException e)
		{
			System.out.println("Empty ResultSet at getUserID() ,, handled successfully.");
			return -1;
		}
	}

	// to get an account's info using Username
	public ResultSet getAccInfo(String role, String username)
	{
		try
		{
			pStmt = connection.prepareStatement("select * from " + role + " where username = ? and is_deleted = 0");
			pStmt.setString(1, username);

			return pStmt.executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// to get a city's ID by its name
	public int getCityID(String cityName)
	{
		ResultSet res = execQuery("select cID from cities where name = \"" + cityName + "\"");

		try
		{
			res.first();
			return res.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	// to get employee's full info using eID
	public ResultSet getEmployeeFullInfo(String ID, String role)
	{
		if(!role.equals("Secretary"))
			return execQuery("select * from " + role + "s where " + role.substring(0, 3) + "ID = " + ID + " and is_deleted = 0");
		else
			return execQuery("select * from secretaries where " + role.substring(0, 3) + "ID = " + ID + " and is_deleted = 0");
	}

	// to get employee's hashed password using eID
	public String getEmployeeHashedPass(String ID, String role)
	{
		ResultSet result = null;

		if(!role.equals("Secretary"))
			result = execQuery("select hashedPass from " + role + "s where " + role.substring(0, 3) + "ID = " + ID);
		else
			result = execQuery("select hashedPass from secretaries where " + role.substring(0, 3) + "ID = " + ID);

		try
		{
			result.first();
			return result.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// to get an employee's username
	public String getEmployeeUsername(String ID, String role)
	{
		ResultSet result = null;

		if(!role.equals("Secretary"))
			result = execQuery("select username from " + role + "s where " + role.substring(0, 3) + "ID = " + ID);
		else
			result = execQuery("select username from secretaries where " + role.substring(0, 3) + "ID = " + ID);

		try
		{
			result.first();
			return result.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// to get the number of employees
	public int[] getNofEmployees()
	{
		int[] recordsN = new int[4];
		recordsN[3] = recordsN[2] = recordsN[1] = recordsN[0] = 0;

		try
		{
			ResultSet mans = execQuery("select manID from managers where is_deleted = 0");
			mans.last();
			recordsN[0] = mans.getRow();
			recordsN[3] += recordsN[0];

			ResultSet docs = execQuery("select docID from doctors where is_deleted = 0");
			docs.last();
			recordsN[1] = docs.getRow();
			recordsN[3] += recordsN[1];

			ResultSet secrs = execQuery("select secID from secretaries where is_deleted = 0");
			secrs.last();
			recordsN[2] = secrs.getRow();
			recordsN[3] += recordsN[2];
		}

		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return recordsN;
	}

	// to get the number of patients
	public int getNofPatients()
	{
		try
		{
			ResultSet mans = execQuery("select patID from patients");
			mans.last();

			return mans.getRow();
		}

		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	// to add a prescription
	public void addPrescription(String docID, String patID, String date, String time, String description, String totalCost)
	{
		try
		{
			pStmt = connection.prepareStatement("insert into prescriptions values (null, ?, ?, ?, ?, ?, ?)");
			pStmt.setString(1, docID);
			pStmt.setString(2, patID);
			pStmt.setString(3, date);
			pStmt.setString(4, time);
			pStmt.setString(5, description);
			pStmt.setString(6, totalCost);
			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to add an appointment
	public void addAppointment(String docID, String patID, LocalDate date, LocalTime time)
	{
		try
		{
			pStmt = connection.prepareStatement("insert into appointments values (null, ?, ?, ?, ?)");
			pStmt.setString(1, docID);
			pStmt.setString(2, patID);
			pStmt.setString(3, date.toString());
			pStmt.setString(4, time.toString());
			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to add an address
	public int addAddress(String street, String building, int cityID)
	{
		try
		{
			pStmt = connection.prepareStatement("select adrID from addresses where cID = ? and street = ? and building = ?");
			pStmt.setInt(1, cityID);
			pStmt.setString(2, street);
			pStmt.setString(3, building);
			ResultSet initAddr = pStmt.executeQuery();

			if(initAddr.isBeforeFirst())
			{
				initAddr.first();
				return initAddr.getInt(1);
			}

			pStmt = connection.prepareStatement("insert into addresses values (null, ?, ?, ?)");
			pStmt.setInt(1, cityID);
			pStmt.setString(2, street);
			pStmt.setString(3, building);
			pStmt.executeUpdate();

			ResultSet lastAddressID = execQuery("SELECT adrID FROM addresses ORDER BY adrID DESC LIMIT 1");
			lastAddressID.first();

			return lastAddressID.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	// to add a patient
	public void addPatient(int adrID, String name, String gender, String sim, LocalDate dob, String ID)
	{
		try
		{
			pStmt = connection.prepareStatement("insert into patients values (null, ?, ?, ?, ?, ?, ?)");
			pStmt.setInt(1, adrID);
			pStmt.setString(2, name);
			pStmt.setString(3, gender);
			pStmt.setString(4, sim);
			pStmt.setString(5, dob.toString());
			pStmt.setString(6, ID);

			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to add an employee
	public void addEmployee(int adrID, String name, String gender, String salary, String sim, LocalDate dob, String username, String hashedPass, String ID, String role)
	{
		try
		{
			if(!role.equals("Secretary"))
				pStmt = connection.prepareStatement("insert into " + role + "s values (null, ?, ?, ?, ?, ?, ?, ?, ? , ?, 0)");
			else
				pStmt = connection.prepareStatement("insert into secretaries values (null, ?, ?, ?, ?, ?, ?, ?, ? , ?, 0)");

			pStmt.setInt(1, adrID);
			pStmt.setString(2, name);
			pStmt.setString(3, gender);
			pStmt.setString(4, salary);
			pStmt.setString(5, sim);
			pStmt.setString(6, dob.toString());
			pStmt.setString(7, username);
			pStmt.setString(8, hashedPass);
			pStmt.setString(9, ID);

			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to delete an employee
	public void deleteEmployee(String role, String ID)
	{
		try
		{
			if(role.equals("Secretary"))
				pStmt = connection.prepareStatement("update secretaries set is_deleted = 1 where secID = ?");

			else if(role.equals("Doctor"))
				pStmt = connection.prepareStatement("update doctors set is_deleted = 1 where docID = ?");
			else
				pStmt = connection.prepareStatement("update managers set is_deleted = 1 where manID = ?");

			pStmt.setString(1, ID);

			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to update an employee's info
	public void updateEmployeeInfo(int adrID, String name, String salary, String sim, LocalDate dob, String username, String password, String ID, String role, String eID)
	{
		try
		{
			if(role.equals("Manager"))
				pStmt = connection.prepareStatement("update managers set adrID = ?, name = ?, salary = ?, phoneNum = ?, dateOfBirth = ?, username = ?, hashedPass = ?, ID = ? where manID = ?");
			else if(role.equals("Secretary"))
				pStmt = connection.prepareStatement("update secretaries set adrID = ?, name = ?, salary = ?, phoneNum = ?, dateOfBirth = ?, username = ?, hashedPass = ?, ID = ? where secID = ?");
			else
				pStmt = connection.prepareStatement("update doctors set adrID = ?, name = ?, salary = ?, phoneNum = ?, dateOfBirth = ?, username = ?, hashedPass = ?, ID = ? where docID = ?");


			pStmt.setInt(1, adrID);
			pStmt.setString(2, name);
			pStmt.setString(3, salary);
			pStmt.setString(4, sim);
			pStmt.setString(5, dob.toString());
			pStmt.setString(6, username);
			if(password.equals(""))
				pStmt.setString(7, getEmployeeHashedPass(eID, role));
			else
				pStmt.setString(7, DigestUtils.sha384Hex(password));
			pStmt.setString(8, ID);
			pStmt.setString(9, eID);

			pStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// to update a user's password
	public void updatePass(String newPass, String username, String role) throws SQLException
	{
		pStmt = connection.prepareStatement("update " + role + " set hashedPass = ? where username = ? and is_deleted = 0");
		pStmt.setString(1, newPass);
		pStmt.setString(2, username);
		pStmt.executeUpdate();
	}

	// to check if a patient's patID is found or not
	public boolean isPatientFound(String ID)
	{
		ResultSet isFound = execQuery("select COUNT(*) from patients where patID = " + ID);
		try
		{
			isFound.first();
			return isFound.getInt(1) == 1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	// to check if there is an appointment in the specified time and date
	public boolean isTimeTaken(String docID, LocalDate date, LocalTime time)
	{
		try
		{
			pStmt = connection.prepareStatement("select time from appointments where date = ? and docID = " + docID);
			pStmt.setString(1, date.toString());

			ResultSet times = pStmt.executeQuery();

			while(times.next())
				if(times.getString(1).substring(0, 4).equals(time.toString().substring(0, 4)))
					return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return true;
		}

		return false;
	}

	// to check if the specified date's day is a Friday
	public boolean isFriday(LocalDate date)
	{
		SimpleDateFormat dft = new SimpleDateFormat("EEEE");

		return dft.format(java.sql.Date.valueOf(date)).equals("Friday");
	}

	// to check if a username is used or not
	public boolean isUsernameFound(String username)
	{
		try
		{
			pStmt = connection.prepareStatement("select COUNT(*) from doctors where username = ? and is_deleted = 0");
			pStmt.setString(1, username);
			ResultSet docs = pStmt.executeQuery();
			docs.first();
			if(docs.getInt(1) > 0)
				return true;

			pStmt = connection.prepareStatement("select COUNT(*) from managers where username = ? and is_deleted = 0");
			pStmt.setString(1, username);
			ResultSet mans = pStmt.executeQuery();
			mans.first();
			if(mans.getInt(1) > 0)
				return true;

			pStmt = connection.prepareStatement("select COUNT(*) from secretaries where username = ? and is_deleted = 0");
			pStmt.setString(1, username);
			ResultSet recrs = pStmt.executeQuery();
			recrs.first();
			if(recrs.getInt(1) > 0)
				return true;

			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
