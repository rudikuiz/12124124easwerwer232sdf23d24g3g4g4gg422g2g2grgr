package com.piramidsoft.wablastadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.piramidsoft.wablastadmin.Utils.AppConf;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;
import com.piramidsoft.wablastadmin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentWasap extends Fragment {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.etNomor)
    EditText etNomor;
    @BindView(R.id.etText)
    EditText etText;
    @BindView(R.id.btSubmit)
    Button btSubmit;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    Unbinder unbinder;
    @BindView(R.id.btClear)
    Button btClear;

    private FragmentActivity mActivity;
    private OwnProgressDialog loading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wasap, container, false);
        unbinder = ButterKnife.bind(this, view);

        mActivity = getActivity();
        loading = new OwnProgressDialog(mActivity);
        GetNomor();

        return view;
    }

    public void Submit() {

        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_SENDBLAST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jo = new JSONObject(response);
                    String result = jo.getString("result");
                    String message = jo.getString("message");

                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(getString(R.string.refresh)));


                    if (result.equals("true")) {

                        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();

                        try {
                            Socket mSocket = IO.socket(AppConf.SIGNALLING_URL);
                            mSocket.connect();
                            mSocket.emit("send", "data");
//                            mSocket.disconnect();

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {

                        new AlertDialog.Builder(mActivity)
                                .setMessage(message)
                                .setPositiveButton("OK", null)
                                .show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, getString(R.string.gagal), Toast.LENGTH_LONG).show();

                }

                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(mActivity, getString(R.string.gagal), Toast.LENGTH_LONG).show();
            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("pengirim", etNomor.getText().toString().trim());
                params.put("pesan", etText.getText().toString().trim());


//                AndLog.ShowLog("SUMIT", params.toString());
                return params;
            }
        };

        stringRequest.setTag(FragmentWasap.class.getSimpleName());
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);
    }

    public void GetNomor() {

        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_PENGIRIM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jo = new JSONObject(response);
                    String message = jo.getString("message");
                    etNomor.setText(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, getString(R.string.gagal), Toast.LENGTH_LONG).show();

                }

                loading.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(mActivity, getString(R.string.gagal), Toast.LENGTH_LONG).show();
            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("page", "0");

//                AndLog.ShowLog("SUMIT", params.toString());
                return params;
            }
        };

        stringRequest.setTag(FragmentWasap.class.getSimpleName());
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        VolleyHttp.getInstance(mActivity).cancelPendingRequests(FragmentLogs.class.getSimpleName());
    }


    @OnClick({R.id.btSubmit, R.id.btClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btSubmit:
                if (!etText.getText().toString().isEmpty() && !etNomor.getText().toString().isEmpty()) {

                    Submit();
                }
                break;
            case R.id.btClear:
                etText.setText(null);
                etText.requestFocus();
                break;
        }
    }
}
