package edu.kvcc.cis298.cis298termproject;

public class ExchangeRateItem implements java.io.Serializable {

    // Variables
    private String mFromIsoId;
    private String mFromRate;
    private String mToIsoId;
    private String mToRate;
    private String mTimeStamp;

    // Getters and setters
    public String getFromIsoId() {
        return mFromIsoId;
    }

    public void setFromId(String fromIsoId) {
        mFromIsoId = fromIsoId;
    }

    public String getFromRate() {
        return mFromRate;
    }

    public void setFromRate(String fromRate) {
        mFromRate = fromRate;
    }

    public String getToIsoId() {
        return mToIsoId;
    }

    public void setToIsoId(String toIsoId) {
        mToIsoId = toIsoId;
    }

    public String getToRate() {
        return mToRate;
    }

    public void setToRate(String toRate) {
        mToRate = toRate;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    // Method to calculate exchange rate.
    public Double calcExchangeRate() {

        // Cast exchange rates to double. These rates are based on the US dollar.
        Double fromExchangeRateDbl = Double.parseDouble(mFromRate);
        Double toExchangeRateDbl = Double.parseDouble(mToRate);

        // Calculate exchange rate.
        Double exchangeRateDbl = toExchangeRateDbl/fromExchangeRateDbl;

        // Return exchange rate.
        return exchangeRateDbl;
    }

    // Method to calculate "To" amount.
    public Double calcOutput(Double inputAmount, Double exchangeRateDbl) {

        // Calculate "To" currency amount.
        return inputAmount * exchangeRateDbl;
    }
}
