package com.example.medicine_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText sign_up_email;
    private EditText sign_up_password;
    private FirebaseAuth mAuth;
    private Button sign_up_bt;
    private EditText re_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up_email = (EditText) findViewById(R.id.sign_up_email);
        sign_up_password = (EditText) findViewById(R.id.sign_up_password);
        sign_up_bt = (Button) findViewById(R.id.sign_up_bt);
        re_password = (EditText) findViewById(R.id.re_password);

        sign_up_bt.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void register() {
        String email = sign_up_email.getText().toString().trim();
        String password =  sign_up_password.getText().toString().trim();
        String re_pass = re_password.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(SignUp.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(SignUp.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(re_pass)) {
            Toast.makeText(SignUp.this, "Please re-enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(re_pass)) {
            Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUp.this, Login.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        @Override
    public void onClick(View view) {
        if (view == sign_up_bt)
            register();
    }
}



