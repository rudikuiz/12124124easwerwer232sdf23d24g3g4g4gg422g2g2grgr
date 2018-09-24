package com.piramidsoft.wablastadmin.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piramidsoft.wablastadmin.LokasiActivity;
import com.piramidsoft.wablastadmin.Model.LogModel;
import com.piramidsoft.wablastadmin.Model.ProcessModel;
import com.piramidsoft.wablastadmin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ayo Maju on 10/07/2018.
 * Updated by Muhammad Iqbal on 10/07/2018.
 */

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.LogHolder> {


    private ArrayList<ProcessModel> arrayList;
    private Context context;

    public ProcessAdapter(ArrayList<ProcessModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_process, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, final int position) {
        holder.etTanggal.setText(arrayList.get(position).getTgl());
        holder.etTeks.setText(arrayList.get(position).getTeks());
        holder.etImsi.setText(arrayList.get(position).getImsi());
        holder.etNomor.setText(arrayList.get(position).getNomor());
        holder.etNama.setText(arrayList.get(position).getNama());

//        Glide.with(context).load(arrayList.get(position).getFoto()).placeholder(R.drawable.ic_profile).into(holder.imgPhoto);
//        Glide.with(context).load(arrayList.get(position).getFoto()).into(holder.imgPhoto);

        Glide.with(context).load(arrayList.get(position).getFoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgPhoto);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgDialog(arrayList.get(position));

            }
        });

        holder.imgLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LokasiActivity.class);
                intent.putExtra("lokasi", arrayList.get(position).getLokasi());
                intent.putExtra("nama", arrayList.get(position).getNama());
                context.startActivity(intent);

            }
        });

        ////////////////////

        if (arrayList.get(position).getWa().equals("1")) {
            if (arrayList.get(position).getWaSend().equals("1")) {
                holder.imgWa.setImageResource(R.drawable.ic_check_mark_double);
            } else {
                holder.imgWa.setImageResource(R.drawable.ic_check_mark);
            }

        } else if (arrayList.get(position).getWa().equals("0")) {

            holder.imgWa.setImageResource(R.drawable.ic_pending);

        } else {

            if (arrayList.get(position).getWaSend().equals("1")) {
                holder.imgWa.setImageResource(R.drawable.ic_failed);

            } else {
                holder.imgWa.setImageResource(R.drawable.ic_pending);
            }
        }

        ////////////////////
        if (arrayList.get(position).getTg().equals("1")) {

            if (arrayList.get(position).getTgSend().equals("1")) {
                holder.imgTg.setImageResource(R.drawable.ic_check_mark_double);
            } else {
                holder.imgTg.setImageResource(R.drawable.ic_check_mark);
            }

        } else if (arrayList.get(position).getTg().equals("0")) {

            holder.imgTg.setImageResource(R.drawable.ic_pending);

        }else {

            if (arrayList.get(position).getTgSend().equals("1")) {
                holder.imgTg.setImageResource(R.drawable.ic_failed);
            } else {
                holder.imgTg.setImageResource(R.drawable.ic_pending);
            }
        }


        if(arrayList.get(position).isSelected()){
            holder.lyParent.setBackgroundColor(Color.parseColor("#bf33cc33"));
        }else{
            holder.lyParent.setBackgroundColor(Color.WHITE);
        }

        ///////////////////
        if ((position >= getItemCount() - 1)) {
            bottom("bottom");
        } else {
            bottom("top");
        }

    }

    public void bottom(String pos) {

        Intent b = new Intent(context.getString(R.string.count));
        b.putExtra("pos", pos);
        LocalBroadcastManager.getInstance(context).sendBroadcast(b);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etTanggal)
        TextView etTanggal;
        @BindView(R.id.etTeks)
        TextView etTeks;
        @BindView(R.id.etImsi)
        TextView etImsi;
        @BindView(R.id.etNomor)
        TextView etNomor;
        @BindView(R.id.etNama)
        TextView etNama;
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.imgLokasi)
        ImageButton imgLokasi;
        @BindView(R.id.imgWa)
        ImageView imgWa;
        @BindView(R.id.imgTg)
        ImageView imgTg;
        @BindView(R.id.lyParent)
        LinearLayout lyParent;

        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void imgDialog(ProcessModel value) {


        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_foto, null);

        ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgFoto);
        LinearLayout lyParent = (LinearLayout) view.findViewById(R.id.lyParent);


        Glide.with(context).load(value.getFoto().replace("thumb_", "")).into(imgPhoto);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();

        lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

    }
}
