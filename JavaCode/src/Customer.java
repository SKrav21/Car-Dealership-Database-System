import java.util.Scanner;
import java.lang.Exception;
import java.sql.*;
import java.util.ArrayList;
public class Customer {

	public static void login(Scanner scanner, Connection con) {
		String firstName;
		String lastName;
		String query;
		PreparedStatement statement;
		ResultSet result;
		int cID = 0;
		int aID;
		int payID;
		try {
			System.out.println("Enter your first name");
			firstName = scanner.nextLine();
			System.out.println("Enter your last name");
			lastName = scanner.nextLine();
			query = "select cID, aID, payID from Customer where name_first = ? and name_last = ?";
			statement = con.prepareStatement(query);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			result = statement.executeQuery();
			if(result.next()) {
				cID = result.getInt(1);
				aID = result.getInt(2);
				payID = result.getInt(3);
				System.out.println("Login Successful");
				System.out.println("Welcome " + firstName + " " + lastName);
				customerMainMenu(scanner, con, cID, aID, payID);
			}
			else {
				System.out.println("An account under the name " + firstName + " " + lastName + " was not found");
				System.out.println("Returning to main menu");
				MainClass.mainMenu(scanner, con);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static void createCustomer(Scanner scanner, Connection con) {
		int cID;
		int aID;
		int payID;
		String firstName;
		String lastName;
		String email;
		Long phone;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			System.out.println("Enter your first name");
			firstName = scanner.nextLine();
			System.out.println("Enter your last name");
			lastName = scanner.nextLine();
			System.out.println("Enter your email");
			email = scanner.nextLine();
			System.out.println("Enter your phone number(just the digits without any dashes, spaces, or slashes)");
			phone = MainClass.getLong(scanner,  1000000000L, 9999999999L);
			query = "select name_first, name_last from Customer where name_first = ? and name_last = ?";
			statement = con.prepareStatement(query);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			result = statement.executeQuery();
			if(result.next()) {
				System.out.println("An account under your name already exists");
				System.out.println("Returning to main menu");
				MainClass.mainMenu(scanner, con);
			}
			else {
				cID = MainClass.createID(scanner, con, "cID", "Customer");
				aID = createAddress(scanner, con);
				payID = createPayment(scanner, con);
				query = "insert into Customer values(?, ?, ?, ?, ?, ?, ?)";
				statement = con.prepareStatement(query);
				statement.setInt(1, cID);
				statement.setInt(2, aID);
				statement.setInt(3, payID);
				statement.setString(4, firstName);
				statement.setString(5, lastName);
				statement.setString(6, email);
				statement.setLong(7, phone);
				statement.executeUpdate();
				System.out.println("Account Creation Successful");
				System.out.println("Welcome " + firstName + " " + lastName);
				customerMainMenu(scanner, con, cID, aID, payID);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static int createAddress(Scanner scanner, Connection con) {
		int aID = -1;
		String planet;
		String environment;
		String street;
		String query;
		PreparedStatement statement;
		try {
			System.out.println("Enter your planet/moon");
			planet = scanner.nextLine();
			System.out.println("Enter your environment/biome");
			environment = scanner.nextLine();
			System.out.println("Enter your street Address");
			street = scanner.nextLine();
			aID = MainClass.createID(scanner, con, "aID", "Address");
			query = "insert into Address values (?, ?, ?, ?)";
			statement = con.prepareStatement(query);
			statement.setInt(1, aID);
			statement.setString(2, planet);
			statement.setString(3, environment);
			statement.setString(4, street);
			statement.executeUpdate();
		}
		catch(Exception e) {
		}
		return aID;
	}

	public static int createPayment(Scanner scanner, Connection con) {
		int payID = -1;
		long cardNumber;
		String date;
		int year;
		int month;
		int day;
		int security_code;
		String company;
		String query;
		PreparedStatement statement;
		try {
			System.out.println("Enter your cardNumber");
			cardNumber = MainClass.getLong(scanner, 1000000000000000L, 99999999999999999L);
			System.out.println("Enter year of the expiration date");
			year = MainClass.getInt(scanner, 2022, 3000);
			System.out.println("Enter month of the expiration date in numerical form (for example enter 12 for Decemeber)");
			month = MainClass.getInt(scanner,  1, 12);
			System.out.println("Enter day of the expiration date");
			if(month == 2) {
				day  =  MainClass.getInt(scanner,  1, 28);
			}
			else if(month == 4 || month == 6 || month == 9 || month == 11) {
				day  =  MainClass.getInt(scanner,  1, 30);
			}
			else {
				day  =  MainClass.getInt(scanner,  1, 31);
			}
			date = String.valueOf(year).concat("-").concat(String.valueOf(month)).concat("-").concat(String.valueOf(day));
			System.out.println("Enter the security code");
			security_code = MainClass.getInt(scanner, 100, 9999);
			System.out.println("Enter the company");
			company = scanner.nextLine();
			payID = MainClass.createID(scanner, con, "payID", "Payment");
			query = "insert into Payment values (?, ?, to_date(?,'yyyy-mm-dd'), ?, ?)";
			statement = con.prepareStatement(query);
			statement.setInt(1, payID);
			statement.setLong(2, cardNumber);
			statement.setString(3, date);
			statement.setInt(4, security_code);
			statement.setString(5,  company);
			statement.executeUpdate();
		}
		catch(Exception e) {
		}
		return payID;
	}
	
	public static void customerMainMenu(Scanner scanner, Connection con, int cID, int aID, int payID) {
		System.out.println("Welcome to the Alset Eccentric Vehicles Customer Menu!");
		int menuChoice = 0;
		do {
			printCustomerMainMenu();
			try {
				menuChoice = MainClass.getInt(scanner, 1, 5);
			}
			catch (Exception e) {
				System.out.println("You need to choose 1, 2, 3, 4, or 5");
			}
			switch(menuChoice) {
			case 1:
				MainClass.browseShowrooms(scanner, con);
				break;
			case 2:
				vehicleSelector(scanner, con, cID, aID, payID);
				break;
			case 3:
				customerAccountMenu(scanner, con, cID, aID, payID);
				break;
			case 4:
				int VIN = getVIN(scanner, con, cID);
				if(VIN != -1) {
					vehicleService(scanner, con, cID, aID, payID, VIN);
				}
				else {
					System.out.println("You cannot service your vehicle since you do not own a vehicle.");
				}
				break;
			default:
				System.out.println("Logging Out To Main Menu");
				break;
			}
		}while(menuChoice > 0 && menuChoice < 5);
	}
	
	public static void printCustomerMainMenu() {
		System.out.println("\nSelect an operation:\r\n"
				+ "1: Browse The Showrooms\r\n"
				+ "2: Configure a Vehicle\r\n"
				+ "3: View Account Information\r\n"
				+ "4: Vehicle Maintenance and Servicing\r\n"
				+ "5: Exit");
	}
	
	public static void vehicleSelector(Scanner scanner, Connection con, int cID, int aID, int payID) {
		char modelChar;
		String condition;
		int minMiles = 0;
		long maxMiles = 999999999999L;
		int capacity;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			do {
				System.out.println("Please Choose a model by entering its corresponding character.");
				System.out.println("To select a Moon Vehicle, enter m");
				System.out.println("To select an Undersea Vehicle, enter u");
				System.out.println("To select a Space Vehicle, enter s");
				System.out.println("To select a Karting Vehicle, enter k");
				modelChar = scanner.nextLine().charAt(0);
			}while(modelChar != 'm' && modelChar != 'u' && modelChar != 's' && modelChar != 'k');
			System.out.println("Enter 'Used' to search for used vehicles or 'New' to search for new vehicles");
			condition = scanner.nextLine();
			if(condition.equalsIgnoreCase("used")) {
				minMiles = 1;
			}
			else if(condition.equalsIgnoreCase("new")) {
				maxMiles = 0;
			}//if anything else is entered it will search for both
			do {
				System.out.println("Enter 2, 4, or 6 for the desired capacity of your vehicle");
				capacity = MainClass.getInt(scanner, 2, 6);
			}while(capacity != 2 && capacity != 4 && capacity != 6);
			query = "select VIN, color, model, year, capacity, base_price, miles, sID from Vehicle where model = ? and miles >= ? and miles <= ? and capacity = ?";
			statement = con.prepareStatement(query);
			statement.setString(1, String.valueOf(modelChar));
			statement.setInt(2, minMiles);
			statement.setLong(3, maxMiles);
			statement.setInt(4, capacity);
			result = statement.executeQuery();
			System.out.println("Vehicles in our inventory matching your specifications will be listed below");
			boolean vehicleFound = false;
			while((result.next()) && (!vehicleFound)) {
				int VIN = 		result.getInt(1);
				String color =  result.getString(2);
				String model =  result.getString(3);
				int year =		result.getInt(4);
				capacity = 		result.getInt(5);
				int basePrice = result.getInt(6);
				long miles =	result.getLong(7);
				int sID = 		result.getInt(8);
				int totalCost = basePrice;
				System.out.println("\nColor: " 		+ color);
				System.out.println("Model: " 		+ model);
				System.out.println("Year: " 		+ year);
				System.out.println("Capacity: " 	+ capacity);
				System.out.println("Base Price: $" 	+ basePrice);
				System.out.println("Miles: " 		+ miles);
				if(miles > 0) {
					totalCost = printVehicleAddOns(con, VIN, basePrice);
					System.out.println("The total cost of this vehicle is $" 		+ totalCost);
				}
				System.out.println("To select this vehicle, enter 'Yes'\nTo view more vehicles, enter 'Continue'");
				String choice = scanner.nextLine();
				if(choice.equalsIgnoreCase("yes")) {
					vehicleFound = true;
					if(miles == 0) {
						configureVehicle(scanner, con, cID, aID, payID, VIN);
						totalCost = printVehicleAddOns(con, VIN, basePrice);
						System.out.println("The total cost of this vehicle with the upgrades you installed is $" 		+ totalCost);
					}
					System.out.println("Be patient, this step might take upwards of a minute. It is a large transaction. Do not shut down the program.");
					createTransaction(scanner, con, cID, aID, payID, VIN, sID, totalCost);
					setOwnership(con, cID, VIN);
				}
			}
			if(vehicleFound) {
				System.out.println("Thank you for choosing to purchase from Alset Eccentric Vehicles");
			}
			else {
				System.out.println("Unfortunately, we do not have any more vehicles that match your specifications");
			}
		}
		catch(Exception e) {
		}
	}
	
	public static void configureVehicle(Scanner scanner, Connection con, int cID, int aID, int payID, int VIN) {
		String query;
		PreparedStatement statement;
		try {
			System.out.println("Alset Eccentric Vehicles is currently offering the following upgrades");
			int currentNumUpgrades = printAllUpgrades(con);
			System.out.println("\nAlset Eccentric Vehicles offers the following upgrade packages");
			int currentNumPackages = printAllPackages(con);
			System.out.println("\nIf you would like to add on a package, enter 'package'."
							+  "\nIf you would like to add on individual upgrades, enter 'upgrades'."
							+  "\nIf you do not wish to purchase any additional add ons, enter 'done.");
			String addOnChoice = scanner.nextLine();
			if(addOnChoice.equalsIgnoreCase("package")) {
				System.out.println("Enter the package number of the package you want to add");
				int packageNumber = MainClass.getInt(scanner, 1, currentNumPackages);
				query = "insert into Vehicle_Package values(?, ?)";
				statement = con.prepareStatement(query);
				statement.setInt(1, VIN);
				statement.setInt(2, packageNumber);
				statement.executeUpdate();
			}
			else if(addOnChoice.equalsIgnoreCase("upgrades")) {
				ArrayList<String> upgradeList = new ArrayList<String>();
				boolean addMoreUpgrades = false;
				do {
					addMoreUpgrades = false;
					System.out.println("Enter the upgrade number of the upgrade you want to add or enter 0 to cancel");
					int upgradeNumber = MainClass.getInt(scanner, 1, currentNumUpgrades);
					while(upgradeList.contains(String.valueOf(upgradeNumber))) {
						System.out.println("You have already selected that upgrade. Enter a different upgrade number or enter 0 to cancel.");
						upgradeNumber = MainClass.getInt(scanner, 1, currentNumUpgrades);
					}
					if(upgradeNumber != 0) {
						query = "insert into Vehicle_Upgrade values(?, ?)";
						statement = con.prepareStatement(query);
						statement.setInt(1, VIN);
						statement.setInt(2, upgradeNumber);
						statement.executeUpdate();
						if(upgradeList.size() <= 12 && upgradeList.size() <= currentNumUpgrades) {
							System.out.println("Enter 'more' to select more upgrades or enter 'done' to finalize your purchase.");
							if(scanner.nextLine().equalsIgnoreCase("more")) {
								addMoreUpgrades = true;
							}
						}
					}
				}while(addMoreUpgrades);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static int printAllUpgrades(Connection con) {
		int numUpgrades = 0;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select upgid, upgrade_name, price from Upgrade order by upgid asc";
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			while(result.next()) {
				numUpgrades++;
				System.out.println("Upgrade ID: " + result.getInt(1) + " Upgrade Name: " + result.getString(2) + " Upgrade Price: $" + result.getInt(3));
			}
		}
		catch(Exception e){
		}
		return numUpgrades;
	}
	
	public static int printAllPackages(Connection con) {
		int numPackages = 0;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select pkgid, package_name, price from Package order by pkgid asc";
			statement = con.prepareStatement(query);
			result = statement.executeQuery();
			while(result.next()) {
				numPackages++;
				System.out.println("\nPackage ID: " + result.getInt(1) + "\nPackage Name: " + result.getString(2) + "\nPackage Price: $" + result.getInt(3));
				printPackageContents(con, result.getInt(1), result.getInt(3));
			}
		}
		catch(Exception e){
		}
		return numPackages;
	}
	
	public static void createTransaction(Scanner scanner, Connection con, int cID, int aID, int payID, int VIN, int sID, int cost) {
		int tID;
		String query;
		PreparedStatement statement;
		try {
			tID = MainClass.createID(scanner, con, "tID", "Transaction");
			query = "insert into transaction values (?, ?, ?, ?, ?, ?, ?)";
			statement = con.prepareStatement(query);
			statement.setInt(1, tID);
			statement.setInt(2, sID);
			statement.setInt(3, payID);
			statement.setInt(4, cID);
			statement.setInt(5, VIN);
			statement.setInt(6, cost);
			statement.setDate(7, java.sql.Date.valueOf(java.time.LocalDate.now()));
			statement.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e);
			System.out.println("createTransaction");

		}
	}
	
	public static void setOwnership(Connection con, int cID, int VIN) {
		String query;
		PreparedStatement statement;
		try {
			query = "update Vehicle set cID = ? where VIN = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, cID);
			statement.setInt(2, VIN);
			statement.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e);
			System.out.println(" error during setOwnership");
		}
	}
	
	public static int printVehicleAddOns(Connection con, int VIN, int baseCost) {
		int totalCost = baseCost;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select package.pkgid, package.package_name, package.price from package, vehicle_package where vehicle_package.vin = ? and vehicle_package.pkgid = package.pkgid";
			statement = con.prepareStatement(query);
			statement.setInt(1, VIN);
			result = statement.executeQuery();
			if(result.next()) {
				System.out.println("This Vehicle has the " + result.getString(2) + " package installed.");
				System.out.println("This package costs $" + result.getInt(3));
				printPackageContents(con, result.getInt(1), result.getInt(3));
				totalCost += result.getInt(3);
			}
			else {
				query = "select upgrade.upgid, upgrade.upgrade_name, upgrade.price from upgrade, vehicle_upgrade where vehicle_upgrade.vin = ? and vehicle_upgrade.upgid = upgrade.upgid";
				statement = con.prepareStatement(query);
				statement.setInt(1, VIN);
				result = statement.executeQuery();
				if(result.next()) {
					do {
						System.out.println("This Vehicle has the " + result.getString(2) + " upgrade installed.");
						System.out.println("This upgrade costs $" + result.getInt(3));
						totalCost += result.getInt(3);
					}while(result.next());
				}
				else {
					System.out.println("This vehicle does not have any packages or upgrades installed");
				}
			}
		}
		catch(Exception e) {
		}
		return totalCost;
	}
	
	public static void printPackageContents(Connection con, int pkgID, int packageCost) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select upgrade.upgid, upgrade.upgrade_name, upgrade.price from upgrade, package_upgrade where package_upgrade.pkgid = ? and package_upgrade.upgid = upgrade.upgid";
			statement = con.prepareStatement(query);
			statement.setInt(1, pkgID);
			result = statement.executeQuery();
			System.out.println("This package contains the following upgrades");
			int costSeperate = 0;
			while(result.next()) {
				System.out.println(result.getString(2));
				costSeperate += result.getInt(3);
			}
			int discount = costSeperate - packageCost;
			System.out.println("This package costs $" + discount + " less than the sum of buying its upgrades seperately");
		}
		catch(Exception e) {
		}
	}
	
	public static void customerAccountMenu(Scanner scanner, Connection con, int cID, int aID, int payID) {
		System.out.println("Welcome to the your Alset Eccentric Vehicles Account Page");
		int menuChoice = 0;
		do {
			printCustomerAccountMenu();
			try {
				menuChoice = MainClass.getInt(scanner, 1, 4);
			}
			catch (Exception e) {
				System.out.println("You need to choose 1, 2, 3, or 4");
			}
			switch(menuChoice) {
			case 1:
				viewCustomer(scanner, con, cID);
				break;
			case 2:
				viewAddress(scanner, con, aID);
				break;
			case 3:
				viewPayment(scanner, con, payID);
				break;
			default:
				System.out.println("Returning To Customer Main Menu");
				break;
			}
		}while(menuChoice > 0 && menuChoice < 4);
	}
	
	public static void printCustomerAccountMenu() {
		System.out.println("\nSelect an operation:\r\n"
				+ "1: View your name, phone, and email\r\n"
				+ "2: View Address\r\n"
				+ "3: View Payment Info\r\n"
				+ "4: Exit");
	}
	
	public static void viewCustomer(Scanner scanner, Connection con, int cID) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select cID, name_first, name_last, email, phone from Customer where cID = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, cID);
			result = statement.executeQuery();
			if(result.next()) {
				System.out.println("Customer ID Number: " 	+ result.getInt(1));
				System.out.println("Name: " 				+ result.getString(2) + " " + result.getString(3));
				System.out.println("Email Address: " 		+ result.getString(4));
				System.out.println("Phone Number: " 		+ result.getInt(5));
			}
			else {
				System.out.println("An error occured when attempting to access your information. For security reasons, you are being logged out.");
				MainClass.mainMenu(scanner,  con);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static void viewAddress(Scanner scanner, Connection con, int aID) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select aID, planet, environment, street from Address where aID = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, aID);
			result = statement.executeQuery();
			if(result.next()) {
				System.out.println("Address ID Number: " 		+ result.getInt(1));
				System.out.println("Planet/Celestial Body: " 	+ result.getString(2));
				System.out.println("Environment: " 				+ result.getString(3));
				System.out.println("Street: " 					+ result.getString(4));

			}
			else {
				System.out.println("An error occured when attempting to access your information. For security reasons, you are being logged out.");
				MainClass.mainMenu(scanner,  con);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static void viewPayment(Scanner scanner, Connection con, int payID) {
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select payID, card_number, to_char(expiration_date, 'yyyy-mm-dd') as expirationDate, security_code, company from Payment where payID = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, payID);
			result = statement.executeQuery();
			if(result.next()) {
				System.out.println("Payment ID Number: " 	+ result.getInt(1));
				System.out.println("Credit Card Number: " 	+ result.getLong(2));
				System.out.println("Expiration Date: " 		+ result.getDate(3).toString());
				System.out.println("Security Code: " 		+ result.getInt(4));
				System.out.println("Company: " 				+ result.getString(5));
			}
			else {
				System.out.println("An error occured when attempting to access your information. For security reasons, you are being logged out.");
				MainClass.mainMenu(scanner,  con);
			}
		}
		catch(Exception e) {
		}
	}
	
	public static int getVIN(Scanner scanner, Connection con, int cID) {
		int VIN = 0;
		ArrayList<String[]> vehicles = new ArrayList<String[]>();
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select VIN, year, color, model from Vehicle, Customer where Vehicle.cID = Customer.cID and Customer.cID = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, cID);
			result = statement.executeQuery();
			while(result.next()) {
				String[] vehicleArray = new String[4];
				vehicleArray[0] = (String.valueOf(result.getInt(1)));
				vehicleArray[1] =(String.valueOf(result.getInt(2)));
				vehicleArray[2] =(result.getString(3));
				vehicleArray[3] =(result.getString(4));
				vehicles.add(vehicleArray);
			}
			int numVehicles = vehicles.size();
			if(numVehicles == 1) {
				VIN = Integer.parseInt(vehicles.get(0)[0]);
			}
			else if(numVehicles > 1) {
				int selection;
				System.out.println("You own multiple vehicles that are listed below");
				for(int i = 0; i < numVehicles; i++) {
					int j = i + 1;
					String year = vehicles.get(i)[1];
					String color = vehicles.get(i)[2];
					String model = vehicles.get(i)[3];
					System.out.println("Number " + j + " " + year + " " + color + " Model " + model);			
				}
				System.out.println("Enter the number listed above for the vehicle you wish to have serviced.");
				selection = MainClass.getInt(scanner, 1, numVehicles);
				VIN = Integer.parseInt(vehicles.get(selection - 1)[0]);
			}
		}
		catch(Exception e) {
		}
		return VIN;
	}
	
	public static void vehicleService(Scanner scanner, Connection con, int cID, int aID, int payID, int VIN) {
		int mID = MainClass.createID(scanner, con, "mID", "Maintenance");
		int numMiles = getPreviousMiles(scanner, con, VIN);
		int sID = getSID(scanner, con, VIN);
		int cost = (int)(Math.random() * 600) + 200;
		String query;
		PreparedStatement statement;
		try {
			numMiles += (int)(Math.random() * 200000000000L) + 10000000000L;
			query = "insert into transaction values (?, ?, ?, ?, ?, ?, ?)";
			statement = con.prepareStatement(query);
			statement.setInt(1, mID);
			statement.setInt(2, VIN);
			statement.setInt(3, sID);
			statement.setInt(4, payID);
			statement.setInt(5, cost);
			statement.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
			statement.setInt(7, numMiles);
			statement.executeUpdate();
		}
		catch(Exception e) {
		}		
	}
	
	public static int getPreviousMiles(Scanner scanner, Connection con, int VIN) {
		int maxMiles = 0;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select miles from Vehicle where VIN = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, VIN);
			result = statement.executeQuery();
			if(result.next()) {
				maxMiles = result.getInt(1);
			}
		}
		catch(Exception e) {
		}
		return maxMiles;
	}
	
	public static int getSID(Scanner scanner, Connection con, int VIN) {
		int sID = 0;
		String query;
		PreparedStatement statement;
		ResultSet result;
		try {
			query = "select sID from Vehicle where VIN = ?";
			statement = con.prepareStatement(query);
			statement.setInt(1, VIN);
			result = statement.executeQuery();
			if(result.next()) {
				sID = result.getInt(1);
			}
		}
		catch(Exception e) {
		}
		return sID;
	}
}