package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonRegistration;
    EditText eTextLogin;
    EditText eTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                User user = databaseAdapter.getUser(login);
                if (user == null) {
                    Toast toast = Toast.makeText(this, "Данный пользователь не существует!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if (password.hashCode() != user.getPassword()) {
                    Toast toast = Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(this, TripListActivity.class);
                intent.putExtra("login", login);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                TripForm.reset();
                startActivity(intent);
            }
        });
    }
}
