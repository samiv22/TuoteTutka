package com.raivogaming.vegescanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private ArrayList<Tuote> array;
    private Context context;
    private StorageReference reference;

    RecyclerAdapter(Context context, ArrayList<Tuote> array){
        this.array = array;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //LATAA LAYOUTTTIIN TIEDOT ARRAYSTA

        reference = FirebaseStorage.getInstance().getReference();
        Log.i("myTag2", reference.getPath());
        Log.i("myTag2", reference.child("tuotekuvat").child("123456789").getPath());
        reference.child("tuotekuvat").child(array.get(position).getViivakoodi()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("myTag", uri.toString());
                Picasso.with(context).load(uri.toString()).into(holder.imageView);
            }
        });
        holder.textView.setText(array.get(position).getNimi());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TuoteActivity.class);
                i.putExtra("barcode", array.get(position).getViivakoodi()+"");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tuotekuva);
            textView =  itemView.findViewById(R.id.tuotenimi);
        }
    }

    void setFilter(ArrayList<Tuote> newList){
        array = new ArrayList<>();
        for(int i = 0; i<newList.size(); i++){
            array.add(newList.get(i));
        }
        notifyDataSetChanged();
    }
}
