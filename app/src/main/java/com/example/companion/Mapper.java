package com.example.companion;

import java.util.Calendar;

public class Mapper {
    public static String convertCalendarToString(Calendar calendar) {
        String year=calendar.get(Calendar.YEAR)<1000?"000"+calendar.get(Calendar.YEAR):calendar.get(Calendar.YEAR)+"";
        String day=calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
        String month=calendar.get(Calendar.MONTH)+1<10?"0"+(calendar.get(Calendar.MONTH)+1):calendar.get(Calendar.MONTH)+1+"";
        String hour=calendar.get(Calendar.HOUR_OF_DAY)<10?"0"+calendar.get(Calendar.HOUR_OF_DAY):""+calendar.get(Calendar.HOUR_OF_DAY);
        String minute=calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):""+calendar.get(Calendar.MINUTE);
        return year+"-"+month+"-"+day+" "+hour+":"+minute;
    }
    public static Calendar convertStringToCalendar(String string) {
        String[] elements=string.split(" |:|-");
        for (int i=0;i<elements.length;i++){
            if (elements[i].charAt(0)=='0') elements[i]=elements[i].substring(1);
        }
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(elements[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(elements[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(elements[2]));
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(elements[3]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(elements[4]));
        return calendar;
    }
}
