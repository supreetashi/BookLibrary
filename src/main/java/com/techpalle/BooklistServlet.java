package com.techpalle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BooklistServlet")
public class BooklistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BooklistServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<html><head><title>Enter Book ID</title><link rel='stylesheet' type='text/css' href='style.css'></head><body>");
        pw.println("<h2>Enter Book ID to Update</h2>");

        // Form to get book ID
        pw.println("<form method='get' action='BooklistServlet'>");
        pw.println("<label>Book ID:</label> <input type='number' name='id' required>");
        pw.println("<br><br><input type='submit' value='Fetch Data'>");
        pw.println("</form>");

        // Get book ID parameter
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            pw.println("<h3>Please enter a book ID.</h3>");
            pw.println("</body></html>");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            pw.println("<h3>Invalid book ID. Please enter a valid number.</h3>");
            pw.println("</body></html>");
            return;
        }

        Connection con = null;
        PreparedStatement p = null;
        ResultSet r = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib", "root", "ramashi53");

            String query = "SELECT * FROM booklist1 WHERE bid = ?";
            p = con.prepareStatement(query);
            p.setInt(1, id);
            r = p.executeQuery();

            if (r.next()) {
                String name = r.getString("bname");
                int edition = r.getInt("bedition");
                int price = r.getInt("bprice");

                pw.println("<h2>Update Book Details</h2>");
                pw.println("<form action='UpdateServlet' method='post'>");
                pw.println("<input type='hidden' name='bid' value='" + id + "'>");
                pw.println("<label>Book Name:</label> <input type='text' name='bname' value='" + name + "' required><br><br>");
                pw.println("<label>Book Edition:</label> <input type='number' name='bedition' value='" + edition + "' required><br><br>");
                pw.println("<label>Book Price:</label> <input type='number' name='bprice' value='" + price + "' required><br><br>");
                pw.println("<input type='submit' value='Update Book'>");
                pw.println("</form>");

                // Delete book form
                pw.println("<form action='DeleteServlet' method='post'>");
                pw.println("<input type='hidden' name='bid' value='" + id + "'>");
                pw.println("<input type='submit' value='Delete Book'>");
                pw.println("</form>");

            } else {
                pw.println("<h3>No book found with the given ID.</h3>");
            }
        } catch (ClassNotFoundException e) {
            pw.println("<h3>Database driver not found.</h3>");
            e.printStackTrace();
        } catch (SQLException e) {
            pw.println("<h3>Database error occurred.</h3>");
            e.printStackTrace();
        } finally {
            try {
                if (r != null) r.close();
                if (p != null) p.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        pw.println("<br><a href='index.html'>Go Back</a>");
        pw.println("</body></html>");
    }
}
