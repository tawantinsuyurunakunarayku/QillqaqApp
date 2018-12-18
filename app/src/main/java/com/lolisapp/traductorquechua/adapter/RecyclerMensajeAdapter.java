package com.lolisapp.traductorquechua.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lolisapp.traductorquechua.R;
import com.lolisapp.traductorquechua.bean.Mensaje;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by USUARIO on 06/11/2017.
 */

public class RecyclerMensajeAdapter extends RecyclerView.Adapter<RecyclerMensajeAdapter.RecyclerMensajeViewHolder> {

    RecyclerMensajeAdapter.OnActionListener onActionListener;
    private List<Mensaje> listaMensaje;
    private Context context;
    private ProgressDialog progressDialog;



    public RecyclerMensajeAdapter(Context context, RecyclerMensajeAdapter.OnActionListener listener) {
        this.context = context;
        this.listaMensaje = new ArrayList<Mensaje>();
        this.onActionListener = listener;
    }


    @Override
    public RecyclerMensajeAdapter.RecyclerMensajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerMensajeAdapter.RecyclerMensajeViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensajes, parent, false);
        vh = new RecyclerMensajeAdapter.RecyclerMensajeViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerMensajeAdapter.RecyclerMensajeViewHolder viewHolder, int position) {
        if (context != null) {

            viewHolder.tvEmisor.setText(listaMensaje.get(position).getEmisor());
            viewHolder.tvFecha.setText(listaMensaje.get(position).getFecha().toString());
            viewHolder.tvMensaje.setText(listaMensaje.get(position).getMensaje());


        }
    }

    @Override
    public int getItemCount() {
        return listaMensaje.size();
    }


    public void addMoreItems(List<Mensaje> moreItems) {
        listaMensaje.addAll(moreItems);
        notifyDataSetChanged();
    }

    public Mensaje getItem(int pos){
        return listaMensaje.get(pos);
    }


    public interface OnActionListener {
        void onClickAction(Integer position);
    }

    public class RecyclerMensajeViewHolder extends RecyclerView.ViewHolder  {




        @Bind(R.id.tv_emisor)
        TextView tvEmisor;
        @Bind(R.id.tv_mensaje)
        TextView tvMensaje;
        @Bind(R.id.tv_fecha)
        TextView tvFecha;


        public RecyclerMensajeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionListener.onClickAction(getAdapterPosition());
                }
            });

        }


    }


}

