package Controller;

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
            app.patch("mesages/{message_id}", this::updateMessage);
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

    }

    private void createMessage(Context ctx)
    {

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

    private void updateMessage(Context ctx)
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessageText = ctx.body();

        Message updatedMessage = socialMediaService.updateMessageText(message_id, newMessageText);

        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(socialMediaService.updateMessageText(message_id, newMessageText));
        } else {
            ctx.status(400);
        }

    }

    private void getMessageUser(Context ctx)
    {

    }


}