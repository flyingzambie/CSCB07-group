package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DataNotFoundException;
import com.b07.exceptions.DatabaseContainsInvalidDataException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.UserNotFoundException;
import com.b07.users.Roles;
import com.b07.users.User;

/**
 * A series of helper methods for use by the store application
 * 
 * @author Alex Efimov
 */

public class StoreHelpers {



  /**
   * check whether password matches a user's password
   * 
   * @param role the user's role
   * @param id the user's id
   * @param password the password
   * @return whether the login was successful
   * @throws DatabaseContainsInvalidDataException
   * @throws SQLException
   * @throws DatabaseInsertException
   * @throws DataNotFoundException
   * @throws UserNotFoundException
   */
  public static final User login(Roles role, int id, String password)
      throws SQLException, DatabaseInsertException {
    User user = DatabaseSelectHelper.getUserDetails(id);
    if (user == null) {
      return null;
    }
    int roleId = user.getRoleId();
    // if (!Roles.valueOf(DatabaseSelectHelper.getRoleName(roleId)).equals(role)) {
    // throw new DatabaseInsertException();
    // }
    if (user.authenticate(password)) {
      return user;
    }
    return null;
  }

  /**
   * Creates a prompt for a user to login with their ID and password, to the given role.
   * 
   * @param reader to read the input
   * @param role: the role to log in as
   * @return the User if login is succesful, null otherwise
   * @throws IOException
   * @throws SQLException
   */
  public static final User loginPrompt(BufferedReader reader, Roles role)
      throws IOException, SQLException {
    System.out.println("Input your ID");
    int id;
    String password;
    try {
      id = Integer.parseInt(reader.readLine());
      System.out.println("Input your password");
      password = reader.readLine();
      User user = login(role, id, password);
      if (user == null) {
        System.out.println("Incorrect password");
        return null;
      }
      return user;
    } catch (NumberFormatException e) {
      System.out.println("Unable to read ID. Please input only the number.");
    } catch (DatabaseInsertException e) {
      System.out.println("The specified role was not found for the user with the given ID.");
    }
    return null;
  }

  /**
   * This method presents the user with a list of choices and returns the user's choice as an int.
   * Prints an error message if the user did not make a valid choice.
   * @param choices an array of choices as strings that will be printed for the user
   * @param reader a reader to get the user's input from
   * @return the user's choice, -1 if the user did not make a valid choice
   */
  public static int choiceDialog(String[] choices, BufferedReader reader) {
    for(String string: choices) {
      System.out.println(string);
    }
    int response = -1;
    try {
      response = Integer.parseInt(reader.readLine());
    } catch (NumberFormatException e) {
      System.out.println("Please choose a number.")
    }
    return response;
  }

}
