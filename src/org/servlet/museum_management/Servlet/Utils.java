package org.servlet.museum_management.Servlet;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static String getAction(HttpServletRequest request) {
//        String action = request.getServletPath();
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        } else {
            action = action.replaceAll("/$", "");
        }
        return action;
    }

}
