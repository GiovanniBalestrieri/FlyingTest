package com.example.giovanni.bttest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giovanni.bttest.Libraries.sendStartTask;

/**
 * Created by userk on 12/04/15.
 */
public class Exploration extends Fragment {

    private View mContentView;
    private View mLoadingView;
    private int mAnimationDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.exploration, container, false);

        mContentView = v.findViewById(R.id.explo);

        // save the loading spinner
        mLoadingView = v.findViewById(R.id.loading_spinner);

        // remove content
        mContentView.setVisibility(View.GONE);

        // save the animation duration
        mAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);


        new sendStartTask(mContentView, mLoadingView, mAnimationDuration).execute();
        return v;
    }
}

