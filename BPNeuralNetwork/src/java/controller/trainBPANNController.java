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
import model.NetworkBP;
import static model.NeuralNetwork.createExistingNeuralNetwork;
import static model.TrainingData.getDataFromFile;
import model.TrainingData;
import utils.ValidationUtils;

/**
 *
 * @author varut
 */
public class trainBPANNController extends HttpServlet {



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
        String url = request.getContextPath() + "/view/trainBP.jsp";
        HttpSession session = request.getSession();
        session.invalidate();
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
        //get paths
        //cleearing the current session
        HttpSession session = request.getSession();
        session.invalidate();
        ValidationUtils validation = new ValidationUtils();
        boolean emptyME = validation.emptyString("minError", request);
        boolean emptyML = validation.emptyString("learningRate", request);
        boolean emptyLayer =  validation.emptyString("layers", request);
        boolean emptyEWI = validation.emptyString("epochWithoutImprovement", request);
        if(emptyME && emptyML && emptyLayer && emptyEWI){  
            boolean validME = validation.isDoubleAndWithinRange("minError", 0.0001, 1.0, request);
            boolean validLR = validation.isDoubleAndWithinRange("learningRate",0.001, 1.0, request);
            boolean validLayer =  validation.isIntAndWithinRange("layers",3, 100, request);
            boolean validEWI = validation.isIntAndWithinRange("epochWithoutImprovement",1, 100000, request);
            if(validME && validLR && validLayer && validEWI){  
                String path = getServletContext().getRealPath("/WEB-INF/classes/model/dataset.txt");
                String BPWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/weights.txt");
                double minError = Double.parseDouble(request.getParameter("minError"));
                double learningRate = Double.parseDouble(request.getParameter("learningRate"));
                //pass in variable
                int numLayers = Integer.parseInt(request.getParameter("layers"));
                int epochWithoutImprovement = Integer.parseInt(request.getParameter("epochWithoutImprovement"));
                model.NeuralNetwork.createNewNeuralNetwork(path,BPWeightsPath, numLayers, learningRate, epochWithoutImprovement, minError);
                session = request.getSession(true);
                //redirect so the page will be loading while model is training
                session.setAttribute("success", "Training completed");
            }
        }
        String url = request.getContextPath() + "/view/trainBP.jsp";
        response.sendRedirect(url);
    }
    



}
