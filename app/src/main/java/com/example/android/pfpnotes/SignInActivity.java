package com.example.android.pfpnotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.pfpnotes.common.CommonHelper;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.RealtimeData;
import com.example.android.pfpnotes.net.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    public static final String SOURCE_ACTIVITY_NAME = "source_activity_name";

    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

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

        final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.email_wrapper);
        final TextInputLayout passwordWrapper =
                (TextInputLayout) findViewById(R.id.password_wrapper);
        emailWrapper.setHint(getString(R.string.email));
        passwordWrapper.setHint(getString(R.string.password));

        CheckBox stayIn = findViewById(R.id.stay_in);

        mSignInButton = findViewById(R.id.btn_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonHelper.hideKeyboard(SignInActivity.this);

                String email = emailWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();

                if (!validateEmail(email)) {
                    emailWrapper.setError(getString(R.string.error_Invalid_email));
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError(getString(R.string.error_invalid_password));
                } else {
                    emailWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    // do sign in
                    signIn(email, password);
                    v.setVisibility(View.GONE);
                    mSigningInProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
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
                if (sourceActivityName != null) {
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
                finish();
            }
        } else {
            mSignInButton.setVisibility(View.VISIBLE);
            mSigningInProgressBar.setVisibility(View.GONE);
        }
    }
}
