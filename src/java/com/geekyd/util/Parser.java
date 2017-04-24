package com.geekyd.util;

import com.geekyd.entity.Data;
import com.geekyd.entity.HackerEarthJson;
import java.util.Calendar;
import java.util.Date;

public class Parser 
{
    Data d;
    Converter c;

    public Parser() 
    {
    c = new Converter();
    }
    public Data parseCC(String cell)
    {
        d= new Data();
        d.setCompany("Codechef");
        d.setImageIcon("codechef.png");
        d.setName(cell.split("&")[1]);
        d.setStart(c.getDateFullCC(cell.split("&")[2]));
        d.setEnd(c.getDateFullCC(cell.split("&")[3]));
        d.setUrl(cell.split("&")[4]);
        return d;
    }
    public Data parseCF(String cell)
    {
        d = new Data();
        d.setCompany("Codeforces");
        d.setImageIcon("codeforces.png");
        d.setName(cell.split("&")[0]);
        d.setStart(c.getDateFormat(cell.split("&")[2]));
        d.setEnd(c.addDate(d.getStart(), cell.split("&")[3]));        
        d.setUrl(cell.split("&")[6]);
        return d;
    }
    public Data parseHE(HackerEarthJson json)
    {
        d = new Data();
        d.setCompany("HackerEarth");
        d.setImageIcon("hackerearth.png");
        d.setName(json.getTitle().trim());
        d.setStart(c.getDateHackerEarth(json.getStart_tz()));
        d.setEnd(c.getDateHackerEarth(json.getEnd_tz()));
        d.setUrl(json.getUrl());
        //Contest or hackathon
       
        return d;
    }
    public Data parseSJ(String cell)
    {
        d = new Data();
        d.setName(cell.split("&")[0].split("(")[0]);
        d.setStart(c.getDate(cell.split("&")[1]));
        d.setEnd(c.getDate(cell.split("&")[2]));
        return d;
    }
    
}
