/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.GAANN;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author varut
 */
public class NetworkGA{

    /**
     * @return the numOfLayer
     */
    public int getNumOfLayer() {
        return numOfLayer;
    }

    /**
     * @param numOfLayer the numOfLayer to set
     */
    public void setNumOfLayer(int numOfLayer) {
        this.numOfLayer = numOfLayer;
    }

    /**
     * @return the networkErrorSquared
     */
    public double getNetworkErrorSquared() {
        return networkErrorSquared;
    }

    /**
     * @param networkErrorSquared the networkErrorSquared to set
     */
    public void setNetworkErrorSquared(double networkErrorSquared) {
        this.networkErrorSquared = networkErrorSquared;
    }

    /**
     * @return the prediction
     */
    public double[] getPrediction() {
        return prediction;
    }

    /**
     * @param prediction the prediction to set
     */
    public void setPrediction(double[] prediction) {
        this.prediction = prediction;
    }

    /**
     * @return the trainingError
     */
    public ArrayList<Double> getTrainingError() {
        return trainingError;
    }

    /**
     * @param trainingError the trainingError to set
     */
    public void setTrainingError(ArrayList<Double> trainingError) {
        this.trainingError = trainingError;
    }

    /**
     * @return the validationError
     */
    public ArrayList<Double> getValidationError() {
        return validationError;
    }

    /**
     * @param validationError the validationError to set
     */
    public void setValidationError(ArrayList<Double> validationError) {
        this.validationError = validationError;
    }

    /**
     * @return the testingError
     */
    public double getTestingError() {
        return testingError;
    }

    /**
     * @param testingError the testingError to set
     */
    public void setTestingError(double testingError) {
        this.testingError = testingError;
    }

    /**
     * @return the networkError
     */
    public double getNetworkError() {
        return networkError;
    }

    /**
     * @param networkError the networkError to set
     */
    public void setNetworkError(double networkError) {
        this.networkError = networkError;
    }

    
    int hiddenNode;
    int inputNode;
    int outNode;
    private int numOfLayer;
    double[][][] hWeights;
    double[][] oWeights;
    //testing error
    private double testingError;
    private ArrayList<Double> trainingError;
    private ArrayList<Double> validationError;
    //unsigned eror, store any for any 
    //given data without knowing if it is training or testing
    private double networkErrorSquared;
    private double networkError;
    private double[] prediction;
  public NetworkGA( int inputNode, int hiddenNode, int outNode, int numOfLayer) {          
            this.hiddenNode = hiddenNode;
            this.inputNode = inputNode;
            this.outNode = outNode;
            this.numOfLayer = numOfLayer;
            hWeights = new double[numOfLayer - 2][][];
            for (int i = 0; i < numOfLayer - 2; i++) {

                //add 1 for bias
                hWeights[i] = generateWeights(hiddenNode, inputNode + 1);
            }

            //add 1 for bias for each node
            oWeights  = generateWeights(outNode, hiddenNode + 1);
  
    }
    
    private double[] generateBias(int numOfBias)
    {
        Random rand = new Random();
        double min = -1;
        double max = 1;

        double[] bias = new double[numOfBias];
        for (int i = 0; i < numOfBias; i++) {
            
            bias[i] = min + (max - min) * rand.nextDouble();

        }
        return bias;
    }
    
    private double[][] generateWeights(int numOfNode1, int numOfNode2)
    {

        double[][] weights = new double[numOfNode1][numOfNode2];
        for (int i = 0; i < numOfNode1; i++) {

           weights[i] = generateBias(numOfNode2);
           
            
        }
        return weights;
    }
    
    
    public void setHiddenWeight(double[][][] weights, int layerNum)
    { 
       for (int i = 0; i < layerNum - 2; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                for (int k = 0; k < weights[0][0].length; k++) {
                    hWeights[i][j][k] = weights[i][j][k];
                }

            }
        }

    }
    public void setOutWeight(double[][] weights)
    {

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                oWeights[i][j] = weights[i][j];

            }
        }
    }
    
    public double[] [][] gethWeights()
    {
        return this.hWeights;
    }
    
    public double [][] getoWeights()
    {
        return oWeights;
    }
    
    public int getInputnode()
    {
        return this.inputNode;
    }
    
    public int getHiddenNode()
    {
        return this.hiddenNode;
    }
    
    public int getOuterNode()
    {
        return this.outNode;
    }
    
}
