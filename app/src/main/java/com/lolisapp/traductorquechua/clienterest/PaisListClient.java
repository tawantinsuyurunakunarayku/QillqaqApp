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
import com.lolisapp.traductorquechua.bean.Pais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by USUARIO on 07/12/2017.
 */

public class PaisListClient {

    public static final String RESOURCE = "country";
    private static final String TAG = "insertar usuario nuevo";
    Context context;
    PaisListClient.PaisListClientListener listener;

    public PaisListClient(Context context, PaisListClient.PaisListClientListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void getListCountry() {
        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion),context.getString(R.string.codigoAutorizacion));
        cabecera.put(context.getString(R.string.tipoContenido), context.getString(R.string.tipoJson));

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.ubas)+RESOURCE,
                null
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("correcto",response.toString());
                        try {
                            ArrayList<Pais> listaPais=getPaisFromJson(response);

                                listener.onSuccess(listaPais);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        requestQueue.add(jsonObjectRequest);


    }

    private ArrayList<Pais> getPaisFromJson(JSONObject jsonObject) throws JSONException {
        ArrayList<Pais> listaPais=new ArrayList<>();
        JSONArray jsonArray=jsonObject.getJSONArray("countries");

        for (int i = 0; i <jsonArray.length() ; i++) {
            Pais pais=new Pais();
            pais.setIdPais(jsonArray.getJSONObject(i).getLong("country_id"));
            pais.setNombre(jsonArray.getJSONObject(i).getString("name"));
            listaPais.add(pais);

        }

        return listaPais;
    }




    public interface PaisListClientListener {
        void onSuccess(ArrayList<Pais> listaPais);

        void onError(String message);

    }
}