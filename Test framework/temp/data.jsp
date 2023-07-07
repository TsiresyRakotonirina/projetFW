<%@page import="etu002015.model.Emp" %>
<%
    Integer argument = new Integer(request.getParameter("arg"));
    out.println(argument);
%>