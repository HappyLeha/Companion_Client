package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;


public class AllListActivity extends AppCompatActivity {
    Button buttonAdd;

    EditText eTextFrom;
    EditText eTextTo;
    EditText eTextCount;
    EditText eTextTransport;
    EditText eTextCost;
    ListView lw;
    TextView tviewDateFromResult;
    TextView tviewTimeFromResult;
    TextView tviewDateToResult;
    TextView tviewTimeToResult;
    Calendar dateFrom;
    Calendar timeFrom;
    Calendar dateTo;
    Calendar timeTo;
    Calendar dateTimeFrom;
    Calendar dateTimeTo;
    ArrayAdapter<String> ad;
    ArrayList<Trip> trips;
    ArrayList<String> tripsS;
    Intent intentTrip;
    String driver;
    String selection;
    String[] selectionArgs;
    static final Uri CONTENT_TRIPS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/trips");
    static final Uri CONTENT_USERS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        dateFrom= Calendar.getInstance();
        timeFrom= Calendar.getInstance();
        dateTo= Calendar.getInstance();
        timeTo= Calendar.getInstance();
        dateTimeFrom=Calendar.getInstance();
        dateTimeTo=Calendar.getInstance();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        /*tabSpec.setContent(R.id.linearLayout);
        tabSpec.setIndicator("Фильтр");
        tabHost.addTab(tabSpec);*/

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Поездки");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayout3);
        tabSpec.setIndicator("Добавить");
        tabHost.addTab(tabSpec);

       /* tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.linearLayout4);
        tabSpec.setIndicator("Профиль");
        tabHost.addTab(tabSpec);*/

        tabHost.setCurrentTab(2);
        buttonAdd=findViewById(R.id.buttonAdd);

        eTextFrom=findViewById(R.id.etextFrom);
        eTextTo=findViewById(R.id.etextTo);
        eTextCount=findViewById(R.id.etextCount);
        eTextTransport=findViewById(R.id.etextTransport);
        eTextCost=findViewById(R.id.etextCost);
        lw=findViewById(R.id.List);
        tviewDateFromResult=findViewById(R.id.txtviewDateFromResult);
        tviewTimeFromResult=findViewById(R.id.txtviewTimeFromResult);
        tviewDateToResult=findViewById(R.id.txtviewDateToResult);
        tviewTimeToResult=findViewById(R.id.txtviewTimeToResult);
        intentTrip = new Intent(this, TripActivity.class);
        Bundle arguments = getIntent().getExtras();
        driver = arguments.get("driver").toString();
        setInitialDateTime();
        buttonAdd.setOnClickListener((v)->{


                String fromS=eTextFrom.getText().toString().trim();
                String toS=eTextTo.getText().toString().trim();
                String countS=eTextCount.getText().toString().trim();
                String transportS=eTextTransport.getText().toString().trim();
                String costS=eTextCost.getText().toString().trim();
                try {
                    if (fromS.isEmpty()||toS.isEmpty()||countS.isEmpty()||transportS.isEmpty()||costS.isEmpty()) throw new Exception();
                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG);

                    toast.show();
                    return;
                }

                try {
                    dateTimeFrom.set(Calendar.HOUR_OF_DAY,timeFrom.get(Calendar.HOUR_OF_DAY));
                    dateTimeFrom.set(Calendar.MINUTE,timeFrom.get(Calendar.MINUTE));
                    dateTimeFrom.set(Calendar.YEAR,dateFrom.get(Calendar.YEAR));
                    dateTimeFrom.set(Calendar.MONTH,dateFrom.get(Calendar.MONTH));
                    dateTimeFrom.set(Calendar.DAY_OF_MONTH,dateFrom.get(Calendar.DAY_OF_MONTH));
                    dateTimeTo.set(Calendar.HOUR_OF_DAY,timeTo.get(Calendar.HOUR_OF_DAY));
                    dateTimeTo.set(Calendar.MINUTE,timeTo.get(Calendar.MINUTE));
                    dateTimeTo.set(Calendar.YEAR,dateTo.get(Calendar.YEAR));
                    dateTimeTo.set(Calendar.MONTH,dateTo.get(Calendar.MONTH));
                    dateTimeTo.set(Calendar.DAY_OF_MONTH,dateTo.get(Calendar.DAY_OF_MONTH));

                    if (dateTimeTo.compareTo(dateFrom)<=0) throw new Exception();

                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this,"Дата и время отправления не может быть дальше или совпадать с датой и временем прибытия!", Toast.LENGTH_LONG);

                    toast.show();
                    return;
                }
                try {
                    if (Calendar.getInstance().compareTo(dateTimeFrom)>0||Calendar.getInstance().compareTo(dateTimeTo)>0) throw new Exception();
                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Дата прибытия или отправления не может быть раньше текущей даты!", Toast.LENGTH_LONG);

                    toast.show();
                    return;
                }
                try {
                  if (Integer.parseInt(countS)<=0) throw new Exception();
                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Количество мест должно быть числом больше 0!", Toast.LENGTH_LONG);

                    toast.show();
                    return;
                }
                try {
                    if (Double.parseDouble(costS)<0.0) throw new Exception();
                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Стоимость должна быть число не меньше 0!", Toast.LENGTH_LONG);

                    toast.show();
                    return;
                }
                ContentValues cv=new ContentValues();
                cv.put("id",Trip.count);
                cv.put("dateTimeFrom",dateTimeFrom.get(Calendar.DAY_OF_MONTH)+" "+dateTimeFrom.get(Calendar.MONTH)+" "+dateTimeFrom.get(Calendar.YEAR)+" "+dateTimeFrom.get(Calendar.HOUR_OF_DAY)+" "+dateTimeFrom.get(Calendar.MINUTE));
                cv.put("dateTimeTo",dateTimeTo.get(Calendar.DAY_OF_MONTH)+" "+dateTimeTo.get(Calendar.MONTH)+" "+dateTimeTo.get(Calendar.YEAR)+" "+dateTimeTo.get(Calendar.HOUR_OF_DAY)+" "+dateTimeTo.get(Calendar.MINUTE));
                cv.put("fromPlace",fromS);
                cv.put("toPlace",toS);
                cv.put("countOfPlaces",Integer.parseInt(countS));
                cv.put("currentCountOfPlaces",0);
                cv.put("transport",transportS);
                cv.put("cost",Double.parseDouble(costS));
                cv.put("driver",driver);
                getContentResolver().insert(CONTENT_TRIPS_TYPE,cv);

                Trip.incrementCount();


            Toast toast = Toast.makeText(this, "Поездка успешно создана!", Toast.LENGTH_LONG);

                toast.show();



        });

            String[] columns={"id","driver","fromPlace","toPlace"};
            Cursor cursor=getContentResolver().query( CONTENT_TRIPS_TYPE,columns,null,null,null);

            trips = new ArrayList();


            if (cursor.moveToFirst()) {
                do {
                    trips.add(new Trip(cursor.getString(2),cursor.getString(3),cursor.getInt(0),cursor.getString(1))); //
                } while (cursor.moveToNext());
            }

            tripsS=new ArrayList<>();
            for (Trip x:trips) {
                tripsS.add(" id:                                      " + x.id + "\n Водитель:                        " + x.getDriver() + "\n Место отправления:     " + x.getFrom() + "\n Место назначения:       " + x.getTo());
            }

            cursor.close();
            ad=new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, tripsS);


            lw.setAdapter(ad);



        lw.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                intentTrip.putExtra("id",trips.get(position).id);
                intentTrip.putExtra("login",driver);
                intentTrip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentTrip);



            }
        });

    }
    private void setInitialDateTime() {

        tviewDateFromResult.setText(DateUtils.formatDateTime(this,
                dateFrom.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        ));
        tviewTimeFromResult.setText(DateUtils.formatDateTime(this,
                timeFrom.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME
        ));
        tviewDateToResult.setText(DateUtils.formatDateTime(this,
                dateTo.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
        tviewTimeToResult.setText(DateUtils.formatDateTime(this,
                timeTo.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME
        ));
    }
    TimePickerDialog.OnTimeSetListener tf=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeFrom.set(Calendar.MINUTE, minute);

            setInitialDateTime();
        }
    };
    TimePickerDialog.OnTimeSetListener tt=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            timeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeTo.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };


    DatePickerDialog.OnDateSetListener df=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateFrom.set(Calendar.YEAR, year);
            dateFrom.set(Calendar.MONTH, monthOfYear);
            dateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            setInitialDateTime();
        }
    };
    DatePickerDialog.OnDateSetListener dt=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateTo.set(Calendar.YEAR, year);
            dateTo.set(Calendar.MONTH, monthOfYear);
            dateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    public void setDateFrom(View v) {
        new DatePickerDialog(this, df,
                dateFrom.get(Calendar.YEAR),
                dateFrom.get(Calendar.MONTH),
                dateFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void setDateTo(View v) {
        new DatePickerDialog(this, dt,
                dateTo.get(Calendar.YEAR),
                dateTo.get(Calendar.MONTH),
                dateTo.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTimeFrom(View v) {
        new TimePickerDialog(this, tf,
                timeFrom.get(Calendar.HOUR_OF_DAY),
                timeFrom.get(Calendar.MINUTE), true)
                .show();
    }
    public void setTimeTo(View v) {
        new TimePickerDialog(this, tt,
                timeTo.get(Calendar.HOUR_OF_DAY),
                timeTo.get(Calendar.MINUTE), true)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        switch(id){
            case R.id.delete :
                selection="login=?";
                selectionArgs=new String[]{driver};
                getContentResolver().delete(CONTENT_USERS_TYPE, selection, selectionArgs);

                startActivity(intent);
                return true;
            case R.id.exit:

                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
