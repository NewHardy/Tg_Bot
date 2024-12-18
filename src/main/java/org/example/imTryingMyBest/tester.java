package org.example.imTryingMyBest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class tester
{
    public static void main(String[] args) {
        Date date=new Date();
        //System.out.println(date);
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println(dateFormat.format(date));
        //dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(dateFormat.format(date));
        //dateFormat=new SimpleDateFormat("E, MMM dd yyyy");
        //System.out.println(dateFormat.format(date));
        System.out.println(date.getTime());
        System.out.println(System.currentTimeMillis());


        //Formato 2: 2024-07-16 09:09:04
        //Formato 3: 16:00
    }
}
