package com.piramidsoft.wablastadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piramidsoft.wablastadmin.Model.SpyModel;
import com.piramidsoft.wablastadmin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.LogHolder> {


    private ArrayList<SpyModel> arrayList;
    private Context context;

    public KontakAdapter(ArrayList<SpyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_log_call, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, final int position) {
        int i = position + 1;
        holder.etNomor.setText(String.valueOf(i));
        holder.etItem.setText(arrayList.get(position).getKontak());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etNomor)
        TextView etNomor;
        @BindView(R.id.etItem)
        TextView etItem;
        @BindView(R.id.lyParent)
        LinearLayout lyParent;

        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
