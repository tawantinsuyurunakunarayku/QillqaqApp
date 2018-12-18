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
import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.volley.ErrorMessage;
import com.lolisapp.traductorquechua.util.volley.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by USUARIO on 24/09/2017.
 */

public class VerificarEmailUsuarioClient {
    public static final String RESOURCE = "email";
    private static final String TAG = "UserByEmailClient";
    ClientByEmailListener _listener;
    Context context;

    public VerificarEmailUsuarioClient(Context _context, ClientByEmailListener _listener) {
        this.context = _context;
        this._listener = _listener;

    }

    public void findUserByEmail(String email) {
        Log.d(TAG, "Preparando para llamar a servicio de  getUserByEmail...");

        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", email);

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
                        Log.d("correcto",response.toString());
                        try {
                            User user=getUserFromJson(response);
                            if (user.getUserId()==0L){
                                _listener.onSuccess(null);
                            }else {
                                _listener.onSuccess(user);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
//
                        if(error.networkResponse.data!=null) {
                            try {
                               String body = new String(error.networkResponse.data,"UTF-8");
                                Log.d("incorrecto", body);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.d("incorrecto", String.valueOf(error.networkResponse));

                        String   message = context.getResources().getString(R.string.generic_error);
                        _listener.onError(message);

                    }
                }
        );

        requestQueue.add(jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));

    }

    private User getUserFromJson(JSONObject jsonObject) throws JSONException {
        User user=new User();
        user.setEmail(jsonObject.getString("email"));
        user.setUserId(jsonObject.getLong("user_id"));
        user.setLastName(jsonObject.getString("last_name"));
        user.setFirstName(jsonObject.getString("first_name"));
        return user;
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


    public interface ClientByEmailListener {
        void onSuccess(User client);

        void onError(String message);
    }


}