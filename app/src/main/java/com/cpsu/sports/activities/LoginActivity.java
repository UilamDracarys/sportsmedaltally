package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.Utils.PreferenceUtils;
import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.repo.UserRepo;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;

    TextInputEditText mTextUsername;
    TextInputEditText mTextPassword;
    Button mBtnLogin;
    TextView mBtnRegister;

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        init();
    }

    private void init() {
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        mBtnLogin = findViewById(R.id.btnLogIn);
        mBtnRegister = findViewById(R.id.btnRegister);
        PreferenceUtils prefs = new PreferenceUtils();

        username = prefs.getUsername(this);
        if (username != null ) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        mBtnLogin.requestFocus();
        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isInputValid(mTextUsername.getText().toString(), mTextPassword.getText().toString())) {
                    String username = mTextUsername.getText().toString().trim();
                    String password = mTextPassword.getText().toString().trim();
                    UserRepo userRepo = new UserRepo();
                    if (userRepo.checkUser(username, password)) {
                        PreferenceUtils.saveUsername(username, LoginActivity.this);
                        PreferenceUtils.savePassword(password, LoginActivity.this);
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        main.putExtra("username", mTextUsername.getText().toString());
                        startActivity(main);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Log in not found. Please check your username and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    private boolean isInputValid(String username, String password) {
        if (username.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Username is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}