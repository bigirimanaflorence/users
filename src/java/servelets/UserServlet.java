package servelets;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class UserServlet extends HttpServlet {

    private final String url = "jdbc:mysql://localhost:3306/users_crud";
    private final String dbUser = "root";
    private final String password = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<String[]> users = new ArrayList<>();
        String editId = request.getParameter("edit");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, dbUser, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                String[] userRow = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    String.valueOf(rs.getInt("age"))
                };
                users.add(userRow);
            }

            // Si on modifie, récupérer l'utilisateur à éditer
            String[] userToEdit = null;
            if (editId != null) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id=?");
                ps.setInt(1, Integer.parseInt(editId));
                ResultSet editRs = ps.executeQuery();
                if (editRs.next()) {
                    userToEdit = new String[]{
                        editRs.getString("id"),
                        editRs.getString("nom"),
                        editRs.getString("prenom"),
                        String.valueOf(editRs.getInt("age"))
                    };
                }
                editRs.close();
                ps.close();
            }

            request.setAttribute("userList", users);
            request.setAttribute("editUser", userToEdit);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, dbUser, password);

            if ("add".equals(action)) {
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                int age = Integer.parseInt(request.getParameter("age"));
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (nom, prenom, age) VALUES (?, ?, ?)");
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setInt(3, age);
                ps.executeUpdate();
                ps.close();
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                int age = Integer.parseInt(request.getParameter("age"));
                PreparedStatement ps = con.prepareStatement("UPDATE users SET nom=?, prenom=?, age=? WHERE id=?");
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setInt(3, age);
                ps.setInt(4, id);
                ps.executeUpdate();
                ps.close();
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                ps.close();
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("users");
    }
}
