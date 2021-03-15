package com.bobocode.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * To create a servlet you have to extend your class from {@link HttpServlet}.
 * Also add annotation {@link WebServlet} with parameter to map a path for URL.
 */
@WebServlet("/")
public class WelcomeServlet extends HttpServlet {

    /**
     * This method is overridden from {@link HttpServlet} class.
     * It called by the server (container) to allow servlets to handle GET requests.
     *
     * @param request an {@link HttpServletRequest} object that contains the request
     *                the client has made of the servlet.
     * @param response an {@link HttpServletResponse} object that contains the response
     *                 the servlet sends to the client.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html><body align=\"center\">");
            out.println("<img src=\"logo_white.svg\" alt=\"Bobocode\" width=\"500\">");
            out.println("<h1>" + "Good job! This page is a response of <code>WelcomeServlet</code> object." + "</h1>");
            out.println("<h2>" + "You should create your own class <code>DateServlet</code> which returns " +
                    "current date as a response on <code>/date</code> path.<br> Use <code>LocalDate.now()</code> " +
                    "to get current date." + "</h2>");
        out.println("</body></html>");
    }
}