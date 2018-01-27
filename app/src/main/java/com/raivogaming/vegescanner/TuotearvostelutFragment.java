package com.raivogaming.vegescanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TuotearvostelutFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Arvostelu> arvostelut;
    private ArvostelutAdapter arvostelutAdapter;
    private FloatingActionButton leaveRatingButton;

    private FirebaseDatabase database;
    private DatabaseReference arvosteluRef;

    public TuotearvostelutFragment() {}

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceStat){

        arvostelut = new ArrayList<>();

        final String barcode = getActivity().getIntent().getStringExtra("barcode");

        database = FirebaseDatabase.getInstance();

        arvostelutAdapter = new ArvostelutAdapter(getActivity().getApplicationContext(), arvostelut);

        arvosteluRef = database.getReference().child("Arvostelut").child(barcode);
        arvosteluRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arvostelut.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Arvostelu arvostelu = snapshot.getValue(Arvostelu.class);
                    arvostelu.setUid(snapshot.getKey());
                    arvostelut.add(arvostelu);
                }
                recyclerView = view.findViewById(R.id.tuotearvostelut_recyclerview);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(arvostelutAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        leaveRatingButton = view.findViewById(R.id.leaveRatingButton);
        leaveRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ArvosteluActivity.class);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tuotearvostelut, container, false);
    }
}
