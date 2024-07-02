package com.example.application_shopii.SignIn;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.application_shopii.MainActivity;
import com.example.application_shopii.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText email, pass;
    ImageView imageView;
    Button login;
    Switch switchToggle;
    Boolean modeAdmin = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findID();

        firebaseAuth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        switchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               animateSwitch(isChecked);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeAdmin==false){
                    loginWithEmailPassUser(email.getText().toString().trim(), pass.getText().toString().trim());
                } else  loginWithEmailPassAdmin(email.getText().toString().trim(), pass.getText().toString().trim());

            }
        });


    }

    private void loginWithEmailPassUser(String mail, String pass) {
        if (!TextUtils.isEmpty(mail)
            && !TextUtils.isEmpty(pass))
        {
            firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(SignInActivity.this, "sucsess login with email pass as a user", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loginWithEmailPassAdmin(String mail, String pass) {
        if (!TextUtils.isEmpty(mail)
                && !TextUtils.isEmpty(pass))
        {
            firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(SignInActivity.this, "sucsess login with email pass as a admin", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void findID() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        imageView = findViewById(R.id.imageView);
        switchToggle = findViewById(R.id.switch_toggle);
        login = findViewById(R.id.login);
    }

    private void animateSwitch(boolean isChecked) {
        float start = isChecked ? 0f : 1f;
        float end = isChecked ? 1f : 0f;

        modeAdmin = true;

        ObjectAnimator.ofFloat(switchToggle, "alpha", start, end)
                .setDuration(500)
                .start();

        // Optional: Change thumb color with animation
        int color = isChecked ? getResources().getColor(R.color.colorAccent) : getResources().getColor(android.R.color.darker_gray);
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator colorAnim = ObjectAnimator.ofArgb(switchToggle.getThumbDrawable(), "colorFilter", color);
        colorAnim.setDuration(500);
        colorAnim.addUpdateListener(animation -> {
            switchToggle.getThumbDrawable().setColorFilter((int) animation.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
        });
        colorAnim.start();
    }
}