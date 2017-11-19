package com.android2ee.basile.multiplication.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android2ee.basile.multiplication.BuildConfig;
import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.AchievementLevel;
import com.android2ee.basile.multiplication.cross.KeyBoardCallBack;
import com.android2ee.basile.multiplication.service.AssesmentService;
import com.android2ee.basile.multiplication.view.dialog.CongratsDialog;
import com.android2ee.basile.multiplication.view.mother.MotherActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class AssessmentActivity extends MotherActivity implements KeyBoardCallBack{
    private static final String TAG = "AssessmentActivity";
    /***********************************************************
     * Attributes
     **********************************************************/
    private int questionNumber = 0;
    private int score = 0;
    private long elapsedTime = 0;
    private int userAnswer;
    private boolean shouldCallFinish=false;
    private boolean partyAborted=false;
    private TextView txvElapsedTime,  txvScore,
            txvQuestionNumber, txvQuestion,txvRightAnswer;
//    private EditText edtAnswer;
    private ImageView imvBadgeAnswer,imvScoreBadge;
    private ImageButton imbNextQuestion;
    private LinearLayout lilRightAnswer;
    private TableLayout tblKeyboard;
    // define the progressbar
    ProgressBar bar;

    //MAnaging the chroono
    private Handler chronoHandler;
    private Runnable chronoRunnable,chronoAlertRunnable;
    private AtomicBoolean chronoRuns;

    /***********************************************************
     * Managing LifeCycle
     **********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.activity_assessment);
        getSupportActionBar().setTitle(getString(R.string.ass_act_toolbar_title, MyApplication.ins().getMaxMultiplicationValue()));
        getSupportActionBar().setSubtitle(R.string.ass_act_toolbar_subtitle);

        txvElapsedTime = (TextView) findViewById(R.id.txvElapsedTime);
        txvScore = (TextView) findViewById(R.id.txvScore);
        txvQuestionNumber = (TextView) findViewById(R.id.txvQuestionNumber);
        txvQuestion = (TextView) findViewById(R.id.txvQuestion);
        txvRightAnswer = (TextView) findViewById(R.id.txvRightAnswer);
        imbNextQuestion= (ImageButton) findViewById(R.id.imbNextQuestion);
        imvBadgeAnswer= (ImageView) findViewById(R.id.imvBadgeAnswer);
        imvScoreBadge= (ImageView) findViewById(R.id.imvScoreBadge);
        lilRightAnswer= (LinearLayout) findViewById(R.id.lilRightAnswer);
        tblKeyboard= (TableLayout) findViewById(R.id.tblKeyboard);
        // Instanciate the progress bar
        bar = (ProgressBar) findViewById(R.id.progress);
        shouldCallFinish=false;
        initializeWidget();

        chronoHandler=new Handler();
        chronoRuns =new AtomicBoolean(false);
        chronoRunnable=new Runnable() {
            @Override
            public void run() {
                updateTime();
                //each second increment
                if(chronoRuns.get()){
                    chronoHandler.postDelayed(this,1000);
                    incrementBar();
                }
            }
        };
        chronoAlertRunnable=new Runnable() {
            @Override
            public void run() {
                updateTime();
                //each second increment
                if(chronoRuns.get()){
                    stopIfAssessmentOver();
                }
            }
        };
    }
    private void initializeWidget(){
        txvElapsedTime.setText(getString(R.string.mainact_txvTemps,120));
        txvQuestionNumber.setText(getString(R.string.mainact_txvQuestionNumber,0,
                MyApplication.ins().getMaxQuestionNumber()));
        txvScore.setText(getString(R.string.mainact_txvScore,0));
        txvQuestion.setText("");
        imvBadgeAnswer.setVisibility(View.GONE);
        setListeners();
    }

    private void setListeners() {
        imbNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart shouldCallFinish"+shouldCallFinish);
        Log.e(TAG,"onstart");
        chronoHandler.postDelayed(chronoAlertRunnable,120000);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG,"onRestoreInstanceState");
        long[]result=new long[3];
        AssesmentService.getInstance().loadAssessment(result);

        Log.e(TAG," res is "
                +result[0]
                +result[1]
                +result[2]
        );
        elapsedTime=result[0];
        score= (int) result[1];
        questionNumber= (int) result[2];
        txvQuestionNumber.setText(getString(R.string.mainact_txvQuestionNumber,questionNumber,
                MyApplication.ins().getMaxQuestionNumber()));
        txvScore.setText(getString(R.string.mainact_txvScore,score));
        txvElapsedTime.setText(getString(R.string.mainact_txvTemps,120-elapsedTime));
        chronoHandler.removeCallbacks(chronoAlertRunnable);
        chronoHandler.postDelayed(chronoAlertRunnable,
                (120-elapsedTime)*1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume shouldCallFinish"+shouldCallFinish);
        if(shouldCallFinish){
            finish();
        }else {
            if (elapsedTime <= 2) {
                startAssesment();
            } else {
                resumeAssessment();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"onSaveInstanceState");
        AssesmentService.getInstance().storeAssessment(
                elapsedTime,
                score,
                questionNumber);
    }


    @Override
    protected void onStop() {
        super.onStop();
        chronoRuns.set(false);
        chronoHandler.removeCallbacks(chronoAlertRunnable);
    }

    @Override
    public void finish() {
        super.finish();
        if(!partyAborted){
            AssesmentService.getInstance().saveAsynch(score,(int)elapsedTime);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        partyAborted=true;
    }

    /***********************************************************
     *  Business Methods
     **********************************************************/
    /**
     * Start assessment routine
     */
    private void startAssesment(){
        //rebuild the data
        AssesmentService.getInstance().init();
        resumeAssessment();
    }

    /**
     * Same as startassessment but it's when the device rotated, we resume the preivous game.
     */
    private void resumeAssessment(){
        //launch the timeElpased
        chronoRuns.set(true);
        chronoHandler.post(chronoRunnable);
        //display the question
        txvQuestion.setText(AssesmentService.getInstance().getNewQuestion(questionNumber));
        questionNumber++;
        txvQuestionNumber.setText(getString(R.string.mainact_txvQuestionNumber,questionNumber,
                MyApplication.ins().getMaxQuestionNumber()));
    }
    /**
     * Give me the next question
     */
    private void nextQuestion(){
        //restore components visibility
        imvBadgeAnswer.setVisibility(View.GONE);
        imbNextQuestion.setVisibility(View.GONE);
        lilRightAnswer.setVisibility(View.GONE);
        tblKeyboard.setVisibility(View.VISIBLE);
        //clear next answer
        userAnswer =0;
        //display the question
        txvQuestion.setText(AssesmentService.getInstance().getNewQuestion(questionNumber));
        questionNumber++;
        txvQuestionNumber.setText(getString(R.string.mainact_txvQuestionNumber,questionNumber,
                MyApplication.ins().getMaxQuestionNumber()));
    }

    /**
     * The question have been given
     * Analyse it
     */
    private void questionAnswered(){
        if(BuildConfig.isallowed){
            Log.e(TAG,"BuldConfig IsAllowed and ResValue "+getString(R.string.hidden_string));
        }
        userAnswer = getUserAnswer();
        //analyse the answer
        if(userAnswer ==AssesmentService.getInstance().getAnswer()){
            imvBadgeAnswer.setImageResource(R.drawable.ic_check);
            //update score
            score++;
            txvScore.setText(getString(R.string.mainact_txvScore,score));
        }else{
            imvBadgeAnswer.setImageResource(R.drawable.ic_wrong);
            txvRightAnswer.setText(AssesmentService.getInstance().getQuestion());
            lilRightAnswer.setVisibility(View.VISIBLE);
        }
        //update graphical components
        imvBadgeAnswer.setVisibility(View.VISIBLE);
        imbNextQuestion.setVisibility(View.VISIBLE);
        tblKeyboard.setVisibility(View.GONE);
        switch (AchievementLevel.getLevel(score,elapsedTime,questionNumber)){
            case AchievementLevel.SUPER_HERO:
                imvScoreBadge.setImageResource(R.mipmap.ic_badge_superhero);
                break;
            case AchievementLevel.HERO:
                imvScoreBadge.setImageResource(R.mipmap.ic_badge_hero);
                break;
            case AchievementLevel.GOOD:
                imvScoreBadge.setImageResource(R.mipmap.ic_badge_good);
                break;
            case AchievementLevel.FAILED:
                imvScoreBadge.setImageResource(R.mipmap.ic_badge_failed);
                break;
        }
        //if the limit number of questions is reach
        stopIfAssessmentOver();
    }

    /**
     * Keyboard call back
     * @param v
     */
    public void keyboardCallback(View v){
        int maxValue=0;
        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
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
            case R.id.btn0:
                maxValue=0;
                break;
            case R.id.btnOk:
                questionAnswered();
                return;
            case R.id.btnDelete:
                userAnswer = userAnswer /10;
                txvQuestion.setText(AssesmentService.getInstance().getQuestion(userAnswer));
                return;
            default:
                return;
        }

        userAnswer = userAnswer *10+maxValue;
        txvQuestion.setText(AssesmentService.getInstance().getQuestion(userAnswer));
    }
    /** This method increment the bar */
    private void incrementBar() {
        // increment the bar by 1
            bar.incrementProgressBy(-1);
    }
    private int getUserAnswer() {
        try{
            return userAnswer;
        }catch(NumberFormatException ex){
            return -1;
        }
    }

    private void updateTime(){
        elapsedTime++;
        txvElapsedTime.setText(getString(R.string.mainact_txvTemps,(120-elapsedTime)));
    }

    /***********************************************************
     *  MAnaging the stop conditions
     **********************************************************/
    private void stopIfAssessmentOver(){
        //two condition
        if(elapsedTime>=120
                || questionNumber>=MyApplication.ins().getMaxQuestionNumber()){
            //firsts top the timer
            chronoHandler.removeCallbacks(chronoRunnable);
            tblKeyboard.setVisibility(View.GONE);
            shouldCallFinish=true;
            Log.e(TAG,"stopIfAssessmentOver shouldCallFinish"+shouldCallFinish);
            //then stop the assessment:
            //make an animation on the prgress bar if it's the time
            //on the txv questNum else
            //=> store the records
            //=> make a nice animation to congrats
            showCongrats();
            //=>back to Main
//            finish();
        }
    }

    private static final String CONGRATS_DIALOG = "congratsDialog";
    /**
     * display the Congrats Dialog
     */
    private void showCongrats() {
        CongratsDialog congrats= (CongratsDialog) getSupportFragmentManager().findFragmentByTag("congratsDialog");
        if(congrats==null){
            congrats=new CongratsDialog();
            congrats.setAchievementLevel(AchievementLevel.getLevel(score,elapsedTime,questionNumber),
                    score,
                    (int) elapsedTime,
                    MyApplication.ins().getMaxMultiplicationValue());
            getSupportFragmentManager().beginTransaction().add(congrats, CONGRATS_DIALOG).commit();
        }else{
            congrats.setAchievementLevel(AchievementLevel.getLevel(score,elapsedTime,questionNumber),
                    score,
                    (int) elapsedTime,
                    MyApplication.ins().getMaxMultiplicationValue());
            getSupportFragmentManager().beginTransaction().show(congrats).commit();
        }
    }

}
