package com.example.danialmirza.nomnom.Utils;

import android.content.Context;
import android.content.Intent;
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

import com.example.danialmirza.nomnom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, name, password, phone;
    private EditText mEmail, mPassword, mName, mPhone;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    /*
    -------------------------------FIREBASE-------------------------------
    */

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private boolean isTaken;

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                name = mName.getText().toString();
                password = mPassword.getText().toString();
                phone = mPhone.getText().toString();
                String rt_password = ((EditText)findViewById(R.id.et_rt_password)).getText().toString();

                if(checkInputs(email, name, password, phone)){
                    boolean error = false;

                    if(password.length() < 6)
                    {
                        mPassword.setError("Password length must be greater or equal to 6.");
                        error = true;
                    }
                    if(phone.length() != 11 || (!phone.startsWith("030") && !phone.startsWith("031") && !phone.startsWith("032") && !phone.startsWith("033") && !phone.startsWith("034") && !phone.startsWith("0355") && !phone.startsWith("036")))
                    {
                        mPhone.setError("Phone number must follow 03XX-XXXXXXX format.");
                        error = true;
                    }
                    if(!validateEmail(email))
                    {
                        mEmail.setError("Email must follow correct format.");
                        error = true;
                    }
                    if(!password.equals(rt_password))
                    {
                        EditText rt_pass = findViewById(R.id.et_rt_password);
                        rt_pass.setError("Passwords do not match.");
                        error = true;
                    }
                    if(emailExists(email))
                    {
                        mEmail.setError("An account is already registered with the entered email address.");
                        error = true;
                    }
                    if(phoneExists(phone))
                    {
                        mPhone.setError("An account is already registered with the entered phone number.");
                        error = true;
                    }
                    if(error)
                    {
                        return;
                    }

                    mProgressBar.setVisibility(View.VISIBLE);

                    phone = phone.substring(1);
                    phone = "+92" + phone;

                    Intent intent = new Intent(RegisterActivity.this, phoneVerifyActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);

                    //firebaseMethods.registerNewEmail(mEmail, mPassword, mName, mPhone);
                }
                else
                {
                    if(email.equals(""))
                    {
                        mEmail.setError("Email field cannot be empty.");
                    }
                    if(name.equals(""))
                    {
                        mName.setError("Name field cannot be empty.");
                    }
                    if(password.equals(""))
                    {
                        mPassword.setError("Password field cannot be empty.");
                    }
                    if(phone.equals(""))
                    {
                        mPhone.setError("Phone field cannot be empty.");
                    }
                }
            }
        });
    }

    public boolean emailExists(final String email)
    {
        isTaken = false;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    String emailCheck = node.child("email").getValue().toString();
                    if(emailCheck.equals(email))
                    {
                        isTaken = true;
                        break;
                    }
                    else
                    {
                        isTaken = false;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Connection Error. Please try again in some time.", Toast.LENGTH_SHORT).show();
            }
        });

        return isTaken;
    }

    public boolean phoneExists(final String phone)
    {
        isTaken = false;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    String phoneCheck = node.child("phone_number").getValue().toString();
                    if(phoneCheck.equals(phone))
                    {
                        isTaken = true;
                        break;
                    }
                    else
                    {
                        isTaken = false;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Connection Error. Please try again in some time.", Toast.LENGTH_SHORT).show();
            }
        });

        return isTaken;
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

    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.et_email);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_register);
        mPassword = (EditText) findViewById(R.id.et_password);
        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);

        btnRegister = findViewById(R.id.btn_register);

        mContext = RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
    }

    private boolean checkInputs(String email, String username, String password, String phone){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("") || phone.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean validateEmail(final String email)
    {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started.");

        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);

        initWidgets();
        setupFirebaseAuth();
        init();
    }
}
