/**
 * <ul>
 * <li>AssesmentService</li>
 * <li>com.android2ee.basile.multiplication.service</li>
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

package com.android2ee.basile.multiplication.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.model.Score;
import com.android2ee.basile.multiplication.dao.ScoreDao;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mathias Seguy - Android2EE on 01/03/2017.
 */
public class AssesmentService {
    private static final String TAG = "AssesmentService";
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String SCORE = "score";
    public static final String QUESTION_NUMBER = "questionNumber";
    public static final int NOT_SET = -1;
    /***********************************************************
     * Singleton
     **********************************************************/

    private static AssesmentService instance=null;
    public static AssesmentService getInstance(){
        if(instance==null){
            instance=new AssesmentService();
        }
        return instance;
    }
    private AssesmentService (){
        result=new int[3];
    }
    public void init(){
        AssessmentRules.getInstance().init();
    }

    /***********************************************************
    *  Business Methods : Managing current question
    **********************************************************/

    int[] result;
    public String getNewQuestion(int questionNumber){
        AssessmentRules.getInstance().getQuestion(questionNumber,result);
        return MyApplication.ins().getResources().getString(R.string.question_string_init,result[0],result[1]);
    }
    public String getQuestion(int answ){
        return MyApplication.ins().getResources().getString(R.string.question_string,result[0],result[1],answ);
    }
    public String getQuestion(){
        return MyApplication.ins().getResources().getString(R.string.question_string,result[0],result[1],result[2]);
    }
    public int getAnswer() {
        return result[2];
    }

    public boolean isAnswerValid(int answer){
        return answer==result[2];
    }

    /***********************************************************
     *  Business Methods : Managing current party
     **********************************************************/
    private SharedPreferences shp;
    private SharedPreferences.Editor shpEdit;


    public void storeAssessment(long elapsedTime,int score, int questionNumber) {
        shpEdit=MyApplication.ins().getSharedPreferences("Assessment", Context.MODE_PRIVATE).edit();
        shpEdit.putLong(ELAPSED_TIME,elapsedTime);
        shpEdit.putInt(SCORE,score);
        shpEdit.putInt(QUESTION_NUMBER,questionNumber).commit();
    }

    public void loadAssessment(long[] result) {
        shp=MyApplication.ins().getSharedPreferences("Assessment", Context.MODE_PRIVATE);
        result[0]=shp.getLong(ELAPSED_TIME, NOT_SET);
        result[1]=shp.getInt(SCORE, NOT_SET);
        result[2]=shp.getInt(QUESTION_NUMBER, NOT_SET);
    }
    /***********************************************************
     *  Managing scoring storage
     **********************************************************/

    ScoreDao scoreDao=null;
    /**
     * Simple
     * @param assessmentScore
     */
    public void save(Score assessmentScore){
        Log.e(TAG, "save() called with: assessmentScore = [" + assessmentScore + "]");
        if(scoreDao==null){
            scoreDao=new ScoreDao();
        }
        Score scoreToDelete=null;
        //save only if better
        boolean tableNotFound=true, betterRecord=false;
        for (Score score : scoreDao.getRecordsTable()) {

            Log.e(TAG, " compare with score = [" + score + "]");
            //first insure it's for the same table
            if(score.getMultiplicationTable()==assessmentScore.getMultiplicationTable()) {
                tableNotFound=false;
                Log.e(TAG, " table found");
                if (score.getScore() < assessmentScore.getScore()){
                    Log.e(TAG, " score.getScore() < assessmentScore.getScore()");
                    betterRecord=true;
                    scoreToDelete=score;
                }else if (score.getScore() == assessmentScore.getScore()){
                    Log.e(TAG, " score.getScore() == assessmentScore.getScore()");
                    if(score.getElapsedtime()>assessmentScore.getElapsedtime()){
                        Log.e(TAG, " score.getElapsedtime()>assessmentScore.getElapsedtime()");
                        betterRecord=true;
                        scoreToDelete=score;
                    }
                }
            }
        }
        Log.e(TAG,"betterRecord="+betterRecord+", assessmentScore="+assessmentScore);
        if(tableNotFound){
            scoreDao.save(assessmentScore);
        }
        if(betterRecord){
            scoreDao.save(assessmentScore);
            scoreDao.delete(scoreToDelete);
        }
    }
    public void save(int score,int elapsedTime ){
        if(scoreDao==null){
            scoreDao=new ScoreDao();
        }
        save(new Score(elapsedTime,
                MyApplication.ins().getMaxMultiplicationValue(),
                score,
                MyApplication.ins().isAssesOnlyThisMultiplicationTable()));
    }
    public ArrayList<Score> getRecordsTable(){
        if(scoreDao==null){
            scoreDao=new ScoreDao();
        }
        ArrayList<Score> ret=scoreDao.getRecordsTable();
        Collections.sort(ret);
        return ret;
    }
}
