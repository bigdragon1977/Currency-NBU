package com.bigdragon.Currency;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class Currency extends Activity
{
	private EditText text;
    private TextView labelview;
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
}
