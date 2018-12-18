package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lolisapp.traductorquechua.bean.Pais;
import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.clienterest.PaisListClient;
import com.lolisapp.traductorquechua.clienterest.RegistrarUsuarioCorreoClient;
import com.lolisapp.traductorquechua.constants.Constants;
import com.lolisapp.traductorquechua.util.Util;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class RegistroExternoActivity extends AppCompatActivity {

    private static final String TAG = "RegistroExternoActivity";
    @Bind(R.id.input_phone)
    EditText etTelefono;
    @Bind(R.id.input_password)
    EditText etPassword;
    @Bind(R.id.input_email)
    EditText etEmail;
    @Bind(R.id.input_name)
    EditText etName;


    @Bind(R.id.input_paterno)
    EditText etPaterno;
    @Bind(R.id.sp_pais)
    Spinner spPais;
    String pext;
    String pemail;
    String pname;

    Integer indidcePaisSeleccionado;
    private ProgressDialog progressDialog;

    private ArrayList<Pais> listaPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_externo);
        ButterKnife.bind(this);

        pemail = getIntent().getExtras().getString("pemail");
        pname = getIntent().getExtras().getString("pname");
        pext = getIntent().getExtras().getString("pext");

        Log.d(TAG, "Se recupero los parametos : " + pemail + ", " + pname);

        etEmail.setText(pemail);
        etName.setText(pname);

        setupToolbar();
        llenarSpinner();

    }




    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void llenarSpinner() {
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();

        PaisListClient paisListClient=new PaisListClient(this, new PaisListClient.PaisListClientListener() {
            @Override
            public void onSuccess(ArrayList<Pais> listaPais) {

                Pais pais=new Pais();
                pais.setNombre(getResources().getString(R.string.registro_mensaje_seleccion_tipo_documento));
                listaPais.add(0, pais);
                RegistroExternoActivity.this.listaPais = new ArrayList<>(listaPais);
                ArrayAdapter<Pais> adaptadorPaises = new ArrayAdapter<Pais>(RegistroExternoActivity.this,
                        android.R.layout.simple_spinner_item, listaPais);
                adaptadorPaises.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spPais.setAdapter(adaptadorPaises);
                progressDialog.dismiss();

            }

            @Override
            public void onError(String message) {

                progressDialog.dismiss();
                Log.d(TAG,message);
                Toast.makeText(RegistroExternoActivity.this,message,Toast.LENGTH_LONG).show();
                finish();
            }
        });

        paisListClient.getListCountry();

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

    private void clearUserFields() {
        etPaterno.setText("");
        etName.setText("");

    }

    @OnClick(R.id.link_login)
    public void yaTengoCuenta(View vista) {
        Intent llamadaLogin = new Intent(RegistroExternoActivity.this, InicioActivity.class);
        startActivity(llamadaLogin);
        this.finish();

    }

    @OnClick(R.id.bt_signup)
    public void registrarUser() {

        if (etEmail.getText().toString().compareTo("") == 0) {

            etEmail.setError(getResources().getString(R.string.registro_message_error_ingrese_correo_electronico));
            etEmail.setFocusable(true);
            etEmail.requestFocus();
        }

        if (!Util.validarCorreo(etEmail.getText().toString())) {

            etEmail.setError(getResources().getString(R.string.registro_message_error_correo_electronico_invalido));
            etEmail.setFocusable(true);
            etEmail.requestFocus();
            return;
        }

        if (etPassword.getText().toString().compareTo(Constants.VACIO) == 0) {
            etPassword.setError(getResources().getString(R.string.registro_message_error_ingrese_contrasena_usuario));
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return;
        }




        if (etPaterno.getText().toString().compareTo("") == 0) {
            etPaterno.setError(getResources().getString(R.string.registro_message_error_ingrese_apellido_paterno));
            etPaterno.setFocusable(true);
            etPaterno.requestFocus();
            return;

        }

        if (indidcePaisSeleccionado == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.seleccione_tipo_doc), Toast.LENGTH_LONG).show();
            return;
        }



        if (etName.getText().toString().compareTo("") == 0) {
            etName.setError(getResources().getString(R.string.registro_message_error_ingrese_nombres));
            etName.setFocusable(true);
            etName.requestFocus();
            return;
        }

        registrar();


    }

    public void registrar() {
        User usuario = new User();
        usuario.setFirstName(etName.getText().toString());
        usuario.setLastName(etPaterno.getText().toString());
        usuario.setEmail(etEmail.getText().toString());
        usuario.setPassword(etPassword.getText().toString());
        usuario.setPhone(etTelefono.getText().toString());
        usuario.setCountryId(listaPais.get(indidcePaisSeleccionado).getIdPais().toString());


        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();
        RegistrarUsuarioCorreoClient registrarUsuarioCorreo = new RegistrarUsuarioCorreoClient(this, new RegistrarUsuarioCorreoClient.RegistrarUsuarioCorreoListener() {

            @Override
            public void onSuccess(User user) {

                SessionManager.getInstance(getApplicationContext()).createUserSession(user);
                Toast.makeText(RegistroExternoActivity.this, getResources().getString(R.string.registro_message_registro_success), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                setResult(RESULT_OK, null);
                finish();
                Intent intent=new Intent(RegistroExternoActivity.this,MainActivity.class);
                startActivity(intent);


            }

            @Override
            public void onError(String message) {
                Log.d(TAG, message);
                progressDialog.dismiss();
                Toast.makeText(RegistroExternoActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        registrarUsuarioCorreo.insertarusuarioPorCorreo(usuario);


    }

    @OnItemSelected(R.id.sp_pais)
    public void tipoDocumentoSeleccionado(View vista, int posicion) {
        indidcePaisSeleccionado = posicion;

    }


}