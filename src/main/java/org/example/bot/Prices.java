package org.example.bot;

public class Prices
{
    private float USD;
    private float EUR;
    private float GBP;

    public void setUSD(float USD) {
        this.USD = USD;
    }

    public void setEUR(float EUR) {
        this.EUR = EUR;
    }

    public void setGBP(float GBP) {
        this.GBP = GBP;
    }

    public float getUSD() {
        return USD;
    }

    public float getEUR() {
        return EUR;
    }

    public float getGBP() {
        return GBP;
    }

    @Override
    public String toString() {
        return "Prices:\n" +
                "USD=" + USD +
                "\nEUR=" + EUR +
                "\nGBP=" + GBP;
    }
}
