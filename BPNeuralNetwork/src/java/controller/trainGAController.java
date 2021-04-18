/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static model.TrainingData.getDataFromFile;
import model.TrainingData;

/**
 *
 * @author varut
 */
public class trainGAController extends HttpServlet {



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getContextPath() + "/view/trainGA.jsp";
        HttpSession session = request.getSession();
        session.setAttribute("success", "");
        response.sendRedirect(url);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            String path = getServletContext().getRealPath("/WEB-INF/classes/model/dataset.txt");
            String GAWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/GAANN/weights.txt");
            TrainingData[] data = getDataFromFile(path);
            double minError = Double.parseDouble(request.getParameter("minError"));
            double mutationRate = Double.parseDouble(request.getParameter("mutationRate"));
            double crossoverRate = Double.parseDouble(request.getParameter("crossoverRate"));
            int numLayers = Integer.parseInt(request.getParameter("layers"));
            int epochWithoutImprovement = Integer.parseInt(request.getParameter("epochWithoutImprovement"));
            
            model.GAANN.NeuralNetwork.createNetwork(path,GAWeightsPath,  minError, numLayers, mutationRate, crossoverRate, epochWithoutImprovement);
            HttpSession session = request.getSession();
            session.setAttribute("success", "Training completed");
            String url = request.getContextPath() + "/view/trainGA.jsp";
            response.sendRedirect(url);
    }


}
