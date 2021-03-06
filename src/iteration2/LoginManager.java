package iteration2;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class implementation of a login manager which is used to store new user information and to validate login information.
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 2.0
 */

public class LoginManager {
	/**
	 * A string used as a salt for the hashing.
	 */
	private static final String SALT = "ilikecats";
	
	/**
	 * Verifies the login information entered by the user.
	 * @param username
	 * @param password
	 * @return true if the information is correct and false otherwise
	 * @throws IOException if the file is not found
	 */
	public static boolean verifyLogin(String username, String password) throws IOException{
		String saltedPassword = SALT + password;
		String hashedPassword = generateHash(saltedPassword);
		//  username and password are stored together to further ensure safety
		String userPass = username + hashedPassword;
		
		//  the BufferedReader class is used to go through each line.
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader("src/assets/UserDatabase.txt"));
			String line = reader.readLine();
			
			while(line != null){
				if(line.equals(userPass)){
					return true;
				}
				else
					line = reader.readLine();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(reader != null)
				reader.close();
		}
		return false;
	}
	
	/**
	 * Saves the user information into a file contained in the project
	 * @return a string showing if the registration was successful
	 */
	public static String register(String username, String password){
		String saltedPassword = SALT + password;
		String hashedPassword = generateHash(saltedPassword);
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/assets/UserDatabase.txt", true));
			writer.write(username + hashedPassword + "\n");
			writer.newLine();
			writer.close();
		}
		catch(IOException e){
			return "Failed to register.";
		}
		return "Registration successful!";
	}
	
	/**
	 * Changes the password of the user using the new password entered by the user
	 * @param username the username of the current user
	 * @param oldPassword the old password of the user used to verify
	 * @param newPassword the new password of the current user
	 * @return a string showing if the change was successful
	 */
	public static String changePassword(String username, String oldPassword, String newPassword){
		//  both passwords are combined with the username to match the database
		String oldSaltedPassword = SALT + oldPassword;
		String oldHashedPassword = generateHash(oldSaltedPassword);
		String oldUserPass = username + oldHashedPassword;
		
		String newSaltedPassword = SALT + newPassword;
		String newHashedPassword = generateHash(newSaltedPassword);
		String newUserPass = username + newHashedPassword;
		
		BufferedReader reader = null;
		
		//  the file is put into a string and the old username combined with the password are replaced
		try{
			reader = new BufferedReader(new FileReader("src/assets/UserDatabase.txt"));
			String line = reader.readLine();
			StringBuffer inputBuffer = new StringBuffer();
			while(line != null){
				if(line.isEmpty()){
					line = reader.readLine();
				}
				else{
					inputBuffer.append(line);
					inputBuffer.append("\n");
					line = reader.readLine();
				}
			}
			String wholeFileText = inputBuffer.toString();
			
			reader.close();
			
			wholeFileText = wholeFileText.replace(oldUserPass, newUserPass);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/assets/UserDatabase.txt"));
			writer.write(wholeFileText);
			writer.newLine();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
			return "Password change failed";
		}
		return "Password Changed!";
	}
	
	/**
	 * The method used to hash the password
	 * @return a hashed string using the algorithm included
	 */
	public static String generateHash(String input){
		StringBuilder hash = new StringBuilder();
		
		/*
		 * Algorithm reused from:
		 * https://veerasundar.com/blog/2010/09/storing-passwords-in-java-web-application/
		 */
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int index = 0; index < hashedBytes.length; ++index) {
				byte b = hashedBytes[index];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash.toString();
	}

	/**
	 * ACCESSORS AND MUTATORS FOR CUSTOMIZATION
	 */
	
	//	....	
}