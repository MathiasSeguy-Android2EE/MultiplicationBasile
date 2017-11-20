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
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android2ee.basile.multiplication.service.AssesmentService;
import com.crashlytics.android.Crashlytics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Mathias Seguy - Android2EE on 01/03/2017.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
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
        Fabric.with(this, new Crashlytics());
        instance=this;
        assService=AssesmentService.getInstance();
        Log.e("MyAppInitializer","Second choices, a log is enough to prove the concept: MyApplication");
        Log.e("MyAppInitializer","Gradle Variable resValues.hidden_string ="+R.string.hidden_string);
        Log.e("MyAppInitializer","Gradle Variable resValues.isBoolAllowed="+R.bool.isBoolAllowed);
        Log.e("MyAppInitializer","Gradle Variable resValues.color_var="+R.color.color_var);
        Log.e("MyAppInitializer","Gradle Variable BuildConfig.isallowed="+BuildConfig.isallowed);
        Log.e("MyAppInitializer","Gradle Variable BuildConfig.isStringallowed="+BuildConfig.isStringallowed);
        Log.e("MyAppInitializer","Gradle Variable BuildConfig.intAllowed="+BuildConfig.intAllowed);
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

    /***********************************************************
     *  Threading strategy
     **********************************************************/
    //This code belongs to your application class ///

    /******************************************************************************************/
    /** Managing destruction : the 1 second pattern**************************************************************************/
    /******************************************************************************************/
    //Listening for activities life cycle to trigger the serviceManager death
    //If you min SDK is 17 you can use the ActivityListener directly
    /**
     * The AtomicInteger to know if there is an active activity
     */
    private AtomicInteger isActivityAlive = new AtomicInteger(0);

    /**
     * To be called by activities when they go in their onStart method
     */
    public void onStartActivity() {
        Log.d(TAG, "onStartActivity() called with: " + "");
        if(mServiceKillerHandler==null||mServiceKiller==null){
            initializeServiceKiller();
        }
        isActivityAlive.set(isActivityAlive.get() + 1);
        //clear the handler, no need to wake up the runnable
        mServiceKillerHandler.removeCallbacks(mServiceKiller);
    }

    /**
     * To be called by activities when they go in their onStop method
     */
    public void onStopActivity() {
        Log.d(TAG, "onStopActivity() called with: " + "");
        isActivityAlive.set(isActivityAlive.get() - 1);
        // launch the Runnable in 2 seconds
        mServiceKillerHandler.postDelayed(mServiceKiller, 1000);
    }
    //The 1 second pattern to kill activityManager in 1 second
    /**
     * The Runnable that will look if there are no activity alive and launch the serviceManager death
     */
    Runnable mServiceKiller = null;
    /**
     * The handler that manages the runnable
     */
    Handler mServiceKillerHandler = null;

    /**
     * initialize the runnable and its Handler
     */
    private void initializeServiceKiller() {
        mServiceKiller = new Runnable() {
            @Override
            public void run() {
                //one second later still no activity alive, so kill ServiceManager
                mServiceKillerHandler.dispatchMessage(mServiceKillerHandler.obtainMessage());
            }
        };
        mServiceKillerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("MyApplication", "in the Handler isActivityAlive==" + isActivityAlive.get());
                if (isActivityAlive.get() == 0) {
                    //What you should do when application should die
                    applicationShouldDie();
                }
            }
        };
    }

    /**
     * Kill them all !
     */
    private void applicationShouldDie() {
        Log.e("MyApplication", "applicationShouldDie is called");
        //first unregister broadcast
        //kill others elements that have to die (Bitmap...)
        //kill your runnable
        mServiceKillerHandler.removeCallbacks(mServiceKiller);
        mServiceKiller = null;
        mServiceKillerHandler = null;
        killKeepAliveThreadExecutor();
        //kill you serviceManager and call unbind and die
        //die
    }

    //Now the Killing method
    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onLowMemory()
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
/******************************************************************************************/
/** Pool Executor for Threads that has to finish they threatment when the application shutdown**/
/******************************************************************************************/
    /**
     * The pool executor to use for all cancellable thread and Threads that has to cancelled when the application shutdown
     */
    private ExecutorService keepAliveThreadsExceutor = null;

    /**
     * @return the cancelableThreadsExceutor
     */
    public final ExecutorService getKeepAliveThreadsExecutor() {
        if (keepAliveThreadsExceutor == null) {
            keepAliveThreadsExceutor = Executors.newFixedThreadPool(12, new BackgroundThreadFactory());
        }
        return keepAliveThreadsExceutor;
    }

    /**
     * And its associated factory
     */
    private class BackgroundThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("KeepAlive" + ((int) (Math.random() * 1000)));
            return t;
        }
    }

    /**
     * Kill all running Thread and destroy then all
     * Kill the cancelableThreadsExceutor
     */
    private void killKeepAliveThreadExecutor() {
        if (keepAliveThreadsExceutor != null) {
            keepAliveThreadsExceutor.shutdown(); // Disable new tasks from being submitted
            try {// as long as your threads hasn't finished
                while (!keepAliveThreadsExceutor.isTerminated()) {
                    // Wait a while for existing tasks to terminate
                    if (!keepAliveThreadsExceutor.awaitTermination(5, TimeUnit.SECONDS)) {
                        // Cancel currently executing tasks
                        keepAliveThreadsExceutor.shutdown();
                        Log.e("MyApp", "Probably a memory leak here");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                keepAliveThreadsExceutor.shutdownNow();
                keepAliveThreadsExceutor = null;
                Log.e("MyApp", "Probably a memory leak here too");
            }
        }
        keepAliveThreadsExceutor = null;
    }

}
