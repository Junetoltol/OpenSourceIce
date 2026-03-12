package com.example;

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

        // 관리자 계정 확인 (space / 123456)
        if ("space".equals(uID) && "123456".equals(uPW)) {
            res.getWriter().write("{\"success\": true, \"role\": \"admin\"}");
            return;
        }

        // DB에서 일반 사용자 확인
        try (Connection conn = DBConn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM members WHERE id = ? AND passwd = ?"
            );
            ps.setString(1, uID);
            ps.setString(2, uPW);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                res.getWriter().write("{\"success\": true, \"role\": \"user\"}");
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
