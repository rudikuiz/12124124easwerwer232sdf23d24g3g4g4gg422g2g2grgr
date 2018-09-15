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


public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.LogHolder> {



    private ArrayList<SpyModel> arrayList;
    private Context context;

    public SmsAdapter(ArrayList<SpyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sms, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, final int position) {

        holder.etNomor.setText(arrayList.get(position).getNomor());
        holder.etText.setText(arrayList.get(position).getInbox());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etNomor)
        TextView etNomor;
        @BindView(R.id.etText)
        TextView etText;

        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
