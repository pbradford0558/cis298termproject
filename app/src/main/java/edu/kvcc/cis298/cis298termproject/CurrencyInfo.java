package edu.kvcc.cis298.cis298termproject;

public class CurrencyInfo {

    // Method to get info for selected currency.
    public static String[] selectCurrencyInfo(int selectedCurrency) {

        String isoCurCode = "";
        String curSymbol = "";
        String curName = "";

        switch (selectedCurrency) {
            case R.id.from_usd_radio_button:
            case R.id.to_usd_radio_button:
                isoCurCode = "USD";
                curSymbol = "$";
                curName = "UNITED STATES DOLLAR (USD)";
                break;

            case R.id.from_gbp_radio_button:
            case R.id.to_gbp_radio_button:
                isoCurCode = "GBP";
                curSymbol = "£";
                curName = "BRITISH POUND (GBP)";
                break;

            case R.id.from_eur_radio_button:
            case R.id.to_eur_radio_button:
                isoCurCode = "EUR";
                curSymbol = "€";
                curName = "EURO (EUR)";
                break;

            case R.id.from_inr_radio_button:
            case R.id.to_inr_radio_button:
                isoCurCode = "INR";
                curSymbol = "₹";
                curName = "INDIAN RUPEE (INR)";
                break;

            case R.id.from_aud_radio_button:
            case R.id.to_aud_radio_button:
                isoCurCode = "AUD";
                curSymbol = "$";
                curName = "AUSTRALIAN DOLLAR (AUD)";
                break;

            case R.id.from_cad_radio_button:
            case R.id.to_cad_radio_button:
                isoCurCode = "CAD";
                curSymbol = "$";
                curName = "CANADIAN DOLLAR (CAD)";
                break;

            case R.id.from_zar_radio_button:
            case R.id.to_zar_radio_button:
                isoCurCode = "ZAR";
                curSymbol = "R";
                curName = "SOUTH AFRICAN RAND (ZAR)";
                break;

            case R.id.from_nzd_radio_button:
            case R.id.to_nzd_radio_button:
                isoCurCode = "NZD";
                curSymbol = "$";
                curName = "NEW ZEALAND DOLLAR (NZD)";
                break;

            case R.id.from_jpy_radio_button:
            case R.id.to_jpy_radio_button:
                isoCurCode = "JPY";
                curSymbol = "¥";
                curName = "JAPANESE YEN (JPY)";
                break;

            case R.id.from_chf_radio_button:
            case R.id.to_chf_radio_button:
                isoCurCode = "CHF";
                curSymbol = "chf";
                curName = "SWISS FRANC (CHF)";
                break;
        }

        // String array to return.
        String[] curArray = new String[3];
        curArray[0] = isoCurCode;
        curArray[1] = curSymbol;
        curArray[2] = curName;

        return curArray;
    }
}
