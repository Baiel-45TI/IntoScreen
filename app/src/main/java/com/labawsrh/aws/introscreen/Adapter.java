package com.labawsrh.aws.introscreen;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {


    DatabaseReference reference;
    FirebaseUser user;
    ImageView delete;
    CheckBox completed;

    private Context context;
    List<Model> modelList;


    public Adapter(Context context, List<Model> modelList) {

        this.context = context;
        this.modelList = modelList;
    }


    @NonNull
    @Override
    public Adapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_info, viewGroup, false);

        return new Adapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.Holder holder, final int position) {
        final Model uploadCurrent = modelList.get(position);
        holder.txt_desk.setText(uploadCurrent.getDesk());
        holder.txt_date.setText(uploadCurrent.getData());
        holder.text_time.setText(uploadCurrent.getTime());
        reference = FirebaseDatabase.getInstance().getReference();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users")
                                .child(user.getUid()).child(modelList.get(position).getDesk());

                        reference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Нажмите кнопку обновить", Toast.LENGTH_SHORT).show();
                                modelList.clear();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        public TextView txt_desk, txt_date, text_time;
        public ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            txt_desk = itemView.findViewById(R.id.desk);
            txt_date = itemView.findViewById(R.id.data);
            text_time = itemView.findViewById(R.id.time);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }

    }

}






