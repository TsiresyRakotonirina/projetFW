<%@page import="etu002015.model.Emp" %>
<%
    Emp e = new Emp();
    out.print(e.getNom());
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Formulaire</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        
        <div>Formulaire</div>
        <p>
            <form action="formulaire" method="post">
                <input type="text" name="nom">
                <input type="submit" value="envoyer">
            </form>
        </p>
        <p>
            <form action="fileUpload" enctype="multipart/form-data" method="post">
                <input type="file" name="file">
                <input type="submit" value="envoyer">
            </form>
        </p>
    </body>
</html>