<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Book List</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<%
    Integer bid = (Integer) request.getAttribute("bid");
    String name = (String) request.getAttribute("tbname");
    String edition = (String) request.getAttribute("tbedition");
    String price = (String) request.getAttribute("tbprice");

   /* out.println("<p>bid: " + bid + "</p>");
    out.println("<p>Name: " + name + "</p>");
    out.println("<p>Edition: " + edition + "</p>");
    out.println("<p>Price: " + price + "</p>"); */

    if (bid == null || name == null || edition == null || price == null) {
        out.println("<h3>Error: Some attributes are missing!</h3>");
    }
%> 

<table border="1" width="80%">
    <tr>
        <th>ID</th>
        <th>Book Name</th>
        <th>Edition</th>
        <th>Price</th>
    </tr>

    <%
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib", "root", "ramashi53");
            
            String query = "SELECT * FROM booklist1";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("bid");
                String nam = rs.getString("bname");
                int bed = rs.getInt("bedition");
                int pri = rs.getInt("bprice");
    %>
                <tr>
                    <td><%= id %></td>
                    <td><%= nam %></td>
                    <td><%= bed %></td>
                    <td><%= pri %></td>
                </tr>
    <%
            }
        } catch (Exception e) {
            out.println("<h3>Error retrieving book list.</h3>");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    %>
</table>
<a href='index.html'>Go Back</a>

</body>
</html>
