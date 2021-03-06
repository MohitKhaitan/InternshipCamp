package com.android.internshipcamp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internshipcamp.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nilesh Singh on 13-12-2015.
 */
public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    Context context;
    List<ParseObject> list;
    ParseRelation relation;
    ParseUser user;
    String comNameStr;
    boolean[] favs;
    int classNum;
    int count = 0;

    public BookmarksAdapter(Context context, List<ParseObject> list, int classNum) {
        this.context = context;
        this.list = list;
        this.classNum = classNum;
        favs = new boolean[this.list.size()];
        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView comName, comSite, location, period;
        RecyclerView recyclerView;
        CircleImageView civ;
        ImageButton stipend, addToFav;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            comName = (TextView) itemView.findViewById(R.id.comName);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.MainInRecycler);
            civ = (CircleImageView) itemView.findViewById(R.id.civMain);
            comSite = (TextView) itemView.findViewById(R.id.comSite);
            location = (TextView) itemView.findViewById(R.id.location);
            period = (TextView) itemView.findViewById(R.id.period);
            stipend = (ImageButton) itemView.findViewById(R.id.stipend);
            addToFav = (ImageButton) itemView.findViewById(R.id.addToFav);
            card = (CardView) itemView.findViewById(R.id.cardMain);
        }

        public TextView getComName() {
            return comName;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public CircleImageView getCiv() {
            return civ;
        }

        public TextView getComSite() {
            return comSite;
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getPeriod() {
            return period;
        }

        public ImageButton getStipend() {
            return stipend;
        }

        public ImageButton getAddToFav() {
            return addToFav;
        }

        public CardView getCard() {
            return card;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_bookmarks, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        final ParseObject obj = list.get(position);
        comNameStr = obj.get("comName").toString();
        h.getComName().setText(comNameStr);

        final String comSiteStr = obj.get("comSite").toString();
        h.getComSite().setText(comSiteStr);

        final ArrayList<String> list1 = new ArrayList<>();
        JSONArray array = obj.getJSONArray("comRequirements");

        for (int i = 0; i < array.length(); i++) {
            try {
                list1.add(array.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        h.getRecyclerView().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        h.getRecyclerView().setAdapter(new ReqAdapter(context, list1));

        obj.getParseFile("comLogo").getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    h.getCiv().setImageBitmap(bmp);
                } else {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        String fLoc = "";
        int lenLoc = 0;
        JSONArray locArray = obj.getJSONArray("comLocations");
        try {
            fLoc = locArray.get(0).toString();
            lenLoc = locArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (lenLoc > 1) {
            h.getLocation().setText(fLoc + " +" + String.valueOf(lenLoc - 1));
        } else if (lenLoc == 1) {
            h.getLocation().setText(fLoc);
        } else {
            h.getLocation().setText("Sorry!");
        }


        String periodStr = obj.get("comPeriod").toString();
        h.getPeriod().setText(periodStr);

        boolean isStipend = Boolean.valueOf(obj.get("comStipend").toString());
        if (isStipend) {
            h.getStipend().setImageResource(R.mipmap.ic_attach_money_black_48px);
        } else {
            h.getStipend().setImageResource(R.mipmap.ic_money_off_black_48px);
        }

        h.getCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        h.getAddToFav().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);

                final String name = obj.get("comName").toString();

                user = ParseUser.getCurrentUser();
                relation = user.getRelation("userBookmarks");
                relation.remove(obj);
                user.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(context, "Removed " + name, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
