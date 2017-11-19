package com.android2ee.basile.multiplication.view.mother;

import android.support.v7.app.AppCompatActivity;

import com.android2ee.basile.multiplication.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Created by Mathias Seguy alias Android2ee on 16/11/2017.
 */

public class MotherActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.ins().onStartActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.ins().onStopActivity();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EmptyEvent scores) {
        //never used
        //Just avoid eventBus to fail at compilation time with its famous:
        //MainActivity and its super classes have no public methods with the @Subscribe annotation
    }

    /***********************************************************
     *  EmptyEventClass for EventBus to stop throwing exception
     *  MainActivity and its super classes have no public methods with the @Subscribe annotation
     **********************************************************/
    public class EmptyEvent{
    }
}
