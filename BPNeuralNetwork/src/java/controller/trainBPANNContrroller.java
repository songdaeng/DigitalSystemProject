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
import model.NetworkBP;
import static model.NeuralNetwork.createExistingNeuralNetwork;
import static model.TrainingData.getDataFromFile;
import model.TrainingData;

/**
 *
 * @author varut
 */
public class trainBPANNContrroller extends HttpServlet {



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
            String path = "C:/Users/varut/Documents/NetBeansProjects/BPNeuralNetwork/src/java/model/dataset.txt";
            TrainingData[] data =    getDataFromFile(path);
            double minError = Double.parseDouble(request.getParameter("minError"));
            double learningRate = Double.parseDouble(request.getParameter("learningRate"));
            int numLayers = Integer.parseInt(request.getParameter("layers"));
            int epochWithoutImprovement = Integer.parseInt(request.getParameter("epochWithoutImprovement"));
            model.NeuralNetwork.createNewNeuralNetwork(path,numLayers, learningRate, epochWithoutImprovement, minError);
            request.setAttribute("Success", "training completed");
            String url = request.getContextPath() + "/view/trainBP.jsp";
            
            response.sendRedirect(url);
    }



}
