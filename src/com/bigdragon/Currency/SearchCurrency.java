package com.bigdragon.Currency;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.util.Log;

//import org.htmlcleaner.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;

import java.io.IOException;

public class SearchCurrency extends Thread {
	
    final static String url = "http://www.bank.gov.ua/Fin_ryn/OF_KURS/Currency/FindByDate.aspx";

    Handler mHandler;
    final static int STATE_DONE = 0;
    final static int STATE_RUNNING = 1;
    int mState;
    int mTotal;
   
    SearchCurrency(Handler h) {
        mHandler = h;
    }
   
    public void run() {
        
//new ParseSite().execute("http://www.stackoverflow.com");
        //HtmlHelper hh = new HtmlHelper(new URL("http://www.bank.gov.ua/Fin_ryn/OF_KURS/Currency/FindByDate.aspx"));
        mState = STATE_RUNNING;   
        mTotal = 0;
        while (mState == STATE_RUNNING) {
            try {
                //Thread.sleep(100);
        
        Document doc = Jsoup.connect(url).get();
        Element td = doc.select("form#tableForm tr td").first();
            Log.i("INFO","Count TD "+ td.text());

        
        //for (int i = 0; i < td.size(); i++){
            //Element row = td.get(i);
            //Log.i("INFO","TD "+ row.child(1).text());
            //}
            } catch (Exception e) {
                 e.printStackTrace(System.err);
                //Log.e("ERROR", "Thread Interrupted");
            }
            mTotal = 101;
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("total", mTotal);
            msg.setData(b);
            mHandler.sendMessage(msg);
            //mTotal++;
        }
    }
    
    public void setState(int state) {
        mState = state;
    }

}
