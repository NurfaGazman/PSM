package com.example.psm.Controller;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestController extends StringRequest {

    private String token="";

//server URL ikut ipv4 (ipconfig) dlm cmd
    public static String serverUrl = "http://10.0.2.2:8080/PSM";// ip default test kt emulator
    //public static String serverUrl = "http://192.168.8.114:8080/PSM"; //check kt controlpanel
    private JSONObject body; //

    public RequestController(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, serverUrl + url, listener, errorListener);

    }

    public RequestController(int method, String url, JSONObject body, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, serverUrl + url, listener, errorListener);
        this.body = body;
    }

//wajib letak token tidak user xleh masuk
    public RequestController(int method, String url, JSONObject body,String token, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, serverUrl + url, listener, errorListener);
        this.body = body;
        this.token = token;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        if (!token.isEmpty())
            headers.put("Cookie","token=" + token);
        //headers.put("jwtT",jwt.getJwtToken());
        //headers.put("jwtP",jwt.getJwtPayload());
        //headers.put("uid",jwt.getUserId());

        if(body != null){
            headers.put("Content-Type", "application/json; charset=utf-8");
        }
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        //elaborate here with condition, if body null same if not null parse
        super.getBody();
        try {
            Log.d("body",body.toString());
            return body.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            Log.d("ERRPartsbody", uee.toString());
            return null;
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

}
