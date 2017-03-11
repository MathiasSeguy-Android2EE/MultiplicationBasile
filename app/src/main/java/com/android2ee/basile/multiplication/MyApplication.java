/**
 * <ul>
 * <li>MyApplication</li>
 * <li>com.android2ee.basile.multiplication</li>
 * <li>01/03/2017</li>
 * <p>
 * <li>======================================================</li>
 * <p>
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 * <p>
 * /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br>
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 * Belongs to <strong>Mathias Seguy</strong></br>
 * ***************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * <p>
 * *****************************************************************************************************************</br>
 * Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 * Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br>
 * Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */

package com.android2ee.basile.multiplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android2ee.basile.multiplication.service.AssesmentService;
import com.orm.SugarContext;

/**
 * Created by Mathias Seguy - Android2EE on 01/03/2017.
 */
public class MyApplication extends Application {
    private static final String GENERAL = "GENERAL";
    private static final String MAX_MULTIPLICATION_VALUE = "MaxMultiplicationValue";
    /***********************************************************
     *  Singleton
     **********************************************************/
    private static MyApplication instance;
    public static MyApplication ins(){
        return instance;
    }
    /***********************************************************
     *  Keeping the AssessmentService alive
     **********************************************************/
    AssesmentService assService;

    /***********************************************************
     * Managing Life Cycle
     **********************************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        assService=AssesmentService.getInstance();
        SugarContext.init(this);
        Log.e("MyAppInitializer","Second choices, a log is enough to prove the concept: MyApplication");
    }

    /***********************************************************
    *  MAnaging the Multiplication Max Value and the AssessOnlyThisMultiplicationTable
    **********************************************************/
    private static final int NOT_SET=-1;
    private int maxMultiplicationValue=NOT_SET;
    private boolean assesOnlyThisMultiplicationTable=false;
    private SharedPreferences shp;
    private SharedPreferences.Editor shpEdit;

    public int getMaxMultiplicationValue() {
        if(maxMultiplicationValue==NOT_SET){
            shp=getSharedPreferences(GENERAL, Context.MODE_PRIVATE);
            if(shp.contains(MAX_MULTIPLICATION_VALUE)){
                maxMultiplicationValue=shp.getInt(MAX_MULTIPLICATION_VALUE,NOT_SET);
            }
        }
        return maxMultiplicationValue;
    }

    public void setMaxMultiplicationValue(int maxMultiplicationValue) {
        this.maxMultiplicationValue = maxMultiplicationValue;
        shpEdit=getSharedPreferences(GENERAL, Context.MODE_PRIVATE).edit();
        shpEdit.putInt(MAX_MULTIPLICATION_VALUE,maxMultiplicationValue).commit();
    }

    public boolean isAssesOnlyThisMultiplicationTable() {
        return assesOnlyThisMultiplicationTable;
    }

    public void setAssesOnlyThisMultiplicationTable(boolean assesOnlyThisMultiplicationTable) {
        this.assesOnlyThisMultiplicationTable = assesOnlyThisMultiplicationTable;
    }

    /**
     * Clear the max multiplication value
     */
    public void clearmaxMultiplicationValue(){
        setMaxMultiplicationValue(NOT_SET);
    }
    /***********************************************************
     *  MAnaging the number of questions
     **********************************************************/
    private static final int DEFAULT=10;
    private int maxQuestionNumber=DEFAULT;

    public int getMaxQuestionNumber() {
        return maxQuestionNumber;
    }

    public void setMaxQuestionNumber(int maxQuestionNumber) {
        this.maxQuestionNumber = maxQuestionNumber;
    }


}
