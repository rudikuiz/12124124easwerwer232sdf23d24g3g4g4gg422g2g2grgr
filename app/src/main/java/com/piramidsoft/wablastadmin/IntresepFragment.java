package com.piramidsoft.wablastadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.wablastadmin.Adapter.SpyAdapter;
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
import butterknife.Unbinder;

import static com.piramidsoft.wablastadmin.Utils.AppConf.URL_LIST_SPY;

public class IntresepFragment extends Fragment {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.rvSpy)
    RecyclerView rvSpy;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    Unbinder unbinder;

    OwnProgressDialog loading;
    FragmentActivity mActivity;
    SpyAdapter adapter;
    private boolean refresh;
    private ArrayList<SpyModel> arrayList = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intresep_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();
        sessionManager = new SessionManager(mActivity);

        rvSpy.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        rvSpy.setLayoutManager(layoutManager);

        loading = new OwnProgressDialog(mActivity);
        requestQueue = Volley.newRequestQueue(mActivity);

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
        return view;
    }

    private void getData() {
        stringRequest = new StringRequest(Request.Method.POST, URL_LIST_SPY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        SpyModel modelMenu = new SpyModel();
                        modelMenu.setNomor(json.getString("nomor"));
                        modelMenu.setImsi(json.getString("imsi"));
                        modelMenu.setStatus("Success");

                        arrayList.add(modelMenu);
                    }
                    adapter = new SpyAdapter(arrayList, mActivity);
                    rvSpy.setAdapter(adapter);
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

//        {
//
//            @Override
//            protected Map<String, String> getParams() {
//                sessionManager = new SessionManager(mActivity);
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("nomor", sessionManager.getPhone());
//                Log.d("params", String.valueOf(params));
//
//                return params;
//            }
//
//        }

        ;

        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
