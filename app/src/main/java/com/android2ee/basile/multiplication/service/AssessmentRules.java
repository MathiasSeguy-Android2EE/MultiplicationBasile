/**
 * <ul>
 * <li>AssessmentRules</li>
 * <li>com.android2ee.basile.multiplication.service</li>
 * <li>04/03/2017</li>
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

package com.android2ee.basile.multiplication.service;

import android.util.Log;

import com.android2ee.basile.multiplication.BuildConfig;
import com.android2ee.basile.multiplication.MyApplication;

/**
 * Created by Mathias Seguy - Android2EE on 04/03/2017.
 */
public class AssessmentRules {
    private static final String TAG = "AssessmentRules";
    /***********************************************************
     * Singleton
     **********************************************************/

    private static AssessmentRules instance=null;
    public static AssessmentRules getInstance(){
        if(instance==null){
            instance=new AssessmentRules();
        }
        return instance;
    }
    private AssessmentRules (){}
    /***********************************************************
     *  Initialization
     **********************************************************/
    int[] multiplicatorFactor, value,answer;

    public void init(){
        multiplicatorFactor =new int[MyApplication.ins().getMaxQuestionNumber()];
        value=new int[MyApplication.ins().getMaxQuestionNumber()];
        answer=new int[MyApplication.ins().getMaxQuestionNumber()];
        if(MyApplication.ins().isAssesOnlyThisMultiplicationTable()){
            //Table Multiplication only
            initThisMultiplicationTableOnly();

        }else{
            //range initialization
            initRangeFrom0ToMultiplicationtTable();

        }


        if(BuildConfig.DEBUG) {
            for (int i = 0; i < MyApplication.ins().getMaxQuestionNumber(); i++) {
                Log.e(TAG, " The " + i + " operation is v*mv " + value[i] + " x " + multiplicatorFactor[i] + " = " + answer[i] + " (for table=" +
                        MyApplication.ins().getMaxMultiplicationValue() + ")");
            }
        }
    }
    /***********************************************************
     *  When managing the Multiplicationtablex[0,10] and
     *  only the MultiplicationTable itself
     **********************************************************/
    private void initThisMultiplicationTableOnly() {
        //consider the table to learn as a line with 10 rows and 1 columns
        //You randomly pick a place in this line
        int position;
        int multiplicationTable= MyApplication.ins().getMaxMultiplicationValue();
        int linelenght=11;//+1 because of zero
        boolean[] alreadyPickedPosition=new boolean[linelenght];
        for (int i = 0; i < alreadyPickedPosition.length; i++) {
            alreadyPickedPosition[i]=false;
        }
        for (int i = 0; i < MyApplication.ins().getMaxQuestionNumber(); i++) {
            position=(int)Math.rint(Math.random()*linelenght);
            //insure you never used it before or it's not a 0 value
            if (position==linelenght)position=linelenght-1;
            while(alreadyPickedPosition[position]){
                if(position>=linelenght-1){
                    position=0;
                }
                position++;
            }
            //then calculate your coordinate (abs=multiplicator, ord=value)
            multiplicatorFactor[i]= multiplicationTable;
            value[i]= position;
            answer[i]=AssesmentOperation.caculate(value[i],multiplicatorFactor[i]);
        }
    }

    /***********************************************************
     *  When managing the range [1,Multiplicationtable]x[0,10] and
     *  not only the MultiplicationTable itself
     **********************************************************/

    /**
     * Initialize the values for when you want to assess the range [1,Multiplicationtable]x[0,10]
     */
    private void initRangeFrom0ToMultiplicationtTable() {
        //consider the table to learn as a rectangle with 10 rows and maxVMultiplicationValue columns
        //You randomly pick a place in this rectangle and you convert to know its position
        //so you have you mulitplicatorValue and the value
        //To avoid having multiple time the same operation, just track the indexes
        int position;
        int multiplicationTable= MyApplication.ins().getMaxMultiplicationValue();
        boolean keepItSimple=multiplicationTable<4?true:false;
        int rectSize=11*(multiplicationTable+1);//+1 because of zero
        int rectSizeHard=11*(multiplicationTable-1);//+1-2 because of zero - {0,1}
        boolean[] alreadyPickedPosition=new boolean[rectSize];
        for (int i = 0; i < alreadyPickedPosition.length; i++) {
            alreadyPickedPosition[i]=false;
        }
        for (int i = 0; i < MyApplication.ins().getMaxQuestionNumber(); i++) {
            //so find your new operation position
            if(keepItSimple){
                position=(int)Math.rint(Math.random()*rectSize);
            }else{
                //only from 2 to multiplicationTable
                position=(int)Math.rint(Math.random()*rectSizeHard)+2*multiplicationTable;
            }
            //insure you never used it before or it's not a 0 value
            if (position==rectSize)position=rectSize-1;
            if(position<2*multiplicationTable)position=position+2*multiplicationTable;
            while(alreadyPickedPosition[position]
                    ||getValue(position, getMultiplicatorValue(position,multiplicationTable),multiplicationTable)==0){
                if(position>=rectSize-1){
                    position=0;
                }
                position++;
                if(!keepItSimple){
                    //insure 0 or 1 not reached neither as value neither as multiplicatorFactor
                    if(position<=(2*multiplicationTable+1))position=position+2*multiplicationTable+1;
                    if(getValue(position, getMultiplicatorValue(position,multiplicationTable),multiplicationTable)==0) position=position+2;
                    if(getValue(position, getMultiplicatorValue(position,multiplicationTable),multiplicationTable)==1) position=position+1;
                }
//                Log.e(TAG,"init() position found "+position);
            }
            //set it has used
            alreadyPickedPosition[position]=true;
            //then calculate define the question
            multiplicatorFactor[i]= getMultiplicatorValue(position,multiplicationTable);
            value[i]= getValue(position, multiplicatorFactor[i],multiplicationTable);
            answer[i]=AssesmentOperation.caculate(value[i],multiplicatorFactor[i]);
            Log.e(TAG," The "+position+" returns v*mv "+value[i]+" x "+ multiplicatorFactor[i]+" = "+answer[i]+" (for table="+
                    MyApplication.ins().getMaxMultiplicationValue()+")");
        }
    }


    /**
     * According to the position in the rectangle return the ordinate
     * @param position
     * @param multiplicationTable
     * @return
     */
    private int getMultiplicatorValue(int position,int multiplicationTable) {
//        Log.e(TAG,"mv = pos/mulTable : "+position+"/"+multiplicationTable+"="+position/(multiplicationTable+1));
        return position/(multiplicationTable+1);
    }
    /**
     * According to the position in the rectangle return the absissa
     * @param position
     * @param i
     * @return
     */
    private int getValue(int position, int multicplicationValue,int multiplicationTable) {
       return position-(multicplicationValue*(multiplicationTable+1));
    }

    /***********************************************************
     *  Getters
     **********************************************************/
    /**
     * Return the next
     * @param questionNumber
     * @param result
     * @return
     */
    public void getQuestion(int questionNumber, int[] result){
        result[0]=value[questionNumber];
        result[1]= multiplicatorFactor[questionNumber];
        result[2]=answer[questionNumber];
    }
}
