package com.lolisapp.traductorquechua.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.TraduccionHistorica;
import com.lolisapp.traductorquechua.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by USUARIO on 01/11/2017.
 */

public class RecyclerTraduccionAdapter extends RecyclerView.Adapter<RecyclerTraduccionAdapter.RecyclerTraduccionViewHolder> {


    OnActionListener onActionListener;
    private List<TraduccionHistorica> listTraduccionHistorica;
    private Context context;
    private ProgressDialog progressDialog;

    public RecyclerTraduccionAdapter(Context context, OnActionListener listener) {
        this.context = context;
        this.listTraduccionHistorica = new ArrayList<TraduccionHistorica>();
        this.onActionListener = listener;
    }

    public void setListTraduccionHistorica(List<TraduccionHistorica> listTraduccionHistorica) {
        this.listTraduccionHistorica = listTraduccionHistorica;
    }

    @Override
    public RecyclerTraduccionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerTraduccionViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico_traduccion, parent, false);
        vh = new RecyclerTraduccionViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerTraduccionViewHolder viewHolder, int position) {
        if (context != null) {
            viewHolder.tvOriginal.setText(listTraduccionHistorica.get(position).getOriginal());
            viewHolder.tvTraduccion.setText(listTraduccionHistorica.get(position).getTraduccion());
            if (listTraduccionHistorica.get(position).getFavorite()== Constant.TRADUCCION_FAVORITA_TRUE){
                viewHolder.ivStarFill.setVisibility(View.VISIBLE);
                viewHolder.ivFavorito.setVisibility(View.GONE);
            }else{
                viewHolder.ivFavorito.setImageResource(R.drawable.ic_star_border_black_24dp);
                viewHolder.ivStarFill.setVisibility(View.GONE);
                viewHolder.ivFavorito.setVisibility(View.VISIBLE);
            }



        }
    }


    @Override
    public int getItemCount() {
        return listTraduccionHistorica.size();
    }

    public void addMoreItems(List<TraduccionHistorica> moreItems) {
        listTraduccionHistorica.addAll(moreItems);
        notifyDataSetChanged();
    }

    public void addItem(TraduccionHistorica traduccionHistorica) {
        listTraduccionHistorica.add(0,traduccionHistorica);
        notifyItemInserted(0);
    }

    public void deleteItem(int position) {
        TraduccionHistorica traduccionHistorica=listTraduccionHistorica.get(position);
        this.listTraduccionHistorica.remove(position);


    }

    public void markFavorite(Integer position){
        Log.d("adapter","favo");
        if(listTraduccionHistorica.get(position).getFavorite()==0){
            listTraduccionHistorica.get(position).setFavorite(1);
        }else{
            listTraduccionHistorica.get(position).setFavorite(0);
        }
        notifyItemChanged(position);
    }

    public void clear() {
        listTraduccionHistorica.clear();
        notifyDataSetChanged();
    }

    public TraduccionHistorica getTraduccionHistorica(int posicion) {
        return listTraduccionHistorica.get(posicion);
    }

    public List<TraduccionHistorica> getListTraduccionHistorica() {
        return listTraduccionHistorica;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public interface OnActionListener {
        void onActionFavorite(Integer position);
        void onActionText(Integer position);
    }

    public class RecyclerTraduccionViewHolder extends RecyclerView.ViewHolder  {
        @Bind(R.id.ll_star)
        View vistaStar;

        @Bind(R.id.ll_texto)
        View vistaTexto;

        @Bind(R.id.tv_traduccion)
        TextView tvOriginal;
        @Bind(R.id.tv_original)
        TextView tvTraduccion;
        @Bind(R.id.iv_star)
        ImageView ivFavorito;
        @Bind(R.id.iv_starFill)
        ImageView ivStarFill;

        public RecyclerTraduccionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            vistaTexto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionListener.onActionText(getAdapterPosition());
                }
            });
            vistaStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionListener.onActionFavorite(getAdapterPosition());
                }
            });
        }


    }

}
