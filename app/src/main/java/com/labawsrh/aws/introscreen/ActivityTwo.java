package com.labawsrh.aws.introscreen;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityTwo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private  EditText textTest;
    private NotificationManagerCompat notificationManager;
    EditText dateView;
    EditText textView;
    ImageView imageView;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ImageView speech;
    Menu action_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        notificationManager = NotificationManagerCompat.from(this);


        action_settings = (Menu) findViewById(R.id.action_settings);
        dateView = (EditText) findViewById(R.id.dateView);
        textTest = (EditText) findViewById(R.id.textTest) ;
        textView = (EditText) findViewById(R.id.textView);
        db = FirebaseDatabase.getInstance();

        speech = findViewById(R.id.btn);
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMic(view);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.clock);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText editText = (EditText) findViewById(R.id.dateView);
        dateView.setText(hourOfDay + " : " + minute);


        imageView = (ImageView) findViewById(R.id.btn_next);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new Kalendar();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        EditText editText = (EditText) findViewById(R.id.textView);
        textView.setText(currentDateString);
    }


     public void  onClickMic(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 10:
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textTest.setText(text.get(0));
                    break;

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.galachka, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_settings:
                addToDatabase();
                sendOnChannel1();
                sendOnChannel2();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addToDatabase() {

        if (TextUtils.isEmpty(dateView.getText().toString())) {

            Toast.makeText(ActivityTwo.this, "Введите текст", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(textTest.getText().toString())) {

            Toast.makeText(ActivityTwo.this, "Введите ваш номер телефона", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(textView.getText().toString())) {

            Toast.makeText(ActivityTwo.this, "Введите тип", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(auth.getUid());

        Model user = new Model(
                dateView.getText().toString(),
                textTest.getText().toString(),
                textView.getText().toString()
        );
        reference.child(user.getDesk())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivityTwo.this, "Готово", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityTwo.this, Main2Activity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityTwo.this, "Готово" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendOnChannel1() {
        String title = dateView.getText().toString();
        String message = textView.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2() {
        String title = dateView.getText().toString();
        String message = textView.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2, notification);
    }

}




