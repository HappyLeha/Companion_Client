package com.example.companion;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends AppCompatActivity {
    ListView lw;
    ArrayAdapter<String> ad;
    List<Trip> trips;
    ArrayList<String> tripsS;
    Intent intentTrip;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        Bundle arguments = getIntent().getExtras();
        login = arguments.get("login").toString();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[]{"Результаты поиска","Предстоящие поездки","Состоявшиеся поездки"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    refresh(position);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this,MainActivity.class);
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
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent intentCreateTrip=new Intent(this,CreateTripActivity.class);
        intentCreateTrip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentCreateTrip.putExtra("login",login);
        Intent intentForm=new Intent(this,FormActivity.class);
        intentForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentForm.putExtra("login",login);
        Intent intentEditUser=new Intent(this,EditUserActivity.class);
        intentEditUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentEditUser.putExtra("login",login);
        switch (id) {
            case R.id.form :
                startActivity(intentForm);
                return true;
            case R.id.createTrip :
             startActivity(intentCreateTrip);
             return true;
            case R.id.profile :
                startActivity(intentEditUser);
                return true;
                case R.id.exit:
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void refresh(int position) {
        lw=findViewById(R.id.List);
        try  {

            switch (position) {
                 case 0:
                     HTTP.TripsPost httpPost=new HTTP.TripsPost();
                     httpPost.execute(TripForm.get());
                     if (httpPost.get()==null) {
                         Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                         toast.show();
                         onClickExit(null);
                         return;
                     }
                     else {
                         trips = httpPost.get();
                     }
                 break;
                 case 1:
                     HTTP.TripsFutureGet httpGet2=new HTTP.TripsFutureGet();
                     httpGet2.execute(login);
                     if (httpGet2.get()==null) {
                         Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                         toast.show();
                         onClickExit(null);
                         return;
                     }
                     else {
                         trips = httpGet2.get();
                     }
                     break;
                 case 2:
                     HTTP.TripsComplietedGet httpGet1=new HTTP.TripsComplietedGet();
                     httpGet1.execute(login);
                     if (httpGet1.get()==null) {
                         Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                         toast.show();
                         onClickExit(null);
                         return;
                     }
                     else {
                         trips = httpGet1.get();
                     }
                     break;
             }
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
            toast.show();
            onClickExit(null);
        }
        tripsS=new ArrayList<>();
        //trips=new ArrayList<>();
        for (Trip x:trips) {
            tripsS.add(" id:                                      " + x.getId() + "\n Водитель:                        " + x.getDriver() + "\n Место отправления:     " + x.getFrom() + "\n Место назначения:       " + x.getTo());
        }
        ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tripsS);
        lw.setAdapter(ad);
        intentTrip=new Intent(this,TripActivity.class);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                intentTrip.putExtra("id",trips.get(position).getId());
                intentTrip.putExtra("login",login);
                intentTrip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentTrip);
            }
        });
    }
    public void onClickExit (View v) {
        Toast toast = Toast.makeText(this, "Китайцы!", Toast.LENGTH_LONG);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
