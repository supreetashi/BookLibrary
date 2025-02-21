package com.techpalle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RetrieveServlet")
public class RetrieveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RetrieveServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Book Data</title><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body>");
        out.println("<h2>Book Details</h2>");
        out.println("<table border='1' cellpadding='5' cellspacing='0'>");
        out.println("<tr><th>Book ID</th><th>Book Name</th><th>Edition</th><th>Price</th></tr>");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib", "root", "ramashi53");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM booklist1";

            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                
                
                out.println("<tr>");
                out.println("<td>" + rs.getInt("bid") + "</td>");
                out.println("<td>" + rs.getString("bname") + "</td>");
                out.println("<td>" + rs.getString("bedition") + "</td>");
                out.println("<td>" + rs.getInt("bprice") + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("<br><a href='index.html'>Go Back</a>");
            out.println("</body></html>");
            
            rs.close();
            stmt.close();
            con.close();
            
        } catch (ClassNotFoundException e) {
            out.println("<p style='color:red;'>Database Driver not found!</p>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
