package com.bigdragon.Currency;

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
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Currency extends Activity
{
	private EditText text;
    private TextView labelview;
    SearchCurrency mProgressThread;
    ProgressDialog mProgressDialog;

    private final int DIALOG_EXIT = 0;
    private final int DIALOG_ABOUT = 1;
    static final int DIALOG_PROGRESS = 2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        labelview = (TextView) findViewById(R.id.label);
        labelview.setText("Currency GRN\n");
        text = (EditText) findViewById(R.id.editText1);
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        //////////////////////////////////////////////////
        DbOpenHelper dbOpenHelper = new DbOpenHelper(Currency.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
//                cv.put(DbOpenHelper.LOGIN,loginEditText.getText().toString());
//                cv.put(DbOpenHelper.PASSW,passEditText.getText().toString());
//                db.insert(DbOpenHelper.TABLE_NAME,null,cv);
//      db.close();

        /////////////////////////////////////////////////
                    showDialog(DIALOG_PROGRESS);
        //ProgressDialog dialog = ProgressDialog.show(Currency.this, "","Loading. Please wait...", true);

    }
    public void cu_Clics(View view){
    	switch (view.getId()){
    	case R.id.button1:
    		RadioButton fromButton = (RadioButton) findViewById(R.id.radio0);
    		RadioButton toButton = (RadioButton) findViewById(R.id.radio1);
    		if (text.getText().length() == 0){
    		Toast.makeText(this,"Please enter the number ",Toast.LENGTH_LONG).show();
    		return;
    		}
    		float inputValue = Float.parseFloat(text.getText().toString());
    		if (fromButton.isChecked()){
    			text.setText(String.valueOf(convertValtoGrn(inputValue)));
                chengeLabel();
    		}else{
    			text.setText(String.valueOf(convertValtoVal(inputValue)));
                chengeLabel();
    		}
    		if(toButton.isChecked()){
    			text.setText(String.valueOf(convertValtoVal(inputValue)));
    		}else{
    			text.setText(String.valueOf(convertValtoGrn(inputValue)));
    			}

    		break;
		}
    }
    private void chengeLabel(){
                labelview.setText("Currency US\n");
    }
    private float convertValtoGrn(float valut){
    	return (valut / 8);
    }
    private float convertValtoVal(float grn){
    	return(grn * 8);
    }
    /*Create menu Item*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.currency_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                 case R.id.m_exit:
                    quit();
                case R.id.m_about:
                    showDialog(DIALOG_ABOUT);
                    //ShowDialog();
                    break;
                default:
                break;
                 }
                 return true;
                 //return false;
                 }

    //private void ShowDialog(){

        //AlertDialog dialog = new AlertDialog.Builder(this).create();
        //dialog.setMessage("Your final score: ");
        //dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try this level again",
        //new DialogInterface.OnClickListener() {
            //public void onClick(DialogInterface dialog, int which) {
                ////mScore = 0;
                ////start_level();
            //}
        //});
        //dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Advance to next level",
        //new DialogInterface.OnClickListener() {
        //public void onClick(DialogInterface dialog, int which) {
            ////mLevel++;
            ////start_level();
        //}
        //});
        //dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Back to the main menu",
        //new DialogInterface.OnClickListener() {
            //public void onClick(DialogInterface dialog, int which) {
                ////mLevel = 0;
                //finish();
            //}
        //});
        //dialog.show();

    //}
/////////////////////////////////////////////////////////////////////////
@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_EXIT:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Currency.this.finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }
            });
            builder.setCancelable(false);
            return builder.create();
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
        case DIALOG_PROGRESS:
            mProgressDialog = new ProgressDialog(Currency.this);
            //mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Loading...");
            mProgressThread = new SearchCurrency(handler);
            mProgressThread.start();
            return mProgressDialog;
        default:
        return null;
        }
    }
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int total = msg.getData().getInt("total");
            mProgressDialog.setProgress(total);
            if (total >= 100){
                dismissDialog(DIALOG_PROGRESS);
                mProgressThread.setState(SearchCurrency.STATE_DONE);
                Toast.makeText(getApplicationContext(), "Task is finished", Toast.LENGTH_SHORT).show();
            }
        }
    };
/////////////////////////////////////////////////////////////////////////

    private void quit(){
    this.finish();
    super.onDestroy();
    }
}
