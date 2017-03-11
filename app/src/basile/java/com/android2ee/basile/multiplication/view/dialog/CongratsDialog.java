/**
 * <ul>
 * <li>CongratsDialog</li>
 * <li>com.android2ee.basile.multiplication.view.dialog</li>
 * <li>05/03/2017</li>
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

package com.android2ee.basile.multiplication.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.AchievementLevel;

/**
 * Created by Mathias Seguy - Android2EE on 05/03/2017.
 */
public class CongratsDialog extends DialogFragment {
    /***********************************************************
     * Constants
     **********************************************************/
    private static final String TAG = "CongratsDialog";
    public static final String ACHIEVEMENT_LEVEL = "achievementLevel";
    public static final String SCORE = "score";
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String MULTIPLICATION_TABLE = "multiplicationTable";
    /***********************************************************
     * Attributes
     **********************************************************/
    int achievementLevel,score, elapsedTime,multiplicationTable;
    View view;
    TextView txvScore,txvMessageTitle,txvElapsedTime;
    ImageView  imvAchievementBadge;

    /***********************************************************
     * LifeCycle Management
     **********************************************************/
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //use the builder pattern
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        //if you have custom view implement it here
        view=LayoutInflater.from(getActivity()).inflate(R.layout.congrats_dialog,null);
        //and so do your find view by Id
        txvScore= (TextView) view.findViewById(R.id.txvScore);
        txvMessageTitle= (TextView) view.findViewById(R.id.txvMessageTitle);
        txvElapsedTime= (TextView) view.findViewById(R.id.txvElapsedTime);
        imvAchievementBadge= (ImageView) view.findViewById(R.id.imvAchievementBadge);

        //then create you Dialog itself
        builder.setIcon(R.mipmap.ic_captaindroid)
                // Set Dialog Title
                .setTitle("Game Over")
                // Set Dialog Message
                //.setMessage("Alert DialogFragment Tutorial")
                //or your specifc view
                .setView(view)
                //don't dismiss :
                //TODO find a solution
                .setCancelable(false)
                // Positive button
                .setPositiveButton(getString(R.string.congrats_dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle in onDissmiss
                    }
                });

        //then restore your data if rotation has occured
        if(savedInstanceState!=null){
            achievementLevel=savedInstanceState.getInt(ACHIEVEMENT_LEVEL);
            score=savedInstanceState.getInt(SCORE);
            elapsedTime=savedInstanceState.getInt(ELAPSED_TIME);
            multiplicationTable=savedInstanceState.getInt(MULTIPLICATION_TABLE);
        }
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        //update your AlertDialog according to your data model in this method
        AlertDialog dialog=((AlertDialog)getDialog());
        if(achievementLevel== AchievementLevel.SUPER_HERO){
            dialog.setTitle(getString(R.string.congrats_dialog_title_superhero));
            txvMessageTitle.setText(getString(R.string.congrats_dial_mess_superhero,multiplicationTable));
            imvAchievementBadge.setImageResource(R.mipmap.ic_badge_superhero);
        }else if(achievementLevel== AchievementLevel.HERO){
            dialog.setTitle(getString(R.string.congrats_dialog_title_hero));
            txvMessageTitle.setText(getString(R.string.congrats_dial_mess_hero));
            imvAchievementBadge.setImageResource(R.mipmap.ic_badge_hero);
        }else if(achievementLevel== AchievementLevel.GOOD){
            dialog.setTitle(getString(R.string.congrats_dialog_title_good));
            txvMessageTitle.setText(getString(R.string.congrats_dial_mess_good));
            imvAchievementBadge.setImageResource(R.mipmap.ic_badge_good);
        }else {//achievementLevel== AchievementLevel.FAILED
            dialog.setTitle(getString(R.string.congrats_dialog_title_failed));
            txvMessageTitle.setText(getString(R.string.congrats_dial_mess_failed));
            imvAchievementBadge.setImageResource(R.mipmap.ic_badge_failed);
        }
        txvScore.setText(getString(R.string.congrats_dial_score,score, MyApplication.ins().getMaxQuestionNumber()));
        txvElapsedTime.setText(getString(R.string.congrats_dial_elapsedtime,elapsedTime));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e(TAG,"onDismiss dialog");
                getActivity().finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ACHIEVEMENT_LEVEL,achievementLevel);
        outState.putInt(SCORE,score);
        outState.putInt(ELAPSED_TIME,elapsedTime);
        outState.putInt(MULTIPLICATION_TABLE,multiplicationTable);
    }


    /***********************************************************
     *  Business Methods
     **********************************************************/

    /**
     * Define the parameters of the dialog according to seom business rules
     * @param achievementLevel
     * @param score
     * @param elapsedTime
     */
    public void setAchievementLevel(int achievementLevel,int score,int elapsedTime,int multiplicationTable) {
        this.achievementLevel = achievementLevel;
        this.score=score;
        this.elapsedTime=elapsedTime;
        this.multiplicationTable=multiplicationTable;
    }
}
