package com.example.medicine_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button sign_in_bt;
    private TextView sign_up;
    private EditText sign_in_email;
    private EditText sign_in_password;
    private FirebaseAuth mAuth;
    private TextView reset_pwd;
//    private Button sign_in_google;
  //  private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_in_bt = (Button) findViewById(R.id.sign_in_bt);
        sign_up = (TextView) findViewById(R.id.sign_up);
        reset_pwd = (TextView) findViewById(R.id.reset_pwd);

        sign_in_email = (EditText) findViewById(R.id.email);
        sign_in_password = (EditText) findViewById(R.id.password);
        sign_in_bt.setOnClickListener(this);
       // sign_in_google.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            finish();
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
        }
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent i1 = new Intent(Login.this, SignUp.class);
                startActivity(i1);
            }
        });

        reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent i2 = new Intent(Login.this, ResetPassword.class);
                startActivity(i2);
            }
        });
    }
    private void login() {
        String email = sign_in_email.getText().toString().trim();
        String password =  sign_in_password.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);
                        }
                    }
                });
    }

    public void googleSignIn() {
     /*   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GoogleSignInApi, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 100);
       */
    }



    @Override
    public void onClick(View view) {
        if (view == sign_in_bt)
            login();
    }
}
