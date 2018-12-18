package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lolisapp.traductorquechua.clienterest.CodeClient;
import com.lolisapp.traductorquechua.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPassActivity extends AppCompatActivity {


    private static final String TAG = "recuPassActiv";
    @Bind(R.id.input_email)
    EditText et_correo;
    @Bind(R.id.bt_enviar_correo)
    Button btnEnviarCorreo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);
        ButterKnife.bind(this);
        setupToolbar();
    }

    @OnClick(R.id.bt_enviar_correo)
    public void enviarCorreo(View vista) {
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();
        CodeClient codeClient=new CodeClient(this, new CodeClient.actionListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Intent intent=new Intent(RecoverPassActivity.this,UpdatePassActivity.class);
                startActivityForResult(intent,1);
                Toast.makeText(RecoverPassActivity.this, getResources().getString(R.string.mensaje_exito_recuperar_contrasenia), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(RecoverPassActivity.this, message, Toast.LENGTH_LONG).show();

            }
        });
        codeClient.sendCode(et_correo.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK){
            //finish();
          setResult(RESULT_OK);
            finish();
            //  Toast.makeText(RecoverPassActivity.this,"SU CONTRASEÑA FUE ACTUALIZADA CORRECTAMENTE",Toast.LENGTH_LONG).show();

        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
