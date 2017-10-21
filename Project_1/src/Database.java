/**
	Database
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Database
{
	private ArrayList<Item> inventory;
	private ArrayList<User> accounts;

	/**
	 * Constructor
	 * @throws FileNotFoundException 
	 */
	public Database() throws FileNotFoundException
	{
		inventory = new ArrayList<Item>();
		accounts = new ArrayList<User>();
		
		parseInventoryFromFile();
		parseUserAccountsFromFile();
	}
	
	/**
	 * The parseInventoryFromFile method opens up the Uberstock Inventory
	 * file and parses the items into an ArrayList of Items.
	 * @throws FileNotFoundException 
	 */
	public void parseInventoryFromFile() throws FileNotFoundException
	{
		// open the file to read Items into the array
		String fileName;
		File file;
		Scanner inputFile;
		fileName = "src/DB/Inventory.txt";
		file = new File(fileName);
		inputFile = new Scanner(file);
		
		// variables needed to read in Items from the file
		int i = 0; // incrementor
		String input, itemType, itemName;
		double price;
		int quantity;
		
		// read the items from the file into the inventory array
		while(inputFile.hasNext())
		{
			// tokenize the lines from the file and
			// create an array of Items
			String[] tokens;
			input = inputFile.nextLine();
			tokens = input.split(",");
			itemType = tokens[0];
			itemName = tokens[1];
			price = Double.parseDouble(tokens[2]);
			quantity = Integer.parseInt(tokens[3]);
			
			inventory.add(i, new Item(itemType, itemName, price, quantity));
			i++;
		}
		
		// close the file
		inputFile.close();
	}
	
	/**
	 * The writeCurrentInventory method creates a PrintWriter output file object
	 * and opens the Inventory.txt file for overwriting.
	 * writeCurrentInventory steps through the ArrayList of Items,
	 * creating String objects formatted to the desired output and saves them to the
	 * file.
	 * @param currentInventory The ArrayList of Items to overwrite the Inventory file with.
	 * @throws FileNotFoundException
	 */
	public void writeCurrentInventory(ArrayList<Item> currentInventory) throws FileNotFoundException
	{
		PrintWriter outputFile = new PrintWriter("src/DB/Inventory.txt");
		String outputLine;
		
		for (Item itm : inventory)
		{
			outputLine = "";
			outputLine += itm.getItemType() + ","
					+ itm.getItemName() + ","
					+ itm.getItemPrice() + ","
					+ itm.getItemQuantity();
			
			outputFile.println(outputLine);
		}
		
		outputFile.close();
	}
	
	/**
	 * The copyInventory method copies an ArrayList of Items.
	 * @return A copy of an ArrayList of Items.
	 */
	public ArrayList<Item> copyInventory()
	{
		ArrayList<Item> copy = new ArrayList<Item>();
		
		for (int i = 0; i < inventory.size(); i++)
		{
			copy.add(inventory.get(i));
		}
		
		return copy;
	}
	
	/**
	 * The parseUserAccountsFromFile method opens up the Uberstock UserAccounts
	 * file and parses the accounts into an ArrayList of UserAccounts.
	 * @throws FileNotFoundException 
	 */
	public void parseUserAccountsFromFile() throws FileNotFoundException
	{
		// open the file to read UserAccounts into the array
		String fileName;
		File file;
		Scanner inputFile;
		fileName = "src/DB/UserAccounts.txt";
		file = new File(fileName);
		inputFile = new Scanner(file);

		// variables needed to read in UserAccounts into the array
		int i = 0;
		String input, firstName, lastName, email, password, rewards;
		char middleInitial;
		String type;
		double reward = 0;
		
		while(inputFile.hasNext())
		{
			// tokenize the lines from the file and
			// create an array of UserAccounts
			String[] tokens;
			input = inputFile.nextLine();
			tokens = input.split(",");
			firstName = tokens[0];
			middleInitial = tokens[1].charAt(0);
			lastName = tokens[2];
			email = tokens[3];
			password = tokens[4];
			type = tokens[5];
			rewards = tokens[6];
			
			if(!rewards.equals("null"))
				reward = Double.parseDouble(rewards);
			
			if(type.equals("ADMIN"))
				accounts.add(i, new Admin(firstName, middleInitial, lastName, email, password));
			else if(type.equals("STANDARD"))
				accounts.add(i, new Standard(firstName, middleInitial, lastName, email, password));
			else if(type.equals("MEMBER"))
				accounts.add(i, new Member(firstName, middleInitial, lastName, email, password, reward));
			else
				System.out.println("ERROR: parseUserAccountsFromFile()");
			
			i++;
		}
		
		// close the file
		inputFile.close();
	}
	
	/**
	 * The accountExistsAt method checks the ArrayList of UserAccounts
	 * and determines if the email and password parameters match a
	 * UserAccount's email and password.
	 * @param email The email to be checked.
	 * @param password The password to be checked.
	 * @return The index of the account in the ArrayList if it exists;
	 * 			otherwise, -1.
	 */
	public int accountExistsAt(String email, String password)
	{
		for (int i = 0; i < accounts.size(); i++)
		{
			if (email.equalsIgnoreCase(accounts.get(i).getEmail())
					&& password.equals(accounts.get(i).getPassword()))
				return i;
		}
		return -1;
	}
	
	/**
	 * The emailExists method checks if the email parameter matches an
	 * email in the ArrayList of UserAccounts.
	 * @param email The email to be checked for its existence.
	 * @return True if found; false otherwise.
	 */
	public boolean emailExists(String email)
	{
		boolean status = false;
		
		for (User anAccount : accounts)
			if (anAccount.getEmail().equalsIgnoreCase(email))
				status = true;

		return status;
	}
	
	/**
	 * The getUserAccountAt accepts an index of the UserAccount
	 * to return from the ArrayList of UserAccounts.
	 * @param index The UserAccount location in the ArrayList of User Accounts.
	 * @return A reference to a UserAccount.
	 */
	public User getUserAccountAt(int index)
	{	
		User myAccount = accounts.get(index);
		
		return myAccount;
	}
	
	/**
	 * The addUserAccount method adds a new user account
	 * to the ArrayList of UserAccounts.
	 * @param newUser The UserAccount to be added to the
	 * 			accounts ArrayList.
	 */
	public void addUserAccount(User newUser)
	{
		accounts.add(newUser);
		
		try
		{
			writeCurrentUserAccounts(); // Write current user accounts when new user is added.
		}
		catch(FileNotFoundException e)
		{
			System.out.println();
			System.out.println(e);
			System.out.println();
		}
	}
	
	/**
	 * The writeCurrentUserAccounts method creates a PrintWriter output file object
	 * and opens the UserAccounts.txt file for overwriting.
	 * writeCurrentUserAccounts steps through the ArrayList of USerAccounts,
	 * creating String objects formatted to the desired output and saves them to the
	 * file.
	 * @throws FileNotFoundException
	 */
	public void writeCurrentUserAccounts() throws FileNotFoundException
	{
		PrintWriter outputFile = new PrintWriter("src/DB/UserAccounts.txt");
		String outputLine;
		
		for (User anAccount : accounts)
		{
			outputLine = "";
			outputLine += anAccount.getFirstName() + "," 
					+ anAccount.getMiddleInitial() + ","
					+ anAccount.getLastName() + ","
					+ anAccount.getEmail() + ","
					+ anAccount.getPassword() + ","
					+ anAccount.getType();
			
			// Check acct type for rewards.
			if(anAccount.getType().equals("MEMBER") && anAccount instanceof Member)
				outputLine += "," + anAccount.getReward();
			else
				outputLine += ",null";
			
			outputFile.println(outputLine);
		}
		
		outputFile.close();
	}

	public void displayInventory()
	{
		int i = 0;
		for (i = 0; i < inventory.size(); i++)
		{
			if (inventory.get(i) != null)
			{
				System.out.println(inventory.get(i));
				System.out.println();
			}
		}
	}
	
	public void displayAccounts()
	{
		int i = 0;
		for (i = 0; i < accounts.size(); i++)
		{
			if (accounts.get(i) != null)
			{
				System.out.println(accounts.get(i));
				System.out.println();
			}
		}
	}
}
