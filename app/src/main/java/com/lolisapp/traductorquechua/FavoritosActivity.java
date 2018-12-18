package com.lolisapp.traductorquechua;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.lolisapp.traductorquechua.adapter.RecyclerTraduccionAdapter;
import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.clienterest.ListPhrasesClient;
import com.lolisapp.traductorquechua.clienterest.PhrasesClient;
import com.lolisapp.traductorquechua.customviews.DividerItemDecoration;
import com.lolisapp.traductorquechua.database.DatabaseHistorico;
import com.lolisapp.traductorquechua.util.Util;
import com.lolisapp.traductorquechua.util.session.SessionManager;

import java.util.ArrayList;

public class FavoritosActivity extends BaseActivity implements RecyclerTraduccionAdapter.OnActionListener {

    RecyclerView rvFavoritos;
    LinearLayoutManager llFavoritos;
    RecyclerTraduccionAdapter recyclerTraduccionAdapter;
    DatabaseHistorico databaseHistorico;
    SearchView searchView;
    Spinner spinner;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        rvFavoritos = (RecyclerView) findViewById(R.id.rv_favoritos);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvFavoritos.addItemDecoration(itemDecoration);

        llFavoritos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFavoritos.setHasFixedSize(true);
        rvFavoritos.setLayoutManager(llFavoritos);

        recyclerTraduccionAdapter = new RecyclerTraduccionAdapter(this, this);
        rvFavoritos.setAdapter(recyclerTraduccionAdapter);
        databaseHistorico = new DatabaseHistorico(this);
        callListTraduccion();
    }


    private void callListTraduccion(){
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();
        ListPhrasesClient listPhrasesClient=new ListPhrasesClient(this, new ListPhrasesClient.AccionesListener() {
            @Override
            public void onSuccess(ArrayList<TraduccionHistorica> listaTraduccion) {
                recyclerTraduccionAdapter.clear();
                recyclerTraduccionAdapter.addMoreItems(listaTraduccion);

                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(FavoritosActivity.this,message,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });
       listPhrasesClient.listPhrases(SessionManager.getInstance(this).getUserLogged().getEmail());
//        listPhrasesClient.listPhrases("rjzevallos.salazar@gmail.com");



    }

    @Override
    public void onActionFavorite(final Integer position) {
        if (progressDialog == null) progressDialog = Util.createProgressDialog(this);
        progressDialog.show();

        PhrasesClient phrasesClient=new PhrasesClient(this, new PhrasesClient.AccionesListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();

                recyclerTraduccionAdapter.markFavorite(position);
                databaseHistorico.updateTraduccionHistorica(
                        recyclerTraduccionAdapter.getTraduccionHistorica(position).getId(),
                        recyclerTraduccionAdapter.getTraduccionHistorica(position).getFavorite());

            }

            @Override
            public void onError(String message) {
                Toast.makeText(FavoritosActivity.this, message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        phrasesClient.markFavorite(SessionManager.getInstance(this).getUserLogged().getEmail(),
                recyclerTraduccionAdapter.getTraduccionHistorica(position).getTraduccion(),
                recyclerTraduccionAdapter.getTraduccionHistorica(position).getFavorite().toString()
                );


    }

    @Override
    public void onActionText(Integer position) {
        Intent i = new Intent(FavoritosActivity.this, MainActivity.class);
        i.putExtra("idFavorito", recyclerTraduccionAdapter.getListTraduccionHistorica().get(position).getId());
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_buscar));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                recyclerTraduccionAdapter.setListTraduccionHistorica(databaseHistorico.getListTraduccionHistoricaFavoritasSearch(query));
                recyclerTraduccionAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==  R.id.action_actualizar){
            callListTraduccion();
            return true;
        }

        if (id ==  R.id.action_ordenar){
            CharSequence colors[] = new CharSequence[] {"Alfabeticamente","Tiempo"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Escoja una opción");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        recyclerTraduccionAdapter.setListTraduccionHistorica(databaseHistorico.getListTraduccionHistoricaFavoritasOrderText());
                        recyclerTraduccionAdapter.notifyDataSetChanged();
                    }else{
                       callListTraduccion();
                    }
                }
            });
            builder.show();
            return true;
        }




        return super.onOptionsItemSelected(item);
    }
}