package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditTripActivity extends AppCompatActivity {
    Button buttonEdit;
    Button buttonBack;
    EditText eTextFrom;
    EditText eTextTo;
    EditText eTextCount;
    EditText eTextTransport;
    EditText eTextCost;
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
    Intent intent;
    String login;
    Trip trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        dateFrom= Calendar.getInstance();
        timeFrom= Calendar.getInstance();
        dateTo= Calendar.getInstance();
        timeTo= Calendar.getInstance();
        dateTimeFrom=Calendar.getInstance();
        dateTimeTo=Calendar.getInstance();
        buttonEdit=findViewById(R.id.buttonEdit);
        buttonBack=findViewById(R.id.buttonBack);
        eTextFrom=findViewById(R.id.etextFrom);
        eTextTo=findViewById(R.id.etextTo);
        eTextCount=findViewById(R.id.etextCount);
        eTextTransport=findViewById(R.id.etextTransport);
        eTextCost=findViewById(R.id.etextCost);
        tviewDateFromResult=findViewById(R.id.txtviewDateFromResult);
        tviewTimeFromResult=findViewById(R.id.txtviewTimeFromResult);
        tviewDateToResult=findViewById(R.id.txtviewDateToResult);
        tviewTimeToResult=findViewById(R.id.txtviewTimeToResult);
        Bundle arguments = getIntent().getExtras();
        login = arguments.get("login").toString();
        int id = arguments.getInt("id");
        dateFrom.set(Calendar.HOUR_OF_DAY,0);
        dateFrom.set(Calendar.MINUTE,0);
        dateTo.set(Calendar.HOUR_OF_DAY,0);
        dateTo.set(Calendar.MINUTE,0);
        timeFrom.set(Calendar.YEAR,0);
        timeFrom.set(Calendar.MONTH,0);
        timeFrom.set(Calendar.DAY_OF_MONTH,0);
        timeTo.set(Calendar.YEAR,0);
        timeTo.set(Calendar.MONTH,0);
        timeTo.set(Calendar.DAY_OF_MONTH,0);
        try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
            trip=databaseAdapter.getTrip(id);
            eTextFrom.setText(trip.getFrom());
            eTextTo.setText(trip.getTo());
            eTextCost.setText(trip.getCost()+"");
            eTextTransport.setText(trip.getTransport());
            eTextCount.setText(trip.getCountOfPlaces()+"");
            dateFrom=trip.getDateTimeFrom();
            timeFrom=trip.getDateTimeFrom();
            dateTo=trip.getDateTimeTo();
            timeTo=trip.getDateTimeTo();
        }
        setInitialDateTime();
        buttonEdit.setOnClickListener((v)->{
            String from=eTextFrom.getText().toString().trim();
            String to=eTextTo.getText().toString().trim();
            String count=eTextCount.getText().toString().trim();
            String transport=eTextTransport.getText().toString().trim();
            String cost=eTextCost.getText().toString().trim();
            try {
                if (from.isEmpty()||to.isEmpty()||count.isEmpty()||transport.isEmpty()||cost.isEmpty()) throw new Exception();
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
                if (dateTimeTo.compareTo(dateTimeFrom)<=0) throw new Exception();
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
                if (Integer.parseInt(count)<=0) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Количество мест должно быть числом больше 0!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try {
                if (Double.parseDouble(cost)<0.0) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Стоимость должна быть число не меньше 0!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            try {
                if (Integer.parseInt(count)<trip.getCurrentCountOfPlaces()) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Количество пассажиров больше количества мест!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                Trip сhangedTrip = new Trip(trip.getId(), dateTimeFrom, dateTimeTo, from, to, Integer.parseInt(count),
                        trip.getCurrentCountOfPlaces(), transport, Double.parseDouble(cost), trip.getDriver());
                databaseAdapter.setTrip(сhangedTrip);

            }



            Toast toast = Toast.makeText(this, "Поездка успешно изменена!", Toast.LENGTH_LONG);

            toast.show();

            intent = new Intent(this, TripActivity.class);
            intent.putExtra("login",login);
            intent.putExtra("id",id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        });
        buttonBack.setOnClickListener((v)->{
        intent = new Intent(this, TripActivity.class);
        intent.putExtra("login",login);
        intent.putExtra("id",id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

}
