package com.sem.e_health;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Addtest extends AppCompatActivity {
    List<Test> testList = new ArrayList<>();
    RecAdapter adapter ;
    RecyclerView recyclerview ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tempRef;
    DatabaseReference glucRef;
    DatabaseReference hardbeatsRef;
    DatabaseReference emgRef;
    String emg ;
    String glucose ;
    String temp ;
    String hartbeats ;
    String finalDate;
    DatabaseReference testRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtest);
        recyclerview = findViewById(R.id.RC1);
        enableSwipeToDeleteAndUndo();

        adapter = new RecAdapter(this,testList);
       ((SimpleItemAnimator) recyclerview.getItemAnimator()).setSupportsChangeAnimations(false);


        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        recyclerview.setHasFixedSize(true);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");
        String docID = intent.getStringExtra("docid");
        tempRef = database.getReference("E-Health/Client live test /"+name+" "+lastname+"/Temp");
        hardbeatsRef = database.getReference("E-Health/Client live test /"+name+" "+lastname+"/Heart Beats");
        emgRef = database.getReference("E-Health/Client live test /"+name+" "+lastname+"/EMG");
        glucRef = database.getReference("E-Health/Client live test /"+name+" "+lastname+"/Glucose");
        DatabaseReference myRef = database.getReference("E-Health/Doctors/"+docID+"/Clients TESTS");
        testRef = myRef.child(name+" "+lastname+" TESTS");
        hardbeatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hartbeats = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        glucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                glucose = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        emgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emg = (String) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Date c = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String dateformatted = dateFormat.format(date);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        finalDate = formattedDate +" "+dateformatted ;
        FloatingActionButton back = findViewById(R.id.back);
        back.setOnClickListener(v ->{

            startActivity(new Intent(Addtest.this,DoctorActivity.class));
        });




        testRef.addValueEventListener(vel);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position,testRef);

            }

        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerview);
    }
    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Test test ;
            testList.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                test = ds.getValue(Test.class);
                if (test != null) {
                    testList.add(test);

                }

            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
