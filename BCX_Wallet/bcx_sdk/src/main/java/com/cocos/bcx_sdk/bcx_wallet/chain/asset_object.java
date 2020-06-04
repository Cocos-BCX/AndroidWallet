package com.cocos.bcx_sdk.bcx_wallet.chain;


import com.cocos.bcx_sdk.bcx_utils.bitlib.util.NumericUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class asset_object {

    public object_id<asset_object> id;
    public String symbol;
    // Maximum number of digits after the decimal point (must be <= 12)
    public int precision = 0;
    // ID of the account which issued this asset.
    object_id<account_object> issuer;

    object_id<asset_dynamic_data_object> dynamic_asset_data_id;

    public asset amount_from_string(String strAmount) {

        long precisionScaled = 1;
        String strDecimalFormat = "0";

        if (precision > 0) {
            strDecimalFormat = strDecimalFormat.concat(".");
            for (int i = 0; i < precision; ++i) {
                precisionScaled *= 10;
                strDecimalFormat = strDecimalFormat.concat("0");
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat(strDecimalFormat, new DecimalFormatSymbols(Locale.ENGLISH));
        String strFormatAmount = decimalFormat.format(NumericUtil.parseDouble(strAmount));
        double result = NumericUtil.parseDouble(strFormatAmount);
        long resultAmount = (long) (result * precisionScaled);
        asset assetObject = new asset(resultAmount, id);
        return assetObject;
    }

    public long get_scaled_precision() {
        long scaled_precision = 1;
        for (int i = 0; i < precision; ++i) {
            scaled_precision *= 10;
        }
        return scaled_precision;
    }
}
