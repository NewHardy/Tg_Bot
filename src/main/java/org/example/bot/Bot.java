package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Bot extends TelegramLongPollingBot
{




    private final   ArrayList<String> commandList= new ArrayList<>(Arrays.asList("/help","/start","/hello","/whatAreYou","/age","/time"));
    private final Consumer<Update> help = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("Hello im Andrey's first Bot \nMy commands are\n", chatId);
        attachButtons(message, Map.of(
                commandList.get(0), commandList.get(0) + "_btn",
                commandList.get(1), commandList.get(1) + "_btn",
                commandList.get(2), commandList.get(2) + "_btn",
                commandList.get(3), commandList.get(3) + "_btn",
                commandList.get(4), commandList.get(4) + "_btn",
                commandList.get(5), commandList.get(5) + "_btn"
        ));
        sendApiMethodAsync(message);
    };
    private final Consumer<Update> start = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("Hello im Andrey's first Bot, made by BotFather, ready to help you\n(try /help to get command list)", chatId);
        sendApiMethodAsync(message);
    };
    private final Consumer<Update> hello = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("Hello im CRYPTO_Bot, what can I help you with", chatId);
        sendApiMethodAsync(message);
    };
    private final Consumer<Update> whatAreYou = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("I am a telegram bot created by BotFather", chatId);
        sendApiMethodAsync(message);
    };
    private final Consumer<Update> age = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("I am a bot i don't have an age", chatId);
        sendApiMethodAsync(message);
    };
    private final Consumer<Update> time = (update)->{
        Long chatId = getChatId(update);
        SendMessage message = createMessage("\"Time is relative\"\nAlbert Einstein", chatId);
        sendApiMethodAsync(message);
    };

    private final Map<String, Consumer> commandMap = Map.of(
            "/help",help,
            "/start",start,
            "/hello",hello,
            "/whatAreYou",whatAreYou,
            "/age", age,
            "/time", time


    );



    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi tBotApi = new TelegramBotsApi(DefaultBotSession.class);
        tBotApi.registerBot(new Bot());
    }





    @Override
    public String getBotUsername() {return "@AGAVFirst_bot";}

    @Override
    public String getBotToken() {return "7723695369:AAFc6_Bn0F7x0vjwtGow-ROH7inewh8B4gs";}


    @Override
    public void onUpdateReceived(Update update)
    {
        System.out.println("From: "+update.getMessage().getChat().getUserName());
        Long chatId = getChatId(update);
        String messageText = update.getMessage().getText();
        System.out.println("We got: "+messageText);

        if (update.hasMessage())
        {
            messageHandler(update,chatId);
        }
        if (update.hasCallbackQuery())
        {
            System.out.println("we got callbaquery");
            callBackQueryHandler(update,chatId);
        }
    }
    private SendMessage createMessage(String text, Long chatId)
    {
        SendMessage message= new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }
    private Long getChatId(Update update)
    {
        if (update.hasMessage())
        {
            return update.getMessage().getChatId();
        }
        else if (update.hasCallbackQuery())
        {
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }
    private void messageHandler(Update update, Long chatId)
    {
        String message="";
        String text = update.getMessage().getText();
        if (!text.matches("^/\\w+"))
        {
            message="this is not a command, try /help to get command list";
        }
        else
        {
            if(commandMap.keySet().contains(text)){
                commandMap.get(text).accept(update);
            }
            else
            {
                message="we don't have this command registered";
            }
        }
        SendMessage message1 = createMessage(message,chatId);
        sendApiMethodAsync(message1);
    }
    private void callBackQueryHandler(Update update, Long chatId)
    {
        String button=update.getCallbackQuery().getData();
        String message="";
        if(commandMap.keySet().contains(button))
        {
            commandMap.get(button).accept(update);
        }
        else
        {
            message="we don't have this Button WTF";
        }
        SendMessage message1 = createMessage(message,chatId);
        sendApiMethodAsync(message1);
    }
    private void attachButtons (SendMessage message, Map<String,String> buttons)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List <List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonText:buttons.keySet())
        {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonText);
            button.setCallbackData(buttons.get(buttonText));
            keyboard.add(Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }
}
