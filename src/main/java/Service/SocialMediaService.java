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

    public Account verify(Account account)
    {
        return socialMediaDAO.verifyUserCredentials(account);
    }

    public Message createMessage(Message message)
    {
        Message messages = socialMediaDAO.createMessage(message);
        if(!message.getMessage_text().isBlank() && message.getMessage_text().length() < 255 
            && message.getPosted_by() != 0)
            {
                return messages;
            }
        return null;
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

    public Message updateMessageText(int messageId, String newMessageText) {
        // Check if the new message_text is not blank and not over 255 characters
        if (newMessageText != null && !newMessageText.isEmpty() && newMessageText.length() < 255) {
            return socialMediaDAO.updateMessageText(messageId, newMessageText);
        } else {
            return null; // Invalid new message_text
        }
    }

    public List<Message> getMessageByUser(int account_id){
        List<Message> messages = socialMediaDAO.getMessagesByUserId(account_id);
        return messages;
    }
    
}
