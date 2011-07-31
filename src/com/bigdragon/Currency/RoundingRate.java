package com.bigdragon.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingRate {

public float RoundingFloat (Float Args) {
        String ArgsString = String.valueOf(Args);
        float tmpFloat = new BigDecimal(Float.parseFloat(ArgsString)).setScale(2, RoundingMode.UP).floatValue();
        return tmpFloat;
    }
public float Rounding (String args) {
        float tmpFloat = new BigDecimal(Float.parseFloat(args)).setScale(2, RoundingMode.UP).floatValue();
        return tmpFloat;
    }
}
