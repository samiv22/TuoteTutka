package com.raivogaming.vegescanner;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArvostelutAdapter extends RecyclerView.Adapter<ArvostelutAdapter.MyViewHolder> {

    private Context context;
    private List<Arvostelu> arvostelut;
    private StorageReference myStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView arvostelija, arvostelu;
        public RatingBar ratingBar;
        public ImageView suositteleeImage;
        public CircleImageView userImage;

        public MyViewHolder(View view){
            super(view);
            arvostelija = view.findViewById(R.id.main_page_row_arvostelija);
            arvostelu = view.findViewById(R.id.main_page_row_arvostelu);
            ratingBar = view.findViewById(R.id.main_page_row_ratingbar);
            suositteleeImage = view.findViewById(R.id.suosittelee_image);
            userImage = view.findViewById(R.id.main_page_row_userimage);
        }
    }

    public ArvostelutAdapter(Context context, List<Arvostelu> arvostelut){
        this.context = context;
        this.arvostelut = arvostelut;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_page_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Arvostelu arvostelu = arvostelut.get(position);
        holder.arvostelija.setText(arvostelu.getArvostelija());
        holder.arvostelu.setText(arvostelu.getArvostelu());
        holder.ratingBar.setRating(arvostelu.getArvosana());
        if(!arvostelu.isSuosittelee()){
            holder.suositteleeImage.setVisibility(View.INVISIBLE);
        }
        myStorageRef = FirebaseStorage.getInstance().getReference();
        Log.i("myTag", myStorageRef.getPath());
        myStorageRef.child("profileimages").child(arvostelu.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri.toString()).into(holder.userImage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arvostelut.size();
    }
}
