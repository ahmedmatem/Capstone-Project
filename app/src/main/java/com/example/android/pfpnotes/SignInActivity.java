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
import android.widget.Toast;

import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.RealtimeData;
import com.example.android.pfpnotes.net.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    private Connection mConnection;
    private FirebaseAuth mAuth;

    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // TODO: check internet connection before signing in
        mConnection = new Connection(this);
        if(!mConnection.isConnected()){
            Intent intent =
                    new Intent(SignInActivity.this, NoteListActivity.class);
            startActivity(intent);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            updateUI(user);
        }

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        CheckBox stayIn = (CheckBox) findViewById(R.id.stay_in);

        Button signIn = (Button) findViewById(R.id.btn_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: show progressbar
                String email = username.getText().toString();
                String pass = password.getText().toString();
                validate(email, pass);
                signIn(email, pass);
            }
        });
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
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            if(mConnection.isConnected()) {
                new RealtimeData(SignInActivity.this).read();
            }

            mPreferences = new Preferences(this);
            mPreferences.writeUserEmail(currentUser.getEmail());

            Intent intent = new Intent(SignInActivity.this, NoteListActivity.class);
            startActivity(intent);
        }
    }
}
