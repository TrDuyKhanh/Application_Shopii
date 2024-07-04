package com.example.application_shopii.Auth;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.application_shopii.MainActivity;
import com.example.application_shopii.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button btnregister;
    EditText email, pass, pass2;
    Switch aSwitch;
    Boolean modeAdmin=false;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findID();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email.getText().toString().trim())
                && !TextUtils.isEmpty(pass.getText().toString().trim())
                && !TextUtils.isEmpty(pass2.getText().toString().trim())){
                    String mail = email.getText().toString().trim();
                    String pas = pass.getText().toString().trim();
                    String pas2 = pass2.getText().toString().trim();
                    if (pas.equals(pas2)){
                        CreateAccount(mail, pas, pas2);
                    } else Toast.makeText(SignUpActivity.this, "please enter pass and pass again correct", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(SignUpActivity.this, "fill all box please ", Toast.LENGTH_SHORT).show();
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modeAdmin = isChecked;
                animateSwitch(isChecked);
            }
        });
    }

    private void CreateAccount(String mail, String pas, String pas2) {
        firebaseAuth.createUserWithEmailAndPassword(mail, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    logEmailPassUser(mail, pas);

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = firebaseUser.getUid();

                    Map<String, Object> infor = new HashMap<>();
                    infor.put("email", mail);
                    infor.put("pass", pas);
                    if(modeAdmin==true){
                        infor.put("role", "0");
                        db.collection("admin").document(uid).set(infor)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SignUpActivity.this, "register as admin success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "register as admin fail", Toast.LENGTH_SHORT).show();
                                        //Log.d("fail", e.getMessage());
                                    }
                                });
                    } else if (modeAdmin==false){
                        Log.d("fail else", "else run ");
                        infor.put("role", "1");
                        db.collection("user").document(uid).set(infor)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SignUpActivity.this, "register as user success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "register as user fail", Toast.LENGTH_SHORT).show();
                                        //Log.d("fail", e.getMessage());
                                    }
                                });
                    }


            }

        }
        });
    }

    private void findID() {
        btnregister = findViewById(R.id.Register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        pass2 = findViewById(R.id.pass2);
        aSwitch = findViewById(R.id.switch_toggle);
    }
    private void animateSwitch(boolean isChecked) {
        float start = isChecked ? 0f : 1f;
        float end = isChecked ? 1f : 0f;

        ObjectAnimator.ofFloat(aSwitch, "alpha", start, end)
                .setDuration(500)
                .start();

        int color = isChecked ? getResources().getColor(R.color.colorAccent) : getResources().getColor(android.R.color.darker_gray);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator colorAnim = ObjectAnimator.ofArgb(aSwitch.getThumbDrawable(), "colorFilter", color);
        colorAnim.setDuration(500);
        colorAnim.addUpdateListener(animation -> {
            aSwitch.getThumbDrawable().setColorFilter((int) animation.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
        });
        colorAnim.start();
    }
    public void logEmailPassUser(String mail, String pass) {
        if (!TextUtils.isEmpty(mail)
                && !TextUtils.isEmpty(pass))
        {
            firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user1 = firebaseAuth.getCurrentUser();
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}