
package com.geekyd.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Converter 
{
    Date d;
    Calendar c;
    HashMap<String,Integer> hm;

    public Converter() 
    {
        hm = new HashMap<>();
        hm.put("Jan",0);hm.put("Feb",1);hm.put("Mar",2);hm.put("Apr",3);hm.put("May",4);hm.put("Jun",5);
        hm.put("Jul",6);hm.put("Aug",7);hm.put("Sep",8);hm.put("Oct",9);hm.put("Nov",10);hm.put("Dec",11);
    }
       
    // format yyyy-mm-dd hh:mm:ss
    public java.sql.Timestamp javaToSQL(Date da)
    {
        return new Timestamp(da.getTime());
    }
    public Date getDateFull(String s)
    {
        
        d = new Date();
        c =Calendar.getInstance();
            c.set(getInt(s.split(" ")[0].split("-")[0])
            , getInt(s.split(" ")[0].split("-")[1])-1
            , getInt(s.split(" ")[0].split("-")[2])
            , getInt(s.split(" ")[1].split(":")[0])
            , getInt(s.split(" ")[1].split(":")[1])
            , getInt(s.split(" ")[1].split(":")[2]));
    
    d = c.getTime();
    return d;
    }
    public Date getDateHackerEarth(String s)
    {
       
        d = new Date();
        c = Calendar.getInstance();
        c.set(getInt(s.split(" ")[0].split("-")[0])
            , getInt(s.split(" ")[0].split("-")[1])-1
            , getInt(s.split(" ")[0].split("-")[2])
            , getInt(s.split(" ")[1].split(":")[0])
            , getInt(s.split(" ")[1].split(":")[1])
            , getInt((s.split(" ")[1].split(":"))[2].substring(0,2))); // coz the last term second can have atmost 2 digits
        d = c.getTime();
        return d;
    }
    // format yyyy-mm-dd
    public Date getDate(String s)
    {
        d = new Date();
        c = Calendar.getInstance();
        c.set(getInt(s.split("-")[0]), getInt(s.split("-")[1])-1, getInt(s.split("-")[2]),00,00,00);
        d = c.getTime();
        return d;
    }
    // format mmm/dd/yyyy hh:mm
    public Date getDateFormat(String s)
    {
        d = new Date();
        c = Calendar.getInstance();
        c.set(getInt(s.split(" ")[0].split("/")[2]),getMonthFromString(s.split(" ")[0].split("/")[0]) ,getInt(s.split(" ")[0].split("/")[1])
                ,getInt(s.split(" ")[1].split(":")[0])+2,getInt(s.split(" ")[1].split(":")[1])+30,0);
        d = c.getTime();
        return d;
    }
    public Date addDate(Date date, String s)
    {
        c.setTime(date);
        d = new Date();
        d = c.getTime();
        if(s.split(":").length>2)
        {
            d.setDate(d.getDate()+getInt(s.split(":")[0]));
            d.setHours(d.getHours() + getInt(s.split(":")[1]));
            d.setMinutes(d.getMinutes() + getInt(s.split(":")[2]));
            }
        else
        {
            d.setHours(d.getHours() + getInt(s.split(":")[0]));
            d.setMinutes(d.getMinutes() + getInt(s.split(":")[1]));
        }  
        return d;
    }
    public int getInt(String st)
    {
        return Integer.parseInt(st);
    }
    public String getCode(String st)
    {
        return st.substring(0,st.length()-1);
    }
    public int getMonthFromString(String s)
    {
        return hm.get(s);
    }
}