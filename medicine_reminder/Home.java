package com.example.medicine_reminder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "DEEPIKA";
    private ImageView med_bt;
    private ImageView log_out_bt;
    private ImageView shop;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference medRef = db.collection("medicines");
    private ListView listView;
    private ArrayList<Medicine> med_list = new ArrayList<>();
    private ArrayList<String> med_names = new ArrayList<>();
    private TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
  //      Bundle extras = getIntent().getExtras();
  //      if(extras != null) {
    //        Uri ringtone = extras.getString("ringtone");
      //      ringtone.stop();



        med_bt = (ImageView) findViewById(R.id.med_bt);
        log_out_bt = (ImageView) findViewById(R.id.log_out_bt);
        mAuth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.listView);
        shop = (ImageView) findViewById(R.id.shop);

        log_out_bt.setOnClickListener(this);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Shop.class);
                startActivity(i);
            }
        });

        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent i = new Intent(Home.this, Login.class);
            startActivity(i);
        }

        FirebaseUser fbu = mAuth.getCurrentUser();

        med_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        String uid = fbu.getUid();
        loadMeds(uid);
    }


    public void openActivity2(){
        Intent intent = new Intent(Home.this, addMeds.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view == log_out_bt) {
            mAuth.signOut();
            finish();
            Intent i = new Intent(Home.this, Login.class);
            startActivity(i);
        }
    }

    private void loadMeds(String uid) {
        db.collection("medicines")
                .whereEqualTo("uid", uid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<Medicine> m = documentSnapshots.toObjects(Medicine.class);
                    empty = (TextView) findViewById(R.id.empty);
                    if(m.size() != 0)
                        empty.setText("");

                    med_list.addAll(m);
                    Log.d(TAG, "onSuccess: " + med_list);
                    final CustomArrayAdapter adapter = new CustomArrayAdapter(med_list);
                    listView.setAdapter(adapter);

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
                            alert.setMessage("Do you want to remove this item?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Medicine med = (Medicine) parent.getItemAtPosition(position);
                                    String medId = med.getMedId();
                                    med_list.remove(med);
                                    adapter.notifyDataSetChanged();
                                    if(med_list.size() == 0)
                                        db.collection("medicines").document(medId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Home.this,"Medicine successfully deleted!", Toast.LENGTH_LONG).show();
                                                        Intent i1 = new Intent(Home.this, Home.class);
                                                        startActivity(i1);

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting medicine", e);
                                                    }
                                                });

                                    else
                                        db.collection("medicines").document(medId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Home.this,"Medicine successfully deleted!", Toast.LENGTH_LONG).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting medicine", e);
                                                    }
                                                });
                                }
                            });
                            alert.setNegativeButton("No", null);
                            alert.show();
                            return true;
                        }
                    });
                }
            }
        });

    }

    private class CustomArrayAdapter extends BaseAdapter{
        ArrayList<Medicine> medicines;
        LayoutInflater inflater;

        CustomArrayAdapter(ArrayList<Medicine> medicines) {
            this.medicines = medicines;
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return medicines.size();
        }

        @Override
        public Object getItem(int position) {
            return medicines.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) convertView = inflater.inflate(R.layout.activity_show_med, null);

            Medicine med = medicines.get(position);
            String med_name = med.getMed_name();
            int hour = med.getHour();
            int minute = med.getMinute();
            double dosage = med.getDosage();

            TextView mName = (TextView) convertView.findViewById(R.id.med_name);
            TextView mHour = (TextView) convertView.findViewById(R.id.med_time);
            TextView mDos = (TextView) convertView.findViewById(R.id.dosage);

            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);

            mName.setText(med_name);
            mHour.setText(String.format("%02d:%02d", hour, minute));
            mDos.setText(", " + dosage);
            return convertView;
        }
    }

}
