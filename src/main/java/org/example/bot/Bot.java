package org.example.bot;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jdk.jfr.SettingControl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class Bot extends TelegramLongPollingBot
{

    private ArrayList<User> userList;
    private final Gson gson= new Gson();
    private final   ArrayList<String> commandList= new ArrayList<>(Arrays.asList("/help","/start","/hello","/whatAreYou","/age","/time","/BTC","/ETH","/Settings","/setAlarm","/test"));
    private final HashMap <String, String> helpCommandMap= new HashMap<>();

    private final Consumer<Update> noCommand= (update -> {
        Long chatId = getChatId(update);
        SendMessage message= createMessage("we don't have this command registered",chatId);
        sendApiMethodAsync(message);

    });
    private final Consumer<Update> help = (update)->{
        SendMessage message = createMessage("Hello im Andrey's first Bot \nMy commands are\n", getChatId(update));
        attachButtons(message, helpCommandMap);
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
    private final Consumer<Update> BTC = (update)->{
        Long chatId = getChatId(update);
        SendMessage message;
        try
        {
            String messageText=getPrices("BTC").toString();
            message = createMessage(messageText, chatId);
            sendApiMethodAsync(message);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    };
    private final Consumer<Update> ETH = (update)->{

        Long chatId = getChatId(update);
        SendMessage message;
        try
        {
            String messageText=getPrices("ETH").toString();
            message = createMessage(messageText, chatId);
            sendApiMethodAsync(message);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    };
    private final Consumer<Update> settings = (update -> {
       Long chatId = getChatId(update);
        SendMessage message = createMessage("Settings", chatId);
        attachButtons(message, Map.of(
                "/setAlarm", "/setAlarm_btn"
        ));
        sendApiMethodAsync(message);
    });
    private final Consumer<Update> setAlarm = (update -> {
        Long chatId = getChatId(update);
        SendMessage message = createMessage("Choose your time when to get notification",chatId);
        attachButtons(message,Map.of(
                "0:00","0:00_btn",
                "1:00","1:00_btn",
                "2:00","2:00_btn",
                "3:00","3:00_btn",
                "4:00","4:00_btn",
                "5:00","5:00_btn",
                "6:00","6:00_btn",
                "7:00","7:00_btn",
                "8:00","8:00_btn",
                "9:00","9:00_btn",
                "10:00","10:00_btn",
                "11:00","11:00_btn",
                "12:00","12:00_btn",
                "13:00","13:00_btn",
                "14:00","14:00_btn",
                "15:00","15:00_btn",
                "16:00","16:00_btn",
                "17:00","17:00_btn",
                "18:00","18:00_btn",
                "19:00","19:00_btn",
                "20:00","20:00_btn",
                "21:00","21:00_btn",
                "22:00","22:00_btn",
                "23:00","23:00_btn"
        ));

        sendApiMethodAsync(message);

    });
    private final Consumer<Update> test = (update -> {
        Long chatId = getChatId(update);
        SendMessage message = createMessage("hi i am tester",chatId);
        sendApiMethodAsync(message);

    });

    private final Map<String, Consumer<Update>> commandMap = new HashMap<>();

    private final ArrayList<Consumer<Update>> consumerList=new ArrayList<>(Arrays.asList(
            help,start,hello,whatAreYou,age,time,BTC,ETH,settings,setAlarm,test
    ));

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi tBotApi = new TelegramBotsApi(DefaultBotSession.class);
        tBotApi.registerBot(new Bot());

    }

    private Prices getPrices(String coin) throws IOException {
        URL url = new URL("https://min-api.cryptocompare.com/data/price?fsym="+coin+"&tsyms=USD,EUR,GBP");
        HttpURLConnection connector = (HttpURLConnection) url.openConnection();
        connector.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(connector.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while((inputLine=br.readLine())!=null)
        {
            sb.append(inputLine).append("\n");
        }
        br.close();
        String json = sb.toString();
        Type type = new TypeToken<Prices>(){}.getType();
        return new Gson().fromJson(json,type);
    }


    @Override
    public String getBotUsername() {return "@AGAVFirst_bot";}

    @Override
    public String getBotToken() {return "7723695369:AAFc6_Bn0F7x0vjwtGow-ROH7inewh8B4gs";}


    @Override
    public void onUpdateReceived(Update update)
    {
        Long chatId = getChatId(update);
        if (update.hasMessage())
        {
            System.out.println("we got Message");
            messageHandler(update,chatId);
        }
        if (update.hasCallbackQuery())
        {
            System.out.println("we got callbaquery");
            callBackQueryHandler(update);
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
        String text = update.getMessage().getText();
        if (!text.matches("^/\\w+"))
        {
            SendMessage message=createMessage("this is not a command, try /help to get command list",chatId);
            sendApiMethodAsync(message);
        }
        else
        {
            if(commandMap.keySet().contains(text)){
                commandMap.get(text).accept(update);
            }
            else
            {
                noCommand.accept(update);
            }

        }
    }
    private void callBackQueryHandler(Update update)
    {
        String button=btnToName(update.getCallbackQuery().getData());
        commandMap.getOrDefault(button,noCommand).accept(update);
    }
    private String btnToName(String button)
    {
        return button.substring(0,button.length()-4);
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

    {
        for (String value : commandList) {
            helpCommandMap.put(value,value+"_btn");
        }
        for (int i = 0; i < commandList.size(); i++) {
            commandMap.put(commandList.get(i),consumerList.get(i));
        }
    }
}
