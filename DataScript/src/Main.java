import org.apache.commons.codec.digest.DigestUtils;
import java.sql.Date;
import java.sql.Timestamp;

public class Main
{
	public static void main(String[] args)
	{
		DatabaseHandler databaseHandler = DatabaseHandler.getInstance();


		String[] cities = {"Ramallah", "Jenin", "Gaza", "Hebron", "Al Bireh", "Beitunia", "Bethlehem", "Jericho", "Nablus", "Qalqilya", "Rafah", "Tulkarm", "Tubas", "Rafah"};

		for(String city : cities)
			databaseHandler.execUpdate("insert into cities values (null, \"" + city + "\")");

		for (int i = 0; i < 92; i++)
			databaseHandler.execUpdate("insert into addresses values (null, " + (int )(Math.random() * 14 + 1) + ", " + "\"Street " + (int )(Math.random() * 99 + 1) + "\", " + "\"Building " + (int )(Math.random() * 999 + 1) + "\")");

		/////////////////////

		String[] manNames = {"Salem", "Lamar", "Bob"};
		String[] manGender = {"Male", "Female", "Male"};
		String[] manDOB = {"1970-06-02", "1979-04-26", "1985-12-03"};
		String[] manUsername = {"manSalem", "lamar1979", "bobino"};
		String[] manHashes = {DigestUtils.sha384Hex("Salem1970="), DigestUtils.sha384Hex("bestIsLamar11"), DigestUtils.sha384Hex("bobManager0312")};

		for(int i = 0; i < 3; i++)
			databaseHandler.execUpdate("insert into managers values (null, " + (int )(Math.random() * 92 + 1) + ", \"" + manNames[i] + "\", \"" + manGender[i] + "\", " +
					(Math.random() * 90000 + 70000) + ", " + (long )(Math.random() * 99999998 + 11111112) + ", \"" + manDOB[i] + "\", \"" + manUsername[i] + "\", \"" + manHashes[i] + "\", " +
					(long )(Math.random() * 999999998 + 111111112) + ", " + 0 + " )");

		/////////////////////

		String[] docNames = {"Ahmad", "Rana", "Alex", "Sama"};
		String[] docGender = {"Male", "Female", "Male", "Female"};
		String[] docDOB = {"1972-02-18", "1989-12-26", "1983-09-03", "1990-01-17"};
		String[] docUsername = {"ahmad1972", "rana1989", "alex1983", "sama1990"};
		String[] docHashes = {DigestUtils.sha384Hex("Ahmad1970="), DigestUtils.sha384Hex("rana1989R="), DigestUtils.sha384Hex("alex1983A="), DigestUtils.sha384Hex("sama1990S=")};

		for(int i = 0; i < 4; i++)
			databaseHandler.execUpdate("insert into doctors values (null, " + (int )(Math.random() * 92 + 1) + ", \"" + docNames[i] + "\", \"" + docGender[i] + "\", " +
					(Math.random() * 90000 + 70000) + ", " + (long )(Math.random() * 99999998 + 11111112) + ", \"" + docDOB[i] + "\", \"" + docUsername[i] + "\", \"" + docHashes[i] + "\", " +
					(long )(Math.random() * 999999998 + 111111112) + ", " + 0 + " )");

		/////////////////////

		String[] secNames = {"Sari", "Luna"};
		String[] secGender = {"Male", "Female"};
		String[] secDOB = {"1991-06-02", "1992-04-26"};
		String[] secUsername = {"secSari", "luna1992"};
		String[] secHashes = {DigestUtils.sha384Hex("Sari1991+"), DigestUtils.sha384Hex("Luna1992+")};

		for(int i = 0; i < 2; i++)
			databaseHandler.execUpdate("insert into secretaries values (null, " + (int )(Math.random() * 92 + 1) + ", \"" + secNames[i] + "\", \"" + secGender[i] + "\", " +
					(Math.random() * 90000 + 70000) + ", " + (long )(Math.random() * 99999998 + 11111112) + ", \"" + secDOB[i] + "\", \"" + secUsername[i] + "\", \"" + secHashes[i] + "\", " +
					(long )(Math.random() * 999999998 + 111111112) + ", " + 0 + " )");

		/////////////////////

		String[] patGender = {"Male", "Female", "Male", "Female", "Male", "Female"};
		String[] patDOB = {"1991-06-02", "1992-04-26", "1980-04-26", "1950-12-16", "2012-05-23", "2002-10-02"};

		for(int i = 1; i <= 6; i++)
			databaseHandler.execUpdate("insert into patients values (null, " + (int )(Math.random() * 92 + 1) + ", \"Patient" + i + "\", \"" + patGender[i-1] + "\", " +
					(int )(Math.random() * 9000000 + 7000000) + ", \"" + patDOB[i-1] + "\", " + (long )(Math.random() * 999999998 + 111111112) + " )");

		/////////////////////

		long beginTime = Timestamp.valueOf("2017-01-01 09:00:00").getTime();
		long endTime = Timestamp.valueOf("2018-01-05 19:00:00").getTime();
		long diff = endTime - beginTime + 1;

		for(int i = 0; i < 51; i++)
			databaseHandler.execUpdate("insert into appointments values (null, " + (int )(Math.random() * 4 + 1) + ", " + (int )(Math.random() * 6 + 1) + ", \"" +
					new Date(beginTime + (long) (Math.random() * diff)).toString() + "\", \"" + (int )(Math.random() * 10 + 9) + ":" + (int )(Math.random() * 60 + 0) + "\")");

		/////////////////////

		long beginTime2 = Timestamp.valueOf("2017-01-01 09:00:00").getTime();
		long endTime2 = Timestamp.valueOf("2018-01-05 19:00:00").getTime();
		long diff2 = endTime2 - beginTime2 + 1;

		for(int i = 0; i < 32; i++)
			databaseHandler.execUpdate("insert into prescriptions values (null, " + (int )(Math.random() * 4 + 1) + ", " + (int )(Math.random() * 6 + 1) + ", \"" +
					new Date(beginTime2 + (long) (Math.random() * diff2)).toString() + "\", \"" + (int )(Math.random() * 10 + 9) + ":" + (int )(Math.random() * 60 + 0) + "\", \""
					+ "medicines, etc.." + "\", " + (int )(Math.random() * 300 + 50) + ")");

	}
}