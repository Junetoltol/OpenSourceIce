package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/members")
public class MembersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");

        try (Connection conn = DBConn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, email, signupTime FROM members ORDER BY signupTime DESC"
            );
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) sb.append(",");
                sb.append("{")
                  .append("\"id\":\"").append(rs.getString("id")).append("\",")
                  .append("\"email\":\"").append(rs.getString("email")).append("\",")
                  .append("\"signupTime\":\"").append(rs.getString("signupTime")).append("\"")
                  .append("}");
                first = false;
            }
            sb.append("]");
            res.getWriter().write(sb.toString());
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("[]");
        }
    }
}
