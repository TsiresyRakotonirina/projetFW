<%@page import="etu002015.model.Emp" %>
<%
    <!-- mapseo anle argument fotsny -->
    Integer argument = new Integer(request.getParameter("arg"));
    out.println(argument);  
%>