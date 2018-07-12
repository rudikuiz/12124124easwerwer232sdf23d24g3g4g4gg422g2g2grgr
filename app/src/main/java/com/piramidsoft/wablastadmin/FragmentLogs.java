package com.piramidsoft.wablastadmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.wablastadmin.Adapter.LogAdapter;
import com.piramidsoft.wablastadmin.Model.LogModel;
import com.piramidsoft.wablastadmin.Utils.AppConf;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;
import com.piramidsoft.wablastadmin.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentLogs extends Fragment {

    @BindView(R.id.rvLogs)
    RecyclerView rvLogs;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    Unbinder unbinder;
    OwnProgressDialog loading;
    FragmentActivity mActivity;
    LogAdapter adapter;
    private boolean refresh;
    private ArrayList<LogModel> dataSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();

        refresh = false;
        dataSet = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        rvLogs.setLayoutManager(layoutManager);
        adapter = new LogAdapter(dataSet, mActivity);
        loading = new OwnProgressDialog(mActivity);
        rvLogs.setAdapter(adapter);

//        loading.show();
        Swipe.setRefreshing(true);
        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh = true;
                getData();

            }


        });

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(onRefresh, new IntentFilter(getString(R.string.refresh)));
        getData();
        return view;
    }

    private void getData() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (refresh) {
                        dataSet.clear();
                    }

                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jos = ja.getJSONObject(i);

                        String id = jos.getString("id");
                        String created_at = jos.getString("created_at");
                        String pengirim = jos.getString("pengirim");
                        String pesan = jos.getString("pesan");
                        String total = jos.getString("total");
                        String flag = jos.getString("flag");


                        LogModel model = new LogModel();
                        model.setTgl(created_at);
                        model.setPengirim(pengirim);
                        model.setTeks(pesan);
                        model.setCount(total);
                        model.setStatus(flag);

                        dataSet.add(model);
                    }

                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                    //ShowToast("Request Timeout");
                }


                Swipe.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Swipe.setRefreshing(false);
            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("page", "0");

                return params;
            }
        };

        stringRequest.setTag(FragmentLogs.class.getSimpleName());
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    public BroadcastReceiver onRefresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            refresh = true;
            getData();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        VolleyHttp.getInstance(mActivity).cancelPendingRequests(FragmentLogs.class.getSimpleName());
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onRefresh);
    }
}
