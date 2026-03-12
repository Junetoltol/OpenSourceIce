// POST /api/logout - 서버 세션을 무효화하여 로그아웃 처리
package com.odbo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write("{\"success\":true}");
    }
}
