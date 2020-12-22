package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class EditUserActivity extends AppCompatActivity {
String login;
User user;
EditText etextLogin;
EditText etextNewPassword;
EditText etextRepeatPassword;
EditText etextPassword;
TextView tviewPassword;
TextView tviewDriver;
TextView tviewPassenger;
TextView tviewRaiting;
Button buttonNewPassword;
Button buttonEdit;
Button buttonDelete;
boolean isPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Bundle arguments = getIntent().getExtras();
        login = arguments.get("login").toString();
        etextLogin=findViewById(R.id.etextLogin);
        etextNewPassword=findViewById(R.id.etextNewPassword);
        etextRepeatPassword=findViewById(R.id.etextRepeatPassword);
        etextPassword=findViewById(R.id.etextPassword);
        tviewDriver=findViewById(R.id.txtviewDriverResult);
        tviewPassenger=findViewById(R.id.txtviewPassengerResult);
        tviewRaiting=findViewById(R.id.txtviewRaitingResult);
        buttonNewPassword=findViewById(R.id.buttonChangePassword);
        tviewPassword=findViewById(R.id.txtviewPassword);
        buttonEdit=findViewById(R.id.buttonEdit);
        buttonDelete=findViewById(R.id.buttonDelete);
        try  {
            HTTP.UserGet httpGet=new HTTP.UserGet();
            user=null;
            httpGet.execute(login);
            HTTP.UserCountPost httpPost=new HTTP.UserCountPost();
            httpPost.execute(login);
            Count count=(Count)httpPost.get();
            HTTP.RatingsGet httpGet1=new HTTP.RatingsGet();
            httpGet1.execute(login);
            Double raiting=httpGet1.get();
            if (httpGet.get()!=null&&httpGet.get().getClass()!=User.class||count==null||raiting==null) {
                Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                toast.show();
                onClickExit(null);
            }
            else user=(User)httpGet.get();
            etextLogin.setText(user.getLogin());
            tviewDriver.setText(count.getDriver()+"");
            tviewPassenger.setText(count.getPassenger()+"");
            String raitingS=raiting+"";
            raitingS=raitingS==null?"Нет рейтинга":raitingS;
            tviewRaiting.setText(raitingS);
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
            toast.show();
            onClickExit(null);
        }
        buttonNewPassword.setOnClickListener((v)->{
            if (tviewPassword.getVisibility()== View.GONE) {
                tviewPassword.setVisibility(View.VISIBLE);
                etextNewPassword.setVisibility(View.VISIBLE);
                etextRepeatPassword.setVisibility(View.VISIBLE);
                isPassword=true;
            }
            else {
                tviewPassword.setVisibility(View.GONE);
                etextNewPassword.setVisibility(View.GONE);
                etextRepeatPassword.setVisibility(View.GONE);
                isPassword=false;
            }
        });
        buttonEdit.setOnClickListener((v)->{
            String newLogin=etextLogin.getText().toString().trim();
            String password=etextPassword.getText().toString().trim();
            String newPassword=etextNewPassword.getText().toString().trim();
            String repeatPassword=etextRepeatPassword.getText().toString().trim();
            User newUser;
            if (newLogin.isEmpty()||password.isEmpty()||isPassword&&(newPassword.isEmpty()||repeatPassword.isEmpty())) {
                Toast toast = Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (isPassword) {
              if (!newPassword.equals(repeatPassword))  {
                  Toast toast = Toast.makeText(this, "Введённые пароли должны совпадать!", Toast.LENGTH_LONG);
                  toast.show();
                  return;
              }
                newUser=new User(-1,newLogin,AES.encrypt(newPassword));
            }
            else newUser=new User(-1,newLogin,AES.encrypt(password));
            if (!Arrays.equals(AES.encrypt(password),user.getPassword())) {
                Toast toast = Toast.makeText(this, "Введён неверный пароль!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            HTTP.UserPut httpPut=new HTTP.UserPut();
            httpPut.execute(login,newUser);
            try {
                switch (httpPut.get()) {
                    case HttpURLConnection.HTTP_OK: {
                        Toast toast = Toast.makeText(this, "Пользователь успешно изменён!", Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(this, TripListActivity.class);
                        intent.putExtra("login", newLogin);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    }
                    case HttpURLConnection.HTTP_CONFLICT: {
                        Toast toast = Toast.makeText(this, "Данный пользователь уже существует!", Toast.LENGTH_LONG);
                        toast.show();
                        break;
                    }
                    default: {
                        Toast toast = Toast.makeText(this, "Сервер недоступен! Изменение пользователя невозможно", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Сервер недоступен! Изменение пользователя невозможно", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        buttonDelete.setOnClickListener((v)->{
            try {
                HTTP.UserDelete httpDelete = new HTTP.UserDelete();
                HTTP.UsersGet httpGet = new HTTP.UsersGet();
                httpDelete.execute(login);
                if (httpDelete.get()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "Сервер недоступен! Удаление пользователя невозможно!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Сервер недоступен! Удаление пользователя невозможно!", Toast.LENGTH_LONG);
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
    public void onClickExit (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intentForm=new Intent(this,FormActivity.class);
        intentForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentForm.putExtra("login",login);
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent intentList=new Intent(this,TripListActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentList.putExtra("login",login);
        Intent intentCreateTrip=new Intent(this,CreateTripActivity.class);
        intentCreateTrip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentCreateTrip.putExtra("login",login);
        switch (id) {
            case R.id.form :
                startActivity(intentForm);
                return true;
            case R.id.tripList :
                startActivity(intentList);
                return true;
            case R.id.createTrip :
                startActivity(intentCreateTrip);
                return true;
                case R.id.exit:
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
