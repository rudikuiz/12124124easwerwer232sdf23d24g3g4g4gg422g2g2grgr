package com.piramidsoft.wablastadmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.piramidsoft.wablastadmin.Adapter.ProcessAdapter;
import com.piramidsoft.wablastadmin.Model.ProcessModel;
import com.piramidsoft.wablastadmin.Utils.AppConf;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;
import com.piramidsoft.wablastadmin.Utils.SessionManager;
import com.piramidsoft.wablastadmin.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProcessActivity extends AppCompatActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.rvLogs)
    RecyclerView rvLogs;
//    @BindView(R.id.Swipe)
//    SwipeRefreshLayout Swipe;

    OwnProgressDialog loading;
    ProcessAdapter adapter;
    @BindView(R.id.btBack)
    ImageButton btBack;
    @BindView(R.id.pbLoading)
    GoogleProgressBar pbLoading;
    @BindView(R.id.txPengirim)
    TextView txPengirim;
    @BindView(R.id.txCount)
    TextView txCount;
    @BindView(R.id.txInterval)
    TextView txInterval;
    @BindView(R.id.pbCenter)
    ProgressBar pbCenter;
    @BindView(R.id.txFrekuensi)
    TextView txFrekuensi;
    private boolean refresh;
    private ArrayList<ProcessModel> dataSet;
    private ArrayList<ProcessModel> dataFix;
    private Handler handler;
    private Handler handlerInterval;
    //    private int[] DELAY = {5000, 5500, 6000};
    private int[] DELAY = {2000, 2500, 3000}; // ORI
    //    private int[] DELAY = {500, 700, 900};
    private int POSISI = 0;
    private int INTERVAl = 1;
    private SessionManager sessionManager;
    private String id;
    private String url;
    private boolean bottom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ButterKnife.bind(this);

        handler = new Handler();
        handlerInterval = new Handler();
        sessionManager = new SessionManager(ProcessActivity.this);
        id = getIntent().getStringExtra("id");
        if (id.equals("0")) {

            String freq = getIntent().getStringExtra("frekuensi");
            txFrekuensi.setText(getString(R.string.frekuensi) + " : " + freq);

        }
        url = AppConf.URL_LOGS_NUMBER;

        refresh = false;
        POSISI = 0;
        INTERVAl = 1;
        dataSet = new ArrayList<>();
        dataFix = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(ProcessActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        rvLogs.setLayoutManager(layoutManager);
        adapter = new ProcessAdapter(dataFix, ProcessActivity.this);
        loading = new OwnProgressDialog(ProcessActivity.this);
        rvLogs.setAdapter(adapter);

        txPengirim.setText(getString(R.string.pengirim) + " : " + sessionManager.getPhone());

//        loading.show();
//        Swipe.setRefreshing(true);
//        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                refresh = true;
//                getData();
//
//            }
//
//
//        });


        if (!id.equals("0")) {

            url = AppConf.URL_LOGS_NUMBER_DONE;
            pbLoading.setVisibility(View.GONE);
            pbCenter.setVisibility(View.VISIBLE);

        } else {
            pbLoading.setVisibility(View.VISIBLE);

        }

        getData();


    }

    private void getData() {


        Log.d("url", url + " ;; " + id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("url", response);


                try {

                    if (refresh) {
                        dataSet.clear();
                    }

                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jos = ja.getJSONObject(i);

                        String id = jos.getString("id_blast");
                        String waktu = jos.getString("waktu");
                        String pesan = jos.getString("pesan");
                        String imsi = jos.getString("imsi");
                        String nomor = jos.getString("nomor");
                        String status = jos.getString("status");
                        String foto = jos.getString("foto");
                        String nama = jos.getString("nama");
                        String lokasi = jos.getString("lokasi");
                        String wa = jos.getString("wa");
                        String tg = jos.getString("tg");

                        ProcessModel model = new ProcessModel();
                        model.setId(id);
                        model.setTgl(waktu);
                        model.setTeks(pesan);
                        model.setImsi(imsi);
                        model.setNomor(nomor);
                        model.setStatus(status);
                        model.setFoto(foto);
                        model.setNama(nama);
                        model.setLokasi(lokasi);
                        model.setWa(wa);
                        model.setTg(tg);
                        dataSet.add(model);

                    }

//                    adapter.notifyDataSetChanged();


                    if (id.equals("0")) {

                        handlerInterval.postDelayed(runnableIntervel, 1000);
                        handler.postDelayed(runnable, DELAY[0]);

                    } else {

                        pbLoading.setVisibility(View.GONE);

//                        handlerInterval.removeCallbacks(runnableIntervel);

                        String count = jo.getString("count");
                        String interval = jo.getString("interval");
                        String frekuensi = jo.getString("frekuensi");

                        txFrekuensi.setText(getString(R.string.frekuensi) + " : " + frekuensi);
                        txCount.setText(getString(R.string.count) + " : " + count);
                        txInterval.setText(getString(R.string.interval) + " : " + interval + "s");


                        adapter = new ProcessAdapter(dataSet, ProcessActivity.this);
                        rvLogs.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //ShowToast("Request Timeout");
                }


                LocalBroadcastManager.getInstance(ProcessActivity.this).registerReceiver(broadcastReceiver, new IntentFilter(getString(R.string.count)));

                pbCenter.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbCenter.setVisibility(View.GONE);

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("page", "0");
                params.put("id_blast", id);

                return params;
            }
        };

        stringRequest.setTag(FragmentLogs.class.getSimpleName());
        VolleyHttp.getInstance(ProcessActivity.this).addToRequestQueue(stringRequest);

    }


    public Runnable runnableIntervel = new Runnable() {
        @Override
        public void run() {

            txInterval.setText(getString(R.string.interval) + " : " + INTERVAl + "s");
            INTERVAl++;

            if (POSISI < dataSet.size()) {
                handlerInterval.postDelayed(runnableIntervel, 1000);
            }

        }
    };

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Date oldDate = new Date(); // oldDate == current time
            Date newDate = new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(3)); // Adds 3 hours

            if (POSISI < dataSet.size()) {

                String dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oldDate);

                dataSet.get(POSISI).setTgl(dates);
                dataFix.add(dataSet.get(POSISI));
                adapter.notifyItemChanged(POSISI);

                if (bottom) {
                    rvLogs.smoothScrollToPosition(POSISI);
                }

                POSISI++;

                txCount.setText(getString(R.string.count) + " : " + POSISI);


                Random rn = new Random();
                int range = (DELAY.length - 1) - 0 + 1;
                int randomNum = rn.nextInt(range) + 0;
                handler.postDelayed(runnable, DELAY[randomNum]);
            } else {
                setDone();
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(ProcessActivity.this, "Done", Toast.LENGTH_LONG).show();
            }

        }
    };


    private void setDone() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_SET_PROSES_DONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("page", "0");
                params.put("id_blast", id);

                return params;
            }
        };

        stringRequest.setTag(FragmentLogs.class.getSimpleName());
        VolleyHttp.getInstance(ProcessActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(ProcessActivity.this).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(runnable);
        handlerInterval.removeCallbacks(runnableIntervel);
        VolleyHttp.getInstance(ProcessActivity.this).cancelPendingRequests(FragmentLogs.class.getSimpleName());
    }

    @OnClick(R.id.btBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String b = intent.getStringExtra("pos");
            if (b.equals("bottom")) {

                bottom = true;
            } else {

                bottom = false;
            }

        }
    };

}