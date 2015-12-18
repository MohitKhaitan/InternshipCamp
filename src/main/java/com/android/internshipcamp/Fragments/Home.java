package com.android.internshipcamp.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.internshipcamp.Adapters.MainAdapter;
import com.android.internshipcamp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    MainAdapter adapter;
    RecyclerView.LayoutManager llm;
    RecyclerView recycler;
    SwipeRefreshLayout srl;
    ParseQuery<ParseObject> query;
    ProgressBar pgBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        llm = new LinearLayoutManager(getActivity());

        recycler = (RecyclerView) v.findViewById(R.id.recyclerMain);
        recycler.setLayoutManager(llm);

        pgBar = (ProgressBar) v.findViewById(R.id.pgBar);

        loadMainData(false);

        srl = (SwipeRefreshLayout) v.findViewById(R.id.srlMain);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeColors(Color.parseColor("#ff5722"), Color.parseColor("#2196F3"), Color.parseColor("#009688"));

        return v;
    }

    void loadMainData(boolean isReload) {
        if (!isReload) {
            pgBar.setVisibility(View.VISIBLE);
        }

        query = ParseQuery.getQuery("Companies");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    adapter = new MainAdapter(getActivity(), objects, 0);
                    recycler.setAdapter(adapter);

                    if (pgBar.getVisibility() == View.VISIBLE) {
                        pgBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMainData(true);
                srl.setRefreshing(false);
            }
        }, 1500);

    }
}
