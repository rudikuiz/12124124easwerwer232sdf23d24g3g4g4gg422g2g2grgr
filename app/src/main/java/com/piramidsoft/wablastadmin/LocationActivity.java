package com.piramidsoft.wablastadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.wablastadmin.Adapter.GaleriAdapter;
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

import static com.piramidsoft.wablastadmin.Utils.AppConf.URL_DETAIL_SPY;

public class LocationActivity extends AppCompatActivity {
    OwnProgressDialog loading;

    private boolean refresh;
    private ArrayList<SpyModel> arrayList = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    SessionManager sessionManager;
    String tabel, imsi;
    SpyModel modelMenu = new SpyModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Intent intent = getIntent();
        tabel = intent.getStringExtra("tabel");
        imsi = intent.getStringExtra("imsi");
        loading = new OwnProgressDialog(LocationActivity.this);
        loading.show();
        requestQueue = Volley.newRequestQueue(LocationActivity.this);
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

                        modelMenu.setLat_cid(json.getString("lat_cid"));
                        modelMenu.setLong_cid(json.getString("lon_cid"));
                        modelMenu.setLat_gps(json.getString("lat_gps"));
                        modelMenu.setLong_gps(json.getString("lon_gps"));

                    }
                    Intent intent = new Intent(LocationActivity.this, LokasiActivity.class);
                    if (modelMenu.getLat_gps().equals("0") && modelMenu.getLong_gps().equals("0")){
                        intent.putExtra("lokasi", modelMenu.getLat_cid() +","+modelMenu.getLong_cid());

                    }else {
                        intent.putExtra("lokasi", modelMenu.getLat_gps() +","+modelMenu.getLong_gps());

                    }

                    intent.putExtra("nama","");
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                sessionManager = new SessionManager(LocationActivity.this);
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
}
