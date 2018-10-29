package com.piramidsoft.wablastadmin;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.Unbinder;

public class FragmentIndosat extends Fragment {


    @BindView(R.id.btDown)
    ImageButton btDown;
    @BindView(R.id.btSearch)
    ImageButton btSearch;
    @BindView(R.id.txPengirim)
    TextView txPengirim;
    @BindView(R.id.txFrekuensi)
    TextView txFrekuensi;
    @BindView(R.id.txInterval)
    TextView txInterval;
    @BindView(R.id.txCount)
    TextView txCount;
    @BindView(R.id.pbLoading)
    GoogleProgressBar pbLoading;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.rvLogs)
    RecyclerView rvLogs;
    @BindView(R.id.pbCenter)
    ProgressBar pbCenter;
    Unbinder unbinder;
    @BindView(R.id.btUp)
    ImageButton btUp;
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
    FragmentActivity mActivity;
    OwnProgressDialog loading;
    ProcessAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_indosat, container, false);
        unbinder = ButterKnife.bind(this, view);

        mActivity = getActivity();
        handler = new Handler();
        handlerInterval = new Handler();
        sessionManager = new SessionManager(mActivity);
        isGetData = false;
        selectedPosition = 0;
        id = mActivity.getIntent().getStringExtra("id");
        if (id.equals("0")) {

            String freq = mActivity.getIntent().getStringExtra("frekuensi");
            txFrekuensi.setText(getString(R.string.frekuensi) + " : " + freq);

            if (freq.equals("2100 MHz")) {
                DELAY.clear();
                DELAY.add(5000);
                DELAY.add(5500);
                DELAY.add(6000);
            } else {
                DELAY.clear();
//                DELAY.add(2000);
//                DELAY.add(2500);
//                DELAY.add(3000);

                DELAY.add(1500);
                DELAY.add(2000);
                DELAY.add(2500);
            }

        }
        url = AppConf.URL_LOGS_NUMBER_ISAT;

        POSISI = 0;
        INTERVAl = 1;
        dataSet = new ArrayList<>();
        dataFix = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        rvLogs.setLayoutManager(layoutManager);
        adapter = new ProcessAdapter(dataFix, mActivity);
        loading = new OwnProgressDialog(mActivity);
        rvLogs.setAdapter(adapter);
        dataSet.clear();

        txPengirim.setText(getString(R.string.pengirim) + " : " + sessionManager.getPhoneIsat());


        if (!id.equals("0")) {

            url = AppConf.URL_LOGS_NUMBER_DONE;
            pbLoading.setVisibility(View.GONE);
            pbCenter.setVisibility(View.VISIBLE);

        } else {
            pbLoading.setVisibility(View.VISIBLE);
            handlerInterval.postDelayed(runnableIntervel, 1000);

        }

        getData();

        return view;
    }


    private void getData() {

        isGetData = true;

        Log.d("url", url + " ;; " + id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("url", response);


                try {

//                    dataSet.clear();

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


                        adapter = new ProcessAdapter(dataSet, mActivity);
                        rvLogs.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //ShowToast("Request Timeout");
                }


                if(getActivity() != null) {
                    LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver, new IntentFilter(getString(R.string.count)));
                    pbCenter.setVisibility(View.GONE);
                }

                isGetData = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                isGetData = false;
                handler.postDelayed(runnable, DELAY.get(0));
                if(getActivity() != null) {
                    pbCenter.setVisibility(View.GONE);
                }


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

        stringRequest.setTag(ProcessBActivity.class.getSimpleName());
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }


    private void checkProcess() {


//        Log.d("url", url + " ;; " + id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGS_PROCESS_ISAT, new Response.Listener<String>() {
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

                if(getActivity() != null) {
                    pbCenter.setVisibility(View.GONE);
                }
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

        stringRequest.setTag(ProcessBActivity.class.getSimpleName());
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }


    public Runnable runnableIntervel = new Runnable() {
        @Override
        public void run() {

            txInterval.setText(getString(R.string.interval) + " : " + INTERVAl + "s");
            INTERVAl++;

//            if (POSISI < dataSet.size()) {
            handlerInterval.postDelayed(runnableIntervel, 1000);
//            }

        }
    };

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            checkProcess();

            Date oldDate = new Date(); // oldDate == current time
            Date newDate = new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(3)); // Adds 3 hours

            Random rn = new Random();
            int range = (DELAY.size() - 1) - 0 + 1;
            int randomNum = rn.nextInt(range) + 0;

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


                if(getActivity() != null) {
                    handler.postDelayed(runnable, DELAY.get(randomNum));
                }else {
                    handler.removeCallbacks(runnable);
                    handlerInterval.removeCallbacks(runnableIntervel);
                }
            } else {
//                pbLoading.setVisibility(View.GONE);
//                Toast.makeText(mActivity, "Done", Toast.LENGTH_LONG).show();
//
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
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
                        if(getActivity() != null) {
                            getData();
                        }else {
                            handler.removeCallbacks(runnable);
                            handlerInterval.removeCallbacks(runnableIntervel);
                        }
                    }

                }


            }

        }
    };



    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(runnable);
        handlerInterval.removeCallbacks(runnableIntervel);
        VolleyHttp.getInstance(mActivity).cancelPendingRequests(ProcessBActivity.class.getSimpleName());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(runnable);
        handlerInterval.removeCallbacks(runnableIntervel);
        VolleyHttp.getInstance(mActivity).cancelPendingRequests(ProcessBActivity.class.getSimpleName());
        unbinder.unbind();

    }

    @OnClick({R.id.btUp, R.id.btDown, R.id.btSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btUp:
                rvLogs.scrollToPosition(0);
                break;
            case R.id.btDown:
                rvLogs.scrollToPosition(dataFix.size() - 1);
                break;
            case R.id.btSearch:
                searchDialog();
                break;
        }
    }

    private void searchDialog() {


        final Dialog dialogSearch = new Dialog(mActivity);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_search, null);


        final EditText etNomor = (EditText) view.findViewById(R.id.etNomor);
        Button btCari = (Button) view.findViewById(R.id.btCari);


        dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSearch.setContentView(view);

        dialogSearch.show();


        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        Toast.makeText(mActivity, getString(R.string.nomor_tidak_ditemukan), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
