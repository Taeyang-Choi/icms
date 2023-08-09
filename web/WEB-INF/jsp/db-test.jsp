<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr'>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DB Connection Test</title>
</head>
<body>
<%

Statement stmt;
Connection conn = null;


String DB_URL = "jdbc:mysql://localhost:3306/db_portal_2021";
String DB_USER = "root";
String DB_PASSWORD = "postmedia_0!";

try {
	Class.forName("com.mysql.jdbc.Driver");
	conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	stmt = conn.createStatement();
	conn.close();
	out.println("MySql jdbc test: connect ok!!");
} catch (Exception e) {
	out.println(e.getMessage());
}
%>


</div>
</body>
</html>
