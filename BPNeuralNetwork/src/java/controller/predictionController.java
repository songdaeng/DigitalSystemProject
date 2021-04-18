/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.GAANN.NetworkGA;
import static model.GAANN.NeuralNetwork.predictGA;
import model.NetworkBP;
import static model.NeuralNetwork.createExistingNeuralNetwork;
import static model.TrainingData.getDataFromFile;
import static model.NeuralNetwork.predict;
import model.TrainingData;


/**
 *
 * @author varut
 */
public class predictionController extends HttpServlet {

    private void getPrediction(HttpServletRequest request, HttpServletResponse response)
    {
            String path = getServletContext().getRealPath("/WEB-INF/classes/model/dataset.txt");
            String BPWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/weights.txt");
            String GAWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/GAANN/weights.txt");
             TrainingData[] data =    getDataFromFile(path);
            NetworkBP networkBP = createExistingNeuralNetwork(path, BPWeightsPath);
            NetworkGA networkGA = model.GAANN.NeuralNetwork.createExistingNeuralNetwork(path, GAWeightsPath);
            double[] prediction = networkBP.getPrediction();
            double[] GAPrediction = networkGA.getPrediction();
            String path2 = getServletContext().getRealPath("/WEB-INF/classes/model/nextMonthData.txt");
             TrainingData[] data2 =  getDataFromFile(path2);
             String nextMonthValue = Double.toString(data2[0].getExpectValue());
             double[] features = data2[0].getData();
             //requiredd as data get modify for some reason
            double nextMonthPredictionGA = predictGA(features, data[0], GAWeightsPath);
            data2 =  getDataFromFile(path2);
             double[] features2 = data2[0].getData();
            double nextMonthPredictionBP = predict(features2, data[0], BPWeightsPath);

            String newText = "[";
            String[] date = new String[data.length];
            String[] expectedValue = new String[data.length];
            for(int i = 0; i < prediction.length; i++)
            {
                date[i] = data[i].getDate();
                expectedValue[i] = Double.toString(data[i].getExpectValue());
            }
            for(int i = 0; i < prediction.length; i++)
            {
                newText += "[ \' " + date[i] + "\' , " + prediction[i] + ", " + GAPrediction[i] + " , " + expectedValue[i]+ " ]";
                if(i != prediction.length -1)
                {
                    
                    newText += ", ";
                }
                else{
                    newText += ", ";
                    for(int J = 0; J < 50;J++)
                    {
                        newText += "[ \' " + data2[0].getDate() + "\' , " + nextMonthPredictionBP + ", " + nextMonthPredictionGA + " , " + nextMonthValue + " ],";
                    }
                    newText += "[ \' " + data2[0].getDate() + "\' , " + nextMonthPredictionBP + ", " + nextMonthPredictionGA + " , " + nextMonthValue + " ]";
                    newText += " ]";
                }
            }
            request.setAttribute("result", newText);
            request.setAttribute("MSEBP", networkBP.getNetworkErrorSquared());
            request.setAttribute("MSEGA", networkGA.getNetworkErrorSquared());
            request.setAttribute("MADBP", networkBP.getNetworkError());
            request.setAttribute("MADGA", networkGA.getNetworkError());
            
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
    
              getPrediction(request, response);
            //request.setAttribute("test", test);
                       
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/prediction.jsp");
            dispatcher.forward(request,response);
            
            

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
        String marketData = request.getParameter("marketData");
        if(marketData.isEmpty() || marketData == null)
        {       
            request.setAttribute("predictionError", "Input field is empty");
        }
        else{
            String path = "C:/Users/varut/Documents/NetBeansProjects/BPNeuralNetwork/src/java/model/dataset.txt";
            String BPWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/weights.txt");
            String GAWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/GAANN/weights.txt");
            TrainingData[] data =    getDataFromFile(path);
            int featureSet = data[0].getData().length;
            String[] newData = marketData.split(",");
            if(featureSet == newData.length)
            {
                double[] doubleValues = Arrays.stream(newData)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                double predictionBP = predict(doubleValues, data[0],BPWeightsPath );
                //data gets messed up by javaEE as it clone memory. 
                double[] newFeatureSet = Arrays.stream(newData)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                double predictionGA = predictGA(newFeatureSet,data[0], GAWeightsPath);
                request.setAttribute("BPPrediction", predictionBP);
                request.setAttribute("GAPrediction", predictionGA);

            }
            else{
                request.setAttribute("predictionError", "featureset Mismatch");
            }
            
            getPrediction(request, response);
            //request.setAttribute("test", test);r
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/prediction.jsp");
            dispatcher.forward(request,response);
        }
    }
    
    
}
