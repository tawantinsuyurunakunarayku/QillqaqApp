package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.volley.CustomVolleyRequestQueue;
import com.lolisapp.traductorquechua.util.volley.ErrorMessage;
import com.lolisapp.traductorquechua.util.volley.GsonRequest;
import com.lolisapp.traductorquechua.util.volley.VolleyErrorHelper;

import java.util.HashMap;

/**
 * Created by USUARIO on 28/11/2017.
 */

public class PhrasesFavoriteClient{

        public static final String RESOURCE = "favorite_phrases";
        private static final String TAG = "Servicios";
        Context context;
        PhrasesFavoriteClient.AccionListener listener;

        public PhrasesFavoriteClient(Context context, PhrasesFavoriteClient.AccionListener listener) {
            this.context = context;
            this.listener = listener;
        }


        public void insertarusuarioPorCorreo(String email,String text,String likeFlag) {
            HashMap<String, String> parametros = new HashMap();

            HashMap<String, String> cabecera = new HashMap();
            parametros.put("email", email);
            parametros.put("text", text);
            parametros.put("like_flag", likeFlag);


            CustomVolleyRequestQueue.getInstance(context).addToRequestQueue(new GsonRequest<Void>(
                            Request.Method.POST, GsonRequest.TIPO_RAW,
                            context.getString(R.string.ubas) + RESOURCE,
                            Void.class,
                            cabecera,
                            parametros,
                            new Response.Listener<Void>() {
                                @Override
                                public void onResponse(Void response) {
                                    Log.d(TAG, "Exito al insertar");
                                    listener.onSuccess();

                                }
                            }
                            ,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "Error al insertar");
                                    String message = _handdledError(error);
                                    listener.onError(message);
                                }
                            }
                    )

            );
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


public interface AccionListener {
    void onSuccess();

    void onError(String message);

}
}