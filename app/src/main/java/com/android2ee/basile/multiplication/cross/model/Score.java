/**
 * <ul>
 * <li>Score</li>
 * <li>com.android2ee.basile.multiplication.cross</li>
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

package com.android2ee.basile.multiplication.cross.model;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.orm.SugarRecord;

/**
 * Created by Mathias Seguy - Android2EE on 05/03/2017.
 */
public class Score extends SugarRecord implements Comparable<Score>{
    /***********************************************************
     *  Attributes
     **********************************************************/
    private static int NOY_SET=-1;
    private static boolean NOY_SET_BOOLEAN=false;
    private int score=NOY_SET;
    private int elapsedtime=NOY_SET;
    private int multiplicationTable=NOY_SET;
    private boolean thisTableOnly=NOY_SET_BOOLEAN;
    private String name;

    public Score() {
    }

    public Score(int elapsedtime, int multiplicationTable, int score, boolean thisTableOnly) {
        this.elapsedtime = elapsedtime;
        this.multiplicationTable = multiplicationTable;
        this.score = score;
        this.thisTableOnly = thisTableOnly;
        name= MyApplication.ins().getString(R.string.score_owner);
    }

    public int getMultiplicationTable() {
        return multiplicationTable;
    }

    public void setMultiplicationTable(int multiplicationTable) {
        this.multiplicationTable = multiplicationTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isThisTableOnly() {
        return thisTableOnly;
    }

    public void setThisTableOnly(boolean thisTableOnly) {
        this.thisTableOnly = thisTableOnly;
    }

    public int getElapsedtime() {
        return elapsedtime;
    }

    public void setElapsedtime(int elapsedtime) {
        this.elapsedtime = elapsedtime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Score{");
        sb.append("elapsedtime=").append(elapsedtime);
        sb.append(", score=").append(score);
        sb.append(", multiplicationTable=").append(multiplicationTable);
        sb.append(", thisTableOnly=").append(thisTableOnly);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
    public int compareTo(Score otherScore) {

        return this.getMultiplicationTable()-otherScore.getMultiplicationTable();

    }

}
