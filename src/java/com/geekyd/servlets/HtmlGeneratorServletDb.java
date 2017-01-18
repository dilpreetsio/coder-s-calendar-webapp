
package com.geekyd.servlets;

import com.geekyd.db.Connector;
import com.geekyd.entity.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HtmlGeneratorServletDb extends HttpServlet {

    StringBuilder now,future;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            //init strings 
            PrintWriter pr = new PrintWriter(response.getWriter());
            now = new StringBuilder();
            future = new StringBuilder();
            now.append("<div class=\"listHeading\">Live Contests</div>");
            future.append("<div class=\"listHeading\">Future Contests</div>");
            Connector c = new Connector();
            Connection con = c.getConnection();
            ResultSet rs = con.prepareCall("{call getAllContest()}").executeQuery();
            Data d;
            Date date = Calendar.getInstance().getTime();
            while(rs.next())
            {
                d = new Data();
                d.setCompany(rs.getString("company"));
                d.setName(rs.getString("name"));
                d.setUrl(rs.getString("url"));
                d.setStart(rs.getTimestamp("start"));
                d.setEnd(rs.getTimestamp("end"));
                d.setImageIcon(rs.getString("company").toLowerCase()+".png");
                if(date.after(d.getStart()))
                    addToNow(d);
                else
                    addToFuture(d);
            }
            pr.println(now.toString()+""+future.toString());
        }
        catch(Exception e)
        {
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
