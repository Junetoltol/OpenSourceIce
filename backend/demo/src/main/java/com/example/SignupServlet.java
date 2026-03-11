package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String userID = req.getParameter("userID");
        String userPW = req.getParameter("userPW");
        String userMAIL = req.getParameter("userMAIL");

        try (Connection conn = DBConn.getConnection()) {
            // 중복 ID 확인
            PreparedStatement check = conn.prepareStatement(
                "SELECT id FROM members WHERE id = ?"
            );
            check.setString(1, userID);
            if (check.executeQuery().next()) {
                res.setStatus(409);
                res.getWriter().write("{\"success\": false, \"message\": \"이미 사용 중인 아이디입니다.\"}");
                return;
            }

            // 회원 등록
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO members (id, passwd, email) VALUES (?, ?, ?)"
            );
            ps.setString(1, userID);
            ps.setString(2, userPW);
            ps.setString(3, userMAIL);
            ps.executeUpdate();

            res.getWriter().write("{\"success\": true}");
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("{\"success\": false, \"message\": \"서버 오류\"}");
        }
    }
}
