package com.project.csci3130.dalrs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText CurrentPassword;
    private EditText NewPassword1;
    private EditText NewPassword2;

    private Button changepassword;
    private TextView result;

    private static FirebaseUser user;
    private static FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user = LoginInterfaceActivity.getUser();
        auth = LoginInterfaceActivity.getAuth();

        result = (TextView)findViewById(R.id.result);
        CurrentPassword = (EditText) findViewById(R.id.currentpassword);
        NewPassword1 = (EditText) findViewById(R.id.newpassword1);
        NewPassword2 = (EditText) findViewById(R.id.newpassword2);
        changepassword = (Button)findViewById(R.id.resetbutton);
        Button Change = (Button) findViewById(R.id.resetbutton);




        Change.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String newpassword1 = NewPassword1.getText().toString();
                final String newpassword2 = NewPassword2.getText().toString();
                String oldpassword = CurrentPassword.getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                final AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassword);
                if (newpassword1.equals(newpassword2) && (!oldpassword.equals(null))){
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newpassword1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Snackbar snackbar_fail = Snackbar
                                                    .make(findViewById(R.id.result), "Something went wrong. Please try again later", Snackbar.LENGTH_LONG);
                                            snackbar_fail.show();
                                        } else {
                                            Snackbar snackbar_su = Snackbar
                                                    .make(findViewById(R.id.result), "Password Successfully Modified", Snackbar.LENGTH_LONG);
                                            snackbar_su.show();
                                        }
                                    }
                                });
                            } else {
                                Snackbar snackbar_su = Snackbar
                                        .make(findViewById(R.id.result), "Authentication Failed", Snackbar.LENGTH_LONG);
                                snackbar_su.show();
                            }
                        }
                    });

                    startActivity(new Intent(ChangePassword.this,LoginInterfaceActivity.class));
                 }
                 else if(!newpassword1.equals(newpassword2)){
                    result.setText("Your passwords are not the same in two field");
                }
                else if(oldpassword.equals(null)){
                    result.setText("Your must enter you current password.");
                }
            }
        });
    }
}





