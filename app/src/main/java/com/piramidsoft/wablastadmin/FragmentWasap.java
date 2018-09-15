package com.piramidsoft.wablastadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.piramidsoft.wablastadmin.Utils.AppConf;
import com.piramidsoft.wablastadmin.Utils.MediaProcess;
import com.piramidsoft.wablastadmin.Utils.OwnProgressDialog;
import com.piramidsoft.wablastadmin.Utils.SessionManager;
import com.piramidsoft.wablastadmin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

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
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rbFreq)
    RadioGroup rbFreq;
    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;
    @BindView(R.id.btPhoto)
    Button btPhoto;

    private FragmentActivity mActivity;
    private OwnProgressDialog loading;
    private final int code = 1901;
    private String encodedImage;

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


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(mActivity, ProcessActivity.class);
                                intent.putExtra("id", "0");
                                intent.putExtra("frekuensi", getMhz());
                                mActivity.startActivity(intent);

                            }
                        }, 2000);


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
                params.put("frekuensi", getMhz());
                if (encodedImage != null) {
                    params.put("gambar", encodedImage);

                }


//                Log.d("SUMIT", params.toString());
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
                    new SessionManager(mActivity).setPhone(message);

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


    @OnClick({R.id.btSubmit, R.id.btClear, R.id.btPhoto})
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

            case R.id.btPhoto:

                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select Image"), code);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

                imgPhoto.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    public String getMhz() {

        int radioButtonID = rbFreq.getCheckedRadioButtonId();
        View radioButton = rbFreq.findViewById(radioButtonID);
        int idx = rbFreq.indexOfChild(radioButton);

        RadioButton r = (RadioButton) rbFreq.getChildAt(idx);
        String selectedtext = r.getText().toString();

        Log.d("SELD", selectedtext);

        return selectedtext;
    }
}
