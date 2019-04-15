package com.example.boyraztalha.instagramclonedenemesi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername,editFullname,editEmail,editPassword;
    Button buttonRegister;
    TextView textMessage;

    FirebaseAuth mAuth;
    DatabaseReference DatabaseRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = findViewById(R.id.editUsername);
        editFullname = findViewById(R.id.editFullname);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textMessage = findViewById(R.id.textMessage);

        mAuth = FirebaseAuth.getInstance();

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.show();

                String username = editUsername.getText().toString();
                String fullName = editFullname.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(fullName) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }else if (password.length() < 4){
                    Toast.makeText(RegisterActivity.this, "Password can not be smaller than 6!", Toast.LENGTH_SHORT).show();
                }else {
                    register(username,fullName,email,password);
                }
            }
        });
    }

    public void register(final String username, final String fullName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    DatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("fullname",fullName);
                    hashMap.put("bio","");
                    hashMap.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/instagramclonedenemesi-b9a6d.appspot.com/o/profilepicfornow.jpeg?alt=media&token=4732d823-cabb-4315-926d-9696dbe892de");

                    DatabaseRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "You cant register with this" +
                            "email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
