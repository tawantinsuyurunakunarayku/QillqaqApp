package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lolisapp.traductorquechua.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by USUARIO on 28/11/2017.
 */

public class PhrasesClient {

    public static final String RESOURCE = "favorite_phrases";
    private static final String TAG = "Service";
    Context context;
    PhrasesClient.AccionesListener listener;

    public PhrasesClient(Context context, PhrasesClient.AccionesListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void markFavorite(String email,String text,String likeFlag) {
        HashMap<String, String> parametros = new HashMap();

        HashMap<String, String> cabecera = new HashMap();
        parametros.put("email", email);
        parametros.put("text",text);
        parametros.put("like_flag",likeFlag);
        Log.d("body",parametros.toString());
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
                            Log.d("ser","sd");
                            listener.onSuccess();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            //  Log.d("incorrecto", String.valueOf(error.networkResponse.data));

                            Log.d("incorrecto", String.valueOf(error.getMessage()));
                            String   message = context.getResources().getString(R.string.generic_error);
                            listener.onError(message);

                        }
                    }
            ){

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {


                            return Response.success(
                                    null,
                                    null
                            );





                }
        };


        requestQueue.add(jsonObjectRequest);
    }




    public interface AccionesListener {
        void onSuccess();

        void onError(String message);

    }
}