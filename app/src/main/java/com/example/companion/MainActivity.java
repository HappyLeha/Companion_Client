package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonRegistration;
    EditText eTextLogin;
    EditText eTextPassword;
    static final Uri CONTENT_USERS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyContentProvider.createBD(getBaseContext());
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
                String[] progection;
                String selection;
                String[] selectionArgs;
                int dbPassword;
                if (login.isEmpty() || password.isEmpty()) {
                    Toast toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG);

                    toast.show();

                    return;
                }
                progection=new String[]{"login","password"};
                selection="login = ?";
                selectionArgs=new String[]{login+""};
                getContentResolver().query(CONTENT_USERS_TYPE,progection,selection,selectionArgs,null);
                try (Cursor cursor = getContentResolver().query(CONTENT_USERS_TYPE,progection,selection,selectionArgs,null)) {
                    //db.rawQuery("Select * from users where login='" + login + "'", null)

                    if (cursor.getCount() == 0) {

                        Toast toast = Toast.makeText(this, "Данный пользователь не существует!", Toast.LENGTH_LONG);

                        toast.show();

                        return;
                    }
                    cursor.moveToFirst();
                    dbPassword=cursor.getInt(1);
                    if (password.hashCode()!=dbPassword) {
                        Toast toast = Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_LONG);

                        toast.show();


                        return;
                    }

                    Intent intent = new Intent(this, AllListActivity.class);
                    intent.putExtra("driver",login);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

            }

        });
    }
}
