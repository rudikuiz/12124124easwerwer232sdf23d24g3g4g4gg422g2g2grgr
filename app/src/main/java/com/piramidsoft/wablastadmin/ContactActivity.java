package com.piramidsoft.wablastadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.wablastadmin.Adapter.KontakAdapter;
import com.piramidsoft.wablastadmin.Adapter.LogCallAdapter;
import com.piramidsoft.wablastadmin.Model.SpyModel;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;
import com.piramidsoft.wablastadmin.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.piramidsoft.wablastadmin.Utils.AppConf.URL_DETAIL_SPY;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.btBack)
    ImageButton btBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.actionBar)
    AppBarLayout actionBar;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.rvLogs)
    RecyclerView rvLogs;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;

    OwnProgressDialog loading;

    KontakAdapter adapter;
    private boolean refresh;
    private ArrayList<SpyModel> arrayList = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    SessionManager sessionManager;
    String tabel, imsi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        tabel = intent.getStringExtra("tabel");
        imsi = intent.getStringExtra("imsi");
        rvLogs.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(ContactActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        rvLogs.setLayoutManager(layoutManager);

        loading = new OwnProgressDialog(ContactActivity.this);
        requestQueue = Volley.newRequestQueue(ContactActivity.this);

        Swipe.setRefreshing(true);
        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getData();
            }
        });

        arrayList.clear();
        getData();
    }

    private void getData() {
        stringRequest = new StringRequest(Request.Method.POST, URL_DETAIL_SPY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        SpyModel modelMenu = new SpyModel();
                        modelMenu.setKontak(json.getString("kontak"));


                        if(!json.getString("kontak").isEmpty()) {
                            arrayList.add(modelMenu);
                        }
                    }
                    adapter = new KontakAdapter(arrayList, ContactActivity.this);
                    rvLogs.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
                if (Swipe != null) {
                    Swipe.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                sessionManager = new SessionManager(ContactActivity.this);
                Map<String, String> params = new HashMap<String, String>();

                params.put("tabel", tabel);
                params.put("imsi", imsi);

                Log.d("params", String.valueOf(params));

                return params;
            }

        }

        ;

        requestQueue.add(stringRequest);
    }


    @OnClick(R.id.btBack)
    public void onViewClicked() {
        finish();
    }
}
