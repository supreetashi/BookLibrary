package com.techpalle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String name= request.getParameter("tbname");
		String edition= request.getParameter("tbedition");
		String price= request.getParameter("tbprice");
		System.out.println("name :"+ name);
		System.out.println("edition :"+ edition);
		System.out.println("price :"+ price);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/booklib","root","ramashi53");

			String query="insert into booklist1(bname,bedition,bprice) values(?,?,?)";
			PreparedStatement s=con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			s.setString(1,name);
			s.setString(2,edition);
			s.setString(3,price);
			
			 int affectedRows = s.executeUpdate();

	            int id = 0;
	            if (affectedRows > 0) {
	                ResultSet rs = s.getGeneratedKeys();
	                if (rs.next()) {
	                    id = rs.getInt(1); 
	                }
	                rs.close();
	            }
			s.close();
			con.close();
			System.out.println("Inserted successfully. Generated ID: " + id);
		    System.out.println("Forwarding to Booklist.jsp with attributes...");
			
		request.setAttribute("bid", id);
			 request.setAttribute("tbname", name);
	            request.setAttribute("tbedition", edition);
	            request.setAttribute("tbprice", price);
	            
	            RequestDispatcher dispatcher = request.getRequestDispatcher("Booklist.jsp");
	            dispatcher.forward(request, response);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error in RegisterServlet: " + e.getMessage());
		}
	}

}
