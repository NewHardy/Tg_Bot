package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot
{
    private final   ArrayList<String> commandList= new ArrayList<>(Arrays.asList("/start","/hello","/whatAreYou","/age","/time"));

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi tBotApi = new TelegramBotsApi(DefaultBotSession.class);
        tBotApi.registerBot(new Bot());
    }

    @Override
    public String getBotUsername() {
        return "@AGAVFirst_bot";
    }

    @Override
    public String getBotToken() {
        return "7723695369:AAFc6_Bn0F7x0vjwtGow-ROH7inewh8B4gs";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("From: "+update.getMessage().getChat().getUserName());
        Long chatId = getChatId(update);
        String messageText = update.getMessage().getText();
        System.out.println("We got: "+messageText);

        if (update.hasMessage())
        {
            String message="";
            String text = update.getMessage().getText();
            if (text.equals("/help"))
            {
                SendMessage message1 = createMessage("Hello im Andrey's first Bot \nMy commands are\n",chatId);
                attachButtons(message1,Map.of
                        (
                                commandList.get(0), commandList.get(0)+"_btn",
                                commandList.get(1), commandList.get(1)+"_btn",
                                commandList.get(2), commandList.get(2)+"_btn",
                                commandList.get(3), commandList.get(3)+"_btn",
                                commandList.get(4), commandList.get(4)+"_btn"
                        ));
                sendApiMethodAsync(message1);
            }
            else if (!text.matches("^/\\w+"))
            {
                message="this is not a command, try /help to get command list";
            }
            else if (text.matches("^/\\w+"))
            {
                if(text.equals(commandList.get(0)))
                {
                    message="Hello im Andrey's first Bot, made by BotFather, ready to help you\n(try /help to get command list)";
                }
                else if (text.equals(commandList.get(1)))
                {
                    message="Hello im AGAVFirst_bot, what can I help you with";
                }
                else if (text.equals(commandList.get(2)))
                {
                    message="Im a Telegram Bot made by BotFather";
                }
                else if (text.equals(commandList.get(3)))
                {
                    message="Im a bot I don't have an age";
                }
                else if (text.equals(commandList.get(4)))
                {
                    message="\"Time is relative\"\nAlbert Einstein";
                }
                else
                {
                    message="this is a command that we don't have registered, try /help to get command list";
                }
            }
            SendMessage message1 = createMessage(message,chatId);
            sendApiMethodAsync(message1);
        }
        if (update.hasCallbackQuery())
        {
            String button=update.getCallbackQuery().getId();
            String message;
            if(button.equals(commandList.get(0)+"_btn"))
            {
                message="Hello im Andrey's first Bot, made by BotFather, ready to help you\n(try /help to get command list)";
            }
            else if (button.equals(commandList.get(1)+"_btn"))
            {
                message="Hello im AGAVFirst_bot, what can I help you with";
            }
            else if (button.equals(commandList.get(2)+"_btn"))
            {
                message="Im a Telegram Bot made by BotFather";
            }
            else if (button.equals(commandList.get(3)+"_btn"))
            {
                message="Im a bot I don't have an age";
            }
            else if (button.equals(commandList.get(4)+"_btn"))
            {
                message="\"Time is relative\"\nAlbert Einstein";
            }
            else
            {
                message="This button doesn't have a assigned function";
            }
            SendMessage message1 = createMessage(message,chatId);
            sendApiMethodAsync(message1);
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
        return null;
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
