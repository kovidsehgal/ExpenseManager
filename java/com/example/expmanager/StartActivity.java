package com.example.expmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//first Activity
public class StartActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgetPassword;
    private TextView mSignupHere;

    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();

        //login if user have already login
        //no need to login each time
        if(mAuth.getCurrentUser() != null)
        {
          startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            //loadUserInformation();
        };

        mDialog = new ProgressDialog(this);
        loginDetails();

    }

    private void loginDetails()
    {
        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        mForgetPassword = findViewById(R.id.forget_password);
        mSignupHere = findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email Required..");
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    mPass.setError("Password Required..");
                    return;
                }
                mDialog.setMessage("Processing..");
                mDialog.show();
                loginUser(email,pass);
            }
        });

        //not a user, sign up
        mSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        //forget password and receive link for password reset if email already registered
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(StartActivity.this,"Reset Link sent to your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StartActivity.this,"Error this Link is not sent" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                    }
                });

                passwordResetDialog.create().show();
                //startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });

    }

    //firebase check login credentials
    private void loginUser(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(StartActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartActivity.this,HomeActivity.class));
                            finish();
                            mDialog.dismiss();

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(StartActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();

                        }
                }


        });
    }


}