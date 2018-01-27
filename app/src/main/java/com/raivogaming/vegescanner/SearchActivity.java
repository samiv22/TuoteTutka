package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Tuote> array;
    ArrayList<Tuote> newArray;
    TextView textView;
    ImageButton userpagebutton;

    //FIREBASE
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blackStatusBar));

        textView = findViewById(R.id.hae_vahintaan_text);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userpagebutton = findViewById(R.id.search_page_user_button);
            userpagebutton.setImageResource(R.drawable.ic_user);
        }

        //TOOLBAR
        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Etsi");

        recyclerView = findViewById(R.id.search_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Tee haku databaseen ja palauta Array tuotteita

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Tuotteet");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array = new ArrayList<Tuote>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Tuote tempTuote = child.getValue(Tuote.class);
                    if(tempTuote.getNimi() != null){
                        array.add(tempTuote);
                    }
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), array);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Yhteys tuotetietokantaan epäonnistui", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        newArray = new ArrayList<>();

        if(newText.length() > 2){
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i<array.size(); i++){
            if(array.get(i).getNimi() == null){
                Log.i("myTag", "Tuote jolla ei ole nimeä");
            }else{
                String name = array.get(i).getNimi().toLowerCase();
                if(name.contains(newText)){
                    newArray.add(array.get(i));
                }
            }
        }
        recyclerAdapter.setFilter(newArray);
        return true;
    }

    public void toUserPage(View view){
        Intent intent = new Intent(SearchActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void scan(View view){
        Scanner.scan(this);
    }

    //HANDLAA SKANNERI KUN SE LUKENUT VIIVAKOODIN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                Intent i = new Intent(this, TuoteActivity.class);
                i.putExtra("barcode", result.getContents());
                startActivity(i);
                finish();
            }
        }
    }

}
