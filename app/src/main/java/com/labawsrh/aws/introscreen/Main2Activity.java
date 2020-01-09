package com.labawsrh.aws.introscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    public View img;
    private FirebaseAuth mAuth;
    ImageView imageView;
    RecyclerView recyclerView;
    private Adapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseUser mUsers;
    private List<Model> uploadList;
    Button holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));


        holder = (Button) findViewById(R.id.button);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        getData();


        {
            mAuth = FirebaseAuth.getInstance();

            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                Toast.makeText(Main2Activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            imageView = (ImageView) findViewById(R.id.btnOk);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {

                        case R.id.btnOk:
                            Intent intent = new Intent(Main2Activity.this, ActivityTwo.class);

                            startActivity(intent);

                            break;

                    }
                }
            });
        }

    }

    private void getData() {
        uploadList = new ArrayList<>();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model upload = postSnapshot.getValue(Model.class);
                    uploadList.add(upload);
                }

                adapter = new Adapter(Main2Activity.this, uploadList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
