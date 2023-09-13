package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        socialMediaDAO = new SocialMediaDAO();
    }

    public List<Account> getAllAccounts(){
        List<Account> accounts = socialMediaDAO.getAllAccounts();
        return accounts;
    }

    public Account addUser(Account account)
    {
        Account accounts = socialMediaDAO.addAccount(account);
        if(account.getPassword().length() > 4 && !account.getUsername().isEmpty()
            && socialMediaDAO.getAccountById(0) == null){
            return accounts;
        }
        return null;
    }

    public Account getAccount(int account_id)
    {
        return socialMediaDAO.getAccountById(account_id);
    }

    public Account verify(String username, String password)
    {
        System.out.println("Verifying user credentials for username: " + username + password);
        Account verifiedAccount = socialMediaDAO.verifyUserCredentials(username, password);
        Account verifAccount = socialMediaDAO.getAccountById(0);
        System.out.println("Account ID: " + verifAccount);
        System.out.println("Verification result: " + (verifiedAccount != null ? "Success" : "Failure"));
        return verifiedAccount;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = socialMediaDAO.getAllMessages();
        return messages;
    }

    public Message getMessageById(int messageId) {
        return socialMediaDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return socialMediaDAO.deleteMessageById(messageId);
    }

    public Message updateMessageText(int messageId, String newMessageText){
        return socialMediaDAO.updateMessageText(messageId, newMessageText);
    }
    
}
