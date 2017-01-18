package com.geekyd.scraper;

import com.geekyd.entity.Data;
import com.geekyd.entity.HackerEarthJson;
import com.geekyd.entity.HackerEarthResponse;
import com.geekyd.util.Parser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrape 
{
    public ArrayList<Data> data,fData,temp;
    Document doc;
    final String CC="https://www.codechef.com/contests",CF="http://codeforces.com/contests"
            ,SJ="http://www.spoj.com/contests",HE="https://www.hackerearth.com/chrome-extension/events/",
            HR="https://www.hackerrank.com/rest/contests/upcoming?offset=0&limit=10&contest_slug=active";
    String cell="";
    Parser p;
    
    public ArrayList<Data> crawl()
    {
        data = new ArrayList<Data>();
        fData = new ArrayList<Data>();
        p = new Parser();
        data.addAll(getCodechef("Present Contests","Future Contests"));
        data.addAll(getCodeforcesSpoj(CF));
        data.addAll(getHackerearth());
        
        return data;
    }
    public  ArrayList<Data> getCodechef(String type,String type1)
    {
    temp = new ArrayList<Data>();
    String text="";
    try {
            Response r = Jsoup.connect(CC).userAgent("Mozilla/5.0").execute();
            doc = r.parse();
            Elements links = doc.select("h3");
            //present 
            for(Element e : links)
            {
                if(e.text().equals(type))
                {
                    Elements inner = e.nextElementSibling().select("table").first().select("tbody").first().select("tr");
                    cell ="";
                    
                    for(Element in : inner)
                    {
                        Element linkUrl =in.select("a").first();
                        text = CC+linkUrl.attr("href");
                            Elements td = in.select("td");
                            for(Element t : td)
                            {
                                cell+=t.text()+"&";
                            }
                            cell+=text;
                            temp.add(p.parseCC(cell));
                            cell="";
                            text="";
                    }
                }
                else if(e.text().equals(type1))
                {
                    Elements inner = e.nextElementSibling().select("table").first().select("tbody").first().select("tr");
                    cell ="";
                    
                    for(Element in : inner)
                    {
                        Element linkUrl =in.select("a").first();
                        text = CC+linkUrl.attr("href");
                            Elements td = in.select("td");
                            for(Element t : td)
                            {
                                cell+=t.text()+"&";
                            }
                            cell+=text;
                            temp.add(p.parseCC(cell));
                            cell="";
                            text="";
                    }
                }
            }
            
        } catch (IOException ex) 
        {
            System.err.println(ex);
        }
    return temp;
    }
    public ArrayList<Data> getHackerearth()
    {
        temp = new ArrayList<Data>();
        String text,textJson="";
        try
        {
            System.out.println("Getting hacker Earth");
            URL url = new URL(HE);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((text=br.readLine())!=null)
            {
                textJson+=text;
            }
            textJson = textJson.substring(textJson.indexOf("["),textJson.lastIndexOf("]"));
            textJson = textJson+"]";
            Gson gson = new Gson();
            HackerEarthJson[] json =  gson.fromJson(textJson, HackerEarthJson[].class);
            //Conversion logic
            Data d = new Data();
            System.out.println("Length::"+json.length);
            for(int i=0;i<json.length;i++)
            {
                System.out.println("Parsing Index::"+json[i].getTitle());
                temp.add(p.parseHE(json[i]));
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception "+e);
        }
        return temp;
    }
   
    public ArrayList<Data> getCodeforcesSpoj(String url)
    {
        temp = new ArrayList<Data>();
        String text="";
    try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
            Element link = doc.select("table").first();
            //System.out.println(link);
            Elements ele = link.select("tr");
            for(Element e:ele)
            {
                text = CF;
                cell="";
                Elements td = e.select("td");
                for(Element t :td)
                    cell+=t.text()+"&";
               // System.out.println(cell);
                if(!cell.equals(""))
                     temp.add(p.parseCF(cell+text));// url plus the values of the rows
                cell="";text="";
            }
        } catch (IOException ex) {
            }
    
    return temp;
    }
    
}
