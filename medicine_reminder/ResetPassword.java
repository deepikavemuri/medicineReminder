package com.example.medicine_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView email;
    private Button resetSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = (TextView) findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        resetSubmit = (Button) findViewById(R.id.resetSubmit);
        resetSubmit.setOnClickListener(this);
    }

    public void reset() {
        String email1 = email.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email1);
        Toast.makeText(ResetPassword.this,"Reset link sent", Toast.LENGTH_LONG).show();
        Intent i1 = new Intent(ResetPassword.this, Login.class);
        startActivity(i1);

    }

    @Override
    public void onClick(View view) {
        if (view == resetSubmit)
            reset();
    }
}
