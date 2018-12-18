package com.lolisapp.traductorquechua;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lolisapp.traductorquechua.adapter.RecyclerMensajeAdapter;
import com.lolisapp.traductorquechua.bean.Mensaje;
import com.lolisapp.traductorquechua.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TestMessageActivity extends BaseActivity implements RecyclerMensajeAdapter.OnActionListener {


    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    RecyclerView rvMensaje;
    LinearLayoutManager llMensaje;
    RecyclerMensajeAdapter recyclerMensajeAdapter;
    ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message);

        rvMensaje = (RecyclerView) findViewById(R.id.rv_mensajes);
        llMensaje = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMensaje.setHasFixedSize(true);
        rvMensaje.setLayoutManager(llMensaje);

        recyclerMensajeAdapter = new RecyclerMensajeAdapter(this, this);
        rvMensaje.setAdapter(recyclerMensajeAdapter);
        final Handler handler = new Handler(Looper.getMainLooper());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {



                    refreshSmsInbox();


        }





    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshSmsInbox() {
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();
        ArrayList<Mensaje> listaMensaje=new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexFecha=smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;


        do {
            Mensaje mensaje=new Mensaje();
            mensaje.setEmisor(smsInboxCursor.getString(indexAddress));
            mensaje.setMensaje(smsInboxCursor.getString(indexBody));
            Date d = new Date(Long.parseLong(smsInboxCursor.getString(indexFecha)));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String formattedDate = formatter.format(d);
            mensaje.setFecha(formattedDate);
            listaMensaje.add(mensaje);
        } while (smsInboxCursor.moveToNext());
        recyclerMensajeAdapter.addMoreItems(listaMensaje);
        progressDialog.dismiss();

    }

    @Override
    public void onClickAction(Integer position) {
        Intent i = new Intent(TestMessageActivity.this, MainActivity.class);
        i.putExtra("mensajeFavorito", recyclerMensajeAdapter.getItem(position).getMensaje());
        startActivity(i);

    }
}
