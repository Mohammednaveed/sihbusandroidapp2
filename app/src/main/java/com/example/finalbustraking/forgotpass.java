package com.example.finalbustraking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class forgotpass extends AppCompatActivity {
    private EditText editText1;
    private MaterialButton fogpass;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        editText1=findViewById(R.id.forgot_email);
        fogpass=findViewById(R.id.forgot_btn);
        fogpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailforgot=editText1.getText().toString();
                if (TextUtils.isEmpty(emailforgot)) {
                    Toast.makeText(forgotpass.this, "Enter all the fields properly ", Toast.LENGTH_SHORT).show();
                } else {
                    forgot(emailforgot);
                }
            }
        });
    }

    private void forgot(String emailforgot){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(emailforgot);

        mAuth.sendPasswordResetEmail(emailforgot)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            openNewPasswordDialog(emailforgot);
                        }
                        else{
                            Toast.makeText(forgotpass.this,"filed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openNewPasswordDialog(final String email) {
        // Implement a dialog or another UI component to collect the new password from the user.
        // Once you have the new password, update it in Firestore as follows:
        // For example, we'll assume a dialog with an EditText to collect the new password.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set New Password");
        final EditText newPasswordEditText = new EditText(this);
        builder.setView(newPasswordEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newPassword = newPasswordEditText.getText().toString();

                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(forgotpass.this, "Enter a new password", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference userDocRef = db.collection("users").document(email);
                    userDocRef.update("password", newPassword)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(forgotpass.this, "Password updated successfully in Firestore", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(forgotpass.this, login.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(forgotpass.this, "Failed to update the password in Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}