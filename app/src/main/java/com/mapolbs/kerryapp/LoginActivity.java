package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapolbs.kerryapp.Utilities.ApiUtils;
import com.mapolbs.kerryapp.Utilities.ResObj;
import com.mapolbs.kerryapp.Utilities.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    EditText et_username,et_password;
    Button btn_login;
    UserService userService;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btnLogin);

        userService= ApiUtils.getUserService();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(LoginActivity.this,TypeSelectionActivity.class);
                startActivity(intent);*/

                String username=et_username.getText().toString();
                String password=et_password.getText().toString();

                //validate form
                if (validateLogin(username,password))
                {
                    //do login
                    doLogin(username,password);
                }

            }
        });

    }


    private boolean validateLogin(String username,String password) {

        if (username==null||username.trim().length()==0)
        {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password==null||password.trim().length()==0)
        {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void doLogin(final String userName,final String password) {
        Call call=userService.login(userName,password);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {
                    ResObj resObj = (ResObj) response.body();

                    if (resObj.getMessage().equals("true")) {
                        Intent intent = new Intent(LoginActivity.this, TypeSelectionActivity.class);
                        intent.putExtra("username", userName);
                        //intent.putExtra("password",password);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(LoginActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
