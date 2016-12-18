package edu.kvcc.cis298.cis298termproject;

import org.json.JSONObject;

public class JsonRates {
    private static JsonRates mInstance = null;
    private JSONObject mJsonRates;

    // Private constructor to prevent instantiation from other classes.
    private JsonRates() {}

    // Static method to get single instance of JsonRates.
    public static JsonRates getInstance() {
        if (mInstance == null) {
            mInstance = new JsonRates();
        }
        return mInstance;
    }

    // Getter and setter
    public JSONObject getJsonRates() {
        return this.mJsonRates;
    }

    public void setJsonRates(JSONObject value) {
        mJsonRates = value;
    }
}

