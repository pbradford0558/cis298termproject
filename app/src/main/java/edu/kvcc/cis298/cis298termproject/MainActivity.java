package edu.kvcc.cis298.cis298termproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Variables to hold layout widgets.
    private EditText mCurrencyEditText;
    private RadioGroup mFromRadioGroup;
    private RadioGroup mToRadioGroup;
    private FloatingActionButton mFab;

    // Arrays to hold info for "From" and "To" currencies.
    private String[] mFromCurArray;
    private String[] mToCurArray;

    // Variable to indicate whether input choices are valid.
    private boolean inputIsValid = false;

    // Variable to determine whether new exchange rates should be obtained.
    private long timeStampLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to activity_content layout controls.
        mCurrencyEditText = (EditText) findViewById(R.id.currency_edit_text);
        mFromRadioGroup = (RadioGroup) findViewById(R.id.from_radio_group);
        mToRadioGroup = (RadioGroup) findViewById(R.id.to_radio_group);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        // Floating action button listener.
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get resource integers for "from" and "To" buttons.
                int fromButtonId = mFromRadioGroup.getCheckedRadioButtonId();
                int toButtonId = mToRadioGroup.getCheckedRadioButtonId();

                // Validate input choices.
                validateInput(fromButtonId, toButtonId);

                // If no errors, get currency info/exchange rate, and then start ResultActivity.
                if (inputIsValid) {

                    // Get info for "From" and "to" currencies.
                    mFromCurArray = CurrencyInfo.selectCurrencyInfo(fromButtonId);
                    mToCurArray = CurrencyInfo.selectCurrencyInfo(toButtonId);

                    // Get exchange rate from openexchangerates.org website, convert
                    // currency, and start ResultActivity.
                    new FetchExchangeRate().execute();

                    // Reset inputIsValid variable.
                    inputIsValid = false;
                }
            }
        });
    }

    // Method to display toasts if input is invalid; if valid, isValid variable is updated.
    private void validateInput(int fromButtonId, int toButtonId) {

        // Convert input currency amount to string.
        String inputAmountString = mCurrencyEditText.getText().toString();

        // Display toast if a no currency amount entered, and then clear answer.
        if (TextUtils.isEmpty(inputAmountString)) {
            Toast.makeText(MainActivity.this,
                    R.string.amount_toast,
                    Toast.LENGTH_SHORT).show();
        }

        // Display toast if "From" button not selected.
        else if (mFromRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this,
                    R.string.from_toast,
                    Toast.LENGTH_SHORT).show();
        }

        // Display toast if "To" button not selected.
        else if (mToRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this,
                    R.string.to_toast,
                    Toast.LENGTH_SHORT).show();
        }

        // Display toast if the same "From" and "To" buttons are checked.
        else if (((RadioButton) MainActivity.this.findViewById(fromButtonId)).getText() ==
                ((RadioButton) MainActivity.this.findViewById(toButtonId)).getText()) {
            Toast.makeText(MainActivity.this,
                    R.string.same_toast,
                    Toast.LENGTH_SHORT).show();
        }

        // Otherwise, input is valid.
        else {
            inputIsValid = true;
        }
    }

    // Class to get exchange rate from openexchangerates.org website and start ResultActivity.
    private class FetchExchangeRate extends AsyncTask<Void, Void, ExchangeRateItem> {

        @Override
        protected ExchangeRateItem doInBackground(Void... params) {

            // Get exchange rate on a background thread. Parameters are: timeStampLong
            // (equals 0 [first time through logic] or time of previous exchange rate update);
            // mXXCurArray[0] = ISO currency code.
            return new GetOpenExchangeRates().fetchRateInfo(timeStampLong,
                    mFromCurArray[0], mToCurArray[0]);
        }

        @Override
        protected void onPostExecute(ExchangeRateItem rateItem) {

            // Cast input amount to a string.
            String inputAmountString = mCurrencyEditText.getText().toString();

            // Store time stamp for determining when to fetch new exchange rates.
            timeStampLong = Long.valueOf(rateItem.getTimeStamp());

            // Start ResultActivity.
            Intent i = ResultActivity.newIntent(MainActivity.this, inputAmountString,
                    mFromCurArray, mToCurArray, rateItem);
            startActivity(i);
        }
    }
}
