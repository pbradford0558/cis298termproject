package edu.kvcc.cis298.cis298termproject;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetOpenExchangeRates {

    // Constants
    private static final String TAG = "GetOpenExchangeRates";
    private static final String API_URL = "https://openexchangerates.org/api/latest.json?";
    private static final String APP_ID = "e437f944c4b94aecaa69ec578e961fd5";

    // Variable to determine whether new exchange rates should be obtained.
    private boolean timeForNewRates = true;

    public ExchangeRateItem fetchRateInfo(long timeStampLong, String fromIsoCurCode,
                                          String toIsoCurCode) {

        // Instantiate rateItem.
        ExchangeRateItem rateItem = new ExchangeRateItem();

        // Get current time in milliseconds.
        long now = System.currentTimeMillis();

        // Determine difference between now and time stamp. Time stamp equals 0 (first time
        // through logic) or time of last exchange rate update (not first time). Exchange
        // rates are updated once each hour by openexchangerates.org and time stamp is included
        // in the downloaded JSON file.
        long timeDifference = now - (timeStampLong * 1000);

        // If at least 1 hour since exchange rate file was obtained from openexchangerates.org
        // or first time through logic (since timeStampLong is initialized to 0). (60 * 60 * 1000)
        // = 1 hour in milliseconds.
        if (timeDifference > (60 * 60 * 1000)) {

            try {
                // Connect to URL and fetch data.
                String url = Uri.parse(API_URL)
                        .buildUpon()
                        .appendQueryParameter("app_id", APP_ID)
                        .build().toString();

                // Place data in JSON string and log result.
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);

                // Populate jsonBody.
                JSONObject jsonBody = new JSONObject(jsonString);

                // Populate rateItem.
                parseItems(fromIsoCurCode, toIsoCurCode, rateItem, jsonBody);

                // Store exchange rates for use in the next hour.
                JsonRates.getInstance().setJsonRates(jsonBody);

                // Catch and log errors.
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch items", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }

            // If it's been less than 1 hour since exchange rate file was obtained.
        } else {

            try {
                // Retrieve stored exchange rates and log result.
                JSONObject jsonBody = JsonRates.getInstance().getJsonRates();
                Log.i(TAG, "jsonBody: " + jsonBody);

                // Populate jsonBody.
                parseItems(fromIsoCurCode, toIsoCurCode, rateItem, jsonBody);

                // Catch and log errors.
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch items", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
        }

        // Return rateItem.
        return rateItem;
    }

    // Method to fetch string from URL.
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        // Create new URL object from URL that was passed in.
        URL url = new URL(urlSpec);

        // Create new HTTP connection to the specified URL.
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {

            // Create output stream to hold data from the URL.
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Create input stream from HTTP connection.
            InputStream in = connection.getInputStream();

            // Determine whether response code from HTTP request is OK.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " + urlSpec);
            }

            // Variable to hold how many bytes are going to be read in.
            int bytesRead = 0;

            // Array to hold 1024 bytes.
            byte[] buffer = new byte[1024];

            // While reading input stream, write it to output stream.
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            // When everything has been read/written, close output and input streams.
            out.close();
            in.close();

            // Convert output stream to byte array.
            return out.toByteArray();

        } finally {

            // Disconnect from the web.
            connection.disconnect();
        }
    }

    // Method to populate all fields in rateItem.
    private void parseItems(String fromIsoCurCode, String toIsoCurCode,
                            ExchangeRateItem rateItem, JSONObject jsonBody)
            throws IOException, JSONException {

        // Populate "From" and "To" ISO codes
        rateItem.setFromId(fromIsoCurCode);
        rateItem.setToIsoId(toIsoCurCode);

        // Fetch rate information from JSONObject.
        JSONObject ratesJsonObject = jsonBody.getJSONObject("rates");

        // Populate "From" and "To" exchange rates.
        rateItem.setFromRate(ratesJsonObject.getString(fromIsoCurCode));
        rateItem.setToRate(ratesJsonObject.getString(toIsoCurCode));

        // Populate time stamp.
        rateItem.setTimeStamp(jsonBody.getString("timestamp"));
    }
}
