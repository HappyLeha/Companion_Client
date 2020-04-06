package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                if (databaseAdapter.addUser(user)) {
                    Toast toast = Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(this, TripListActivity.class);
                    intent.putExtra("login", login);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    TripForm.reset();
                    startActivity(intent);

                }
                else {
                    Toast toast = Toast.makeText(this, "Данный пользователь уже существует!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            }
        });
    }
}
