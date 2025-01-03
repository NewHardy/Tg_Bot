package org.example.bot;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class Bot extends TelegramLongPollingBot
{

    private static ArrayList<User> userList=new ArrayList<>();
    private final Gson gson= new Gson();
    private final   ArrayList<String> commandList= new ArrayList<>(Arrays.asList("/help","/start","/BTC","/ETH","/Settings","/setAlarmTime","/setAlarmValue","/disableAlarm"));
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
        if (!userList.contains(new User(chatId)))
        {
            userList.add(new User(getChatId(update)));
            SendMessage message = createMessage("you have been registered to the Bot, welcome to the best crypto bot ever",chatId);
            sendApiMethodAsync(message);
        }
        else
        {
            SendMessage message = createMessage("you are registered already registered to the best crypto bot",chatId);
            sendApiMethodAsync(message);
        }
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
                "/setAlarmTime", "/setAlarmTime_btn",
                "/setAlarmValue","/setAlarmValue_btn",
                "/disableAlarm","/disableAlarm_btn"
        ));
        sendApiMethodAsync(message);
    });
    private final Consumer<Update> setAlarmValue = (update -> {
        Long chatId = getChatId(update);
        if (!userList.contains(new User(chatId)))
        {
            userList.add(new User(getChatId(update)));
        }
        SendMessage message = createMessage("Choose the crypto for your alarm",chatId);
        attachButtons(message,Map.of(
                "ETH","ETH_btn",
                "BTC","BTC_btn"
        ));
        sendApiMethodAsync(message);
    });
    private final Consumer<Update> setAlarmTime = (update -> {
        Long chatId = getChatId(update);
        if (!userList.contains(new User(chatId)))
        {
            userList.add(new User(getChatId(update)));
        }
        SendMessage message = createMessage("Choose your time when to get notification",chatId);
        Map <String, String> row1 = createTimeButtons(0,3);
        Map <String, String> row2 = createTimeButtons(4,7);
        Map <String, String> row3 = createTimeButtons(8,11);
        Map <String, String> row4 = createTimeButtons(12,15);
        Map <String, String> row5 = createTimeButtons(16,19);
        Map <String, String> row6 = createTimeButtons(20,23);

        ArrayList<Map<String,String>> list = new ArrayList<>();
        list.add(row1);
        list.add(row2);
        list.add(row3);
        list.add(row4);
        list.add(row5);
        list.add(row6);
        list.add(Map.of(
                "TEST", "15:30_btn"
        ));
        attachButtons(message,list);
        sendApiMethodAsync(message);
    });
    private final Consumer<Update> disableAlarm = (update -> {
        Long chatId = getChatId(update);
        if (!userList.contains(new User(chatId)))
        {
            userList.add(new User(getChatId(update)));
        }
        userList.get(getUserIndex(chatId)).setValue("");
        userList.get(getUserIndex(chatId)).setMins(1);
        userList.get(getUserIndex(chatId)).setHour(24);
        SendMessage message = createMessage("your alarm have been disabled",chatId);

        sendApiMethodAsync(message);

    });
    private final Consumer<Update> test = (update -> {
        Long chatId = getChatId(update);
        SendMessage message = createMessage("hi i am tester",chatId);
        sendApiMethodAsync(message);

    });

    private final Map<String, Consumer<Update>> commandMap = new HashMap<>();

    private final ArrayList<Consumer<Update>> consumerList=new ArrayList<>(Arrays.asList(
            help,start,BTC,ETH,settings,setAlarmTime,setAlarmValue,disableAlarm
    ));

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi tBotApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        tBotApi.registerBot(bot);
        SimpleDateFormat dateFormat =new SimpleDateFormat("HH:mm");
        Thread timeThread = new Thread(() -> {
            while (true) {
                String time=dateFormat.format(new Date());
                int hour=Integer.valueOf(time.substring(0,2));
                int mins=Integer.valueOf(time.substring(3));
                for (User user : userList)
                {
                    if (user.getHour()==hour)
                    {
                        if (user.getMins()==mins)
                        {
                            try {
                                SendMessage message= bot.createMessage("ALARM\n"+user.getValue()+" "+
                                        bot.getPrices(user.getValue()).toString()
                                        ,user.getChatID());
                                bot.sendApiMethodAsync(message);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start();
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
        if (button.matches("\\d{2}:\\d{2}"))
        {
            int hours=Integer.parseInt(button.substring(0,2));
            int minutes=Integer.parseInt(button.substring(3));
            int userIndex = getUserIndex(getChatId(update));
            userList.get(userIndex).setHour(hours);
            userList.get(userIndex).setMins(minutes);
            SendMessage message = createMessage("Notification time set "+hours+":"+minutes,getChatId(update));
            sendApiMethodAsync(message);
        }
        else if (button.equals("ETH")||button.equals("BTC"))
        {
            int userIndex = getUserIndex(getChatId(update));
            userList.get(userIndex).setValue(button);
            SendMessage message = createMessage("Value type set "+button,getChatId(update));
            sendApiMethodAsync(message);
        }
        else
        {
            commandMap.getOrDefault(button,noCommand).accept(update);
        }
    }
    private static int getUserIndex (Long chatId)
    {
        for (int i = 0; i < userList.size(); i++)
        {
            if (userList.get(i).getChatID().equals(chatId))
            {
                return i;
            }
        }
        return -1;
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
    private void attachButtons(SendMessage message, ArrayList<Map<String,String>> arrayButtons)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List <List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Map<String,String> buttons:arrayButtons)
        {
            ArrayList<InlineKeyboardButton> list = new ArrayList<>();
            List <String> keySetList= new ArrayList<>(buttons.keySet().stream().toList());
            keySetList.sort(new TimeComparator());
            for (String buttonText:keySetList)
            {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonText);
                button.setCallbackData(buttons.get(buttonText));
                list.add(button);
            }
            keyboard.add(list);
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

    }
    private Map<String,String> createTimeButtons(int startValue,int endValue)
    {
        Map<String,String> map = new HashMap<>();
        for (int i = startValue; i <= endValue; i++)
        {
            String a = i+":"+"00";
            String b = i+":"+"30";
            map.put(a,a+"_btn");
            map.put(b,b+"_btn");
        }
        return map;
    }
    private static class TimeComparator implements Comparator<String> {
        @Override
        public int compare(String time1, String time2) {
            int minutes1 = Integer.valueOf(time1.replace(":", ""));
            int minutes2 = Integer.valueOf(time2.replace(":", ""));
            return Integer.compare(minutes1, minutes2);
        }
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
