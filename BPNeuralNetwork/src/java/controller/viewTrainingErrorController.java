/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.GAANN.NetworkGA;
import model.NetworkBP;
import static model.NeuralNetwork.getTrainingValidationError;

/**
 *
 * @author varut
 */
public class viewTrainingErrorController extends HttpServlet {


      private void getError(HttpServletRequest request, HttpServletResponse response)
    {
        try{
            String path = getServletContext().getRealPath("/WEB-INF/classes/model/dataset.txt");
            String BPWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/weights.txt");
            String GAWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/GAANN/weights.txt");
            NetworkBP networkBP = getTrainingValidationError(path, BPWeightsPath);
            NetworkGA networkGA = model.GAANN.NeuralNetwork.getTrainingValidationErrorGA(path, GAWeightsPath);
            
            
            ArrayList<Double> BPTrainingError = networkBP.getBPError();
            ArrayList<Double> BPValidationError = networkBP.getTrainingValidationError();
            ArrayList<Double> GATrainingError = networkGA.getTrainingError();
            ArrayList<Double> GAValidationError = networkGA.getValidationError();
            String BPText = "[";
            String GAText = "[";
            int lowestLength = BPTrainingError.size() < GATrainingError.size() ? BPTrainingError.size() : GATrainingError.size() ;
            //get first 5000 epochs or whichh everr is the lowest training error length
            int length = lowestLength < 50 ? lowestLength : 50;
            for(int i = 0; i < length; i++)
            {
                BPText += "[ " + ((i * 100) + 1)  + " , " + BPTrainingError.get(i) + ", " + BPValidationError.get(i) + " ]";
                GAText += "[ " + ((i * 100) + 1)  + " , " + GATrainingError.get(i) + ", " + GAValidationError.get(i) + " ]";
                if(i != (length - 1))
                {
                    BPText += ", ";
                    GAText += ", ";
                }
                else{
                    BPText += " ]";
                    GAText += " ]";
                }
            }
            double GAFinalTrainingError = GATrainingError.get(GATrainingError.size() - 1);
            double GAFinalValidationError = GAValidationError.get(GAValidationError.size() - 1);
            double BPFinalTrainingError =  BPTrainingError.get(BPTrainingError.size() - 1);
            double BPFinalValidationError = BPValidationError.get(BPValidationError.size() - 1);
            request.setAttribute("BPError", BPText);
            request.setAttribute("GAError", GAText);
            request.setAttribute("GAFinalTrainingError", GAFinalTrainingError);
            request.setAttribute("GAFinalValidationError", GAFinalValidationError);
            request.setAttribute("BPFinalTrainingError", BPFinalTrainingError);
            request.setAttribute("BPFinalValidationError", BPFinalValidationError);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }
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
        getError(request, response);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/viewTrainingError.jsp");
        dispatcher.forward(request,response);
    }


}
