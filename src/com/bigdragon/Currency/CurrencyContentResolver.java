package com.bigdragon.Currency;

import android.content.ContentResolver;
import android.content.Context;

public class CurrencyContentResolver extends ContentResolver{

    private DbOpenHelper dbOpenHelper;

    public CurrencyContentResolver(Context context) {
        super(context);
    }

}
