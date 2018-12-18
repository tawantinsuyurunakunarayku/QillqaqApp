package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.volley.CustomVolleyRequestQueue;
import com.lolisapp.traductorquechua.util.volley.ErrorMessage;
import com.lolisapp.traductorquechua.util.volley.GsonRequest;
import com.lolisapp.traductorquechua.util.volley.VolleyErrorHelper;

import java.util.HashMap;

/**
 * Created by USUARIO on 07/11/2017.
 */

public class SendTextToTraduce {
    public static final String RESOURCE = "authenticationcontroller/sendText";
    private static final String TAG = "UserByEmailClient";
    SendTextToTraduce.SendTextToTraduceListener listener;
    Context context;

    public SendTextToTraduce(Context context, SendTextToTraduce.SendTextToTraduceListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void sendText(String mensaje) {
        Log.d(TAG, "Preparando para llamar a servicio de  ...");

        HashMap<String, String> parametros = new HashMap();
        parametros.put("mensaje", mensaje);

        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion),
                context.getString(R.string.codigoAutorizacion));

        CustomVolleyRequestQueue.getInstance(context).addToRequestQueue(
                new GsonRequest<TraduccionHistorica>(
                        Request.Method.POST, GsonRequest.TIPO_DEFAULT,
                        context.getString(R.string.ubas) + RESOURCE,
                        TraduccionHistorica.class,
                        cabecera,
                        parametros,
                        new Response.Listener<TraduccionHistorica>() {
                            @Override
                            public void onResponse(TraduccionHistorica response) {
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


    public interface SendTextToTraduceListener {
        void onSuccess(TraduccionHistorica mensje);

        void onError(String message);
    }


}