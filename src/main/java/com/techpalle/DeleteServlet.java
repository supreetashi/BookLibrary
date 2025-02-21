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

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  // Redirect GET request to POST handler
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("bid"));
        } catch (NumberFormatException e) {
            out.println("<h3>Invalid book ID. Please enter a valid number.</h3>");
            return;
        }

        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib", "root", "ramashi53");

            String sql = "DELETE FROM booklist1 WHERE bid = ?";
            p = con.prepareStatement(sql);
            p.setInt(1, id);

            int rowsDeleted = p.executeUpdate();
            if (rowsDeleted > 0) {
                out.println("<h3>Record deleted successfully.</h3>");
                Statement stmt = con.createStatement();
                String sql1 = "SELECT * FROM booklist1";

                 rs = stmt.executeQuery(sql1);
                 
                 out.println("<html><head><title>Book Data</title><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body>");
                 out.println("<h2>Book Details</h2>");
                 out.println("<table border='1' cellpadding='5' cellspacing='0'>");
                 out.println("<tr><th>Book ID</th><th>Book Name</th><th>Edition</th><th>Price</th></tr>");
                
                while (rs.next()) {
                    
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("bid") + "</td>");
                    out.println("<td>" + rs.getString("bname") + "</td>");
                    out.println("<td>" + rs.getString("bedition") + "</td>");
                    out.println("<td>" + rs.getInt("bprice") + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
                out.println("<a href='index.html'>Back</a>");
                out.println("</body></html>");
            } else {
                out.println("<h3>No record found with the given ID.</h3>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Database driver not found.</h3>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h3>Database error occurred.</h3>");
            e.printStackTrace();
        } finally {
            try {
            	rs.close();
                if (p != null) p.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

