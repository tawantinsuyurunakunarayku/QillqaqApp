package com.lolisapp.traductorquechua;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.clienterest.PhrasesClient;
import com.lolisapp.traductorquechua.clienterest.SendDataFileClient;
import com.lolisapp.traductorquechua.clienterest.SendFileClient;
import com.lolisapp.traductorquechua.util.Util;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends BaseActivity  {

    public static final int REQUEST_DO_LOGIN = 1;
    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";
    private boolean isSpanish = true;

    @Bind(R.id.tv_traducido)

    TextView tvTraduccion;

//    @Bind(R.id.tv_original)
//    TextView tvOriginal;
    @Bind(R.id.tv_first_langua)
    TextView tvFirstLangua;
    @Bind(R.id.iv_animation)
    ImageView ivAnimation;
    @Bind(R.id.tv_second_langua)
    TextView tvSecondLangua;
    @Bind(R.id.btn_mic_start)
    View btnStart;
    @Bind(R.id.btn_mic_stop)
    View btnStop;
    @Bind(R.id.ll_option_middle)
    View llOptionMiddel;
    @Bind(R.id.ll_text_traduction)
    View llTextTraduccion;


    AnimationDrawable frameAnimation;

    MediaRecorder mediaRecorder;
    private String lastAudioRecord;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifiyPermisied();
        setTitletoToolbar("");
    }

    private void initAudio() {
        Log.d("inti","init audio");
        mediaRecorder = getMediaRecorderReady();
    }


    private void initAnimation() {
        llOptionMiddel.setVisibility(View.GONE);
        llTextTraduccion.setVisibility(View.GONE);
        ivAnimation.setVisibility(View.VISIBLE);
        ivAnimation.setBackgroundResource(R.drawable.animsound);
        frameAnimation = (AnimationDrawable) ivAnimation.getBackground();
        frameAnimation.start();
    }

    private void stopAnimation(){
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        llOptionMiddel.setVisibility(View.VISIBLE);
        frameAnimation.stop();
        ivAnimation.setVisibility(View.GONE);
    }



    public MediaRecorder getMediaRecorderReady() {
        createDirectory();
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(16000);
        lastAudioRecord = getPathToAudio();
        mediaRecorder.setOutputFile(lastAudioRecord);
        return mediaRecorder;
    }

    public String getPathToAudio() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_quechua/" + getCode();
        return path;
    }


    @OnClick(R.id.btn_mic_start)
    public void initRecord(){


        if (checkPermission()) {
            mediaRecorder = getMediaRecorderReady();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (Exception e) {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                e.printStackTrace();
            }
            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.VISIBLE);
            initAnimation();
            Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_LONG).show();
            //modeRecord = false;
        } else {
            verifiyPermisied();
        }


    }

    @OnClick(R.id.btn_mic_stop)
    public void stopRecord(){
        mediaRecorder.stop();
        stopAnimation();
        File upload=new File(lastAudioRecord);


        sendDataFile(upload);
      //  sendFileAudio(upload);
    }

    private void sendDataFile(final File file){

        SendDataFileClient sendDataFileClient=new SendDataFileClient(this, new SendDataFileClient.ResponseListener() {
            @Override
            public void onSuccess() {
                sendFileAudio(file);

            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        sendDataFileClient.sendData(SessionManager.getInstance(this).getUserLogged().getEmail(),file.getName());

        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();


    }




    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("request","wew");
        Log.d("code",""+requestCode);
        if(requestCode== REQUEST_PERMISSION_CODE){
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(MainActivity.this, "no puede usar el aplicativo sin dar los permisos necesarios", Toast.LENGTH_LONG).show();
                finish();


            }else{
                initAudio();
            }

        }



    }

    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String code = "audio_quechua_" + date + ".wav";
        return code;
    }

    byte[] getBytes(File file) {
        FileInputStream input = null;
        if (file.exists()) try {
            input = new FileInputStream(file);
            int len = (int) file.length();
            byte[] data = new byte[len];
            int count, total = 0;
            while ((count = input.read(data, total, len - total)) > 0) total += count;
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) try {
                input.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }




    private void sendFileAudio(File file){


        SendFileClient sendFileClient = new SendFileClient(this, new SendFileClient.SendFileClientListener() {
            @Override
            public void onSuccess(TraduccionHistorica traduccionHistorica) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Exito", Toast.LENGTH_SHORT).show();
                llTextTraduccion.setVisibility(View.VISIBLE);
                tvTraduccion.setText(traduccionHistorica.getTraduccion());
       //         tvOriginal.setText(traduccionHistorica.getOriginal());
             //   sendPhraseServer(SessionManager.getInstance(MainActivity.this).getUserLogged().getEmail(),
              //          traduccionHistorica.getTraduccion(),"0");
            }

            @Override
            public void onError(String message) {

                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

        sendFileClient.uploadFile(getBytes(file));


    }


    private void createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "audio_quechua");
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }

    /*
    @OnLongClick(R.id.btn_mic_start)
    public boolean startLong(){
        Log.d("lomg","saas");
        initRecord();
        stopRecord();
        return false;
    }
*/

    private void sendPhraseServer(String email,String text,String like){
        PhrasesClient phrasesClient=new PhrasesClient(this, new PhrasesClient.AccionesListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Log.d("main","favo");


            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        phrasesClient.markFavorite(email, text, like);
    }



    @OnClick(R.id.iv_copy)
    public void copy() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getSystemService(MainActivity.CLIPBOARD_SERVICE);
            clipboard.setText(tvTraduccion.getText().toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(MainActivity.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Your OTP", tvTraduccion.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(MainActivity.this, "copiado al portapapeles", Toast.LENGTH_SHORT).show();

    }


    @OnClick(R.id.ib_sound)
    public void sonar() {

        Toast.makeText(MainActivity.this, "indicar funcionalidad", Toast.LENGTH_SHORT).show();

    }



    @OnClick(R.id.ib_share)
    public void shareTraduccion() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tvTraduccion.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }



    @OnClick(R.id.iv_change_languaje)
    public void changeLanguaje() {
        if (isSpanish) {
            tvFirstLangua.setText("Quechua");
            tvSecondLangua.setText("Español");
        } else {
            tvFirstLangua.setText("Español");
            tvSecondLangua.setText("Quechua");

        }
        isSpanish = !isSpanish;


    }

    private void verifiyPermisied(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||

                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

                ) {


            if(Build.VERSION.SDK_INT >= 23){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAPTURE_AUDIO_OUTPUT,
                        Manifest.permission.READ_SMS}, REQUEST_PERMISSION_CODE);
            }else {
                initAudio();
            }

        }else{
            initAudio();
        }

    }




}