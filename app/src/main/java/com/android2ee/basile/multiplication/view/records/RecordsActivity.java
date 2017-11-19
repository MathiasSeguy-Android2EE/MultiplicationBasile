package com.android2ee.basile.multiplication.view.records;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.model.Score;
import com.android2ee.basile.multiplication.service.AssesmentService;
import com.android2ee.basile.multiplication.view.mother.MotherActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends MotherActivity {
    private static final String TAG = "RecordsActivity";
    /**
     * Result == All added messages
     */
    private RecyclerView rcvRecords;
    /**
     * Listview's arrayaadapter
     */
    private ScoreRcvArrayAdapter arrayAdapter;
    /**
     * ListView 's dataset
     */
    private ArrayList<Score> dataSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.activity_records);
        getSupportActionBar().setTitle(getString(R.string.rec_act_toolbar_title));
        getSupportActionBar().setSubtitle(R.string.rec_act_toolbar_subtitle);
        rcvRecords = (RecyclerView) findViewById(R.id.rcvRecord);
        rcvRecords.setLayoutManager(new LinearLayoutManager(this));
        dataSet=new ArrayList<>();
        arrayAdapter=new ScoreRcvArrayAdapter(this,dataSet);
        rcvRecords.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //load data
        AssesmentService.getInstance().getRecordsTableAsynch();
        //the return is handled in the onEventScoreLoaded method using EventBus

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventScoreLoaded(List<Score> scores) {
        dataSet.clear();
        for (Score score : scores) {
            Log.e(TAG," ascore has been found for table "+score.getMultiplicationTable());
            dataSet.add(score);
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
