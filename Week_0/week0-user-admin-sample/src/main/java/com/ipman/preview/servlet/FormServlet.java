package com.ipman.preview.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ipipman on 2021/2/25.
 *
 * @version V1.0
 * @Package com.ipman.preview.servlet
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/2/25 5:56 下午
 */
public class FormServlet extends HttpServlet {

    private String encoding = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应内容类型
        req.setCharacterEncoding(encoding);
        PrintWriter out = resp.getWriter();
        String title = "Testing";
        String docType =
                "<!doctype html>\n";
        out.println(docType +
                "<html>\n" +
                "<head>\n" +
                "<title>" + title + "</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                "</head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>User1</b>:"
                + req.getParameter("first_name") + "\n" +
                "  <li><b>User2</b>:"
                + req.getParameter("last_name") + "\n" +
                "</ul>\n" +
                "</body></html>");
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");
    }
}
