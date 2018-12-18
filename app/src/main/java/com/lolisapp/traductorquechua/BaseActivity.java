package com.lolisapp.traductorquechua;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lolisapp.traductorquechua.bean.User;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.navigation)
    protected NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        setupButterKnife();
        setupNavigationView();
        setupToolbar();
    }


    private void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            final ActionBar ab = getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setTitletoToolbar(int resId){
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ){
            actionBar.setTitle(resId);
        }
    }


    protected void setTitletoToolbar(String title){
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ){
            actionBar.setTitle(title);
        }
    }


    private void setupButterKnife(){
        ButterKnife.bind(this);
    }


    public void replaceFragment(int rsLayout, Fragment fragment){
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace( rsLayout, fragment );
        fragmentManager.addToBackStack(null);
        fragmentManager.commit();
    }


    private void setupNavigationView() {
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.setItemIconTintList(null);

        TextView _tvFullName = (TextView) headerLayout.findViewById(R.id.tvFullName);
        TextView _tvEmail = (TextView) headerLayout.findViewById(R.id.tvEmail);

        if (!SessionManager.getInstance(this).isLogged()) {
            _tvFullName.setText(getString(R.string.item_not_logged));
            _tvEmail.setText("");
            _tvFullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    Intent i = new Intent(BaseActivity.this, InicioActivity.class);
                 //   startActivityForResult(i, MainActivity.REQUEST_DO_LOGIN);
                    drawerLayout.closeDrawers();
                }
            });
        } else {
            User user=SessionManager.getInstance(getApplicationContext()).getUserLogged();
            _tvFullName.setOnClickListener(null);
            _tvFullName.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
            _tvEmail.setText(SessionManager.getInstance(getApplicationContext()).getUserLogged().getEmail());
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void seleccionarItem(MenuItem itemDrawer) {

        String title = itemDrawer.getTitle().toString();

        switch (itemDrawer.getItemId()) {
            case R.id.item_inicio:
                finalizarActivity();
                Intent intent4 = new Intent(this, MainActivity.class);
                startActivity(intent4);
                break;
            case R.id.item_favoritos:

                Intent intent = new Intent(this, FavoritosActivity.class);
               startActivity(intent);
                finalizarActivity();
                break;
                /*
            case R.id.item_mensajes:
                Intent intent2 = new Intent(this, TestMessageActivity.class);
                startActivity(intent2);
                finalizarActivity();
                break;
*/
            case R.id.item_salir:
                finish();
                SessionManager.getInstance(this).logoutApp();
                Intent nuevoIntento = new Intent(getApplicationContext(), InicioActivity.class);
                startActivity(nuevoIntento);
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MainActivity.REQUEST_DO_LOGIN) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    public void verificarDrawer(){
        if (drawerLayout != null)
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public void finalizarActivity(){
        this.finish();
    }


    public void cerrarDrawer(){
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                if (drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

}
