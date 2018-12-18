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
import com.google.gson.Gson;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.Constant;
import com.lolisapp.traductorquechua.util.volley.ErrorMessage;
import com.lolisapp.traductorquechua.util.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by USUARIO on 28/11/2017.
 */

public class ListPhrasesClient {

    public static final String RESOURCE = "phrases";
    private static final String TAG = "Service";
    Context context;
    ListPhrasesClient.AccionesListener listener;

    public ListPhrasesClient(Context context, ListPhrasesClient.AccionesListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void listPhrases(String email) {
        HashMap<String, String> parametros = new HashMap();

        HashMap<String, String> cabecera = new HashMap();
        parametros.put("email", email);
        Log.d("body",parametros.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);



        JSONObject bodyRequest=new JSONObject(parametros);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.ubas)+RESOURCE,
                bodyRequest
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("correcto",response.toString());
                        try {
                            listener.onSuccess(getTraduccionFromJson(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                   //     Log.d("incorrecto", String.valueOf(error.networkResponse.data));

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






    private ArrayList<TraduccionHistorica> getTraduccionFromJson(JSONObject jsonObject) throws JSONException {
        ArrayList<TraduccionHistorica> listaTraduccion=new ArrayList<>();
        JSONArray jsonArray=jsonObject.getJSONArray("phrases");

        for (int i = 0; i <jsonArray.length() ; i++) {
            TraduccionHistorica traduccionHistorica=new TraduccionHistorica();
            traduccionHistorica.setOriginal(jsonArray.getJSONObject(i).getString("text_spanish"));
            traduccionHistorica.setTraduccion(jsonArray.getJSONObject(i).getString("text_quechua"));
            traduccionHistorica.setFavorite(jsonArray.getJSONObject(i).getInt("like_flag"));
            traduccionHistorica.setId(jsonArray.getJSONObject(i).getInt("phrase_id"));
            listaTraduccion.add(traduccionHistorica);
        }

        Collections.sort(listaTraduccion, new Comparator<TraduccionHistorica>() {
            @Override
            public int compare(TraduccionHistorica t2, TraduccionHistorica t1)
            {

                return  t1.getId().compareTo(t2.getId());
            }
        });
        return listaTraduccion;
    }


    private String _handdledError(VolleyError error) {
        error.printStackTrace();
        String message = "";
        try {

            Log.d(TAG, "errorMessage" + new Gson().toJson(error));
            ErrorMessage errorMessage = VolleyErrorHelper.getErrorMessage(error, context);
            //Log.d(TAG, "errorMessage" + new Gson().toJson(errorMessage));
            if (errorMessage.getJsonError() != null) {
                message = errorMessage.getJsonError().getString(Constants.TAG_MESSAGE_MESSAGE);
                String errorCode = errorMessage.getJsonError().getString(Constants.TAG_MESSAGE_CODE);
                return message;
            } else
                message = context.getResources().getString(R.string.generic_error);
        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            message = context.getResources().getString(R.string.generic_error);
        }
        return message;
    }


    public interface AccionesListener {
        void onSuccess(ArrayList<TraduccionHistorica> listaTraduccion);

        void onError(String message);

    }
}