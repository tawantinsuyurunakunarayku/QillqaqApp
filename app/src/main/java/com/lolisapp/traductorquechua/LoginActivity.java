package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.clienterest.LoginClient;
import com.lolisapp.traductorquechua.util.Util;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupToolbar();
        setTitle(getResources().getString(R.string.login_activity_title));
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getResources().getString(R.string.login_fail_validation));
            return;
        }


        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        login_user(progressDialog, email, Util.encryptPassword(password));

    }


    public void login_user(final ProgressDialog progressDialog, String email, String password) {
        LoginClient verificarUsuarioClient = new LoginClient(this, new LoginClient.VerificarUsuarioListener() {
            @Override
            public void onSuccess(User user) {
                if (user == null) {
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail_validation), Toast.LENGTH_LONG).show();

                } else {
                    SessionManager.getInstance(getApplicationContext()).createUserSession(user); //se pasa como parÃ¡metro el objeto recuperado
                    setResult(RESULT_OK, null); // se retorna al activity desde donde se llamÃ³ RESULT_OK
                    LoginActivity.this.finish();
                    progressDialog.dismiss();


                }
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);


            }
        });
        verificarUsuarioClient.verificarUser(email, password);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }
    }


    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);
        this.finish();
    }

    public void onLoginFailed(String msgError) {
        Toast.makeText(getBaseContext(), msgError, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getResources().getString(R.string.login_email_invalid));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError(getResources().getString(R.string.login_password_invalid));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.tv_forget_password)
    public void olvidasteTuContrasenia(View vista) {
     //   Intent llamadaActividad = new Intent(LoginActivity.this, RecuperacionPasswordActivity.class);
     //   startActivity(llamadaActividad);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
