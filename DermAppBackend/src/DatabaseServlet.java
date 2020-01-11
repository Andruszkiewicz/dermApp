import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*; 
import org.json.JSONObject;

@WebServlet("/DatabaseServlet")
public class DatabaseServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
 
    public DatabaseServlet() {
        super();
 
    }
    Connection connection = sqliteConnection.dbConnector();
    Statement statement = null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("application/json");
    	response.setCharacterEncoding("utf-8");
    	// Get the printwriter object from response to write the required json object to the output stream      
    	PrintWriter out = response.getWriter();
    	ResultSet result=null;
    	
    	try {
			statement = connection.createStatement();
			String sql = ("SELECT * FROM dermData limit 100");
			result =statement.executeQuery(sql);
			System.out.println(result);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	JSONArray jsonArray = new JSONArray();
		try {
			while(result.next()) {
				JSONObject jsonObj = new JSONObject();
				try {
			        jsonObj.put("NAME", result.getString("NAME")); 
			        jsonObj.put("SURNAME", result.getString("SURNAME"));
			        jsonObj.put("IMG", result.getString("IMG"));
			        jsonObj.put("DATE", result.getString("DATE"));
			        jsonObj.put("TIME", result.getString("TIME"));
			        jsonObj.put("CITY", result.getString("CITY"));
			        jsonObj.put("COUNTRY", result.getString("COUNTRY"));
			        jsonObj.put("COMMENT", result.getString("COMMENT"));
			        
			        jsonArray.put(jsonObj);
			    } catch (JSONException e) {
			        e.printStackTrace();
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	out.print(jsonArray);
    	out.flush();
 
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println(request.getParameter("NAME"));
    	try {
    		statement = connection.createStatement();
    		String sql = String.format("INSERT INTO dermData (NAME, SURNAME, IMG, DATE, TIME, CITY, COUNTRY, COMMENT) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", request.getParameter("NAME"),request.getParameter("SURNAME"),request.getParameter("IMG"),request.getParameter("DATE"),request.getParameter("TIME"),request.getParameter("CITY"),request.getParameter("COUNTRY"),request.getParameter("COMMENT"));
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	PrintWriter out = response.getWriter();
    	
    	out.print("{\"post\": \"function\"}");
    	out.flush();
    }
}