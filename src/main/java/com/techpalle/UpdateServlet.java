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

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public UpdateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        int id = 0, edition = 0, price = 0;
        String name = request.getParameter("bname");

        try {
            id = Integer.parseInt(request.getParameter("bid"));
            edition = Integer.parseInt(request.getParameter("bedition"));
            price = Integer.parseInt(request.getParameter("bprice"));
        } catch (NumberFormatException e) {
            out.println("<h3>Invalid input. Please enter valid numbers for ID, Edition, and Price.</h3>");
            return;
        }
        
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs=null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib", "root", "ramashi53");
            
            String sql = "UPDATE booklist1 SET bname = ?, bedition = ?, bprice = ? WHERE bid = ?";
            p = con.prepareStatement(sql);
            p.setString(1, name);
            p.setInt(2, edition);
            p.setInt(3, price);
            p.setInt(4, id);
            
            int rowsUpdated = p.executeUpdate();
            if (rowsUpdated > 0) {
                
                out.println("<html><head><title>Book Data</title><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body>");
                out.println("<h2>Book Details</h2>");
                out.println("<table border='1' cellpadding='5' cellspacing='0'>");
                out.println("<tr><th>Book ID</th><th>Book Name</th><th>Edition</th><th>Price</th></tr>");
                
                Statement stmt = con.createStatement();
                String sql1 = "SELECT * FROM booklist1";

               rs = stmt.executeQuery(sql1);
                
                while (rs.next()) {
                	int bookId = rs.getInt("bid");
                    String highlightClass = (bookId == id) ? "class='highlight-row'" : "";
                    
                    out.println("<tr " + highlightClass + ">");
                    out.println("<td>" + rs.getInt("bid") + "</td>");
//                    out.println("<td>" + rs.getString("bname") + "</td>");
                    // Append "Updated" text inside the Book Name column
                    if (bookId == id) {
                        out.println("<td><div class='name-container'><span class='book-name'>" + rs.getString("bname") + 
                                    "</span><span class='updated-sign'>Updated</span></div></td>");
                    } else {
                        out.println("<td><div class='name-container'><span class='book-name'>" + rs.getString("bname") + 
                                    "</span></div></td>");
                    }


                    out.println("<td>" + rs.getString("bedition") + "</td>");
                    out.println("<td>" + rs.getInt("bprice") + "</td>");
                    
                    out.println("</tr>");
                    
                    
                }
                
                out.println("</table>");
                out.println("<br><a href='index.html'>Go Back</a>");
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
