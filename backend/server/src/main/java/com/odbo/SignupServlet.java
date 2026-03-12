// POST /api/signup - 새 회원을 DB에 등록 (중복 ID는 409, 비밀번호 형식 오류는 400 반환)
package com.odbo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.regex.Pattern;

@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {

    // 비밀번호 규칙: 8~16자, 영문·숫자·특수문자 각 1개 이상
    private static final Pattern PW_PATTERN = Pattern.compile(
        "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$"
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String userID = req.getParameter("userID");
        String userPW = req.getParameter("userPW");
        String userMAIL = req.getParameter("userMAIL");

        // 서버사이드 비밀번호 유효성 검사
        if (userPW == null || !PW_PATTERN.matcher(userPW).matches()) {
            res.setStatus(400);
            res.getWriter().write("{\"success\": false, \"message\": \"비밀번호는 8~16자이며 영문, 숫자, 특수문자를 각 1개 이상 포함해야 합니다.\"}");
            return;
        }

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
