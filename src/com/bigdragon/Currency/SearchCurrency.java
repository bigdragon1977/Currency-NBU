package com.bigdragon.currency;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 @brief Класс для получения курса валют.
 @details Класс реализующий получение курса валют с офицального
 сайта НБУ. Класс является потомком класса Thread.
 */
public class SearchCurrency extends Thread {
    final static String url = "http://www.bank.gov.ua/Fin_ryn/OF_KURS/Currency/FindByDate.aspx";
    public static HashMap<String,Float> currency = new HashMap<String,Float>();
    Handler mHandler;
    final static int STATE_DONE = 0;
    final static int STATE_RUNNING = 1;
    int mState;
    int mTotal;
    String name_currency;
    Float rate;
    int count;
    int key_euro = 0;
    int key_rub = 0;
    int key_dolor = 0;
    int key_gbp = 0;
    int x = 0;
   
    SearchCurrency(Handler h) {
        mHandler = h;
    }
/**
 @brief Вызыватеся при инициализации класса.
 */
    public void run() {
        mState = STATE_RUNNING;   
        mTotal = 0;
        Log.d("DEBUG","Current total => "+ mTotal);
        while (mState == STATE_RUNNING) {
        try {
        Document doc = Jsoup.connect(url).get();
        Elements tr = doc.select("form#tableForm table tbody tr");
        Log.d("DEBUG","Count TR = "+tr.size());
        for(int i = 1; i < tr.size(); i++){
            Elements td = tr.eq(i).select("td");
            if(td.size() == 5){
                for (Element src : td){
                    if(new String(src.text()).equals("USD") || key_dolor == 1){
                        key_dolor = 1;
                        x++;
                            if(x == 1){
                                name_currency = src.text();
                            }else if(x == 2){
                                count = Integer.parseInt(src.text());
                            }else if(x == 4){
                                rate = Float.valueOf(src.text()) / count;
                                String return_round = String.valueOf(rate);
                                rate = Rounding(return_round);
                                currency.put(name_currency,rate);
                                x = 0;
                                key_dolor = 0;
                            }
                    }else if(new String(src.text()).equals("RUB") || key_rub == 1){
                        key_rub = 1;
                        x++;
                            if(x == 1){
                                name_currency = src.text();
                            }else if(x == 2){
                                count = Integer.parseInt(src.text());
                            }else if(x == 4){
                                rate = Float.valueOf(src.text()) / count;
                                String return_round = String.valueOf(rate);
                                rate = Rounding(return_round);
                                currency.put(name_currency,rate);
                                x = 0;
                                key_rub = 0; 
                            }
                    }else if(new String(src.text()).equals("EUR") || key_euro == 1){
                        key_euro = 1;
                        x++;
                            if(x == 1){
                                name_currency = src.text();
                            }else if(x == 2){
                                count = Integer.parseInt(src.text());
                            }else if(x == 4){
                                rate = Float.valueOf(src.text()) / count;
                                String return_round = String.valueOf(rate);
                                rate = Rounding(return_round);
                                currency.put(name_currency,rate);
                                x = 0;
                                key_euro = 0;
                            }
                    }else if(new String(src.text()).equals("GBP") || key_gbp == 1){
                        key_gbp = 1;
                        x++;
                            if(x == 1){
                                name_currency = src.text();
                            }else if(x == 2){
                                count = Integer.parseInt(src.text());
                            }else if(x == 4){
                                rate = Float.valueOf(src.text()) / count;
                                String return_round = String.valueOf(rate);
                                rate = Rounding(return_round);
                                currency.put(name_currency,rate);
                                x = 0;
                                key_gbp = 0;
                            }
                        }
                    }
                }
            }
            mTotal = 101;
            } catch (Exception e) {
                 e.printStackTrace(System.err);
                mTotal = 200;
            }
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("total", mTotal);
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }
    public void setState(int state) {
        mState = state;
    }
public float Rounding (String args) {
        float tmpFloat = new BigDecimal(Float.parseFloat(args)).setScale(2, RoundingMode.UP).floatValue();
        return tmpFloat;
    }
}
