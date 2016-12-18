package edu.kvcc.cis298.cis298termproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    // Variables to hold keys for intent extras.
    private static final String INPUT_AMOUNT = "edu.kvcc.cis298.termproject.input_amount";
    private static final String FROM_ARRAY = "edu.kvcc.cis298.termproject.from_array";
    private static final String TO_ARRAY = "edu.kvcc.cis298.termproject.to_array";
    private static final String RATE_ITEM = "edu.kvcc.cis298.termproject.rate_item";

    // Return new intent to start ResultActivity.
    public static Intent newIntent(Context packageContext, String inputAmount,
                                   String[] fromArray, String[] toArray,
                                   ExchangeRateItem rateItem) {
        Intent i = new Intent(packageContext, ResultActivity.class);
        i.putExtra(INPUT_AMOUNT, inputAmount);
        i.putExtra(FROM_ARRAY, fromArray);
        i.putExtra(TO_ARRAY, toArray);
        i.putExtra(RATE_ITEM, rateItem);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Return the intents.
        String mInputAmount = getIntent().getStringExtra(INPUT_AMOUNT);
        String[] mFromCurArray = getIntent().getStringArrayExtra(FROM_ARRAY);
        String[] mToCurArray = getIntent().getStringArrayExtra(TO_ARRAY);
        ExchangeRateItem mRateItem = (ExchangeRateItem)
                getIntent().getSerializableExtra(RATE_ITEM);

        // Get references to the layout controls.
        ImageView mFromImage = (ImageView) findViewById(R.id.from_image);
        ImageView mToImage = (ImageView) findViewById(R.id.to_image);
        TextView mFromName = (TextView) findViewById(R.id.from_name);
        TextView mToName = (TextView) findViewById(R.id.to_name);
        TextView mFromAmount = (TextView) findViewById(R.id.from_amount);
        TextView mToAmount = (TextView) findViewById(R.id.to_amount);
        TextView mExchangeRate = (TextView) findViewById(R.id.exchange_rate);
        TextView mDateTime = (TextView) findViewById(R.id.date_time);

        // Variables.
        Double exchangeRateDbl;
        Double inputAmountDbl;
        Double toAmountDbl;
        String resultString;
        Long timeStampLong;
        int resourceIdInt;
        TextView resultTextView;

        // Array to hold constant "From" amounts.
        Double[] amountArray = new Double[4];
        amountArray[0] = 10.0;
        amountArray[1] = 50.0;
        amountArray[2] = 100.0;
        amountArray[3] = 250.0;

        // Create decimal formats.
        DecimalFormat twoDecimals = new DecimalFormat("0.00");
        DecimalFormat eightDecimals = new DecimalFormat("0.00######");

        // Populate "From" and "To" image and name; mXXCurArray = currency name.
        mFromImage.setImageResource(getResources().getIdentifier(mFromCurArray[0].toLowerCase(),
                "drawable", getPackageName()));
        mToImage.setImageResource(getResources().getIdentifier(mToCurArray[0].toLowerCase(),
                "drawable", getPackageName()));
        mFromName.setText(mFromCurArray[2]);
        mToName.setText(mToCurArray[2]);

        // Calculate and populate exchange rate.
        exchangeRateDbl = mRateItem.calcExchangeRate();
        mExchangeRate.setText(eightDecimals.format(exchangeRateDbl));

        // Cast and populate "From" amount.
        inputAmountDbl = Double.parseDouble(mInputAmount);
        resultString = mFromCurArray[1] + " " + twoDecimals.format(inputAmountDbl);
        mFromAmount.setText(resultString);

        // Calculate and populate "To" amount.
        toAmountDbl = mRateItem.calcOutput(inputAmountDbl, exchangeRateDbl);
        resultString = mToCurArray[1] + " " + twoDecimals.format(toAmountDbl);
        mToAmount.setText(resultString);

        // Convert timestamp and populate date/time.
        timeStampLong = Long.valueOf(mRateItem.getTimeStamp());
        Date date = new Date(timeStampLong * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a z", Locale.US);
        mDateTime.setText(sdf.format(date));

        // Calculate, format, and populate constant "From" amounts and related "To" amounts.
        for (int i = 0; i < 4; ++i) {

            // Format "From" amount; mFromCurArray[1] = currency symbol.
            resultString = mFromCurArray[1] + " " + twoDecimals.format(amountArray[i]);

            // Build "From" resource id.
            resourceIdInt = getResources().getIdentifier
                    ("from_" + amountArray[i].intValue() + "_amount", "id", getPackageName());

            // Get reference to constant "From" layout control and populate its amount.
            resultTextView = (TextView) findViewById(resourceIdInt);
            resultTextView.setText(resultString);

            // Calculate and format "To" amount; mFromCurArray[1] = currency symbol.
            toAmountDbl = mRateItem.calcOutput(amountArray[i], exchangeRateDbl);
            resultString = mToCurArray[1] + " " + twoDecimals.format(toAmountDbl);

            // Build "To" resource id.
            resourceIdInt = getResources().getIdentifier
                    ("to_" + amountArray[i].intValue() + "_amount", "id", getPackageName());

            // Get reference to related "To" layout control and populate its amount.
            resultTextView = (TextView) findViewById(resourceIdInt);
            resultTextView.setText(resultString);
        }
    }
}
