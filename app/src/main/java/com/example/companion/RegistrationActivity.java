package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {
    Button buttonBack;
    Button buttonRegistration;
    EditText eTextLogin;
    EditText eTextPassword;
    EditText eTextRepeatPassword;
    static final Uri CONTENT_USERS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/users");
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
            try (SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null)) {
                String login = eTextLogin.getText().toString().trim();
                String password = eTextPassword.getText().toString().trim();
                String repeatPassword = eTextRepeatPassword.getText().toString().trim();
                String[] progection;
                String selection;
                String[] selectionArgs;
                if (login.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    Toast toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG);

                    toast.show();

                    return;
                }
                progection=new String[]{"login","password"};
                selection="login = ?";
                selectionArgs=new String[]{login+""};
                try (Cursor cursor = getContentResolver().query(CONTENT_USERS_TYPE,progection,selection,selectionArgs,null)) {

                    if (cursor.getCount() != 0) {

                        Toast toast = Toast.makeText(this, "Данный пользователь уже существует!", Toast.LENGTH_LONG);

                        toast.show();

                        return;
                    }
                    if (!password.equals(repeatPassword)) {
                        Toast toast = Toast.makeText(this, "Введенные пароли не совпадают!", Toast.LENGTH_LONG);

                        toast.show();


                        return;
                    }
                    ContentValues cv=new ContentValues();
                    cv.put("login",login);
                    cv.put("password",password.hashCode());
                    getContentResolver().insert(CONTENT_USERS_TYPE,cv);



                    Toast toast = Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_LONG);

                    toast.show();
                    Intent intent = new Intent(this, AllListActivity.class);
                    intent.putExtra("driver",login);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

        });


    }
}
