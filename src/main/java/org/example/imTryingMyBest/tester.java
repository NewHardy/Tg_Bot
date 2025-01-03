package org.example.imTryingMyBest;

import org.example.bot.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tester
{
    public static void main(String[] args) {
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(new User(12431414132L));
        userArrayList.add(new User(12431414131L));
        userArrayList.add(new User(12431414133L));
        userArrayList.add(new User(12431414136L));
        userArrayList.add(new User(12431414134L));
        userArrayList.add(new User(12431414135L));

        userArrayList.stream().sorted().forEach(x-> System.out.println(x.getChatID()));
    }
}
