package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lolisapp.traductorquechua.clienterest.ForgetPasswordClient;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePassActivity extends AppCompatActivity {

    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.et_correo)
    EditText et_correo;
    @Bind(R.id.et_pass)
    EditText etPass;
    @Bind(R.id.et_pass_repeat)
    EditText etPassRepeat;
    @Bind(R.id.bt_enviar_correo)
    Button btnEnviarCorreo;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);

        ButterKnife.bind(this);
        setupToolbar();
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

    @OnClick(R.id.bt_enviar_correo)
    public void verify(){

        if (et_correo.getText().toString().compareTo(Constants.VACIO) == 0) {
            et_correo.setError(getResources().getString(R.string.registro_message_error_campo_vacio));
            et_correo.setFocusable(true);
            et_correo.requestFocus();
            return;
        }

        if (et_code.getText().toString().compareTo(Constants.VACIO) == 0) {
            et_code.setError(getResources().getString(R.string.registro_message_error_campo_vacio));
            et_code.setFocusable(true);
            et_code.requestFocus();
            return;
        }


        if (etPass.getText().toString().compareTo(Constants.VACIO) == 0) {
            etPass.setError(getResources().getString(R.string.registro_message_error_campo_vacio));
            etPass.setFocusable(true);
            etPass.requestFocus();
            return;
        }

        if (!etPass.getText().toString().equals(etPassRepeat.getText().toString())) {
            etPassRepeat.setError(getResources().getString(R.string.mensaje_contrasenias_no_coinciden));
            etPassRepeat.setFocusable(true);
            etPassRepeat.requestFocus();
            return;
        }

        ForgetPasswordClient forgetPasswordClient=new ForgetPasswordClient(this, new ForgetPasswordClient.actionListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePassActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();
        forgetPasswordClient.sendCode(et_correo.getText().toString(), et_code.getText().toString(),etPass.getText().toString());

    }
}
