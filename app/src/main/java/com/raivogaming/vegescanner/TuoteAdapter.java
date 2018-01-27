package com.raivogaming.vegescanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TuoteAdapter extends RecyclerView.Adapter<TuoteAdapter.ViewHolder>{

    private List<Tuote> tuoteList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tuote_nimi, tuote_kuvaus;
        public ImageView tuote_kuva, tuote_vegaani, tuote_gluteeniton, tuote_arvostelu;

        public ViewHolder(View v){
            super(v);
        }
    }

    public TuoteAdapter(List<Tuote> tuoteList){
        this.tuoteList = tuoteList;
    }

    @Override
    public TuoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TuoteAdapter.ViewHolder holder, int position){
        Tuote tuote = tuoteList.get(position);
        holder.tuote_kuva.setImageResource(R.drawable.userimage);
        holder.tuote_nimi.setText("Tuote nimi");
        holder.tuote_kuvaus.setText("Tänne tulee tuotteen kuvaus jos sellainen on");
        holder.tuote_vegaani.setImageResource(R.drawable.vegaani_icon);
        holder.tuote_gluteeniton.setImageResource(R.drawable.gluteeniton_icon);
        holder.tuote_arvostelu.setImageResource(R.drawable.starsmiling);
    }

    @Override
    public int getItemCount(){
        return tuoteList.size();
    }

}
