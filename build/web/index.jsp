<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    ArrayList<String[]> userList = (ArrayList<String[]>) request.getAttribute("userList");
    String[] editUser = (String[]) request.getAttribute("editUser");
%>
<!DOCTYPE html>
<html>
<head>
    <title>CRUD Utilisateurs</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>CRUD Utilisateurs</h1>

    <div class="form-container">
        <form method="post" action="users">
            <input type="hidden" name="action" value="<%= (editUser != null) ? "update" : "add" %>">
            <% if (editUser != null) { %>
                <input type="hidden" name="id" value="<%= editUser[0] %>">
            <% } %>
            <input type="text" name="nom" placeholder="Nom" required value="<%= (editUser != null) ? editUser[1] : "" %>">
            <input type="text" name="prenom" placeholder="Prénom" required value="<%= (editUser != null) ? editUser[2] : "" %>">
            <input type="number" name="age" placeholder="Âge" required value="<%= (editUser != null) ? editUser[3] : "" %>">
            <button type="submit"><%= (editUser != null) ? "Modifier" : "Ajouter" %></button>
            <% if (editUser != null) { %>
                <a href="users" class="cancel">Annuler</a>
            <% } %>
        </form>
    </div>

    <table>
        <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Âge</th>
            <th>Actions</th>
        </tr>
        <% if (userList != null) {
            for (String[] u : userList) {
        %>
        <tr>
            <td><%= u[1] %></td>
            <td><%= u[2] %></td>
            <td><%= u[3] %></td>
            <td>
                <a href="users?edit=<%= u[0] %>">✏️</a>
                <form method="post" action="users" style="display:inline">
                    <input type="hidden" name="id" value="<%= u[0] %>">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" onclick="return confirm('Supprimer cet utilisateur ?')">❌</button>
                </form>
            </td>
        </tr>
        <% }} %>
    </table>
</body>
</html>
