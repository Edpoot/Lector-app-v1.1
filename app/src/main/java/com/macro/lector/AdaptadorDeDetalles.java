package com.macro.lector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.macro.lector.Entidades.Detalle;
import java.util.List;


public class AdaptadorDeDetalles extends RecyclerView.Adapter<AdaptadorDeDetalles.ViewHolder> {
    private List<Detalle> categoriaList;
    private Context context;
    public MyAdapterListener onClickListener;


    public  class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView descripcionIncidencia;
        public ImageView delete;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                }
            });

            descripcionIncidencia = (TextView) v.findViewById(R.id.tvDescripcionIncidencia);
            delete = (ImageView) v.findViewById(R.id.ivDelete);
        }
    }

    public AdaptadorDeDetalles(Context context, MyAdapterListener listener) {
        this.context = context;
        this.onClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (categoriaList != null)
            return categoriaList.size();
        return 0;
    }

    @Override
    public AdaptadorDeDetalles.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_resumen_layout, viewGroup, false);
        return new AdaptadorDeDetalles.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdaptadorDeDetalles.ViewHolder viewHolder, final int i) {
        final String descripcion;
        descripcion = categoriaList.get(i).getDescripcion();

//        viewHolder.descripcionIncidencia.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viewHolder.descripcionIncidencia.setText(descripcion);
//                viewHolder.descripcionIncidencia.clearFocus();
//            }
//        },100);

        viewHolder.descripcionIncidencia.setText(descripcion);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.OnRemoveClick(i);
            }
        });


    }


    public void notifyData(List<Detalle> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.categoriaList = myList;
        notifyDataSetChanged();
    }


    public interface MyAdapterListener {
        void OnRemoveClick(int position);
    }
}
