// GET /api/members - DB에서 전체 회원 목록을 JSON으로 반환 (관리자 전용, 세션 role 확인)
package com.odbo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/members")
public class MembersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");

        // 세션에서 role 확인 — 관리자(admin)만 접근 허용
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        if (!"admin".equals(role)) {
            res.setStatus(401);
            res.getWriter().write("{\"success\":false,\"message\":\"관리자 권한이 필요합니다.\"}");
            return;
        }

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
            res.getWriter().write("{\"success\":true,\"members\":" + sb + "}");
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("{\"success\":false,\"members\":[]}");
        }
    }
}
