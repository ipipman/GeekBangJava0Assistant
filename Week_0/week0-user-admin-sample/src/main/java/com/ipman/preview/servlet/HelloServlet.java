package com.ipman.preview.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
 * @date 2021/2/25 11:53 上午
 */

// WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/")
// 一个Servlet总是继承自HttpServlet，然后覆写doGet()或doPost()方法
public class HelloServlet extends HttpServlet {

    // doGet()方法传入了HttpServletRequest和HttpServletResponse两个对象，分别代表HTTP请求和响应
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 设置响应类型:
        resp.setContentType("text/html");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        pw.write("<h1>Hello, world!</h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }

}
