import java.sql.*;

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

	public static DatabaseHandler getInstance()
	{
		if(handler == null)
			handler = new DatabaseHandler();
		return handler;
	}

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

	public int execUpdate(String query)
	{
		int result;
		try
		{
			stmt = connection.createStatement();
			result = stmt.executeUpdate(query);
		}
		catch (SQLException ex)
		{
			System.out.println("Exception at 'execUpdate()':dataHandler :" + ex.getLocalizedMessage());
			return -1;
		}
		finally
		{
		}
		return result;
	}
}