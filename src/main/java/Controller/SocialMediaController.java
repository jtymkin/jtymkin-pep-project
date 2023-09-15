package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     //dependency
     SocialMediaService socialMediaService;

     public SocialMediaController()
     {
        socialMediaService = new SocialMediaService();
     }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        //endpoints
            //create new account
            app.post("/register", this::createAccount);
            //verify login
            app.post("/login", this::verifyLogin);
            //create new message
            app.post("/messages", this::createMessage);
            //retrieve all messages
            app.get("/messages", this::getAllMessages);
            //retrieve message by ID
            app.get("/messages/{message_id}", this::getMessagesById);
            //delete message by ID
            app.delete("/messages/{message_id}", this::deleteMessageById);
            //update message text by ID
            app.patch("messages/{message_id}", this::updateMessage);
            //retrieve all messages by user
            app.get("accounts/{account_id}/messages", this::getMessageUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //handlers

    private void createAccount(Context ctx){

        Account account = ctx.bodyAsClass(Account.class);

        Account addAccount = socialMediaService.addUser(account);
        
        if(addAccount != null)
        {
            ctx.json(addAccount);
        }
        else{
            ctx.status(400);
        }
        
    }

    private void verifyLogin(Context ctx) 
    {
        Account account = ctx.bodyAsClass(Account.class);

        // Assuming socialMediaService.authenticateUser is a method for user authentication
        Account isAuthenticated = socialMediaService.verify(account);
        
        if (isAuthenticated != null) {
            // Authentication successful
            ctx.json(account);
        } else {
            // Authentication failed
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx)
    {
        Message message = ctx.bodyAsClass(Message.class);

        Message create = socialMediaService.createMessage(message);
        
        if(create != null)
        {
            ctx.json(create);
        }
        else{
            ctx.status(400);
        }
        
    }

    private void getAllMessages(Context ctx)
    {
        ctx.status(200);
        ctx.json(socialMediaService.getAllMessages());
    }

    private void getMessagesById(Context ctx)
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = socialMediaService.getMessageById(message_id);

        if(message == null)
        {
            ctx.json("");
        }
        else{
        ctx.status(200);
        ctx.json(socialMediaService.getMessageById(message_id));}
        
    }

    private void deleteMessageById(Context ctx)
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = socialMediaService.getMessageById(message_id);


        if(message == null)
        {
            ctx.json("");
        }
        else{
            ctx.status(200);
            ctx.json(socialMediaService.getMessageById(message_id));
        }

    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        Message message = ctx.bodyAsClass(Message.class);
        System.out.println("Received Message: " + message);
    
        // Extract messageId from the URL or request parameters
        int messageId;
        try {
            messageId = Integer.parseInt(ctx.pathParam("message_id")); // Assuming "message_id" is the parameter name
        } catch (NumberFormatException e) {
            ctx.status(400); // Invalid message_id in the URL
            return;
        }
    
        // Check if the message is valid
        if (message != null) {
            // Update the message text in the database
            Message result = socialMediaService.updateMessageText(messageId, message.getMessage_text());
    
            if (result != null) {
                ctx.json(result);
            } else {
                ctx.status(400); // Update failed
            }
        } else {
            ctx.status(400); // Invalid message in the request body
        }
    }

    private void getMessageUser(Context ctx)
    {
        // Extract the account_id from the context or request parameters
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        // Call the service or DAO method to retrieve messages by user
        List<Message> userMessages = socialMediaService.getMessageByUser(accountId);

        // Set the HTTP status to 200 (OK) and return the messages as JSON
        ctx.status(200).json(userMessages);
    }


}