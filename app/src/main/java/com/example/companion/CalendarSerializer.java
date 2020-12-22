package com.example.companion;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Calendar;

public class CalendarSerializer extends StdSerializer<Calendar> {
    private static final long serialVersionUID = 1L;
    public CalendarSerializer(){
        super(Calendar.class);
    }
    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        gen.writeString(value.get(Calendar.YEAR)+"-"+value.get(Calendar.MONTH)+"-"+value.get(Calendar.DAY_OF_MONTH)+
                " "+value.get(Calendar.HOUR_OF_DAY)+":"+value.get(Calendar.MINUTE));
    }
}

