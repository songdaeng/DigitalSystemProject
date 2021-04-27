/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author varut
 * Class NetworkBP
 * store all relevant neural network data for backpropagation  
 */
public class NetworkBP{

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

    /**
     * @return the trainingValidationError
     */
    public ArrayList<Double> getTrainingValidationError() {
        return trainingValidationError;
    }

    /**
     * @param trainingValidationError the trainingValidationError to set
     */
    public void setTrainingValidationError(ArrayList<Double> trainingValidationError) {
        this.trainingValidationError = trainingValidationError;
    }

    /**
     * @return the BPError
     */
    public ArrayList<Double> getBPError() {
        return BPError;
    }

    /**
     * @param BPError the BPError to set
     */
    public void setBPError(ArrayList<Double> BPError) {
        this.BPError = BPError;
    }

    /**
     * @return the trainingError
     */
    public double getTrainingError() {
        return trainingError;
    }

    /**
     * @param trainingError the trainingError to set
     */
    public void setTrainingError(double trainingError) {
        this.trainingError = trainingError;
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
     * @return the hiddenBias
     */
    public double[][] getHiddenBias() {
        return hiddenBias;
    }

    /**
     * @param hiddenBias the hiddenBias to set
     */
    public void setHiddenBias(double[][] hiddenBias) {
        this.hiddenBias = hiddenBias;
    }

    /**
     * @return the outerBias
     */
    public double[] getOuterBias() {
        return outerBias;
    }

    /**
     * @param outerBias the outerBias to set
     */
    public void setOuterBias(double[] outerBias) {
        this.outerBias = outerBias;
    }

    /**
     * @return the bias
     */
   
    /**
     * @return the oActivation
     */
    public double[] getoActivation() {
        return oActivation;
    }

    /**
     * @param oActivation the oActivation to set
     */
    public void setoActivation(double[] oActivation) {
        this.oActivation = oActivation;
    }

    /**
     * @return the hActivation
     */
    public double[][] gethActivation() {
        return hActivation;
    }

    /**
     * @param hActivation the hActivation to set
     */
    public void sethActivation(double[][] hActivation) {
        this.hActivation = hActivation;
    }
    private int hiddenNode;
    private int inputNode;
    private int outNode;
    private int numOfLayer;
    private double[][] hiddenBias;
    private double[] outerBias;
    double[][][] hWeights;
    double[][] oWeights;
    private double[] oActivation;
    private double[][] hActivation;
    //training error
    private double trainingError;
    //testing error
    private double testingError;
    private double[] prediction;
    private ArrayList<Double> BPError;
    private ArrayList<Double> trainingValidationError;
    //unsigned eror, store any for any 
    //given data without knowing if it is training or testing
    private double networkErrorSquared;
    private double networkError;
    public NetworkBP( int inputNode, int hiddenNode, int outNode, int numOfLayer) {          
            this.hiddenNode = hiddenNode;
            this.inputNode = inputNode;
            this.outNode = outNode;
            this.numOfLayer = numOfLayer;
            hWeights = new double[numOfLayer - 2][][];
            hiddenBias = new double[numOfLayer - 2][hiddenNode];
            for (int i = 0; i < numOfLayer - 2; i++) {
                hiddenBias[i] = generateBias(hiddenNode);
                hWeights[i] = generateWeights(hiddenNode, inputNode);
            }
            outerBias = generateBias(outNode);
            
            oWeights  = generateWeights(outNode, hiddenNode);
  
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
        //System.out.println(bias[0]);
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
       for (int i = 0; i < weights.length; i++) {
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
