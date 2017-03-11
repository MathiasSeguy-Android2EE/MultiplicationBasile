package com.android2ee.basile.multiplication;

import android.util.Log;

/**
 * Created by Mathias Seguy - Android2EE on 07/03/2017.
 */

public class MyLilaApplication extends MyApplication {
    private static final String TAG = "MyLilaApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //I do somthing
        //like intializing a specific library
        Log.e("MyAppInitializer","Second choices, a log is enough to prove the concept: MyLilaApplication");
    }
}
