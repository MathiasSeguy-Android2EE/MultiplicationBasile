/**
 * <ul>
 * <li>ScoreRcvArrayAdapter</li>
 * <li>com.android2ee.acms.training.januarymmvxii.first.view.main.array_adapters</li>
 * <li>19/01/2017</li>
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

package com.android2ee.basile.multiplication.view.records;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android2ee.basile.multiplication.MyApplication;
import com.android2ee.basile.multiplication.R;
import com.android2ee.basile.multiplication.cross.AchievementLevel;
import com.android2ee.basile.multiplication.cross.model.Score;

import java.util.ArrayList;

/**
 * Created by Mathias Seguy - Android2EE on 19/01/2017.
 */
public class ScoreRcvArrayAdapter extends RecyclerView.Adapter<ScoreRcvArrayAdapter.ViewHolder>
        {
    private static final String TAG = "ScoreRcvArrayAdapter";
    /***********************************************************
     *  Attributes
     **********************************************************/
    /**
     * The inflater that parse the layout xml and builds the views tree
     */
    LayoutInflater inflater;
    /**
     * The view that displays elements
     */
    View rowView;
    /**
     * The object displayed
     */
    Score score;
    /**
     * The ViewHolder
     */
    ViewHolder vh;
    /**
     * The dataset
     */
    ArrayList<Score> dataset;

    public ScoreRcvArrayAdapter(Context context, ArrayList<Score> data) {
        inflater = LayoutInflater.from(context);
        dataset = data;
    }

    /***********************************************************
     * Managing Create and Bind
     **********************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = inflater.inflate(R.layout.item_record, parent, false);
        vh = new ScoreRcvArrayAdapter.ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        score =dataset.get(position);
        holder.getTxvTable().setText(MyApplication.ins().
                getString(R.string.item_record_table,score.getMultiplicationTable()));
        holder.getTxvScore().setText(MyApplication.ins().
                getString(R.string.item_record_score,score.getScore()));
        holder.getTxvRange().setText(score.isThisTableOnly()?
                MyApplication.ins().getString(R.string.item_range_thistableonly)
                :MyApplication.ins().getString(R.string.item_range_tablerange));
        holder.getTxvTime().setText(MyApplication.ins().
                getString(R.string.item_record_temps,score.getElapsedtime()));
        switch (AchievementLevel.getLevel(score.getScore(),score.getElapsedtime())){
            case AchievementLevel.SUPER_HERO:
                holder.getImvBadgeAnswer().setImageResource(R.mipmap.ic_badge_superhero);
                break;
            case AchievementLevel.HERO:
                holder.getImvBadgeAnswer().setImageResource(R.mipmap.ic_badge_hero);
                break;
            case AchievementLevel.GOOD:
                holder.getImvBadgeAnswer().setImageResource(R.mipmap.ic_badge_good);
                break;
            case AchievementLevel.FAILED:
                holder.getImvBadgeAnswer().setImageResource(R.mipmap.ic_badge_failed);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }




    /***********************************************************
     * ViewHolder pattern
     **********************************************************/


    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        /***********************************************************
         * Attributes
         **********************************************************/
        TextView txvTable,txvScore,txvTime,txvRange;
        ImageView imvBadgeAnswer;
        View view;
        /***********************************************************
        *  Constructors
        **********************************************************/
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            //instantiate graphical elements
            txvTable=(TextView)view.findViewById(R.id.txvTable);
            txvScore=(TextView)view.findViewById(R.id.txvScore);
//            txvRecordOwner=(TextView)view.findViewById(R.id.txvRecordOwner);
            imvBadgeAnswer= (ImageView) view.findViewById(R.id.imvBadgeAnswer);
            txvTime= (TextView) view.findViewById(R.id.txvTime);
            txvRange= (TextView) view.findViewById(R.id.txvRange);
        }

        public ImageView getImvBadgeAnswer() {
            return imvBadgeAnswer;
        }

        public TextView getTxvScore() {
            return txvScore;
        }

        public TextView getTxvTable() {
            return txvTable;
        }

        public TextView getTxvTime() {
            return txvTime;
        }

        public TextView getTxvRange() {
            return txvRange;
        }
    }
}
