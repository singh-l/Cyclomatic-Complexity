<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test JSP</title>
</head>
<body>

  <%
  String promoId = (String)request.getParameter("ProcessId");
  
  String msg = (String)request.getParameter("Message");
  
  System.out.println("PromoId : "+promoId+"\nMessage : "+msg);
  %>
</body>
</html>