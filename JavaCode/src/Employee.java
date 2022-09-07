import java.util.Scanner;
import java.lang.Exception;
import java.sql.*;
public class Employee {

	public static void employeeMainMenu(Scanner scanner, Connection con) {
		System.out.println("Welcome to the Alset Eccentric Vehicles Customer Menu!");
		int menuChoice = 0;
		do {
			printEmployeeMainMenu();
			try {
				menuChoice = MainClass.getInt(scanner, 1, 5);
			}
			catch (Exception e) {
				System.out.println("You need to choose 1, 2, 3, or 4");
			}
			switch(menuChoice) {
			case 1:
				MainClass.browseShowrooms(scanner, con);
				break;
			case 2:
				scheduleRepair(scanner, con);
				break;
			case 3:
				issueRecall(scanner, con);
				break;
			default:
				System.out.println("Logging Out To Main Menu");
				break;
			}
		}while(menuChoice > 0 && menuChoice < 4);
	}

	public static void printEmployeeMainMenu() {
		System.out.println("\nSelect an operation:\r\n"
				+ "1: Browse The Showrooms\r\n"
				+ "2: Contact a Customer to Schedule a Repair\r\n"
				+ "3: Get Customer Contact Information to Issue a Recall\r\n"
				+ "4: Exit");
	}
	
	public static void scheduleRepair(Scanner scanner, Connection con) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select VIN, miles, name_last, email, phone from Vehicle, Customer where Vehicle.cID = Customer.cID and miles > 950000000000";
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			System.out.println("The information of the customers who will be contacted to come in for repairs is listed below.");
			System.out.println(String.format("%-8s%-14s%-12s%-30s%-16s", "VIN", "Miles", "Last Name", "Email", "Phone"));
			while(result.next()) {
				System.out.println(String.format("%-8d%-14d%-12s%-30s%-16d", result.getInt(1), result.getLong(2), result.getString(3), result.getString(4), result.getLong(5)));
			}
		}
		catch(Exception e) {
		}
	}
	
	public static void issueRecall(Scanner scanner, Connection con) {
		char modelChar;
		int year;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			do {
				System.out.println("Please Choose a model of vehicles that need to be recalled by entering its corresponding character.");
				System.out.println("To select a Moon Vehicle, enter m");
				System.out.println("To select an Undersea Vehicle, enter u");
				System.out.println("To select a Space Vehicle, enter s");
				System.out.println("To select a Karting Vehicle, enter k");
				modelChar = scanner.nextLine().charAt(0);
			}while(modelChar != 'm' && modelChar != 'u' && modelChar != 's' && modelChar != 'k');
			System.out.println("Enter the year of the vehicles that need to be repaired");
			year = MainClass.getInt(scanner,  2004, 2020);
			query = "select VIN, year, name_last, email, phone from Vehicle, Customer where Vehicle.cID = Customer.cID and model = ? and year = ?";
			statement = con.prepareStatement(query);
			statement.setString(1, String.valueOf(modelChar));
			statement.setInt(2, year);
			result = statement.executeQuery();
			System.out.println("The information of the customers who own vehicles that need to be recalled is listed below");
			System.out.println(String.format("%-8s%-6s%-6s%-12s%-30s%-16s", "VIN", "Model", "Year", "Last Name", "Email", "Phone"));
			while(result.next()) {
				System.out.println(String.format("%-8d%-6c%-6d%-12s%-30s%-16d", result.getInt(1), modelChar, result.getInt(2), result.getString(3), result.getString(4), result.getLong(5)));
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
