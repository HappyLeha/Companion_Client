package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {
    Button buttonBack;
    Button buttonRegistration;
    EditText eTextLogin;
    EditText eTextPassword;
    EditText eTextRepeatPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        buttonBack=findViewById(R.id.buttonBack);
        buttonRegistration=findViewById(R.id.buttonRegistration);
        eTextLogin=findViewById(R.id.etextLogin);
        eTextPassword=findViewById(R.id.etextPassword);
        eTextRepeatPassword=findViewById(R.id.etextRepeatPassword);
        buttonBack.setOnClickListener((v)->{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });
        buttonRegistration.setOnClickListener((v)->{
            String login = eTextLogin.getText().toString().trim();
            String password = eTextPassword.getText().toString().trim();
            String repeatPassword = eTextRepeatPassword.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {


                if (!password.equals(repeatPassword)) {
                    Toast toast = Toast.makeText(this, "Введенные пароли не совпадают!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                User user = new User(login,password.hashCode());
                HTTP.UserPost httpPost=new HTTP.UserPost();
                httpPost.execute(user);
                switch (httpPost.get()) {
                    case HttpURLConnection.HTTP_CREATED: {
                        Toast toast = Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(this, TripListActivity.class);
                        intent.putExtra("login", login);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        TripForm.reset();
                        HTTP.UsersGet httpGet=new HTTP.UsersGet();
                        httpGet.execute();
                        if (httpGet.get()!=null) databaseAdapter.refreshUsers(httpGet.get());
                        startActivity(intent);
                        break;
                    }
                    case HttpURLConnection.HTTP_CONFLICT: {
                        Toast toast = Toast.makeText(this, "Данный пользователь уже существует!", Toast.LENGTH_LONG);
                        toast.show();
                        break;
                    }
                    default: {
                        Toast toast = Toast.makeText(this, "Сервер недоступен! Создание пользователя невозможно", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Сервер недоступен! Создание пользователя невозможно", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


}
