/**
 * <ul>
 * <li>AchievementLevel</li>
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

package com.android2ee.basile.multiplication.cross;

import com.android2ee.basile.multiplication.MyApplication;

/**
 * Created by Mathias Seguy - Android2EE on 05/03/2017.
 */
public class AchievementLevel {
    public static final int FAILED = -1;
    public static final int GOOD = 0;
    public static final int HERO = 1;
    public static final int SUPER_HERO = 2;
    /**
     * Depending on the parameters below, it returns your level
     * Calcul is based one this max version
     * @param score
     * @param elapsedTime
     * @return
     */
    public static int getLevel(int score, long elapsedTime){
        return getLevel(score,elapsedTime, MyApplication.ins().getMaxQuestionNumber());
    }
    /**
     * Depending on the parameters below, it returns your level
     * @param score
     * @param elapsedTime
     * @param numberOfQuestionAnswered
     * @return
     */
    public static int getLevel(int score, long elapsedTime, int numberOfQuestionAnswered){
        //ok, I undrestand better when numberOfQuestionAnswered == 20
        int questionsNum=20;
        int relativeScore=(score*20)/numberOfQuestionAnswered;
        //between 0 - 9 => Failed
        //10-15=> Good
        //15-18 HERO if elepasedTime >120 SUPER_HERO else
        //18-20 SUPER_HERO
        if(relativeScore<=9){
            return FAILED;
        }else if(relativeScore<15){
            return  GOOD;
        }else if(relativeScore<18){
            if(elapsedTime>120){
                return HERO;
            }else {
                return  SUPER_HERO;
            }
        }else{
            return SUPER_HERO;
        }

    }
}
