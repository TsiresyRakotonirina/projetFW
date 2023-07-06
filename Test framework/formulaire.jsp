<%@page import="etu002015.model.Emp" %>
<%
    Emp e = (Emp) request.getAttribute("emp");
    out.println(e.getNom());
%>