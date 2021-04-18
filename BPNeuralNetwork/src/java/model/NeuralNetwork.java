/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static model.CalculationLibrary.absoluteError;
import static model.CalculationLibrary.sigmoid;
import static model.CalculationLibrary.squaredError;
import static model.TrainingData.denormaliseData;
import static model.TrainingData.getDataFromFile;
import static model.TrainingData.normaliseData;
import static model.TrainingData.normalisedAllData;

/**
 *
 * @author varut
 */    

public class NeuralNetwork {

   
     
    public static NetworkBP getWeightsFromFile(String path)
    {
        NetworkBP network = null;
        //for scalbility it can take any amount of hidden layer, for future
        double[][] hiddenBias = null;
        double[] outBias = new double[outNode];
        double[][][] hiddenWeights = null;
        double[][] outWeights = new double[outNode][hiddenNode];
        ArrayList<Double> BPError = new ArrayList<>();
        ArrayList<Double> vError = new ArrayList<>();
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
                    network = new NetworkBP(inputNode, hiddenNode, outNode, layer);
                    hiddenWeights = new double[LAYERNUM - 2][hiddenNode][inputNode];
                    hiddenBias =  new double[LAYERNUM - 2][hiddenNode]; 
                }
                else if(content.matches("Hidden Bias"))
                {
                    
                    String[] layer = line.split(";");
                    
                    for (int i = 0; i < layer.length; i++) {
                        
                        String[] nodes = layer[i].split("\\s+");
                        int length = nodes.length;
                        for (int j = 0; j < length ; j++) {
                            hiddenBias[i][j] = Double.parseDouble(nodes[j]);
                        }
                    }
                }
                else if(content.matches("Outer Bias"))
                {
                    String[] temp = line.split("\\s+");
                    for (int i = 0; i < temp.length; i++) {
                        outBias[i] = Double.parseDouble(temp[i]);
                    }
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
                        BPError.add(Double.parseDouble(temp[i]));
                    }
                }
            else if(content.matches("Validation Error"))
                {
                    String[] temp = line.split("\\s+");
                    for (int i = 0; i < temp.length; i++) {
                        vError.add(Double.parseDouble(temp[i]));
                    }
                }
 
            }
            network.setHiddenBias(hiddenBias);
            network.setOuterBias(outBias);
            network.setHiddenWeight(hiddenWeights, LAYERNUM);
            network.setOutWeight(outWeights);
            network.setBPError(BPError);
            network.setTrainingValidationError(vError);
            fr.close();    

        }
        //catching exception
        catch(IOException e)  
        {  
            System.out.println("");
        }  
        //reconstructing the data so it does not expty array

        return network;
    }
    
    //global variable
    static int inputNode = 5;
    static int hiddenNode = 6;
    static int outNode = 1;
    static int LAYERNUM = 5;



  
    
    /**
     * calculate hidden layer
     * calculate the output of each hidden node
     * return the output of hidden layer
     */
    
    public static double[][] calculateHiddenLayer(TrainingData data, NetworkBP network)
    {
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[][][] hiddenWeights = network.gethWeights();
        double[] inputs = data.getData();
        double[][] bias = network.getHiddenBias();
        //forward feed and sigmoid transfer
        for (int i = 0; i < LAYERNUM - 2; i++) {
           
            for (int j = 0; j < hiddenNode; j++) {
                hNodeOut[i][j] += bias[i][j];//biased
                for (int k = 0; k < inputNode; k++) {
                       if(i == 0 )
                       {
                       
                        hNodeOut[i][j] += hiddenWeights[i][j][k] * inputs[k];
                       }
                       else{
                           hNodeOut[i][j] += hiddenWeights[i][j][k] * hNodeOut[i - 1][j];
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
    public static double[] calculateOutLayer( NetworkBP network, double [] hNodeOut)
    {
        double[] iNodeOut = new double[outNode]; 
        double[][] outputWeights = network.getoWeights();
        double[] bias = network.getOuterBias();

        //get the outnode's output with sigmoid transsfer at the end
          for (int i = 0; i < outNode; i++) {
                iNodeOut[i] = 0;
                for (int j = 0;  j < hiddenNode; j++) {
                    if(j == 0)
                    {
                        iNodeOut[i] += bias[i]; //biased'
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
     * 
     * Train using backpropagation until the desired error is reached or 
     *  100000 epochs without improvement 
     */
    static double maxEpochWithoutImprovement = 100;
    static double minDesiredError = 0.001;
    public static void train( TrainingData [] data, TrainingData [] validation,  NetworkBP network)
    {
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        NetworkBP bestWeights = null;
        double LowestError = 100000;
        ArrayList<Double> trainingError = new ArrayList<>();
        ArrayList<Double> validationError = new ArrayList<>();
        int epochWithoutImprovement = 0;
        int epoch = 0 ;
        double error = 0.0;
        double[] iNodeOut = new double[outNode]; 
       while(LowestError > minDesiredError){
            error = 0.0;
     
            for (int t = 0; t < data.length; t++) {
                hNodeOut = calculateHiddenLayer(data[t], network);
                network.sethActivation(hNodeOut);
                iNodeOut = calculateOutLayer(network,  hNodeOut[hNodeOut.length - 1]);
                network.setoActivation(iNodeOut);
                //total error
                error += squaredError(iNodeOut[0] ,data[t].getExpectValue());
               
                backPropagate(data[t], network);
                

            }// to stop an error
            
            if(epochWithoutImprovement > maxEpochWithoutImprovement )
            {
                LowestError = 0; 
            }
            if(error < LowestError)
            {
                LowestError = error;
                bestWeights = network;
                epochWithoutImprovement= 0;
      
            }
            if(epoch > 101 || epoch == 0)
            {
                //adding training curve
                forward(validation, network);
                double vError = network.getNetworkErrorSquared();
                validationError.add(vError);
                trainingError.add(error);   
                //breakout if validation more error than traininng butt not first epoch
                if(vError > error && epoch != 0){
                    LowestError = 0;
                }
                epoch = 1;
                System.out.println(error);
            }
            epoch++;
            epochWithoutImprovement++;
            
        }
        trainingError.add(error);
        double vError = network.getNetworkErrorSquared();
        validationError.add(vError);
        network = bestWeights;
        network.setTrainingValidationError(validationError);
        network.setBPError(trainingError);
    }
    public static double[] forward( TrainingData [] data, NetworkBP network)
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
    
    public static double forward( TrainingData data, NetworkBP network)
    {
        double prediction = 0;
        double[][] hNodeOut = new double[LAYERNUM - 2][hiddenNode];
        double[] iNodeOut = new double[outNode]; 
        hNodeOut = calculateHiddenLayer(data, network);

        iNodeOut = calculateOutLayer(network,  hNodeOut[hNodeOut.length - 1]);
        //classification 

        prediction = iNodeOut[0];
        return prediction;
    }
    private static double sigmoidDerivative(double val)
    {
        return (val * (1.0 - val));
    }
        // Unit errors.
    

    public static double LEARNRATE = 0.5;
    private static void backPropagate(TrainingData data, NetworkBP network)
    {
        double outNodeError[] = new double[outNode];
        double[] predictionDiff = new double[outNode];
        double[] outActivation = network.getoActivation();
      
        double derivativeOut = 0.0;
        double[][] hiddenBias = network.getHiddenBias();
        double[] outerBias = network.getOuterBias();
        
        // Calculate the output layer error (step 3 for output cell).s
        for(int j = 0; j < outNode; j++)
        {
            predictionDiff[j] = (outActivation[j] - data.getExpectValue());
            
            derivativeOut = predictionDiff[j] * sigmoidDerivative( outActivation[j]);
            outNodeError[j] = LEARNRATE * derivativeOut;
            //System.out.println("out activate " + ( predictionDiff[j] * sigmoidDerivative( outActivation[j])));
        }
        
        double[][] hActivation = network.gethActivation();
        int hiddenLayerNum = hActivation.length;
        // Calculate the hidden layer error (step 3 for hidden cell).
        double[][] outWeights = network.getoWeights();
        for(int out = 0; out < outNode; out++)
        {
            
            for(int hid = 0; hid < hiddenNode; hid++)
            {
                //System.out.println("before:" + outWeights[out][hid]);
                outWeights[out][hid] -= outNodeError[out] * hActivation[0][hid];
                //System.out.println("after:" + outWeights[out][hid]);
            }
            //setting new outweights
            
        }
        network.setOutWeight(outWeights);
        double[][][] hWeights = network.gethWeights();
       
        double[] inputs = data.getData();
        // Update the weights for the output layer (step 4).
        for (int layer = LAYERNUM - 3; layer > -1; layer--) {
            
            for(int hid = 0; hid < hiddenNode; hid++)
            {
                for(int input = 0; input < inputNode; input++)
                {

                    //updating the weights adding current weights to prediction different
                    //* input value coming throught the node * learning rate and SD
                    //System.out.println(hWeights[hid][input]);
                    if(layer == 0){
                        hWeights[layer][hid][input] -= LEARNRATE * inputs[input] * derivativeOut *  sigmoidDerivative(hActivation[layer][hid]);
                    }
                    else{
                        hWeights[layer][hid][input] -= LEARNRATE * hActivation[layer - 1][hid] * derivativeOut *  sigmoidDerivative(hActivation[layer][hid]);
                    }
                } // hid
            }
        } // out
            network.setHiddenWeight(hWeights, LAYERNUM);
         //updating bias
        for (int i = 0; i < hiddenBias.length; i++) {
             for (int j = 0; j < hiddenBias[0].length; j++) {
                 hiddenBias[i][j] -= LEARNRATE * derivativeOut;
             }
            
        }
        for (int i = 0; i < outerBias.length; i++) {
                 outerBias[i] -= LEARNRATE * derivativeOut;
        }
        network.setHiddenBias(hiddenBias);
        network.setOuterBias(outerBias);
 
    }
    public static void createNewNeuralNetwork(String dataPath, String weightsPath,  int layer, double LearningRate, int epochwithoutImprovement, double minError) {
        
        //java clone the memory as cahnges to children changes the parent object
        //that why need to get file at model level
        TrainingData[] data = 
                 getDataFromFile(dataPath);
        //set nodes length
        inputNode = data[0].getData().length;
        hiddenNode = inputNode + 1;
        LAYERNUM = layer;
        outNode = 1;
        LEARNRATE = LearningRate;
        maxEpochWithoutImprovement = epochwithoutImprovement;
        minDesiredError = minError;
        //how much data is use for training
        double trainingRatio = 0.7;
        normalisedAllData(data);
        //splitting the dataset to train on
        int validationLength = (int) ((data.length * trainingRatio) * 0.1);
        int trainingLength = (int) ((data.length * trainingRatio) - validationLength);
        int testingLength = data.length - (trainingLength + validationLength);

        TrainingData[] trainingData = new TrainingData[trainingLength];
        TrainingData[] validationData = new TrainingData[validationLength]; 
        TrainingData[] testingData = new TrainingData[testingLength];
        dataSplitting(data, trainingData, testingData, validationData, trainingRatio);
    

      NetworkBP network = new NetworkBP(inputNode, hiddenNode, outNode, LAYERNUM);
        
        train(trainingData,validationData, network);

        writeToFile(network, weightsPath);
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
                validationData[counter2].setDate(data[i].getDate());
                counter2++;
            }
            //when testing is full the data goes to training 
           
            else if(i % 10 > splitRatio && counter3 < testingLength){
                testingData[counter3] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue());
                testingData[counter3].setDate(data[i].getDate());
                counter3++;
            }
            else if(counter1 < trainingLength){
                trainingData[counter1] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue()); 
                trainingData[counter1].setDate(data[i].getDate());
                    counter1++;
            }
            else{
                testingData[counter3] = 
                        new TrainingData(data[i].getData(), data[i].getExpectValue());
                testingData[counter3].setDate(data[i].getDate());
                counter3++;
            }
            
        }
    }
    
    public static NetworkBP createExistingNeuralNetwork(String dataPath, String weightsPath) {
        
        TrainingData[] data = 
                 getDataFromFile(dataPath);
         //set nodes length
        inputNode = data[0].getData().length;
        hiddenNode = inputNode + 1;
        LAYERNUM = 4;
        outNode = 1;
        //how much data is use for training
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();
        double predictionMax = max[max.length -1];
        normalisedAllData(data);
       NetworkBP network = getWeightsFromFile(weightsPath);
       
       
        
        
        double[] dataPrediction = forward(data, network);
        network.setNetworkError((network.getNetworkError() / data.length) * predictionMax);
        network.setNetworkErrorSquared((network.getNetworkErrorSquared()/ data.length )* predictionMax);
 
        for (int i = 0; i < dataPrediction.length; i++) {

          
            dataPrediction[i] = denormaliseData(dataPrediction[i] ,min[min.length-1], max[max.length - 1]) ;
        }

        network.setPrediction(dataPrediction);
        return network;
    }
    
        public static NetworkBP createExistingNeuralNetwork(TrainingData[] data, String weightsPath) {
        
         //set nodes length
        inputNode = data[0].getData().length;
        hiddenNode = inputNode + 1;
        LAYERNUM = 4;
        outNode = 1;
        //how much data is use for training
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();
        double predictionMax = max[max.length -1];
        normalisedAllData(data);
        NetworkBP network = getWeightsFromFile(weightsPath);
       
        double[] dataPrediction = forward(data, network);
        network.setNetworkError((network.getNetworkError() / data.length) * predictionMax);
        network.setNetworkErrorSquared((network.getNetworkErrorSquared()/ data.length )* predictionMax);
 
        for (int i = 0; i < dataPrediction.length; i++) {

          
            dataPrediction[i] = denormaliseData(dataPrediction[i] ,min[min.length-1], max[max.length - 1]) ;
        }

        network.setPrediction(dataPrediction);
        return network;
    }
    
    public static NetworkBP getTrainingValidationError(String dataPath, String weightsPath) {
        
        TrainingData[] data = 
                 getDataFromFile(dataPath);
         //set nodes length
        inputNode = data[0].getData().length;
        hiddenNode = inputNode + 1;
        LAYERNUM = 4;
        outNode = 1;
        //how much data is use for training


       NetworkBP network = getWeightsFromFile(weightsPath);
       
        return network;
    }
    
    public static double predict(double[] features, TrainingData data, String weightsPath) {
        

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

       NetworkBP network = getWeightsFromFile(weightsPath);
       
        double dataPrediction = forward(newData, network);
        dataPrediction = denormaliseData(dataPrediction ,min[min.length-1], max[max.length - 1]) ;
        
        return dataPrediction;
    }
    
    public static void writeToFile(NetworkBP network, String path)
    {
        
        String weights = "";
        double[][] hiddenBias = network.getHiddenBias();
        double[] outerBias = network.getOuterBias();
        double[][][] hWeights = network.gethWeights();
        double[][] oWeights = network.getoWeights();
        ArrayList<Double> BPError = network.getBPError();
        ArrayList<Double> vError = network.getTrainingValidationError();
        weights += "layers\n";
        weights += network.getNumOfLayer() + "\n";
        weights += "Hidden Bias\n";
        for (int i = 0; i < hiddenBias.length; i++) {
            for (int j = 0; j < hiddenBias[0].length; j++) {
                weights += hiddenBias[i][j] + " ";
            }
            weights += ";";
        }
        weights += "\nOuter Bias\n";
        for (int i = 0; i < outerBias.length; i++) {
            weights += outerBias[i] + " ";
        }
        weights += "\nHidden Weights\n";
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
         for (Double BPError1 : BPError) {
             weights += BPError1 + " ";
         }
        weights += "\nValidation Error\n";
        for (Double vError1 : vError) {
             weights += vError1 + " ";
        }
        try {
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
}
