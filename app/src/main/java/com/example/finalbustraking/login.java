package com.example.finalbustraking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    private EditText emailEditText1, passEditText1;
    private MaterialButton login1;
    private ProgressBar progressBar;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView forgot = findViewById(R.id.forgot_pass);
        TextView signUpTextView = findViewById(R.id.sign_up_text_view);
        login1 = findViewById(R.id.login_btn);
        emailEditText1 = findViewById(R.id.log_email);
        passEditText1 = findViewById(R.id.log_pass);
        progressBar = findViewById(R.id.progressBar);

        if (SharedPreferencesHelper.isLoggedIn(this)) {
            // User is already logged in, redirect to the appropriate activity
            String userType = SharedPreferencesHelper.getUserType(this);
            launchAppropriateActivity(userType);
            finish();
        }

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = emailEditText1.getText().toString();
                String pass1 = passEditText1.getText().toString();

                if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1)) {
                    Toast.makeText(login.this, "Enter all the fields properly", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar();
                    log(email1, pass1);
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, forgotpass.class);
                startActivity(intent);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        passEditText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passEditText1.getRight() - passEditText1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        hidePassword();
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            hidePassword();
        } else {
            showPassword();
        }
    }

    private void showPassword() {
        passEditText1.setTransformationMethod(null);
        passwordVisible = true;
    }

    private void hidePassword() {
        passEditText1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordVisible = false;
    }

    private void log(final String email1, final String pass1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(email1);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String storedPassword = document.getString("password");
                        if (pass1.equals(storedPassword)) {
                            String userType = document.getString("usertype");
                            SharedPreferencesHelper.setLoggedIn(login.this, true);
                            SharedPreferencesHelper.setUserType(login.this, userType);
                            launchAppropriateActivity(userType);
                        } else {
                            handleLoginFailure();
                        }
                    } else {
                        handleUserNotFound();
                    }
                } else {
                    handleLoginError();
                }
            }
        });
    }

    private void launchAppropriateActivity(String userType) {



        Intent intent;

        if ("driver".equals(userType)) {

            intent = new Intent(login.this, driverlocationparmission.class);
        } else if ("regular".equals(userType)) {

            intent = new Intent(login.this, home.class);
        } else {
            Toast.makeText(login.this, "Invalid user type.", Toast.LENGTH_SHORT).show();
            hideProgressBar();
            return;
        }
        startActivity(intent);
        finish();
    }

    private void handleLoginFailure() {
        Toast.makeText(login.this, "Login Failed: Incorrect credentials", Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    private void handleUserNotFound() {
        Toast.makeText(login.this, "User data not found.", Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    private void handleLoginError() {
        Toast.makeText(login.this, "Error getting user data.", Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        login1.setEnabled(false);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        login1.setEnabled(true);
    }
}
