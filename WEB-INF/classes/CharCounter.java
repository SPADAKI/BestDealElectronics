
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raymond
 */
@WebServlet("/CharCounter")
public class CharCounter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<!DOCTYPE html>\n"
                + "<!--\n"
                + "To change this license header, choose License Headers in Project Properties.\n"
                + "To change this template file, choose Tools | Templates\n"
                + "and open the template in the editor.\n"
                + "-->\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Char counter</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <form action=\"CharCounter\" method=\"post\">\n"
                + "            <textarea id=\"userInput\" name=\"userInput\"></textarea>\n"
                + "            <input type=\"submit\" value=\"submit\"/>\n"
                + "        </form>\n"
                + "    </body>\n"
                + "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String userInput = request.getParameter("userInput");

        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < userInput.length(); i++) {
            char c = userInput.charAt(i);
            if (c != ' ') {
                Integer val = map.get(c);
                if (val != null) {
                    map.put(c, val + 1);
                } else {
                    map.put(c, 1);
                }
            }
        }//end for

        out.print("<table>");
        for (Character key : map.keySet()) {
            
            out.print("<tr>\n"
                    + "    <td>" + key + "</td>\n"
                    + "    <td>" + map.get(key) + "</td> \n"
                    + "  </tr>");
        }
        out.print("</table>");

    }//doPost

}
