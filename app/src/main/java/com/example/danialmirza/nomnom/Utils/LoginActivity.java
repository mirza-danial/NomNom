package com.example.danialmirza.nomnom.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danialmirza.nomnom.HomeActivity;
import com.example.danialmirza.nomnom.R;
import com.example.danialmirza.nomnom.Utils.Fingerprint.fingerPrintVerifyActivity;
import com.example.danialmirza.nomnom.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 6969;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context context;
    private ProgressBar progressBar;
    private EditText et_email, et_password;

    private String email;
    private String password;

    private String id;

    private GoogleSignInClient mGoogleSignInClient;

    /*
    -------------------------------FIREBASE-------------------------------
    */

    private void init(){
        //initialize the button for loggin in
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG,"onClick: attempting to log in.");

                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if(password.equals(""))
                {
                    et_password.setError("Password field cannot be empty.");
                }
                if(email.equals(""))
                {
                    et_email.setError("Email field cannot be empty.");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        progressBar.setVisibility(View.GONE);

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(context, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        Button btnFinger = findViewById(R.id.btn_fingerprint);

        btnFinger.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Log.d(TAG,"onClick: attempting to log in using fingerprint.");

                String email = et_email.getText().toString();
                final String password = et_password.getText().toString();

                SharedPreferences sharedPreferences = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

                if(sharedPreferences.getString("email","").equals("") && sharedPreferences.getString("password","").equals(""))
                {
                    if(password.equals(""))
                    {
                        et_password.setError("Password field cannot be empty.");
                    }
                    if(email.equals(""))
                    {
                        et_email.setError("Email field cannot be empty.");
                    }
                    else
                    {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            progressBar.setVisibility(View.GONE);

                                            saveDetails();

                                            moveOn();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(context, "Authentication failed. Account does not exist. Try again!",
                                                    Toast.LENGTH_SHORT).show();

                                            progressBar.setVisibility(View.GONE);
                                        }

                                        // ...
                                    }
                                });
                    }
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, fingerPrintVerifyActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            }
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if(mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null)
                {
                    Log.d(TAG, "onAuthStateChange:signed_in" + user.getUid());
                }
                else
                {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void saveDetails()
    {
        SharedPreferences sharedPreferences = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", email);
        editor.putString("password", password);

        editor.commit();
    }

    private void moveOn()
    {
        Intent intent = new Intent(LoginActivity.this, fingerPrintVerifyActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressBar_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        context = LoginActivity.this;

        Log.d(TAG, "onCreate: started.");

        progressBar.setVisibility(View.GONE);

        setupFirebaseAuth();
        init();

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btn_google).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btn_google))
        {
            Log.d(TAG, "Google sign in initiated.");
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String u_id = user.getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                            User u = new User();
                            u.setEmail(user.getEmail());

                            if(user.getPhoneNumber()!=null)
                            {
                                u.setPhone_number(Long.parseLong(user.getPhoneNumber()));
                            }
                            else
                            {
                                u.setPhone_number(0);
                            }
                            u.setUsername(user.getDisplayName());
                            u.setUser_id(u_id);
                            u.setPhoto_path("https://firebasestorage.googleapis.com/v0/b/nomnomfirebasetest.appspot.com/o/profile_avatar_placeholder.png?alt=media&token=961418f5-a49c-49b0-94d8-2ca8fc96e2dd");

                            reference.child("users")
                                    .child(u_id)
                                    .setValue(u);

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
