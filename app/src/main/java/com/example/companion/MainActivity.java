package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;

public class MainActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonRegistration;
    EditText eTextLogin;
    EditText eTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRegistration=findViewById(R.id.buttonRegistration);
        buttonLogin=findViewById(R.id.buttonLogin);
        eTextLogin=findViewById(R.id.etextLogin);
        eTextPassword=findViewById(R.id.etextPassword);
        buttonRegistration.setOnClickListener((v)->{
            Intent intent=new Intent(this,RegistrationActivity.class);
            startActivity(intent);
        });
        buttonLogin.setOnClickListener((v)->{
            String login = eTextLogin.getText().toString().trim();
            String password = eTextPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty()) {
                Toast toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            try  {

                HTTP.UserGet httpGet=new HTTP.UserGet();
                User user;
                httpGet.execute(login);
                if (httpGet.get()!=null&&httpGet.get().getClass()!=User.class) {
                    Toast toast = Toast.makeText(this, "Сервер недоступен! Авторизация невозможна!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                else user=(User)httpGet.get();
                if (user == null) {
                    Toast toast = Toast.makeText(this, "Данный пользователь не существует!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if (!Arrays.equals(AES.encrypt(password),user.getPassword())) {
                    Toast toast = Toast.makeText(this, "Введён неверный пароль!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Intent intent = new Intent(this, TripListActivity.class);
                intent.putExtra("login", login);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                TripForm.reset();
                startActivity(intent);
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Сервер недоступен! Авторизация невозможна!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
