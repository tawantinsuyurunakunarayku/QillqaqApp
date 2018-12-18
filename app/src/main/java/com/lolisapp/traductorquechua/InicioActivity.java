package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.clienterest.LoginClient;
import com.lolisapp.traductorquechua.clienterest.VerificarEmailUsuarioClient;
import com.lolisapp.traductorquechua.util.Util;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InicioActivity extends AppCompatActivity {
    private static final String TAG = "InicioActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_SIGNUP_EXTERNO = 2;
    private static final int REQUEST_RECOVER_PASS= 3;

    CallbackManager callbackManager;
    @Bind(R.id.btnLoginFace)
    ImageView _btnLoginFace;
    ProgressDialog progressDialog;
    JSONObject facebookjson;
    GoogleApiClient googleApiClient;
    GoogleApiClient mGoogleApiClient;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.etError)
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ButterKnife.bind(this);
        congifureGmail();
        configureFacebook();


    }

    @OnClick(R.id.btn_login)
    public void loginApp() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getResources().getString(R.string.login_fail_validation));
            return;
        }


        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();


        LoginClient verificarUsuarioClient = new LoginClient(this, new LoginClient.VerificarUsuarioListener() {
            @Override
            public void onSuccess(User user) {
                SessionManager.getInstance(getApplicationContext()).createUserSession(user);
                progressDialog.dismiss();
                finish();
                Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(InicioActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        verificarUsuarioClient.verificarUser(_emailText.getText().toString(), _passwordText.getText().toString());

    }

    @OnClick(R.id.btnRegistro)
    public void goRegistro() {

        Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    private void congifureGmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("INICIOACTIVITY", connectionResult.getErrorMessage());
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    @OnClick(R.id.sign_in_button)
    public void logueoGmail() {
        if (mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                setResult(RESULT_OK, null);
                this.finish();
            }
        }

        if (requestCode == REQUEST_SIGNUP_EXTERNO) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }

        if (requestCode == REQUEST_RECOVER_PASS) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                Toast.makeText(this,"SU CONTRASEÑA FUE ACTUALIZADA CORRECTAMENTE",Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




    private void configureFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        _btnLoginFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(InicioActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "Login Facebook Sucess");
                if (AccessToken.getCurrentAccessToken() != null) {
                    verifyExistUser();
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login Facebook Cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));

                String exceptionAsString = sw.toString();
                error.setVisibility(View.VISIBLE);
                error.setText(exceptionAsString);
                Log.d(TAG, "Login Facebook ErrorMessage");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
                onLoginFailed(getResources().getString(R.string.login_facebook_error));
            }
        });

    }

    private void verifyExistUser() {

        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();


        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d(TAG, "Se completo llamada a graph...");
                facebookjson = response.getJSONObject();
                Log.d(TAG, "respuesta" + facebookjson.toString());
                try {
                    if (facebookjson.has("email")) {
                        String f_email = facebookjson.getString("email");

                        verificarEmail(f_email);
                    } else {
                        onLoginFailed("No se pudo recuperar el email de facebook");
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    onLoginFailed(e.getMessage());
                    progressDialog.dismiss();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private void verificarEmail(final String f_email) {

        new VerificarEmailUsuarioClient(this, new VerificarEmailUsuarioClient.ClientByEmailListener() {
            @Override
            public void onSuccess(final User client) {

                if (client != null) {
                    // if(user.getFlagFacebook()!=null && user.getFlagFacebook().intValue()==1) {

                    SessionManager.getInstance(getApplicationContext()).createUserSession(client);
                    progressDialog.dismiss();
                    setResult(RESULT_OK, null);
                    InicioActivity.this.finish();
                    Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                    startActivity(intent);


                } else {

                    progressDialog.dismiss();

                    String f_name = "";

                    Intent intent = new Intent(InicioActivity.this, RegistroExternoActivity.class);
                    intent.putExtra("pemail", f_email);
                    intent.putExtra("pname", f_name);
                    startActivityForResult(intent, REQUEST_SIGNUP_EXTERNO);


                }

            }

            @Override
            public void onError(String message) {
                onLoginFailed(message);
                progressDialog.dismiss();
            }
        }).findUserByEmail(f_email);


    }

    public void onLoginSuccess() {
        _btnLoginFace.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
            progressDialog.show();
            verificarEmail(result.getSignInAccount().getEmail());

        } else {
            Log.d("INICIOACTIVITY", "error");
        }
    }

    public void onLoginFailed(String msgError) {
        Toast.makeText(getBaseContext(), msgError, Toast.LENGTH_LONG).show();
        _btnLoginFace.setEnabled(true);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
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

    @OnClick(R.id.tv_forget_password)


    public void forgetPass(){

        Intent intent=new Intent(InicioActivity.this,RecoverPassActivity.class);
        startActivityForResult(intent,REQUEST_RECOVER_PASS);


            }

}