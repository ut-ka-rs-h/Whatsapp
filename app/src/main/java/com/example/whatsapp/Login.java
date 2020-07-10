package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUserName, edtLoginPassword;
    private Button btnLogin;
    private TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");



        edtLoginUserName = findViewById(R.id.edtLoginName);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtSignUp);

        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {        //To use enter key as login...
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER & event.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnLogin);

                }
                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnLogin:

                final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Logging in " + edtLoginUserName.getText().toString());
                progressDialog.show();

                ParseUser.logInInBackground(edtLoginUserName.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {

                        if (user != null && e == null) {
                            FancyToast.makeText(Login.this, user.getUsername() + " is logged in",
                                    Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            transitionToSocialMediaActivity();
                        } else {
                            FancyToast.makeText(Login.this, "Error in logging in : " + e.getMessage(),
                                    Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        progressDialog.dismiss();
                    }
                });


                break;

            case R.id.txtSignUp:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;

        }

    }
    //To move down the keyboard on tapping the empty area...
    public void loginLayoutIsTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, WhatsappUsers.class);
        startActivity(intent);
    }
}