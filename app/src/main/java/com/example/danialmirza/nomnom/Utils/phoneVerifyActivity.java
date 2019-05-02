package com.example.danialmirza.nomnom.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.danialmirza.nomnom.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class phoneVerifyActivity extends AppCompatActivity {

    private static final String TAG = "phoneVerifyActivity";

    private String verificationID;

    private Context mContext;

    private Intent intent;

    private String email;
    private String password;
    private String phone;
    private String name;

    private TextView codeTextView;
    private Button verifyButton;
    private EditText codeEditText;
    private ProgressBar progressBar;

    //firebase
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification);
        Log.d(TAG, "onCreate: started.");
        codeEditText = findViewById(R.id.et_phoneCode);
        verifyButton = findViewById(R.id.btn_verify);
        codeTextView = findViewById(R.id.link_signup);
        progressBar = findViewById(R.id.progressBar);

        mContext = phoneVerifyActivity.this;
        //progressBar.setVisibility(View.GONE);
        //pleaseWait.setVisibility(View.GONE);

        firebaseMethods = new FirebaseMethods(mContext);
        mAuth = FirebaseAuth.getInstance();

        intent = getIntent();

        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        sendVerificationCode(phone);

        verifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString();

                if(code.equals("")) {
                    codeEditText.setError("Code is required. Please enter code.");
                    codeEditText.requestFocus();
                    return;
                }
                if(code.length() != 6)
                {
                    codeEditText.setError("Code must be a 6-digit number.");
                }
                else
                {
                    verifyCode(code);
                }

            }
        });

        codeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Log.d(TAG,"Code resent.");
                String phone = intent.getStringExtra("phone");
                sendVerificationCode(phone);
            }
        });
    }

    private void sendVerificationCode(String number)
    {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            user = task.getResult().getUser();
                            Intent intent = new Intent(phoneVerifyActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                            mAuth.getCurrentUser().linkWithCredential(credential)
                                    .addOnCompleteListener(phoneVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "linkWithCredential:success");
                                                user = task.getResult().getUser();

                                            } else {
                                                Log.w(TAG, "linkWithCredential:failure", task.getException());
                                            }
                                        }
                                    });
                            addUserToDatabase(user);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(mContext, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void addUserToDatabase(FirebaseUser user)
    {
        String u_id = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        User u = new User();
        u.setEmail(email);
        u.setPhone_number(Long.parseLong(phone));
        u.setUsername(name);
        u.setUser_id(u_id);
        u.setPhoto_path("https://firebasestorage.googleapis.com/v0/b/nom-nom-237911.appspot.com/o/profile_avatar_placeholder.png?alt=media&token=b04a03eb-3eee-4548-885f-9d7cd5c83148");

        reference.child("users")
                .child(u_id)
                .setValue(u);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                codeEditText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(phoneVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
