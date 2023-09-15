package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * The AccountService class provides high-level methods for managing user accounts.
 * It interacts with the AccountDAO to perform account-related operations.
 */
public class AccountService {

    AccountDAO accountDAO;

    /**
     * Constructs a new AccountService and initializes the associated AccountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Adds a new user account to the system.
     *
     * @param account The Account object representing the user to be added.
     * @return The created Account object, or null if the account data is invalid.
     */
    public Account addUser(Account account) {
        Account createdAccount = accountDAO.addAccount(account);
        
        // Check if the password meets length requirements, username is not empty, and account with ID 0 doesn't exist
        if (account.getPassword().length() > 4 && !account.getUsername().isEmpty()
            && accountDAO.getAccountById(0) == null) {
            return createdAccount;
        }
        return null;
    }

    /**
     * Retrieves an account by its unique account_id from the database.
     *
     * @param account_id The unique identifier of the account to retrieve.
     * @return The Account object with the specified account_id, or null if not found.
     */
    public Account getAccount(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

    /**
     * Verifies user credentials by checking if the provided username and password match an existing account.
     *
     * @param account The Account object containing the username and password to verify.
     * @return The Account object with account_id set if the credentials are valid, or null if verification fails.
     */
    public Account verify(Account account) {
        return accountDAO.verifyUserCredentials(account);
    }
}

