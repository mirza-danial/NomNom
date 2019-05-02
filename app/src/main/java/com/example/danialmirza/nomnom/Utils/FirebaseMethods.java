package com.example.danialmirza.nomnom.Utils;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean validateEmail(final String email)
    {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register a new email and password to Firebase Authentication
     * @param mEmail
     * @param mPassword
     * @param mName
     * @param mPhone
     */
    public void registerNewEmail(final EditText mEmail, final EditText mPassword, final EditText mName, final EditText mPhone){
        final String email = mEmail.getText().toString();
        final String name = mName.getText().toString();
        final String password = mPassword.getText().toString();
        final String phone = mPhone.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            if(password.length() < 6)
                            {
                                mPassword.setError("Password length must be greater or equal to 6.");
                            }
                            else if(phone.length() != 11 || (!phone.startsWith("030") && !phone.startsWith("031") && !phone.startsWith("032") && !phone.startsWith("033") && !phone.startsWith("034") && !phone.startsWith("0355") && !phone.startsWith("036")))
                            {
                                mPhone.setError("Phone number must follow 03XX-XXXXXXX format.");
                            }
                            else if(!validateEmail(email))
                            {
                                mEmail.setError("Email must follow correct format.");
                            }
                        }
                        else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            Toast.makeText(mContext, "Account created successfully! Navigating to phone verification window.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
