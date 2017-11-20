package com.android2ee.basile.multiplication.utils;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import java.util.Collection;

/**
 * Created by Created by Mathias Seguy alias Android2ee on 19/11/2017.
 */

public final class InstrumentedTestUtils {
    public static Activity getActivityInstance() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable( ) {
            public void run() {
                Activity currentActivity;
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity) resumedActivities.iterator().next();
                    activity[0] = currentActivity;
                }
            }
        });

        return activity[0];
    }
/***********************************************************
 * Utils methods
 **********************************************************/

    /**
     * To use like that
     * Assert.assertTrue(isExpectedActivity(AllianceActivity.class));
     *
     * @param expectedActivity
     * @return
     */
    public static boolean  isExpectedActivity(Class expectedActivity) {
        Activity activity = InstrumentedTestUtils.getActivityInstance();
        //you need the exact same class between both object
        return activity != null && expectedActivity.equals(activity.getClass());
    }
}
