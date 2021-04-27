/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author varut
 */
public class ValidationUtils {
    public boolean emptyString(String name, HttpServletRequest request)
    {
        String str = request.getParameter(name);
        HttpSession session = request.getSession();
        if(str.isEmpty())
        {
            session.setAttribute(name + "Error", "This field cannot be empty");
            return false;
        }else{
            session.setAttribute(name, str);
            return true;
        }
    }
    
    public boolean isDoubleAndWithinRange(String name, double min, double max, HttpServletRequest request)
        {
            String str = request.getParameter(name);
            HttpSession session = request.getSession();
            try {
            double number = Double.parseDouble(str);
            if(number > max || number < min)
            {
                session.setAttribute(name + "Error", "Out of Range - min: " + min + " max: " +max );
            }else{
                return true;
            }
        } catch (NumberFormatException e) {
            session.setAttribute(name + "Error", "This is vaild number");
           
        } 
        return false;
    }
    
    public boolean isIntAndWithinRange(String name, int min, int max, HttpServletRequest request)
        {
            String str = request.getParameter(name);
            HttpSession session = request.getSession();
            try {
            int number = Integer.parseInt(str);
            if(number > max || number < min)
            {
                session.setAttribute(name + "Error", "Out of Range - min: " + min + " max: " +max );
            }else{
                return true;
            }
        } catch (NumberFormatException e) {
            session.setAttribute(name + "Error", "This is vaild number");
           
        } 
        return false;
    }
}
