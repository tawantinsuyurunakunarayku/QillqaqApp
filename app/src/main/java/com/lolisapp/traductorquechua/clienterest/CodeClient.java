package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by USUARIO on 11/02/2018.
 */

public class CodeClient {
    public static final String RESOURCE = "code";
    actionListener listener;
    Context context;

    public CodeClient(Context context,actionListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void sendCode(String correo) {

        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", correo);

        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion), context.getString(R.string.codigoAutorizacion));

        RequestQueue requestQueue = Volley.newRequestQueue(context);



        JSONObject bodyRequest=new JSONObject(parametros);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.ubas)+RESOURCE,
                bodyRequest
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("correctoCode",response.toString());

                                listener.onSuccess();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        String   message = context.getResources().getString(R.string.generic_error);
                        listener.onError(message);

                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.TIME_REQUEST,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


    private User getUserFromJson(JSONObject jsonObject) throws JSONException {
        User user=new User();
        user.setEmail(jsonObject.getString("email"));
        user.setUserId(jsonObject.getLong("user_id"));
        user.setLastName(jsonObject.getString("last_name"));
        user.setFirstName(jsonObject.getString("first_name"));
        return user;
    }


    public interface actionListener {
        void onSuccess();

        void onError(String message);
    }


}