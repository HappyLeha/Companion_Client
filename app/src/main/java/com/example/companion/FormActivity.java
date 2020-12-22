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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {
    Button buttonAccept;
    Button buttonDateFrom;
    Button buttonDateTo;
    Button buttonTimeStart;
    Button buttonTimeEnd;
    EditText eTextFrom;
    EditText eTextTo;
    EditText eTextCost;
    TextView tviewDateFromResult;
    TextView tviewTimeStartResult;
    TextView tviewDateToResult;
    TextView tviewTimeEndResult;
    EditText etextFrom;
    EditText etextTo;
    EditText etextCost;
    Calendar dateFrom=Calendar.getInstance();
    Calendar timeStart=Calendar.getInstance();
    Calendar dateTo=Calendar.getInstance();
    Calendar timeEnd=Calendar.getInstance();
    boolean dateFromIsNull=true;
    boolean dateToIsNull=true;
    boolean timeStartIsNull=true;
    boolean timeEndIsNull=true;
    TripForm tripForm;
    Intent intent;
    String login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        buttonAccept=findViewById(R.id.buttonAccept);
        buttonDateFrom=findViewById(R.id.buttonDateFromNull);
        buttonDateTo=findViewById(R.id.buttonDateToNull);
        buttonTimeStart=findViewById(R.id.buttonTimeStartNull);
        buttonTimeEnd=findViewById(R.id.buttonTimeEndNull);
        eTextFrom=findViewById(R.id.etextFrom);
        eTextTo=findViewById(R.id.etextTo);
        eTextCost=findViewById(R.id.etextCost);
        tviewDateFromResult=findViewById(R.id.txtviewDateFromResult);
        tviewTimeStartResult=findViewById(R.id.txtviewTimeStartResult);
        tviewDateToResult=findViewById(R.id.txtviewDateToResult);
        tviewTimeEndResult=findViewById(R.id.txtviewTimeEndResult);
        etextFrom=findViewById(R.id.etextFrom);
        etextTo=findViewById(R.id.etextTo);
        etextCost=findViewById(R.id.etextCost);
        Bundle arguments = getIntent().getExtras();
        login = arguments.get("login").toString();
        dateFrom.set(Calendar.HOUR_OF_DAY,0);
        dateFrom.set(Calendar.MINUTE,0);
        dateTo.set(Calendar.HOUR_OF_DAY,0);
        dateTo.set(Calendar.MINUTE,0);
        timeStart.set(Calendar.YEAR,0);
        timeStart.set(Calendar.MONTH,0);
        timeStart.set(Calendar.DAY_OF_MONTH,0);
        timeEnd.set(Calendar.YEAR,0);
        timeEnd.set(Calendar.MONTH,0);
        timeEnd.set(Calendar.DAY_OF_MONTH,0);
        tripForm=TripForm.get();
        if (tripForm.getDateFrom()!=null) {
            dateFrom = tripForm.getDateFrom();
            setInitialDateFrom();
        }
        else setDateFromNull();
        if (tripForm.getDateTo()!=null) {
            dateTo = tripForm.getDateTo();
            setInitialDateTo();
        }
        else setDateToNull();
        if (tripForm.getTimeStart()!=null) {
            timeStart = tripForm.getTimeStart();
            setInitialTimeStart();
        }
        else setTimeStartNull();
        if (tripForm.getTimeEnd()!=null) {
            timeEnd=tripForm.getTimeEnd();
            setInitialTimeEnd();
        }
        else setTimeEndNull();
        if (tripForm.getFrom()!=null) etextFrom.setText(tripForm.getFrom());
        else etextFrom.setText("");
        if (tripForm.getTo()!=null) etextTo.setText(tripForm.getTo());
        else etextTo.setText("");
        if (tripForm.getCost()!=null) etextCost.setText(tripForm.getCost()+"");
        else etextCost.setText("");
        buttonDateFrom.setOnClickListener((v)->{
            setDateFromNull();
        });
        buttonDateTo.setOnClickListener((v)->{
            setDateToNull();
        });
        buttonTimeStart.setOnClickListener((v)->{
            setTimeStartNull();
        });
        buttonTimeEnd.setOnClickListener((v)->{
            setTimeEndNull();
        });
        buttonAccept.setOnClickListener((v)->{
            String from=eTextFrom.getText().toString().trim();
            String to=eTextTo.getText().toString().trim();
            String cost=eTextCost.getText().toString().trim();
            if (from.equals("")) from=null;
            if (to.equals("")) to=null;
            if (cost.equals("")) cost=null;
            try {

                if (dateTo.compareTo(dateFrom)<=0&&!dateFromIsNull&&!dateToIsNull) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this,"Дата отправления не может быть дальше чем дата прибытия!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try {

                if (timeEnd.compareTo(timeStart)<=0&&!timeStartIsNull&&!timeEndIsNull) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this,"Начальное время не может быть больше чем конечное время!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try {
                if (Calendar.getInstance().compareTo(dateFrom)>0&&!dateFromIsNull) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Дата прибытия не может быть раньше текущей даты!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try {
                if (Calendar.getInstance().compareTo(dateTo)>0&&!dateToIsNull) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Дата отправления не может быть раньше текущей даты!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            try {
                if (cost!=null&&Double.parseDouble(cost)<0.0) throw new Exception();
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Стоимость должна быть числом не меньше 0!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

                /*Trip trip = new Trip(-1, dateTimeFrom, dateTimeTo, from, to, Integer.parseInt(count),
                        0, transport, Double.parseDouble(cost), login);*/
                if (dateFromIsNull) tripForm.setDateFrom(null);
                else tripForm.setDateFrom(dateFrom);
                if (dateToIsNull) tripForm.setDateTo(null);
                else tripForm.setDateTo(dateTo);
                if (timeStartIsNull) tripForm.setTimeStart(null);
                else tripForm.setTimeStart(timeStart);
                if (timeEndIsNull) tripForm.setTimeEnd(null);
                else tripForm.setTimeEnd(timeEnd);
                tripForm.setFrom(from);
                tripForm.setTo(to);
                if (cost!=null) tripForm.setCost(Double.parseDouble(cost));
                else tripForm.setCost(null);
                intent=new Intent(this, TripListActivity.class);
                intent.putExtra("login",login);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                /*try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                    databaseAdapter.test();
                }*/
                startActivity(intent);

        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void setInitialDateFrom() {

        tviewDateFromResult.setText(DateUtils.formatDateTime(this,
                dateFrom.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
        dateFromIsNull=false;
    }
    private void setInitialDateTo() {
        tviewDateToResult.setText(DateUtils.formatDateTime(this,
                dateTo.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
        dateToIsNull=false;
    }
    private void setInitialTimeStart() {
        tviewTimeStartResult.setText(DateUtils.formatDateTime(this,
                timeStart.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME
        ));
        timeStartIsNull=false;
    }
    private void setInitialTimeEnd() {
        tviewTimeEndResult.setText(DateUtils.formatDateTime(this,
                timeEnd.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME
        ));
        timeEndIsNull=false;
    }
    TimePickerDialog.OnTimeSetListener tf=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            timeStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeStart.set(Calendar.MINUTE, minute);
            setInitialTimeStart();
        }
    };
    TimePickerDialog.OnTimeSetListener tt=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeEnd.set(Calendar.MINUTE, minute);
            setInitialTimeEnd();
        }
    };


    DatePickerDialog.OnDateSetListener df=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateFrom.set(Calendar.YEAR, year);
            dateFrom.set(Calendar.MONTH, monthOfYear);
            dateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateFrom();
        }
    };
    DatePickerDialog.OnDateSetListener dt=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTo.set(Calendar.YEAR, year);
            dateTo.set(Calendar.MONTH, monthOfYear);
            dateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTo();
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

    public void setTimeStart(View v) {
        new TimePickerDialog(this, tf,
                timeStart.get(Calendar.HOUR_OF_DAY),
                timeStart.get(Calendar.MINUTE), true)
                .show();
    }
    public void setTimeEnd(View v) {
        new TimePickerDialog(this, tt,
                timeEnd.get(Calendar.HOUR_OF_DAY),
                timeEnd.get(Calendar.MINUTE), true)
                .show();
    }
    public void setDateFromNull() {
        tviewDateFromResult.setText("Не важно");
        dateFromIsNull=true;
    }
    public void setDateToNull() {
        tviewDateToResult.setText("Не важно");
        dateToIsNull=true;
    }
    public void setTimeStartNull() {
        tviewTimeStartResult.setText("Не важно");
        timeStartIsNull=true;
    }
    public void setTimeEndNull() {
        tviewTimeEndResult.setText("Не важно");
        timeEndIsNull=true;
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
        Intent intentList=new Intent(this,TripListActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentList.putExtra("login",login);
        Intent intentCreateTrip=new Intent(this,CreateTripActivity.class);
        intentCreateTrip.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentCreateTrip.putExtra("login",login);
        Intent intentEditUser=new Intent(this,EditUserActivity.class);
        intentEditUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentEditUser.putExtra("login",login);
        switch (id) {
            case R.id.tripList :
                startActivity(intentList);
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
}
