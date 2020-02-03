package com.example.medicine_reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class addMeds extends AppCompatActivity {
    private static addMeds inst;
    public static addMeds instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private TimePicker time;
    private TextView name;
    private Button save;
    private int tp_hour, tp_min;
    private EditText dosage;
    private EditText condition;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meds);

        time = (TimePicker) findViewById(R.id.timePicker1);
        name = (TextView) findViewById(R.id.med_name);
        save = (Button) findViewById(R.id.save);
        dosage = (EditText) findViewById(R.id.dosage);
        condition = (EditText) findViewById(R.id.condition);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == save) {
                    getTime();
                    newMed();
                }
            }
        });

    }

    void newMed() {
        if (user != null)
            uid = user.getUid();
        String med_name = name.getText().toString();
        String med_dosage = dosage.getText().toString();
        String med_condition = condition.getText().toString();

        if (TextUtils.isEmpty(med_name)) {
            Toast.makeText(addMeds.this, "Please enter med name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(med_dosage)) {
            Toast.makeText(addMeds.this, "Please enter a dosage", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(med_dosage) <= 0)  {
            Toast.makeText(addMeds.this, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
            return;
        }

        String medId = db.collection("medicines").document().getId();

        double extract_dosage = Double.parseDouble(med_dosage);

        Map<String, Object> medicine = new HashMap<>();
        medicine.put("medId", medId);
        medicine.put("uid", uid);
        medicine.put("med_name", med_name);
        medicine.put("hour", tp_hour);
        medicine.put("minute", tp_min);
        medicine.put("dosage", extract_dosage);
        medicine.put("condition", med_condition);

        alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, tp_hour);
        calendar.set(Calendar.MINUTE, tp_min);
        Intent myIntent = new Intent(addMeds.this, AlarmReciever.class);
        myIntent.putExtra("med_name", med_name);
        myIntent.putExtra("dosage", med_dosage);
        myIntent.putExtra("medId", medId);
        pendingIntent = PendingIntent.getBroadcast(addMeds.this, 0, myIntent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmMgr.INTERVAL_DAY, pendingIntent);


        db.collection("medicines").document(medId)
                .set(medicine)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(addMeds.this, "Added Medicine!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(addMeds.this, Home.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addMeds.this, "Failed to add!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void setAlarmText(String alarmText) {
        System.out.println(alarmText);
    }

    void getTime() {
        tp_hour = time.getCurrentHour();
        tp_min = time.getCurrentMinute();
    }
}