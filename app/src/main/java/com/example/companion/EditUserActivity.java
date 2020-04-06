package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditUserActivity extends AppCompatActivity {
String login;
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
        try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
            User user=databaseAdapter.getUser(login);
            etextLogin.setText(user.getLogin());
            tviewDriver.setText(databaseAdapter.getCountOfDrivers(login)+"");
            tviewPassenger.setText(databaseAdapter.getCountOfPassengers(login)+"");
            String raiting=databaseAdapter.getRatingByUser(login)+"";
            raiting=raiting==null?"Нет рейтинга":raiting;
            tviewRaiting.setText(raiting);
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
            User user;
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
                newUser=new User(newLogin,newPassword.hashCode());
            }
            else newUser=new User(newLogin,password.hashCode());
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                user=databaseAdapter.getUser(login);
            }
            if (!user.getPassword().equals(password.hashCode())) {
                Toast toast = Toast.makeText(this, "Введён неверный пароль!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
              if  (!databaseAdapter.setUser(login,newUser)) {
                  Toast toast = Toast.makeText(this, "Данный логин уже существует!", Toast.LENGTH_LONG);
                  toast.show();
                  return;
              }
              else {
                  Toast toast = Toast.makeText(this, "Пользователь успешно изменён!", Toast.LENGTH_LONG);
                  toast.show();
                  Intent intent = new Intent(this, TripListActivity.class);
                  intent.putExtra("login", newUser.getLogin());
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
              }
            }
        });
        buttonDelete.setOnClickListener((v)->{
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                databaseAdapter.removeUser(login);
            }
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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
