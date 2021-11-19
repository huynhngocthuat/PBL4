package com.edu.bkdn.utils;

import com.edu.bkdn.models.BaseEntity;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelperUtil {
    private HelperUtil(){}

    public static Timestamp getCorrectTimestamp(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        // Substrac 7 hours
        cal.add(Calendar.HOUR, -7);
        return new Timestamp(cal.getTime().getTime());
    }

    public static <T extends BaseEntity> List<T> correctAllTimestamp(List<T> models){
        for(T model : models){
            model.setCreatedAt(getCorrectTimestamp(model.getCreatedAt()));
            model.setUpdatedAt(getCorrectTimestamp(model.getUpdatedAt()));
            if(model.getDeletedAt() != null){
                model.setDeletedAt(getCorrectTimestamp(model.getDeletedAt()));
            }
        }
        return models;
    }

    public static <T extends BaseEntity> Optional<T> correctSingleTimestamp(Optional<T> model){
        if(model.isPresent()){
            model.get().setCreatedAt(getCorrectTimestamp(model.get().getCreatedAt()));
            model.get().setUpdatedAt(getCorrectTimestamp(model.get().getUpdatedAt()));
            if(model.get().getDeletedAt() != null) {
                model.get().setDeletedAt(getCorrectTimestamp(model.get().getDeletedAt()));
            }
        }
        return model;
    }

    public static Timestamp toTimestamp(String time){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(time);
            return new Timestamp(parsedDate.getTime());
        }
        catch(ParseException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
