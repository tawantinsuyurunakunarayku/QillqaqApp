package com.lolisapp.traductorquechua.clienterest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.util.volley.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



    public class SendFileClient {
        public static final String RESOURCE = "upload";
    SendFileClientListener _listener;
    Context context;

    public SendFileClient(Context context,SendFileClientListener _listener) {
        this._listener = _listener;
        this.context = context;
    }



    public void uploadFile(final byte[] bites) {


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                context.getString(R.string.ubas) + RESOURCE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("response",new String(response.data));
                            JSONObject obj = new JSONObject(new String(response.data));
                                Log.d("TEXT","TRAD "+obj.toString());
                            _listener.onSuccess(parseJsonObject(obj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("eere",error.toString());

                        _listener.onError(error.getMessage());
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long audioName = System.currentTimeMillis();
                params.put("files", new DataPart(audioName + ".wav",bites ));

                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    public interface SendFileClientListener {
        void onSuccess(TraduccionHistorica Traduccion);

        void onError(String message);
    }

    public TraduccionHistorica parseJsonObject(JSONObject jsonObject) throws JSONException {
        TraduccionHistorica traduccionHistorica=new TraduccionHistorica();
        traduccionHistorica.setFavorite(0);
        traduccionHistorica.setOriginal(jsonObject.getString("text_source"));
        traduccionHistorica.setTraduccion(jsonObject.getString("text_target"));
        return traduccionHistorica;
    }
}
