package com.piramidsoft.wablastadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piramidsoft.wablastadmin.Model.LogModel;
import com.piramidsoft.wablastadmin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ayo Maju on 10/07/2018.
 * Updated by Muhammad Iqbal on 10/07/2018.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogHolder> {

    private ArrayList<LogModel> arrayList;
    private Context context;

    public LogAdapter(ArrayList<LogModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_log, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, int position) {
        holder.etTanggal.setText(arrayList.get(position).getTgl());
        holder.etPengirim.setText(arrayList.get(position).getPengirim());
        holder.etTeks.setText(arrayList.get(position).getTeks());
        holder.etCount.setText(arrayList.get(position).getCount());
        holder.etStatus.setText(arrayList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etTanggal)
        TextView etTanggal;
        @BindView(R.id.etPengirim)
        TextView etPengirim;
        @BindView(R.id.etTeks)
        TextView etTeks;
        @BindView(R.id.etCount)
        TextView etCount;
        @BindView(R.id.etStatus)
        TextView etStatus;


        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
