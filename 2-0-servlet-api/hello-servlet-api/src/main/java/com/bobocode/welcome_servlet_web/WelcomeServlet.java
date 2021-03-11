package com.bobocode.welcome_servlet_web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * To create a servlet you have to extend this class from {@link HttpServlet}.
 * Also add annotation {@link WebServlet} with parameter "/hello-servlet" to map a path.
 */
@WebServlet("/hello-servlet") //todo: initialize the class as a servlet
public class WelcomeServlet extends HttpServlet {

    /**
     * This method is overridden from {@link HttpServlet} class.
     * It called by the server (container) to allow a servlet to handle a GET request.
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

        String parameter = request.getParameter("name");
        out.println("<html><body align=\"center\">");
        if (parameter == null) {
            out.println("<h1>" + "Good job! This page is a response of the servlet." + "</h1>");
            out.println("<h2>" + "You should add your name as a parameter in the URL." + "<br>");
            out.println("Just add <code><em>?name=YourName</em></code> to the end of the URL" + "</h2>");
        } else {
            out.println("<h1>" + "Awesome " + parameter + "! "
                    + "You've just passed your name to the servlet!" + "</h1>");
        }
        out.println("</body></html>");
    }
}