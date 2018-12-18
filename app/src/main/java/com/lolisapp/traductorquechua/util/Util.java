package com.lolisapp.traductorquechua.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.WindowManager;

import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.constants.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by josejurado on 4/09/15.
 */
public class Util {

    public static String getSimpleDate(Date fec) {
        String d = "";
        DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_UTC));
        d = formatter.format(fec);

        return d;

    }

    public static String getSimpleHour(Date fec) {
        String d = "";
        DateFormat formatter = new SimpleDateFormat(Constants.SIMPLE_HOUR_24H_FORMAT, Locale.getDefault());
        d = formatter.format(fec);
        return d;
    }

    public static String getSimpleYear(Date fecha){
        DateFormat formatter = new SimpleDateFormat(Constants.SIMPLE_YEAR_FORMAT, Locale.getDefault());
        return  formatter.format(fecha);
    }


    public static double calcularDistancia(double initialLat, double initialLong,
                                           double finalLat, double finalLong) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(initialLat);
        selected_location.setLongitude(initialLong);
        Location near_locations = new Location("locationB");
        near_locations.setLatitude(finalLat);
        near_locations.setLongitude(finalLong);

        double distance = selected_location.distanceTo(near_locations);
        return distance;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }

    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean validarCorreo(String email) {

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();

    }


    public static Double Redondear(double numero) {
        return Math.rint(numero * 100) / 100;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }


    public static AlertDialog createSimpleDialogDeseaLoguearse(final Activity activity) {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.titulo_dialogo_necesita_loguearse))
                .setMessage(activity.getString(R.string.mensaje_dialogo_necesita_loguearse))
                .setPositiveButton(activity.getString(R.string.aceptar_boton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                             //   Intent llamadaServicio = new Intent(activity, InicioActivity.class);
                             //   activity.startActivityForResult(llamadaServicio, MainActivity.REQUEST_DO_LOGIN);
                            }
                        })
                .setNegativeButton(activity.getString(R.string.cancel_boton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        return builder.create();
    }


    public static void swapFragment(FragmentActivity activity, Fragment fragment, int resourceFragmentholder, boolean shouldAddToBackStack) {
        // Get a reference to the fragment manager
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // Start the transaction & specify the holder
        String tag = fragment.getClass().getSimpleName();
        //transaction.replace( R.id.fragment_holder, fragment, tag );
        transaction.replace(resourceFragmentholder, fragment, tag);

        // If desired, add to the backstack
        if (shouldAddToBackStack) {
            transaction.addToBackStack(tag);
        }

        // Make sure to commit!
        transaction.commit();
    }

}

