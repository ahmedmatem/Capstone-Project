package com.example.android.pfpnotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.RealtimeData;
import com.example.android.pfpnotes.net.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    public static final String SOURCE_ACTIVITY_NAME = "source_activity_name";

    private Connection mConnection;
    private FirebaseAuth mAuth;

    private Preferences mPreferences;
    private Bundle mBundle;

    private Button mSignInButton;
    private ProgressBar mSigningInProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        mBundle = getIntent().getExtras();

        mSigningInProgressBar =
                (ProgressBar) findViewById(R.id.progress_bar_signing_in);

        mConnection = new Connection(this);
        if (!mConnection.isConnected()) {
            Intent intent =
                    new Intent(SignInActivity.this, NoteListActivity.class);
            startActivity(intent);
            finish();
        }

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        CheckBox stayIn = findViewById(R.id.stay_in);

        mSignInButton = findViewById(R.id.btn_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String pass = password.getText().toString();
                validate(email, pass);
                signIn(email, pass);
                v.setVisibility(View.GONE);
                mSigningInProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void validate(String email, String pass) {
        //TODO: validate email and password
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            if (mConnection.isConnected()) {
                new RealtimeData(SignInActivity.this).read();
            }

            mPreferences = new Preferences(this);
            mPreferences.writeUserEmail(currentUser.getEmail());
            mPreferences.setSignedIn(true);

            if (mBundle != null && mBundle.containsKey(SOURCE_ACTIVITY_NAME)) {
                String sourceActivityName = mBundle.getString(SOURCE_ACTIVITY_NAME);
                if(sourceActivityName != null) {
                    switch (sourceActivityName) {
                        case "UploadActivity":
                            Intent intent = new Intent(this, UploadActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        default:
                    }
                }
            } else {
                Intent intent = new Intent(this, NoteListActivity.class);
                startActivity(intent);
            }
        } else {
            mSignInButton.setVisibility(View.VISIBLE);
            mSigningInProgressBar.setVisibility(View.GONE);
        }
    }
}
