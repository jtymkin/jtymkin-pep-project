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
            socialMediaDAO.addAccount(account);
        }
        return accounts;
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
