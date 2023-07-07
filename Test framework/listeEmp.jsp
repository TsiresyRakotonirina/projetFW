<%@page contentType="text/html" pageEncoding = "UTF-8"%>
<% Object[] obj = (Object[])request.getAttribute("liste"); %>
<% out.println(obj); %>