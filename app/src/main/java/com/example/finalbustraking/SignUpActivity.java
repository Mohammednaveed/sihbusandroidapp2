package com.example.finalbustraking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseFirestore db;




        // Initialize Firestore


    private EditText emailEditText, passEditText, phoneEditText, fname;
    private Button signup;
    private boolean passwordVisible = false; // Flag to track password visibility
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = FirebaseFirestore.getInstance();

        // ... (the rest of your onCreate code)


        //comnewpass = findViewById(R.id.com_new_pass);
        emailEditText = findViewById(R.id.email_edit_text);
        passEditText = findViewById(R.id.passedit_text);
       phoneEditText = findViewById(R.id.phone_edit_text);
       fname=findViewById(R.id.name_edit_text);

        signup = findViewById(R.id.singnup_button);



        TextView loginTextView = findViewById(R.id.login_text_view);
//        TextView otpsend = findViewById(R.id.opt_send);
//        otpsend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.p
//            }
//        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namef=fname.getText().toString();
                String email = emailEditText.getText().toString();
                String pass = passEditText.getText().toString();


                String phone=phoneEditText.getText().toString();
                //String comfnewpass = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(namef) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(phone) ) {
                    Toast.makeText(SignUpActivity.this, "Enter all the fields properly ", Toast.LENGTH_SHORT).show();
                } else {
                    regis(namef, email,pass,phone);
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the SignUpActivity
                Intent intent = new Intent(SignUpActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        passEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index for the right drawable

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passEditText.getRight() - passEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        // Initially hide the password
        hidePassword();
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // If the password is currently visible, change it to be hidden
            hidePassword();
        } else {
            // If the password is currently hidden, change it to be visible
            showPassword();
        }
    }

    private void showPassword() {
        passEditText.setTransformationMethod(null); // Show the password
        passwordVisible = true;
    }

    private void hidePassword() {
        passEditText.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Hide the password
        passwordVisible = false;
    }


        private void regis(String name, String email,String pass,String phone) {
            // Create a new user document in Firestore
            Map<String, Object> user = new HashMap<>();
            user.put("Name", name);
            user.put("email", email);
            user.put("password", pass);
            user.put("usertype", "regular");
            user.put("phone",phone);// Set the user type as "driver"
            Log.d("SignUpActivity", "Name: " + name);
            Log.d("SignUpActivity", "Phone: " + phone);

            // Add other user data as needed (e.g., name, phone number)

            // Define the path to the user document using the email as the document ID
            String documentPath = "users/" + email;

            // Add the user data to Firestore
            db.document(documentPath)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void Void) {
                            // Document was added successfully
                            Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, login.class); // Change to your login activity
                            startActivity(intent);
                            finish(); // Finish the signup activity
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Registration failed
                            Toast.makeText(SignUpActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
