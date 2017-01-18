package com.geekyd.servlets;

import com.geekyd.db.Connector;
import com.geekyd.entity.Data;
import com.geekyd.scraper.Scrape;
import com.geekyd.util.Converter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlGeneratorServlet extends HttpServlet {
    
    StringBuilder now,future;
    Date date;
    Scrape sol;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        try
        {
            
            PrintWriter pr = response.getWriter();
            //init strings 
            Connector c = new Connector();
            Connection con = c.getConnection();
            now = new StringBuilder();
            future = new StringBuilder();
            now.append("<div class=\"listHeading\">Live Contests</div>");
            future.append("<div class=\"listHeading\">Future Contests</div>");
            //Scraper
            sol = new Scrape();
            ArrayList<Data> data = new ArrayList<Data>();
            data = sol.crawl();
            //data = sol.fData;
            Data d ;
            Iterator i = data.iterator();
            date = Calendar.getInstance().getTime();
            CallableStatement cs = con.prepareCall("{call insertContest(?,?,?,?,?,?)}");
            while(i.hasNext() && !i.equals(null))
            {
                d = (Data)i.next(); 
                cs.setString(1, d.getName());
                cs.setString(2, d.getCompany());
                cs.setString(3, d.getType());
                cs.setString(4, d.getUrl());
                cs.setTimestamp(5,new Converter().javaToSQL(d.getStart()));
                cs.setTimestamp(6,new Converter().javaToSQL(d.getEnd()));
                cs.execute();
                if(date.after(d.getStart()))
                    addToNow(d);
                else
                    addToFuture(d);
            } 
            data = sol.fData;
            i = data.iterator();
            while(i.hasNext() && !i.equals(null))
            {
                d = (Data)i.next();
                if(date.after(d.getStart()))
                    addToNow(d);
                else
                    addToFuture(d);
            }
            pr.println(now.toString()+future.toString());
        }
        catch(Exception e)
        {
            System.out.println("Exception:: "+e);
        }
    }
    public void addToFuture(Data d)
    {
        // add to the Future DIV
       String link =d.getUrl().equals(null)?"":d.getUrl();
       future.append("<a class=\""+d.getCompany().toLowerCase()+"\" href=\""+link+"\" target=\"_blank\" ><div class ='item '>");
                future.append("<div class='image'>");
                    future.append("<img class =\"icon\" src=\"res/"+d.getImageIcon()+"\"></div>");
                future.append("<div class=\"content\">");
                    future.append("<div class='name'>"+d.getName()+"</div>");
                    future.append("<div class='details'>"+"Start:<b>"+d.getStart()+"</b>   End:<b>"+d.getEnd()+"</b></div></div>");
                future.append("<div class=\"extras\">");
                    future.append("<div class=\"remind\">");
                    future.append("<img class=\"remindIcon\" src=\"res/remind.png\">");
                    future.append("</div></div> </div></a>");
    }
    public void addToNow(Data d)
    {
        // add to the Future DIV
        String link =d.getUrl().equals(null)?"":d.getUrl();
        System.out.println("Link for "+d.getName()+"\t");
        now.append("<a class=\""+d.getCompany().toLowerCase()+"\" href=\""+link+"\" target=\"_blank\" ><div class ='item "+d.getName().toLowerCase()+"'>");
                now.append("<div class='image'>");
                    now.append("<img class =\"icon\" src=\"res/"+d.getImageIcon()+"\"></div>");
                now.append("<div class=\"content\">");
                    now.append("<div class='name'>"+d.getName()+"</div>");
                    now.append("<div class='details'>"+"Start:<b>"+d.getStart()+"</b>  End:<b>"+d.getEnd()+"</b></div></div>");
                now.append("<div class=\"extras\">");
                    now.append("<div class=\"remind\">");
                    now.append("<img class=\"remindIcon\" src=\"res/remind.png\">");
                    now.append("</div></div> </div></a>");
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
