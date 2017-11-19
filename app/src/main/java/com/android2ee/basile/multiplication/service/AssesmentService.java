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

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.model.Score;
import com.android2ee.basile.multiplication.dao.MyAppDatabase;
import com.android2ee.basile.multiplication.dao.ScoreDao;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

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
    /***********************************************************
     *  Saving
     **********************************************************/
    // /******************************************************************************************/
    /** Method name: save
     /* Description : Save the element into the database **********/
    /* Param: Score score
    /******************************************************************************************/
    /**
     * Should be called by the View
     * @param score The score you want to save in DAO
     */
    public void saveAsynch(Score score) {
        MyApplication.ins().getKeepAliveThreadsExecutor()
                .execute(new RunnableSave(score));
    }

    /**
     * Screate and save the associated score object
     * @param score The value of the score
     * @param elapsedTime The time to finish the game
     */
    public void saveAsynch(int score,int elapsedTime ){
        getScoreDao();
        saveAsynch(new Score(elapsedTime,
                MyApplication.ins().getMaxMultiplicationValue(),
                score,
                MyApplication.ins().isAssesOnlyThisMultiplicationTable()));
    }
    /**
     * Should only be called from a background thread (So only by another Service's method)
     * Don't ever call this method from the UI Thread
     *
     * @param score The score you want to save in DAO
     */
    public void saveSync(Score score) {
        // your code here
        Log.e(TAG, "save() called with: assessmentScore = [" + score + "]");
        getScoreDao();
        scoreDao.save(score);
    }
    /**
     * This is the runnable that will send the work in a background thread
     */
    private class RunnableSave implements Runnable {
        Score score;
        public RunnableSave(Score score) {
            this.score = score;
        }

        @Override
        public void run() {
            saveSync(score);
        }
    }

    /***********************************************************
     *  Querying Records
     **********************************************************/
    /******************************************************************************************/
    /** Method name: getRecordsTable
     * Description : Returns the list of all the score in the DB **********/
    /******************************************************************************************/
    /**
     * Should be called by the View
     * Returns the list of all the score in the DB
     * Event to listen:
     */
    public void getRecordsTableAsynch() {
        MyApplication.ins().getKeepAliveThreadsExecutor().execute(new RunnableGetRecordsTable());
    }
    /**
     * Should only be called from a background thread (So only by another Service's method)
     * Don't ever call this method from the UI Thread
     *
     * @return The list of Score
     */
    public List<Score> getRecordsTableSync() {
        // your code here
        getScoreDao();
        List<Score> ret=scoreDao.getRecordsTable();
        Collections.sort(ret);
        //post to eventBus
        EventBus.getDefault().post(ret);
        return ret;
        //and returning using an Event (like EventBus or Otto is a good idea)
    }
    /**
     * This is the runnable that will send the work in a background thread
     */
    private class RunnableGetRecordsTable implements Runnable {

        public RunnableGetRecordsTable() {
        }

        @Override
        public void run() {
            getRecordsTableSync();
        }
    }


    /***********************************************************
     *  Attributes
     **********************************************************/


    private void getScoreDao() {
        if(scoreDao==null){
            scoreDao= Room.databaseBuilder(MyApplication.ins(),
                    MyAppDatabase.class, "database-name").build().getScoreDao();
        }
    }

}
