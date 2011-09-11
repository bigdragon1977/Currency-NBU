package com.bigdragon.currency;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.content.Context;
import android.content.DialogInterface;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import android.util.Log;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 @class Currency
 @brief Основной клас проекта
 @details Создает основное окно проекта 
 в котором и происходит робота
 @author Kolomoec Andrey
 @date 2011
 */
public class Currency extends Activity
{
    private static final String[] currency_name_array = {"USD", "EUR", "RUB" ,"GBP"}; ///< Масив с списком валют 
	private EditText text;
    private TextView labelview;
    SearchCurrency mProgressThread;
    ProgressDialog mProgressDialog;
    Spinner s ; 
    public String select_currency; ///< Переменная в которой хранится название (абривеатура) текущей валюты. 
    HashMap<String,Float> local_currency; ///< Асоциативный масив в котором хранится значение Название валюты => Полученый курс
    int STATE = 1;
    int RUN = 0;
    private final int DIALOG_EXIT = 0;
    private final int DIALOG_ABOUT = 1;
    static final int DIALOG_PROGRESS = 2;
    static final int DIALOG_NOT_RATE = 3;
    static final int DIALOG_UPDATE_RATE = 4;
    static final int DIALOG_NOT_UPDATE = 5;
    ArrayAdapter<String> adapter;
    String TitleCurrent = ""; ///< Переменная в которой хранится название текущей валюты
    Button convert_button ; ///< Кнопка конвертации
    Button clear_button ; ///< Кнопка очистки поля ввода
    RadioButton fromButton ; ///< Переключатель 
    RadioButton toButton ; ///< Переключатель

/** 
 @brief Вызывается при первом вызове Activity 
 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (EditText) findViewById(R.id.editText1);
        s = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int index = 0; index < currency_name_array.length; index++){
            adapter.add(currency_name_array[index]);
        }
        s.setAdapter(adapter);
        labelview = (TextView) findViewById(R.id.label);
        TitleCurrent = labelview.getText().toString();
        labelview.setText(TitleCurrent +  " GRN\n");

        clear_button = (Button) findViewById(R.id.button_clear);
        convert_button = (Button) findViewById(R.id.button_convert);
        clear_button.setVisibility(8);
        
    	fromButton = (RadioButton) findViewById(R.id.radio0);
    	toButton = (RadioButton) findViewById(R.id.radio1);
        
        fromButton.setOnClickListener(radioButtonClick);
        toButton.setOnClickListener(radioButtonClick);

        DbOpenHelper dbOpenHelper = new DbOpenHelper(Currency.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor db_cursor = db.query("update_rate",null,null,null,null,null,null);
          if(db_cursor.getCount() == 0){
          STATE = 0;
          showDialog(DIALOG_NOT_RATE);
          }
          db_cursor.close();
            s.setOnItemSelectedListener(new CurrencyOnItemSelectedListener());
    }

        OnClickListener radioButtonClick = new OnClickListener() {
            public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
                if(rb.getText().equals("Convert to GRN")){
                    chengeLabel(select_currency);
                }else{
                    labelview.setText(TitleCurrent + " GRN" + "\n");
                }
            }
        };
/**
 @brief Обработка нажатия клавиши Convert
 */
    public void cu_Clics(View view){
    	switch (view.getId()){
    	case R.id.button_convert:
    		if (text.getText().length() == 0){
    		Toast.makeText(this,"Please enter the number ",Toast.LENGTH_LONG).show();
    		return;
    		}
    		float inputValue = Float.parseFloat(text.getText().toString());
    		if (fromButton.isChecked()){
    			text.setText(String.valueOf(convertValtoGrn(inputValue)));
                chengeLabel(select_currency);
                //adapter.clear();
                //s.setAdapter(adapter);
    		}
    		if(toButton.isChecked()){
    			text.setText(String.valueOf(convertValtoVal(inputValue)));
                //chengeLabel(select_currency);
    		}
    		HideOrVisible(1);
            break;
		}
    }

/**
 @brief Обработка нажатия клавиши Clear
 */
    public void cu_Clear(View views){
       switch (views.getId()){
       case R.id.button_clear:
       HideOrVisible(2);
       break;
       }
    }
/**
 @brief Сокрытие и отображение кнопок Convert и Clear.
 */
    private void HideOrVisible(int key){
        if(key == 1){
            clear_button.setVisibility(0);
            convert_button.setVisibility(8);
        }else{
            clear_button.setVisibility(8);
            convert_button.setVisibility(0);
            text.setText("");
            fromButton.setChecked(true);
            labelview.setText(TitleCurrent +  " GRN\n");
            adapter.clear();
                for(int index = 0; index < currency_name_array.length; index++){
                    adapter.add(currency_name_array[index]);
                }
            s.setAdapter(adapter);
        }
    }
/**
 @brief Отображение название текущей валюты.
 */
    private void chengeLabel(String currency_name){
                labelview.setText(TitleCurrent + " " + currency_name + "\n");
    }
/**
 @brief Перевод выбраной валюты в гривны.
 */
    private float convertValtoGrn(float valut){
        RoundingRate FinalRate = new RoundingRate();
    	return FinalRate.RoundingFloat((valut / Select_rate(select_currency)));
    }
/**
 @brief 
 */
    private float convertValtoVal(float grn){
        RoundingRate FinalRate = new RoundingRate();
    	return FinalRate.RoundingFloat((grn * Select_rate(select_currency)));
    }
/**
@brief Создание меню приложения.
@param menu Передаваемый параметр.
@return true При удачном создании меню
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.currency_menu, menu);
        return true;
    }
/**
 @brief Обработка выбора пункта меню.
 @param item Передаваемый параметр.
 @return true При удачной обработке фукции.
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                 case R.id.update_rate:
            if(needUpdateRate()){
                    showDialog(DIALOG_UPDATE_RATE);
            }else{
                    showDialog(DIALOG_NOT_UPDATE);
            }
                    break;
                 case R.id.m_exit:
                    showDialog(DIALOG_EXIT);
                    break;
                case R.id.m_about:
                    showDialog(DIALOG_ABOUT);
                    break;
                default:
                break;
            }
        return true;
    }
/**
 @brief Создания всех диалогов в программе.
 @param id Индификатор диалога.
 */
@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_EXIT:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    quit();
                    //Currency.this.finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }
            });
            builder.setCancelable(false);
            return builder.create();
        case DIALOG_NOT_RATE:
            AlertDialog.Builder builder_not_rate = new AlertDialog.Builder(this);
            builder_not_rate.setTitle("Message");
            builder_not_rate.setMessage("You want to see the course from the site of the NBU");
                builder_not_rate.setNegativeButton("Ok",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                    showDialog(DIALOG_PROGRESS);
                        }
                });
            builder_not_rate.setCancelable(false);
            return builder_not_rate.create();
        case DIALOG_PROGRESS:
            mProgressDialog = new ProgressDialog(Currency.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Loading...");
            mProgressThread = new SearchCurrency(handler);
            mProgressThread.start();
            RUN = 1;
            return mProgressDialog;
        case DIALOG_UPDATE_RATE:
            AlertDialog.Builder builder_update_rate = new AlertDialog.Builder(this);
            builder_update_rate.setTitle("Message");
            builder_update_rate.setMessage("You want to update the rate from the site of the NBU");
            builder_update_rate.setNegativeButton("Ok",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
                showDialog(DIALOG_PROGRESS);
                  }
                });
            builder_update_rate.setCancelable(false);
            return builder_update_rate.create();
        case DIALOG_NOT_UPDATE:
            AlertDialog.Builder not_update_rate = new AlertDialog.Builder(this);
            not_update_rate.setTitle("Message");
            not_update_rate.setMessage("Update rate is not required");
            not_update_rate.setNegativeButton("Ok",new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialog, int id){
                 dialog.cancel();
                   }
                });

            not_update_rate.setCancelable(false);
            return not_update_rate.create();
        case DIALOG_ABOUT:
            AlertDialog.Builder builder_about = new AlertDialog.Builder(this);
            builder_about.setTitle("About");
            builder_about.setMessage("About dialog");
                builder_about.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        }
                });
            builder_about.setCancelable(false);
            return builder_about.create();
        default:
        return null;
        }
    }
/**
 @brief Вызыватется при каждом вызове диалога
 @param id Индификатор диалога
 @param dialog Диалог
 */
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
          super.onPrepareDialog(id, dialog);
          switch (id) {
        case DIALOG_PROGRESS:
            if(RUN == 0){
                mProgressDialog = new ProgressDialog(Currency.this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Loading...");
                mProgressThread = new SearchCurrency(handler);
                mProgressThread.start();
            }
                break;
            default:
                break;
                }
            }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            RUN=1;
            int total = msg.getData().getInt("total");
            mProgressDialog.setProgress(total);
            if (total >= 100){
                dismissDialog(DIALOG_PROGRESS);
                mProgressThread.setState(SearchCurrency.STATE_DONE);
                local_currency = SearchCurrency.currency;
                if(total != 200){
                Toast.makeText(getApplicationContext(), "Task is finished", Toast.LENGTH_SHORT).show();
                    if(STATE == 0){
                    //Log.d("DEBUG","Start Insert_rate_to_database - NEW");
                        Insert_rate_to_database("new");
                    }else{
                    //Log.d("DEBUG","Start Insert_rate_to_database - UPDATE");
                        Insert_rate_to_database("update");
                    }
                }else{
                Toast.makeText(getApplicationContext(), "Error connect site NBU", Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG","Error connect NBU");
                    }
            RUN = 0;
            }
        }
    };
    private void quit(){
        this.finish();
        super.onDestroy();
    }
    private void Insert_rate_to_database(String state){
        long id_insert_currency = 0;
        long id_return_rate = 0;
        long return_query = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        String insert_date = String.valueOf(dateFormat.format(date));
        DbOpenHelper dbOpenHelper = new DbOpenHelper(Currency.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(Map.Entry<String,Float> all : local_currency.entrySet()){
            String key = all.getKey();
            Float value = all.getValue();
            String insert_value = String.valueOf(value);
            if(state.equals("new")){
                cv.put(DbOpenHelper.FIELD_CURRENCY_NAME,key);
                id_insert_currency = db.insert(DbOpenHelper.TABLE_NAME_CURRENCY,null,cv);
                cv.clear();
                cv.put(DbOpenHelper.FIELD_RATE,insert_value);
                cv.put(DbOpenHelper.FIELD_DATA_UPDATE, insert_date);
                id_return_rate = db.insert(DbOpenHelper.TABLE_NAME_UPDATE,null,cv);
                cv.clear();
                cv.put("id_currency",id_insert_currency);
                cv.put("id_update",id_return_rate);
                db.insert("global_currency",null,cv);
                cv.clear();
            }else if(state.equals("update")){
                String id_query;
                Cursor db_currency_id = db.query("currency_name",null,"currency=?",new String[] {key},null,null,null);
                db_currency_id.moveToNext();
                id_query = db_currency_id.getString(0);
                
                cv.put(DbOpenHelper.FIELD_RATE,insert_value);
                cv.put(DbOpenHelper.FIELD_DATA_UPDATE, insert_date);
                id_return_rate = db.insert(DbOpenHelper.TABLE_NAME_UPDATE,null,cv);
                cv.clear();

                String[] params = new String[] {id_query};
                cv.put("id_update",id_return_rate);
                db.update("global_currency",cv,"id_currency=?",params);
                cv.clear();
            }
        }
        db.close();
        //dismissDialog(DIALOG_PROGRESS);
        //dismissDialog(DIALOG_UPDATE_RATE);
    }
/**
@brief Фукция выборки из базы курса валюты.
@param currency Название валюты
@return Значение курса. Тип возвращеных данных Float.
 */ 
   public float Select_rate(String currency){
        String return_rate = "";
        DbOpenHelper dbOpenHelper = new DbOpenHelper(Currency.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        //Log.d("DEBUG SELECT", "SELECT INPUT " + currency);
        String sql = "SELECT rate FROM update_rate WHERE id_update = (SELECT id_update FROM global_currency WHERE id_currency = ( SELECT id_currency FROM currency_name WHERE currency='USD'))";
        Cursor db_cursor = db.rawQuery(sql,null);
        db_cursor.moveToFirst();
        while(!db_cursor.isAfterLast()){
                    return_rate = db_cursor.getString(0);
                    db_cursor.moveToNext();
        }
        db_cursor.close();
         return Float.valueOf(return_rate);
        }
/**
@brief Фукция определяет треруется ли выполнить обновление курса валют
на основании даты последнего обновления.
@param Не принимает параметров
@return На выходе true - если обновление требуется
false - если не требуется
*/
    public boolean needUpdateRate(){
        String return_date = "";
        String current_date = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        current_date = String.valueOf(dateFormat.format(date));

        DbOpenHelper dbOpenHelper = new DbOpenHelper(Currency.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        //String sql = "SELECT rate FROM update_rate WHERE id_update = (SELECT data FROM update_rate WHERE id_update = ( SELECT id_update FROM global_currency WHERE id_main=1))";
        String sql = "SELECT data FROM update_rate WHERE id_update = ( SELECT id_update FROM global_currency WHERE id_main= ( SELECT MAX(id_main) FROM global_currency))";
        Cursor db_cursor = db.rawQuery(sql,null);
        db_cursor.moveToFirst();
        while(!db_cursor.isAfterLast()){
            return_date = db_cursor.getString(0);
            db_cursor.moveToNext();
        }
        db_cursor.close();
        if(current_date.equals(return_date)){
            return false;
        }else{
            return true;
        }
    }
/**
 @class CurrencyOnItemSelectedListener
 @brief Получение значения при виборе.
 */
public class CurrencyOnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                View view, int pos, long id) {
                select_currency = parent.getItemAtPosition(pos).toString();
                }
        public void onNothingSelected(AdapterView parent) {
        }
    }

}

