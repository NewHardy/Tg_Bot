package org.example.bot;

import java.util.Objects;

public class User implements Comparable<User>
{
    private Long chatID;
    private Integer hour;
    private Integer mins;
    private String currency;
    private int buy;
    private int sell;

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public Long getChatID() {
        return chatID;
    }

    public int getHour() {
        return hour;
    }

    public int getMins() {
        return mins;
    }

    public User(Long userID) {
        this.chatID = userID;
        this.hour=24;
        this.mins=00;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatID, user.chatID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chatID);
    }

    @Override
    public int compareTo(User user) {
        return Long.compare(this.chatID,user.chatID);
    }
}
