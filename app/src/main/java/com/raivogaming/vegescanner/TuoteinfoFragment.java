package com.raivogaming.vegescanner;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class TuoteinfoFragment extends Fragment {

    private StorageReference imageReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    ImageView tuotekuva;
    TextView nimitext, kuvaustext;
    RatingBar ratingBar;

    public TuoteinfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tuoteinfo, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        final Context context = getActivity().getApplicationContext();

        tuotekuva = getView().findViewById(R.id.tuoteinfo_tuotekuva);
        nimitext = getView().findViewById(R.id.tuotteennimitext);
        kuvaustext = getView().findViewById(R.id.tuotekuvausteksti);
        ratingBar = getView().findViewById(R.id.tuotteenratingbar);

        String barcode = getActivity().getIntent().getStringExtra("barcode");
        imageReference = FirebaseStorage.getInstance().getReference().child("tuotekuvat").child(barcode+".png");
        Log.i("myTag", imageReference.toString());

        DatabaseReference tuoteReference = database.getReference("Tuotteet").child(barcode);

        Log.i("myTag", tuoteReference.toString());

        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(tuotekuva);
            }
        });

        database = FirebaseDatabase.getInstance();
        tuoteReference = database.getReference("Tuotteet").child(barcode);

        tuoteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tuote tuote = dataSnapshot.getValue(Tuote.class);
                if(tuote != null){
                    nimitext.setText(tuote.getNimi());
                    kuvaustext.setText(tuote.getKuvaus());
                    //Calculte average rating
                    float average = (tuote.getArvostelut() / tuote.getArvosteluKerrat());
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Toast.makeText(getActivity(), "Jokin meni vikaan", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
