package org.example.bot;

import java.util.Objects;

public class User implements Comparable<User>
{
    private Long chatID;
    private int hour;
    private int mins;
    private String value;
    private int TP;
    private int SL;

    public int getTP() {
        return TP;
    }

    public void setTP(int TP) {
        this.TP = TP;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
