package com.piramidsoft.wablastadmin;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.piramidsoft.wablastadmin.Utils.AndLog;
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
    @BindView(R.id.btBack)
    ImageButton btBack;
    @BindView(R.id.btSearch)
    ImageButton btSearch;
    @BindView(R.id.btUp)
    ImageButton btUp;
    @BindView(R.id.btDown)
    ImageButton btDown;
    private ArrayList<ProcessModel> dataSet;
    private ArrayList<ProcessModel> dataFix;
    private Handler handler;
    private Handler handlerInterval;
    //    private int[] DELAY = {5000, 5500, 6000};
//    private int[] DELAY = {2000, 2500, 3000}; // ORI
    private ArrayList<Integer> DELAY = new ArrayList<>();
    //    private int[] DELAY = {500, 700, 900};
    private int POSISI = 0;
    private int INTERVAl = 1;
    private SessionManager sessionManager;
    private String id;
    private String url;
    private boolean bottom = true;
    private boolean isGetData = false;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ButterKnife.bind(this);

        handler = new Handler();
        handlerInterval = new Handler();
        sessionManager = new SessionManager(ProcessActivity.this);
        isGetData = false;
        selectedPosition = 0;
        id = getIntent().getStringExtra("id");
        if (id.equals("0")) {

            String freq = getIntent().getStringExtra("frekuensi");
            txFrekuensi.setText(getString(R.string.frekuensi) + " : " + freq);

            if (freq.equals("2100 MHz")) {
                DELAY.clear();
                DELAY.add(5000);
                DELAY.add(5500);
                DELAY.add(6000);
            } else {
                DELAY.clear();
                DELAY.add(2000);
                DELAY.add(2500);
                DELAY.add(3000);
            }

        }
        url = AppConf.URL_LOGS_NUMBER;

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

        isGetData = true;

        Log.d("url", url + " ;; " + id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("url", response);


                try {

                    dataSet.clear();

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
                        String wasend = jos.getString("wasend");
                        String tgsend = jos.getString("tgsend");

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
                        model.setWaSend(wasend);
                        model.setTgSend(tgsend);
                        model.setSelected(false);
                        dataSet.add(model);

                    }

//                    adapter.notifyDataSetChanged();


                    if (id.equals("0")) {

                        handlerInterval.postDelayed(runnableIntervel, 1000);
                        handler.postDelayed(runnable, DELAY.get(0));

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


                isGetData = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                isGetData = false;

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

        stringRequest.setTag(ProcessActivity.class.getSimpleName());
        VolleyHttp.getInstance(ProcessActivity.this).addToRequestQueue(stringRequest);

    }


    private void checkProcess() {


//        Log.d("url", url + " ;; " + id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGS_PROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


//                Log.d("url", response);


                try {

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
                        String wasend = jos.getString("wasend");
                        String tgsend = jos.getString("tgsend");

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
                        model.setWaSend(wasend);
                        model.setTgSend(tgsend);
                        model.setSelected(false);


                        for (int x = 0; x < dataFix.size(); x++) {


                            ProcessModel dataOri = dataFix.get(x);

                            if (model.getNomor().equals(dataOri.getNomor())) {

                                if (dataOri.getWa().equals(model.getWa()) &&
                                        dataOri.getTg().equals(model.getTg()) &&
                                        dataOri.getWaSend().equals(model.getWaSend()) &&
                                        dataOri.getTgSend().equals(model.getTgSend())
                                        ) {

                                    // DO NOTHING

                                } else {
                                    dataFix.set(x, model);
                                    adapter.notifyItemChanged(x);
                                }

                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //ShowToast("Request Timeout");
                }

//                adapter.notifyDataSetChanged();

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

        stringRequest.setTag(ProcessActivity.class.getSimpleName());
        VolleyHttp.getInstance(ProcessActivity.this).addToRequestQueue(stringRequest);

    }


    public Runnable runnableIntervel = new Runnable() {
        @Override
        public void run() {

            txInterval.setText(getString(R.string.interval) + " : " + INTERVAl + "s");
            INTERVAl++;

//            if (POSISI < dataSet.size()) {
//                handlerInterval.postDelayed(runnableIntervel, 1000);
//            }

        }
    };

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            checkProcess();

            Date oldDate = new Date(); // oldDate == current time
            Date newDate = new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(3)); // Adds 3 hours

            AndLog.ShowLog("url", POSISI + " -- " + dataSet.size());
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
                int range = (DELAY.size() - 1) - 0 + 1;
                int randomNum = rn.nextInt(range) + 0;
                handler.postDelayed(runnable, DELAY.get(randomNum));
            } else {
//                pbLoading.setVisibility(View.GONE);
//                Toast.makeText(ProcessActivity.this, "Done", Toast.LENGTH_LONG).show();
//
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProcessActivity.this);
//                builder1.setMessage("Error 404");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                setDone();
//                                dialog.dismiss();
//                            }
//                        });
//
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();


                if (id.equals("0")) {

                    if (!isGetData) {
                        getData();
                    }

                }


            }

        }
    };


    private void setDone() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_SET_PROSES_DONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LocalBroadcastManager.getInstance(ProcessActivity.this).unregisterReceiver(broadcastReceiver);
                handler.removeCallbacks(runnable);
                handlerInterval.removeCallbacks(runnableIntervel);
                VolleyHttp.getInstance(ProcessActivity.this).cancelPendingRequests(ProcessActivity.class.getSimpleName());

                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                finish();

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

        stringRequest.setTag(ProcessActivity.class.getSimpleName());
        VolleyHttp.getInstance(ProcessActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(ProcessActivity.this).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(runnable);
        handlerInterval.removeCallbacks(runnableIntervel);
        VolleyHttp.getInstance(ProcessActivity.this).cancelPendingRequests(ProcessActivity.class.getSimpleName());
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

    @OnClick({R.id.btBack, R.id.btSearch, R.id.btUp, R.id.btDown})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProcessActivity.this);
                builder1.setMessage("Scan akan dihentikan?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                setDone();
                            }
                        });

                builder1.setNegativeButton(
                        "Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
            case R.id.btSearch:
                searchDialog();
                break;
            case R.id.btUp:
                rvLogs.scrollToPosition(0);
                break;
            case R.id.btDown:
                rvLogs.scrollToPosition(dataFix.size() - 1);
                break;
        }
    }

    private void searchDialog() {


        final Dialog dialogSearch = new Dialog(ProcessActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_search, null);


        final EditText etNomor = (EditText) view.findViewById(R.id.etNomor);
        Button btCari = (Button) view.findViewById(R.id.btCari);


        dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSearch.setContentView(view);

        dialogSearch.show();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogSearch.dismiss();

                String nomor = etNomor.getText().toString();

                if (nomor.isEmpty()) {
                    etNomor.setError(getString(R.string.masukkan_nomor));
                } else {

                    ProcessModel valueSelected = dataFix.get(selectedPosition);
                    valueSelected.setSelected(false);
                    dataFix.set(selectedPosition, valueSelected);
                    adapter.notifyItemChanged(selectedPosition);

                    boolean isExist = false;

                    for (int i = 0; i < dataFix.size(); i++) {

                        ProcessModel value = dataFix.get(i);

                        if (value.getNomor().contains(nomor)) {
                            value.setSelected(true);
                            selectedPosition = i;
                            rvLogs.scrollToPosition(i);
                            dataFix.set(i, value);
                            adapter.notifyItemChanged(i);
                            isExist = true;
                        }
                    }

                    if (!isExist) {
                        Toast.makeText(ProcessActivity.this, getString(R.string.nomor_tidak_ditemukan), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
