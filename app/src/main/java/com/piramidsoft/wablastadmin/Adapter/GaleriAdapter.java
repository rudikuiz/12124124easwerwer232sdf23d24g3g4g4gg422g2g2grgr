package com.piramidsoft.wablastadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piramidsoft.wablastadmin.FullscreenImageActivity;
import com.piramidsoft.wablastadmin.Model.SpyModel;
import com.piramidsoft.wablastadmin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.piramidsoft.wablastadmin.Utils.AppConf.BASE_URL;


public class GaleriAdapter extends RecyclerView.Adapter<GaleriAdapter.LogHolder> {


    private ArrayList<SpyModel> arrayList = new ArrayList<>();
    private Context context;

    public GaleriAdapter(ArrayList<SpyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override

    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_gambar, parent, false);
        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, final int position) {
//        Glide.with(context).load(BASE_URL + "/" + arrayList.get(position).getPathserver())
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(holder.imgFoto);

        Glide.with(context).load(BASE_URL + "/" + arrayList.get(position).getPathserver()).into(holder.imgFoto);

        holder.rlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullscreenImageActivity.class);
                intent.putExtra("url", BASE_URL + "/" + arrayList.get(position).getPathserver());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgFoto)
        ImageView imgFoto;
        @BindView(R.id.rlImg)
        RelativeLayout rlImg;

        public LogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
