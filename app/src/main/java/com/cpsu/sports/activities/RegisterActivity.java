package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.User;
import com.cpsu.sports.data.repo.UserRepo;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText mTextFullname;
    TextInputEditText mTextUsername;
    TextInputEditText mTextPassword;
    TextInputEditText mTextConfirmPwd;
    TextView mBtnLogin;
    Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        mTextFullname = findViewById(R.id.txtFullname);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        mTextConfirmPwd = findViewById(R.id.txtConfirmPwd);
        mBtnRegister = findViewById(R.id.btnRegister);
        mBtnLogin = findViewById(R.id.btnLogIn);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    User user = new User();
                    UserRepo uRepo = new UserRepo();

                    String userName = mTextUsername.getText().toString().trim();
                    String userFullname = mTextFullname.getText().toString().trim();
                    String userPassword = mTextPassword.getText().toString().trim();

                    if (uRepo.checkForExistingUser(mTextUsername.getText().toString().trim())) {
                        Toast.makeText(RegisterActivity.this, "Username " + userName + " already exists.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    user.setUserFullname(userFullname);
                    user.setUserName(userName);
                    user.setUserPass(userPassword);
                    uRepo.registerUser(user);
                    Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean isInputValid() {
        if (mTextFullname.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Fullname is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mTextUsername.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Username is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mTextPassword.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mTextConfirmPwd.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mTextPassword.getText().toString().trim().equals(mTextConfirmPwd.getText().toString().trim())) {
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}