package com.piramidsoft.wablastadmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.piramidsoft.wablastadmin.Utils.AppConf;
import com.piramidsoft.wablastadmin.Utils.ViewPagerAdapter;
import com.piramidsoft.wablastadmin.Utils.VolleyHttp;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProcessBActivity extends AppCompatActivity {

    @BindView(R.id.label1)
    TextView label1;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.label2)
    TextView label2;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.label3)
    TextView label3;
    @BindView(R.id.lin3)
    LinearLayout lin3;
    @BindView(R.id.viewPagerHome)
    ViewPager viewPagerHome;
    @BindView(R.id.btBack)
    ImageButton btBack;
    private ViewPagerAdapter pagerAdapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_b);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        initView();
    }

    @Override
    public void onBackPressed() {

    }

    private void initView() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new FragmentTelkomsel(), "Telkomsel");
        pagerAdapter.addFragments(new FragmentXl(), "XL");
        pagerAdapter.addFragments(new FragmentIndosat(), "Indosat");
        viewPagerHome.setAdapter(pagerAdapter);

        viewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 2:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPagerHome.setOffscreenPageLimit(pagerAdapter.getCount());

    }

    private void setDone() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_SET_PROSES_DONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyHttp.getInstance(ProcessBActivity.this).cancelPendingRequests(ProcessBActivity.class.getSimpleName());

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

        stringRequest.setTag(ProcessBActivity.class.getSimpleName());
        VolleyHttp.getInstance(ProcessBActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHttp.getInstance(ProcessBActivity.this).cancelPendingRequests(ProcessBActivity.class.getSimpleName());

    }

    @OnClick({R.id.btBack, R.id.lin1, R.id.lin2, R.id.lin3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProcessBActivity.this);
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
            case R.id.lin1:
                viewPagerHome.setCurrentItem(0);
                break;
            case R.id.lin2:
                viewPagerHome.setCurrentItem(1);
                break;
            case R.id.lin3:
                viewPagerHome.setCurrentItem(2);
                break;
        }
    }
}
