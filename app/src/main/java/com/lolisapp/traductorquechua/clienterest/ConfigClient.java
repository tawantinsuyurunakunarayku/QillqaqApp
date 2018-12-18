package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.volley.CustomVolleyRequestQueue;
import com.lolisapp.traductorquechua.util.volley.ErrorMessage;
import com.lolisapp.traductorquechua.util.volley.GsonRequest;
import com.lolisapp.traductorquechua.util.volley.VolleyErrorHelper;

import java.util.HashMap;

/**
 * Created by USUARIO on 28/11/2017.
 */

public class ConfigClient {
    public static final String RESOURCE = "config";
    private static final String TAG = "user";
    ConfigClient.AccionListener listener;
    Context context;

    public ConfigClient(Context context, ConfigClient.AccionListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void verificarUser(String correo, String param,String desc) {
        Log.d(TAG, "Preparando para llamar a servicio de  ...");

        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", correo);
        parametros.put("param", param);
        parametros.put("desc", desc);

        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion), context.getString(R.string.codigoAutorizacion));

        CustomVolleyRequestQueue.getInstance(context).addToRequestQueue(
                new GsonRequest<User>(
                        Request.Method.POST, GsonRequest.TIPO_RAW,
                        context.getString(R.string.ubas) + RESOURCE,
                        User.class,
                        cabecera,
                        parametros,
                        new Response.Listener<User>() {
                            @Override
                            public void onResponse(User response) {
                                Log.d(TAG, "Exito en la verificacion de usuario..");
                                listener.onSuccess((response));
                            }
                        }

                        ,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error en la recuperacion del usuario por email..");
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
            ErrorMessage errorMessage = VolleyErrorHelper.getErrorMessage(error, context);
            Log.d(TAG, "errorMessage" + new Gson().toJson(errorMessage));
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
        void onSuccess(User user);

        void onError(String message);
    }


}