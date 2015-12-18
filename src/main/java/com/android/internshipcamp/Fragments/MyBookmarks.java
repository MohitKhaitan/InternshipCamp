package com.android.internshipcamp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internshipcamp.Adapters.BookmarksAdapter;
import com.android.internshipcamp.R;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class MyBookmarks extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView.LayoutManager llm;
    RecyclerView recycler;
    BookmarksAdapter adapter;
    SwipeRefreshLayout srl;
    FrameLayout frameLayout;
    TextView bEmpty, textOther;
    private ProgressBar pg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookmarks, container, false);

        llm = new LinearLayoutManager(getActivity());

        frameLayout = (FrameLayout) view.findViewById(R.id.myView);
        pg = (ProgressBar) view.findViewById(R.id.bookmarkPg);

        bEmpty = (TextView) view.findViewById(R.id.bookmarkEmpty);
        bEmpty.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "ABeeZee-Regular.ttf"));

        textOther = (TextView) view.findViewById(R.id.textOther);

        recycler = (RecyclerView) view.findViewById(R.id.recyclerMyInterns);
        recycler.setLayoutManager(llm);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(1000);
        recycler.setItemAnimator(animator);

        loadData(0);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srlMyInternships);
        srl.setColorSchemeColors(Color.parseColor("#ff4d4d"), Color.parseColor("#39add1"), Color.parseColor("#009d95"));
        srl.setOnRefreshListener(this);

        return view;
    }

    public void loadData(int isReload) {

        if (isReload == 0) {
            pg.setVisibility(View.VISIBLE);
        }

        ParseUser user = ParseUser.getCurrentUser();
        final ParseRelation relation = user.getRelation("userBookmarks");
        ParseQuery<ParseObject> query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    pg.setVisibility(View.GONE);
                    adapter = new BookmarksAdapter(getActivity(), objects, 1);
                    recycler.setAdapter(adapter);

                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);
                            ParseUser user1 = ParseUser.getCurrentUser();
                            ParseRelation relation1 = user1.getRelation("userBookmarks");
                            ParseQuery<ParseObject> query = relation1.getQuery();
                            relation1.getQuery().countInBackground(new CountCallback() {
                                @Override
                                public void done(int count, ParseException e) {
                                    if (e == null) {
                                        if (count == 0) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    backSetter();
                                                }
                                            }, 500);
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    if (objects.isEmpty()) {
                        backSetter();
                    }
                } else {
                    bEmpty.setVisibility(View.VISIBLE);
                    bEmpty.setText("Nothing here.");
                    pg.setVisibility(View.INVISIBLE);
                    textOther.setVisibility(View.VISIBLE);
                    textOther.setText("You don't seem to have bookmarked any company");
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    frameLayout.setBackgroundColor(Color.parseColor("#FF7043"));
                }
            }
        });
    }

    private void backSetter() {
        frameLayout.setBackgroundColor(Color.parseColor("#FF7043"));
        bEmpty.setVisibility(View.VISIBLE);
        bEmpty.setText("Nothing here.");
        textOther.setVisibility(View.VISIBLE);
        textOther.setText("You don't seem to have bookmarked any company");
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
                loadData(1);
            }
        }, 1000);

    }
}