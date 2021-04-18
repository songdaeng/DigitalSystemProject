/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.GAANN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static model.CalculationLibrary.absoluteError;
import static model.CalculationLibrary.sigmoid;
import static model.CalculationLibrary.squaredError;
import static model.GAANN.geneticAL.GeneticAI;
import model.TrainingData;
import static model.TrainingData.denormaliseData;
import static model.TrainingData.getDataFromFile;
import static model.TrainingData.normaliseData;
import static model.TrainingData.normalisedAllData;


/**
 *
 * @author varut
 */   

public class NeuralNetwork {

    
     /**
     * Get data from file 
     * in: filepath out: dataset
     * @return 
     */
 
    
    //global variable
    static int inputNode = 8;
    static int hiddenNode = 6;
    static int outNode = 1;
    static int LAYERNUM = 5;

    /**
     * Fitness function, use set error as the fitness of rach population
     */
    public static double fitnessFunction(Individual solution, TrainingData[] data)
    {
        //creating network
        NetworkGA network = new NetworkGA(inputNode, hiddenNode, outNode, LAYERNUM);
        double[] weighting = solution.getGene();
        //covert the genes to usable weighting for the NN
        genesToWeights(network, weighting);
        //return the total error of the network 
        return calculatSumErrorSquared(data, network);
   
    }
    
    /**
     * Genes to weights split the weights into each layer of the network
     */
    public static void genesToWeights(NetworkGA network, double[] weighting)
    {
        
        double[][][] hidWeight = new double[LAYERNUM - 2][hiddenNode][inputNode + 1];
        double[][] outWeight = new double[outNode][hiddenNode + 1];
        
        //getting all the connection going to hidden node + 1 include biased
        int counter = 0;
        for (int i = 0; i < LAYERNUM - 2; i++) {
            
        
            for(int j = 0; j < hiddenNode; j++)
            {

                for (int k = 0; k < inputNode + 1; k++) {
                   hidWeight[i][j][k] = weighting[counter];
                   counter++;
                }
            }
            //getting all weighted connections from hidden to outer node

        }            
        for(int j = 0; j < outNode; j++)
        {
            for (int k = 0; k < hiddenNode + 1; k++) {
                outWeight[j][k] = weighting[counter];
                counter++;
            }
        }
        //set up thee network
         network.setHiddenWeight(hidWeight, LAYERNUM);
         network.setOutWeight(outWeight);     
    }
    
    /**
     * Create network 
     * Take a dataset and train the network
     * return graph of the performance
     */

    public static void createNetwork(String dataPath,String weightsPath, double minError, int numOfLayer, double mutationRate
        , double crossoverRate, int epochWithoutImprovement) {
        
        TrainingData[] data =    getDataFromFile(dataPath);
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();

        //set nodes length
        inputNode = data[0].getData().length;
        hiddenNode = inputNode;
        outNode = 1;
        LAYERNUM = numOfLayer;
        //how much data is use for training
        double trainingRatio = 0.7;
        normalisedAllData(data);
        NetworkGA network = new NetworkGA(inputNode, hiddenNode, outNode, LAYERNUM);
        //convert number og nodes into gene's length
        //all inputs * hidden + hiddenbias                                                                      //outlayer
        int geneNum = (hiddenNode * (hiddenNode + 1) *(LAYERNUM - 2)) + (outNode * (hiddenNode + 1));
                //splitting the dataset to train on
                
             //splitting the dataset to train on
        int validationLength = (int) ((data.length * trainingRatio) * 0.1);
        int trainingLength = (int) ((data.length * trainingRatio) - validationLength);
        int testingLength = data.length - (trainingLength + validationLength);

        TrainingData[] trainingData = new TrainingData[trainingLength]; ;
        TrainingData[] validationData = new TrainingData[validationLength]; 
        TrainingData[] testingData = new TrainingData[testingLength];;
        dataSplitting(data, trainingData, testingData, validationData, trainingRatio);
        //training
        Individual bestIndividual = GeneticAI(geneNum, trainingData, validationData, minError, network, mutationRate, crossoverRate, epochWithoutImprovement);        
        
        double[] weighting = bestIndividual.getGene(); 

        
        genesToWeights(network, weighting);  
        writeWeightsToFile(network, weightsPath);
        
    }
    
    public static void writeWeightsToFile(NetworkGA network, String path)
    {   
        String weights = "";
        double[][][] hWeights = network.gethWeights();
        double[][] oWeights = network.getoWeights();
        ArrayList<Double> trainingError = network.getTrainingError();
        ArrayList<Double> validationError = network.getValidationError();
        weights += "layers\n";
        weights += network.getNumOfLayer() + "\n";
        weights += "Hidden Weights\n";
        for (int i = 0; i < hWeights.length; i++) {
            for (int j = 0; j < hWeights[0].length; j++) {
                for (int k = 0; k < hWeights[0][0].length; k++) {
                    weights += hWeights[i][j][k] + " ";
                }
                weights += ",";
            }
            weights += ";";
        }
        
        weights += "\nOut Weights\n";
        for (int i = 0; i < oWeights.length; i++) {
            for (int j = 0; j < oWeights[0].length; j++) {
                weights += oWeights[i][j] + " ";
            }
            weights += "\n";
        }
        weights += "Training Error\n";
         for (Double trainingError1 : trainingError) {
             weights += trainingError1 + " ";
         }
         
        weights += "\nValidation Error\n";
         for (Double validationError1 : validationError) {
             weights += validationError1 + " ";
         }
        try {
          //create file, if not already exist
          File newFile = new File(path);
          newFile.createNewFile(); 

          FileWriter myWriter = new FileWriter(path);
          myWriter.write(weights);
          myWriter.close();
          System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
          System.out.println("An error occurred.");
        }
    }
    
    /**
     * calculate hidden layer
     * calculate the output of each hidden node
     * return the output of hidden layer
     */
    
    public static double[][] calculateHiddenLayer(TrainingData data, NetworkGA network)
    {
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[][][] hiddenWeights = network.gethWeights();
        double[] inputs = data.getData();
        //forward feed and sigmoid transfer
        for (int i = 0; i < LAYERNUM - 2; i++) {
            for (int j = 0; j < hiddenNode; j++) {
                for (int k = 0; k < inputNode; k++) {
                    if(k == 0){
                        hNodeOut[i][j] += hiddenWeights[i][j][k];//biased
                    }
                    else{
                        if(i == 0 )
                        {

                         hNodeOut[i][j] += hiddenWeights[i][j][k] * inputs[k];
                        }
                        else{
                            hNodeOut[i][j] += hiddenWeights[i][j][k] * hNodeOut[i - 1][j];
                        }
                    }
                }


                hNodeOut[i][j] = sigmoid(hNodeOut[i][j]);

            }
        }
        return hNodeOut;
    }
    
    
    
    /**
     * calculateoutlayer
     * calculate the output of each out node
     */
    public static double[] calculateOutLayer( NetworkGA network, double [] hNodeOut)
    {
        double[] iNodeOut = new double[outNode]; 
        double[][] outputWeights = network.getoWeights();
        //get the outnode's output with sigmoid transsfer at the end
          for (int i = 0; i < outNode; i++) {
                iNodeOut[i] = 0;
                for (int j = 0;  j < hiddenNode; j++) {
                    if(j == 0)
                    {
                        iNodeOut[i] += outputWeights[i][j]; //biased'
                    }
                    else{
                        iNodeOut[i] += outputWeights[i][j] * hNodeOut[j];
                    }
                }

               iNodeOut[i] = sigmoid(iNodeOut[i]);
                
            }
        
         return iNodeOut;
    }
    
    /**
     * CalcuateSumErrorSquared
     * calculate the total error of the whole network
     */
    public static double calculatSumErrorSquared(TrainingData[] data, NetworkGA network) {
        
        double error = 0;
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[] iNodeOut = new double[outNode]; 
        //calculating the total error for all dataset 
        for (int t = 0; t < data.length; t++) {
            hNodeOut = calculateHiddenLayer(data[t], network);

            iNodeOut = calculateOutLayer(network,  hNodeOut[hNodeOut.length - 1]);
            //System.out.println(iNodeOut[0] + " " + data[t].expectValue);
            error += squaredError(iNodeOut[0], data[t].getExpectValue());
        
      
        } 

        //return the sum tot the fitness function
        return error;
    }
    public static NetworkGA createExistingNeuralNetwork(String dataPath, String weightsPath) {

        TrainingData[] data =  getDataFromFile(dataPath);
        //set nodes length
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();
        double predictionMax = max[max.length - 1];
        inputNode = data[0].getData().length;
        hiddenNode = inputNode;
        LAYERNUM = 4;
        outNode = 1;
        //how much data is use for training
        normalisedAllData(data);

        NetworkGA network = getWeightsFromFile(weightsPath);

        double[] dataPrediction = forward(data, network);
 
        for (int i = 0; i < dataPrediction.length; i++) {         
            dataPrediction[i] = denormaliseData(dataPrediction[i] ,min[min.length-1], max[max.length - 1]) ;
        }
        network.setNetworkError((network.getNetworkError() / data.length )* predictionMax);
        network.setNetworkErrorSquared((network.getNetworkErrorSquared() / data.length) * predictionMax);
        network.setPrediction(dataPrediction);
        return network;
    }
    
    public static NetworkGA createExistingNeuralNetwork(TrainingData[] data, String weightsPath) {

 
        //set nodes length
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();
        double predictionMax = max[max.length - 1];
        inputNode = data[0].getData().length;
        hiddenNode = inputNode;
        LAYERNUM = 4;
        outNode = 1;
        normalisedAllData(data);
        NetworkGA network = getWeightsFromFile(weightsPath);

        double[] dataPrediction = forward(data, network);
 
        for (int i = 0; i < dataPrediction.length; i++) {         
            dataPrediction[i] = denormaliseData(dataPrediction[i] ,min[min.length-1], max[max.length - 1]) ;
        }
        network.setNetworkError((network.getNetworkError() / data.length )* predictionMax);
        network.setNetworkErrorSquared((network.getNetworkErrorSquared() / data.length) * predictionMax);
        network.setPrediction(dataPrediction);
        return network;
    }
    
    public static NetworkGA getTrainingValidationErrorGA(String dataPath, String weightsPath) {

        TrainingData[] data =  getDataFromFile(dataPath);
        //set nodes length

        inputNode = data[0].getData().length;
        hiddenNode = inputNode;
        LAYERNUM = 4;
        outNode = 1;
        NetworkGA network = getWeightsFromFile(weightsPath);
        return network;
    }
    

    public static NetworkGA getWeightsFromFile(String path)
    {
        NetworkGA network = null;
        double[][][] hiddenWeights = null;
        double[][] outWeights = new double[outNode][hiddenNode + 1];
        ArrayList<Double> trainingError = new ArrayList<>();
        ArrayList<Double> validationError = new ArrayList<>();
        try  {  
            //setting up the file reader
            File file=new File(path);    
            FileReader fr=new FileReader(file);   
            BufferedReader br=new BufferedReader(fr);  
            String line = "";
            String content = "";
            int counter = 0;
            while((line=br.readLine())!=null)  
            {  
                if(Character.isLetter(line.charAt(0)))
                {
                    content = line;
                    counter = 0;
                }
                else if(content.matches("layers"))
                {
                    int layer = Integer.parseInt(line);
                    network = new NetworkGA(inputNode, hiddenNode, outNode, layer);
                    hiddenWeights = new double[LAYERNUM - 2][inputNode][hiddenNode + 1];
                }
                else if(content.matches("Hidden Weights"))
                {
                    String[] layer = line.split(";");
                    for (int i = 0; i < layer.length; i++) {
                        String[] nodes = layer[i].split(",");
                        for (int j = 0; j < nodes.length; j++) {
                            String[] weights = nodes[j].split("\\s+");
                            for (int k = 0; k < weights.length; k++) {                           
                                hiddenWeights[i][j][k] = Double.parseDouble(weights[k]);
                            }
                        }
                    }
                }
                else if(content.matches("Out Weights"))
                {
                    String[] temp = line.split("\\s+");
                    for (int i = 0; i < temp.length; i++) {
                        outWeights[counter][i] = Double.parseDouble(temp[i]);
                    }
                    counter++;
                }
                else if(content.matches("Training Error"))
                {
                    String[] temp = line.split("\\s+");
                    for (int i = 0; i < temp.length; i++) {
                        trainingError.add(Double.parseDouble(temp[i]));
                    }
                }
                else if(content.matches("Validation Error"))
                {
                    String[] temp = line.split("\\s+");
                    for (int i = 0; i < temp.length; i++) {
                        validationError.add(Double.parseDouble(temp[i]));
                    }
                }
 
            }
            network.setHiddenWeight(hiddenWeights, LAYERNUM);
            network.setOutWeight(outWeights);
            network.setTrainingError(trainingError);
            network.setValidationError(validationError);
            fr.close();    

        } catch (IOException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        //catching exception

        //reconstructing the data so it does not expty array

        return network;
    } 
      
     /**
     *
     * @param data
     * @param trainingData
     * @param testingData
     * @param validationData
     * @param trainingRatio
     */
    public static void dataSplitting(TrainingData[] data, TrainingData[] trainingData, 
            TrainingData[] testingData, TrainingData[] validationData, double trainingRatio)
    {
        int validationLength = validationData.length;
        int trainingLength = trainingData.length;
        int testingLength = testingData.length;
        //splitting the training data
                //splitting the training data
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int splitRatio = (int) (trainingRatio * 10) -1;
        for (int i = 0; i < data.length; i++) {
            //if i is odd add to training
            if(i % 10 == 0 && counter2 < validationLength)
            {
                validationData[counter2] = 
                    new TrainingData(data[i].getData(), data[i].getExpectValue()); 
                counter2++;
            }
            //when testing is full the data goes to training 
           
            else if(i % 10 > splitRatio && counter3 < testingLength){
                testingData[counter3] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue());
                counter3++;
            }
            else if(counter1 < trainingLength){
                trainingData[counter1] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue()); 
                    counter1++;
            }
            else{
                testingData[counter3] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue());
                counter3++;
            }
            
        }
    }  
    
    
     public static double predictGA(double[] features, TrainingData data, String weightsPath) {
        

        double[] min = data.getMin();
        double[] max = data.getMax();
        inputNode = data.getData().length;
        hiddenNode = inputNode + 1;
        LAYERNUM = 4;
        outNode = 1;
        double data1[] = features;


        for (int j = 0; j < data1.length; j++) {
            data1[j] = normaliseData(data1[j],  min[j],max[j]);

        }
        TrainingData newData = new TrainingData(data1, 0);

        inputNode = data.getData().length;
        hiddenNode = inputNode;
        LAYERNUM = 4;
        outNode = 1;

        NetworkGA network = getWeightsFromFile(weightsPath);
        double dataPrediction = forward(newData, network);
 
        System.out.println(dataPrediction);
        dataPrediction = denormaliseData(dataPrediction ,min[min.length-1], max[max.length - 1]) ;
         System.out.println(dataPrediction);

        return dataPrediction;
    }
    /**
     * forward
     * Calculate how many datapoint can the network successfully classify
     */
   public static double[] forward( TrainingData [] data, NetworkGA network)
    {
        double[] prediction = new double[data.length];
        double squaredError = 0.0;
        double error = 0.0;
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[] iNodeOut = new double[outNode]; 
        for (int t = 0; t < data.length; t++) {
            hNodeOut = calculateHiddenLayer(data[t], network);
            
            iNodeOut = calculateOutLayer(network,  hNodeOut[hNodeOut.length - 1]);
            //classification 
            squaredError += squaredError(iNodeOut[0] ,data[t].getExpectValue());
            error += absoluteError(iNodeOut[0] ,data[t].getExpectValue());
            prediction[t] = iNodeOut[0];
        }// to stop an error

        network.setNetworkError(error);
        network.setNetworkErrorSquared(squaredError);
        return prediction;
    }
   
       /**
     * forward
     * Calculate how many datapoint can the network successfully classify
     * overload for individual datapoint
     */
   public static double forward( TrainingData data, NetworkGA network)
    {
        double prediction = 0.0;
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[] iNodeOut = new double[outNode]; 
        hNodeOut = calculateHiddenLayer(data, network);
            
        iNodeOut = calculateOutLayer(network,  hNodeOut[hNodeOut.length - 1]);
            //classification 

        prediction = iNodeOut[0];
  
        return prediction;
    }

}
