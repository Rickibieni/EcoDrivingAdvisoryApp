package com.example.tutorialbuild;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton, signInButton;
    private TextView text;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTxtEmail);
        passwordEditText = findViewById(R.id.editTxtPassword);
        nameEditText = findViewById(R.id.editTxtFullName);

        registerButton = findViewById(R.id.button);
        signInButton = findViewById((R.id.button2));

        registerButton.setOnClickListener(v -> registerUser());
        signInButton.setOnClickListener(v -> signInUser());

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


    /**
     * Does button thing on click
     */
    private void registerUser () {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        // TODO: Go to home screen or main activity
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });




        // Assignment to Text
//        .setText(edtTextFN.getText().toString());
//        txtLN.setText(edtTextLN.getText().toString());
//        txtEmail.setText(edtTextEmail.getText().toString());

        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
    }

    private void signInUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // TODO: Go to home screen or main activity
//                        findViewById(R.id.loginLayout).setVisibility(View.GONE);
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        switchToFragment(new HomeFragment());
                    } else {
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Switches to fragment
     * @param fragment - Fragment that is being switched to
     */
    public void switchToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}