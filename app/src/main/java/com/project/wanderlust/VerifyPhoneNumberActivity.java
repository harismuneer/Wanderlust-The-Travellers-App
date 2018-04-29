package com.project.wanderlust;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private static String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Intent intent = getIntent();
        final String string = (String) intent.getSerializableExtra("phone");

        TextView verify = findViewById(R.id.verify);
        verify.setText(getString(R.string.verify_3) + " " + string + " ");
        TextView phone = findViewById(R.id.phone);
        phone.setText(string + ". ");

        //text change listener on text field for 6 digit code
        //called whenever user type
        EditText text = findViewById(R.id.code);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 6) {
                    try {
                        //making credentials using code sent from firebase and code user typed
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, s.toString());

                        //signing in user to firebase if code was correct otherwise showing error message
                        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent intent = new Intent(VerifyPhoneNumberActivity.this, SetProfileDataActivity.class);
                                intent.putExtra("phone", string);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VerifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Crashlytics.logException(e);
                        Toast.makeText(VerifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //signing in user using automatic message recognition
        PhoneAuthProvider.getInstance().verifyPhoneNumber(string, 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        mAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNumberActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Intent intent = new Intent(VerifyPhoneNumberActivity.this, SetProfileDataActivity.class);
                                        intent.putExtra("phone", string);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) { Toast.makeText(VerifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        code = s;
                    }
                });
    }

    public void wrongNumber(View view) { onBackPressed(); }
}
