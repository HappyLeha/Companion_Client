package com.example.companion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDeserializer extends StdDeserializer<Calendar> {
    private static final long serialVersionUID = 1L;
    protected CalendarDeserializer() {
        super(Calendar.class);
    }
    @Override
    public Calendar deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String string=jp.readValueAs(String.class);
        String[] elements=string.split(" |:|-");
        return new GregorianCalendar(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),
                Integer.parseInt(elements[3]),Integer.parseInt(elements[4]));
    }
}
