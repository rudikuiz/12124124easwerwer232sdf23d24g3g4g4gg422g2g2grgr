package com.piramidsoft.wablastadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentLogs extends Fragment {

    @BindView(R.id.rvLogs)
    RecyclerView rvLogs;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    Unbinder unbinder;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    OwnProgressDialog loading;
    FragmentActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();

        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        rvLogs.setLayoutManager(layoutManager);
        requestQueue = Volley.newRequestQueue(mActivity);
        loading = new OwnProgressDialog(mActivity);

        loading.show();
        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getData();

            }


        });
        return view;
    }

    private void getData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
