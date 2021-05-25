/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.GAANN.NetworkGA;
import model.NetworkBP;
import static model.NeuralNetwork.createExistingNeuralNetwork;
import static model.NeuralNetwork.dataSplitting;
import model.TrainingData;
import static model.TrainingData.getDataFromFile;
import static model.TrainingData.normalisedAllData;

/**
 *
 * @author varut
 */
public class viewTrainingGraph extends HttpServlet {

     private void getPrediction(HttpServletRequest request, HttpServletResponse response,
             TrainingData[] data, String graphName, String MSEBP, String MSEGA, String MADBP, String MADGA)
    {
        try{
            String BPWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/weights.txt");
            String GAWeightsPath = getServletContext().getRealPath("/WEB-INF/classes/model/GAANN/weights.txt");
            ArrayList<String> date = new ArrayList<>();
            String[] expectedValue = new String[data.length];
            int length = data[0].getData().length;
            String[][] savedData = new String[data.length][length];
            for(int i = 0; i < data.length; i++)
            {
                date.add( data[i].getDate());
                expectedValue[i] = Double.toString(data[i].getExpectValue());
                double[] featureset = data[i].getData();
                for(int j = 0; j < length; j++)
                {
                  savedData[i][j] = Double.toString(featureset[j]);
                }
            }
            
            NetworkBP networkBP = createExistingNeuralNetwork(data, BPWeightsPath);
            dataReconstruction(data, expectedValue, savedData);
            NetworkGA networkGA = model.GAANN.NeuralNetwork.createExistingNeuralNetwork(data, GAWeightsPath);

            
            double[] prediction = networkBP.getPrediction();
            double[] GAPrediction = networkGA.getPrediction();
            String newText = "[";
            //constrcuting data for teh graph
            for(int i = 0; i < prediction.length; i++)
            {
                newText += "[ \' " + date.get(i) + "\' , " + prediction[i] + ", " + GAPrediction[i] + " , " + expectedValue[i] + " ]";
                if(i != prediction.length -1)
                {
                    newText += ", ";
                }
                else{
                    newText += " ]";
                }
            }
            //set attribute to be send to jsp
            request.setAttribute(graphName, newText);
            request.setAttribute(MSEBP, networkBP.getNetworkErrorSquared());
            request.setAttribute(MSEGA, networkGA.getNetworkErrorSquared());
            request.setAttribute(MADBP, networkBP.getNetworkError());
            request.setAttribute(MADGA, networkGA.getNetworkError());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }
     //transfer back the data saved due to system modify double and class variable
     private void dataReconstruction(TrainingData[] data, String[] expectedValue, String[][] featureSet)
     {
           int length = data[0].getData().length;
            for(int i = 0; i < data.length; i++)
            {
                data[i].setExpectValue(Double.parseDouble(expectedValue[i]));
                double[] features = new double[length];
                for(int j = 0; j < length; j++)
                {
                   features[j] = Double.parseDouble(featureSet[i][j]);
                }
                data[i].setData(features);
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
        try{
            double trainingRatio = 0.7;
            String path = getServletContext().getRealPath("/WEB-INF/classes/model/dataset.txt");
            TrainingData[] data =    getDataFromFile(path);
            double[] min = data[0].getMin();
            double[] max = data[0].getMax();
            int validationLength = (int) ((data.length * trainingRatio) * 0.1);
            int trainingLength = (int) ((data.length * trainingRatio) - validationLength);
            int testingLength = data.length - (trainingLength + validationLength);

            TrainingData[] trainingData = new TrainingData[trainingLength];
            TrainingData[] validationData = new TrainingData[validationLength]; 
            TrainingData[] testingData = new TrainingData[testingLength];


            dataSplitting(data, trainingData, testingData, validationData, trainingRatio);

            trainingData[0].setMax(max);
            trainingData[0].setMin(min);
            testingData[0].setMax(max);
            testingData[0].setMin(min);
            validationData[0].setMax(max);
            validationData[0].setMin(min);
            getPrediction( request,  response, trainingData, "trainingGraph", "trainingMSEBP", "trainingMSEGA", "trainingMADBP", "trainingMADGA");
            getPrediction( request,  response, validationData, "validationGraph", "validationMSEBP", "validationMSEGA", "validationMADBP", "validationMADGA");
            getPrediction( request,  response, testingData, "testingGraph", "testingMSEBP", "testingMSEGA", "testingMADBP", "testingMADGA");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/viewTrainingGraph.jsp");
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
        
    }

}
