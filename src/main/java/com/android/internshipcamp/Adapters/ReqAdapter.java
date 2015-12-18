package com.android.internshipcamp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internshipcamp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nilesh Singh on 14-12-2015.
 */
public class ReqAdapter extends RecyclerView.Adapter<ReqAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;

    public ReqAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_comp_req_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getSetter().setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView setter;
        public ViewHolder(View itemView) {
            super(itemView);
            setter = (TextView) itemView.findViewById(R.id.setterIN);
        }
        public TextView getSetter() { return setter; }
    }
}
