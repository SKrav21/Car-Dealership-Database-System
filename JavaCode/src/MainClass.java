import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Exception;
import java.io.*;
import java.sql.*;
import java.lang.Math;
public class MainClass {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Console console = System.console();
		System.out.println("Enter Oracle user ID");
		String user_name = scanner.nextLine();
		System.out.println("Enter Oracle password");
		char [] pwd = console.readPassword();
		try(
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", user_name, new String (pwd));
			Statement s=con.createStatement();
		) {
			System.out.println("connection successfully made");
			mainMenu(scanner, con);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
   
	public static void mainMenu(Scanner scanner, Connection con) {
		System.out.println("Welcome to Alset Eccentric Vehicles!");
		int menuChoice = 0;
		do {
			printMainMenu();
			try {
				menuChoice = getInt(scanner, 1, 5);
			}
			catch (Exception e) {
				System.out.println("You need to choose 1, 2, 3, 4, or 5");
			}
			switch(menuChoice) {
			case 1:
				Customer.login(scanner, con);
				break;
			case 2:
				Customer.createCustomer(scanner, con);
				break;
			case 3:
				Employee.employeeMainMenu(scanner, con);
				break;
			case 4:
				browseShowrooms(scanner, con);
				break;
			default:
				System.out.println("Goodbye and thank you for choosing Alset Electric Vehicles, We hope to see you soon.");
				break;
			}
		}while(menuChoice > 0 && menuChoice < 5);
	}
	
	public static void printMainMenu() {
		System.out.println("\nSelect an operation:\r\n"
				+ "1: Customer Login\r\n"
				+ "2: Create Customer Account\r\n"
				+ "3: Employee Access\r\n"
				+ "4: Browse The Showroom\r\n"
				+ "5: Exit");
	}
	
	public static int createID(Scanner scanner, Connection con, String IDType, String table) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		int returnVal = -1;
		try {
			query = "select max (".concat(IDType).concat(") from ").concat(table);
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			if(result.next()) {
				returnVal = result.getInt(1) + (int)(Math.random() * 10) + 1;
			}
			else {
				returnVal = (int)(Math.random() * 10) + 1;
			}
		}
		catch(Exception e) {
		}
		return returnVal;
	}
	
	public static void browseShowrooms(Scanner scanner, Connection con) {
		int sID;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			sID = getServiceLocation(scanner, con);
			query = "select VIN, color, model, year, capacity, base_price, miles, sID from Vehicle where sID = ? and showroom = 1";
			statement = con.prepareStatement(query);
			statement.setInt(1, sID);
			result = statement.executeQuery();
			System.out.println("The Vehicles at the showroom of the service center you selected are listed below");
			while(result.next()) {
				int VIN = 		result.getInt(1);
				String color =  result.getString(2);
				String model =  result.getString(3);
				int year =		result.getInt(4);
				int capacity = 	result.getInt(5);
				int basePrice = result.getInt(6);
				long miles =	result.getLong(7);
				sID = 			result.getInt(8);
				System.out.println("\nColor: " 		+ color);
				System.out.println("Model: " 		+ model);
				System.out.println("Year: " 		+ year);
				System.out.println("Capacity: " 	+ capacity);
				System.out.println("Base Price: $" 	+ basePrice);
				System.out.println("Miles: " 		+ miles);
				System.out.println("VIN: " 			+ VIN);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static int getServiceLocation(Scanner scanner, Connection con) {
		int sID = 0;
		int i = 1;
		int userChoice;
		ArrayList<String> serviceLocationList = new ArrayList<String>();
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select sID, planet, environment, street from Address, Service_Location where Address.aID = Service_Location.aID";
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			System.out.println("All of the Alset Eccentric Vehicles Service Centers are listed below");
			System.out.println(String.format("%-10s%-10s%-18s%-22s", "Number", "Planet", "Environment", "Street"));
			while(result.next()) {
				System.out.println(String.format("%-10s%-10s%-18s%-22s", i, result.getString(2), result.getString(3), result.getString(4)));
				serviceLocationList.add(String.valueOf(result.getInt(1)));
				i++;
			}
			System.out.println("Enter the number of the service center whose showroom you wish to see");
			userChoice = getInt(scanner, 1, i);
			sID = Integer.parseInt(serviceLocationList.get(userChoice - 1));
		}
		catch(Exception e) {
		}
		return sID;
	}
	
	public static int getInt(Scanner scanner, int min, int max) {
		boolean flag = true;
		int input = 0;
		do {
			boolean check = scanner.hasNextInt();
			if(check) {
				input = scanner.nextInt();
				if(input >= min && input <= max) {
					flag = false;
				}
				else {
					System.out.println("Your input must be in the range of " + min + " and  " + max);
				}
			}
			else {
				System.out.println("Your input must be in numerical form");
			}
			scanner.nextLine();
		}while(flag);
		return input;
	}
	
	public static long getLong(Scanner scanner, long min, long max) {
		boolean flag = true;
		long input = 0L;
		do {
			boolean check = scanner.hasNextLong();
			if(check) {
				input = scanner.nextLong();
				if(input >= min && input <= max) {
					flag = false;
				}
				else {
					System.out.println("Your input must be in the range of " + min + " and  " + max);
				}
			}
			else {
				System.out.println("Your input must be in in numerical form");
			}
			scanner.nextLine();
		}while(flag);
		return input;
	}
}