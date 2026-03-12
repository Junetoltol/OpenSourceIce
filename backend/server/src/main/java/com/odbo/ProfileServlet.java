// POST /api/profile - 로그인된 사용자의 이메일 또는 비밀번호 수정 (현재 비밀번호 본인 확인 필수)
package com.odbo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/profile")
public class ProfileServlet extends HttpServlet {

    // 비밀번호 규칙: 8~16자, 영문·숫자·특수문자 각 1개 이상 (SignupServlet과 동일)
    private static final java.util.regex.Pattern PW_PATTERN = java.util.regex.Pattern.compile(
        "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$"
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        // 세션에서 로그인된 사용자 ID 확인 (없으면 401)
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.setStatus(401);
            res.getWriter().write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return;
        }
        String userId = (String) session.getAttribute("userId");

        String currentPassword = req.getParameter("currentPassword");
        String newEmail = req.getParameter("newEmail");
        String newPassword = req.getParameter("newPassword");

        if (currentPassword == null || currentPassword.isEmpty()) {
            res.setStatus(400);
            res.getWriter().write("{\"success\": false, \"message\": \"현재 비밀번호를 입력해 주세요.\"}");
            return;
        }

        try (Connection conn = DBConn.getConnection()) {
            // 현재 비밀번호 본인 확인
            PreparedStatement check = conn.prepareStatement(
                "SELECT id FROM members WHERE id = ? AND passwd = ?"
            );
            check.setString(1, userId);
            check.setString(2, currentPassword);
            if (!check.executeQuery().next()) {
                res.setStatus(403);
                res.getWriter().write("{\"success\": false, \"message\": \"현재 비밀번호가 올바르지 않습니다.\"}");
                return;
            }

            // 변경할 항목이 없으면 오류
            if ((newEmail == null || newEmail.isEmpty()) && (newPassword == null || newPassword.isEmpty())) {
                res.setStatus(400);
                res.getWriter().write("{\"success\": false, \"message\": \"변경할 내용을 입력해 주세요.\"}");
                return;
            }

            // 새 비밀번호 유효성 검사
            if (newPassword != null && !newPassword.isEmpty() && !PW_PATTERN.matcher(newPassword).matches()) {
                res.setStatus(400);
                res.getWriter().write("{\"success\": false, \"message\": \"비밀번호는 8~16자이며 영문, 숫자, 특수문자를 각 1개 이상 포함해야 합니다.\"}");
                return;
            }

            // 이메일 또는 비밀번호 업데이트
            if (newEmail != null && !newEmail.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE members SET email = ?, passwd = ? WHERE id = ?"
                );
                ps.setString(1, newEmail);
                ps.setString(2, newPassword);
                ps.setString(3, userId);
                ps.executeUpdate();
            } else if (newEmail != null && !newEmail.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE members SET email = ? WHERE id = ?"
                );
                ps.setString(1, newEmail);
                ps.setString(2, userId);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE members SET passwd = ? WHERE id = ?"
                );
                ps.setString(1, newPassword);
                ps.setString(2, userId);
                ps.executeUpdate();
            }

            res.getWriter().write("{\"success\": true}");
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("{\"success\": false, \"message\": \"서버 오류\"}");
        }
    }
}
