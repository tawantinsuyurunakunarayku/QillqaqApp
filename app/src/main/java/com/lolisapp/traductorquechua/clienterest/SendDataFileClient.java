package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by USUARIO on 10/09/2017.
 */

public class SendDataFileClient {

    public static final String RESOURCE = "dataupload";
    Context context;
    ResponseListener listener;

    public SendDataFileClient(Context context, ResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void sendData(String email,String nameFile) {
        HashMap<String, String> parametros = new HashMap();
        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion),context.getString(R.string.codigoAutorizacion));
        cabecera.put(context.getString(R.string.tipoContenido), context.getString(R.string.tipoJson));
        parametros.put("email", email);
        parametros.put("name", nameFile);

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
                       Log.d("correcto",response.toString());

                        listener.onSuccess();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                  //      Log.d("incorrecto", String.valueOf(error.networkResponse.data));

                        String   message = context.getResources().getString(R.string.generic_error);
                        listener.onError(message);

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);


    }

    private User getUserFromJson(JSONObject jsonObject) throws JSONException {
        User user=new User();
        user.setEmail(jsonObject.getString("email"));
        user.setUserId(jsonObject.getJSONArray("user_id").getLong(0));
        user.setLastName(jsonObject.getString("last_name"));
        user.setFirstName(jsonObject.getString("first_name"));
        return user;
    }




    public interface ResponseListener {
        void onSuccess();

        void onError(String message);

    }
}