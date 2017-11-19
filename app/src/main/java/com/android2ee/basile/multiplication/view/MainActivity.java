package com.android2ee.basile.multiplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.KeyBoardCallBack;
import com.android2ee.basile.multiplication.view.mother.MotherActivity;
import com.android2ee.basile.multiplication.view.records.RecordsActivity;


public class MainActivity extends MotherActivity implements KeyBoardCallBack {
    private static final String TAG = "MainActivity";
    /***********************************************************
     * Attributes
     **********************************************************/
    private Button btnStart;
    private View lastSelected=null;
    private Switch swtThisTableOnly;


    /***********************************************************
     * Managing LifeCycle
     **********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        btnStart= (Button) findViewById(R.id.btnStart);
        btnStart.setEnabled(false);
        swtThisTableOnly= (Switch) findViewById(R.id.swtThisTableOnly);

        // add the listener to the spinner that listen for item selection
        setListeners();
    }



    private void setListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAssesment();
            }
        });
        swtThisTableOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thisTableOnly(isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swtThisTableOnly.setChecked(MyApplication.ins().isAssesOnlyThisMultiplicationTable());
    }


    /***********************************************************
    *  Business Methods
    **********************************************************/

    /**
     *
     */
    private void thisTableOnly(boolean isChecked){
        MyApplication.ins().setAssesOnlyThisMultiplicationTable(isChecked);
    }

    /**
     * CallBack of the keyboard buttons
     * @param v
     */
    public void keyboardCallback(View v){
        int maxValue;
        switch (v.getId()){
            case R.id.btn1:
                maxValue=1;
                break;
            case R.id.btn2:
                maxValue=2;
                break;
            case R.id.btn3:
                maxValue=3;
                break;
            case R.id.btn4:
                maxValue=4;
                break;
            case R.id.btn5:
                maxValue=5;
                break;
            case R.id.btn6:
                maxValue=6;
                break;
            case R.id.btn7:
                maxValue=7;
                break;
            case R.id.btn8:
                maxValue=8;
                break;
            case R.id.btn9:
                maxValue=9;
                break;
            default:
                maxValue=10;
        }
        MyApplication.ins().setMaxMultiplicationValue(maxValue);
        btnStart.setEnabled(true);
        //changing background color
        v.setBackgroundResource(R.drawable.btn_keyboard_shape_selected);
        if(lastSelected!=v) {
            if (lastSelected != null) {
                lastSelected.setBackgroundResource(R.drawable.btn_keyboard_shape);
            }
            lastSelected = v;
        }

        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }
    private void startAssesment(){
        Intent startAssesment=new Intent(this,AssessmentActivity.class);
        startActivity(startAssesment);
    }

    /***********************************************************
     *  Managing Menu
     **********************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_winner){
            Intent showRecord=new Intent(this, RecordsActivity.class);
            startActivity(showRecord);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
