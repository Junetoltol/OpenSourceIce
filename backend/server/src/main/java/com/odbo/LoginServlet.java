// POST /api/login - DB의 role 컬럼으로 관리자/사용자 구분, HttpSession에 userId·role 저장
package com.odbo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String uID = req.getParameter("uID");
        String uPW = req.getParameter("uPW");

        // DB에서 아이디·비밀번호·role·이메일·가입일 조회
        try (Connection conn = DBConn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, email, signupTime, role FROM members WHERE id = ? AND passwd = ?"
            );
            ps.setString(1, uID);
            ps.setString(2, uPW);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role       = rs.getString("role");
                String email      = rs.getString("email");
                String signupTime = rs.getString("signupTime");

                // HttpSession에 userId와 role 저장 (유효시간 300초)
                HttpSession session = req.getSession();
                session.setMaxInactiveInterval(300);
                session.setAttribute("userId", uID);
                session.setAttribute("role", role);

                if ("admin".equals(role)) {
                    res.getWriter().write("{\"success\": true, \"role\": \"admin\"}");
                } else {
                    res.getWriter().write(
                        "{\"success\": true, \"role\": \"user\","
                        + "\"id\": \"" + uID + "\","
                        + "\"email\": \"" + email + "\","
                        + "\"signupTime\": \"" + signupTime + "\"}"
                    );
                }
            } else {
                res.setStatus(401);
                res.getWriter().write("{\"success\": false, \"message\": \"아이디 또는 비밀번호가 틀렸습니다.\"}");
            }
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("{\"success\": false, \"message\": \"서버 오류\"}");
        }
    }
}
