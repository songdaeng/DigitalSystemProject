
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author varut
 */
/**
 * class to store dataset 
 */
public class TrainingData {

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param data the data to set
     */
    public void setData(double[] data) {
        this.data = data;
    }

    /**
     * @param expectValue the expectValue to set
     */
    public void setExpectValue(double expectValue) {
        this.expectValue = expectValue;
    }

    /**
     * @return the max
     */
    public double[] getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(double[] max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public double[] getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(double[] min) {
        this.min = min;
    }
    private double[] data;
    private double expectValue;
    double dataClass; 
    private double[] max;
    private double[] min;
    private String date;
    public TrainingData(double [] data, double expectValue)
    {
        this.data = data;
       this.expectValue = expectValue;
    }
    
    
    public double[] getData()
    {
        return data;
  
    }
    
    public double getExpectValue()
    {
        return expectValue;
  
    }
     
    
    //function for normalised pass in the value and range  
    public static double normaliseData(double x, double min, double max) {

     return (x -  min) / (max - min);

    }
    //fuction for denormalise, actual unnormalise technically
    //pass in value and range
    public static double denormaliseData(double x, double min, double max) {

     return (x * (max - min) + min);

    }
    //normalised all pass in data with range in index 0 of teh class
    public static void normalisedAllData(TrainingData[] data)
    {
        double[] min = data[0].getMin();
        double[] max = data[0].getMax();

        for (int i = 0; i < data.length; i++) {
            double data1[] = data[i].getData();
            double exValue = data[i].getExpectValue();

            for (int j = 0; j < data1.length; j++) {
                data1[j] = normaliseData(data1[j],  min[j],max[j]);
                
            }
            data[i].setData(data1);
            exValue = normaliseData(exValue,  min[min.length-1], max[max.length - 1]);
            data[i].setExpectValue(exValue);
        }
    }
    
    /**
     * Get data from file 
     * in: filepath out: dataset
     */
    public static TrainingData[] getDataFromFile(String path)
    {
        //support upt a 100000 records 
        TrainingData[] data = new TrainingData[100000];
        int counter = 0;
        try  {  
            //setting up the file reader
            File file=new File(path);    
            FileReader fr=new FileReader(file);   
            BufferedReader br=new BufferedReader(fr);  
            double exValue = 0;
            //storing content
            String date = "";
            String line = "";
            //get max and min
            double[] max = null;
            double[] min = null;

           //while there's content to be read
            while((line=br.readLine())!=null)  
            {  
                
                String[] temp = line.split("\\s+");
                int stringLength = temp.length;                
                if(counter ==0)
                {            
                   max = new double[stringLength - 1];
                   min = new double[stringLength - 1];
                }
                
                //getting all value expect for the last value in the line
                exValue = Double.parseDouble(temp[stringLength - 1]);
                date = temp[0];
                String[] rawData = Arrays.copyOfRange(temp, 1, temp.length);
                stringLength = rawData.length;
                double[] value = new double[stringLength - 1];
                for (int i = 0; i < stringLength - 1; i++) {
                    value[i] = Double.parseDouble(rawData[i]);
                    if(counter == 0){
                        //set the first value of the min and max
                        max[i] = value[i];
                        min[i] = value[i];
                        max[stringLength - 1] = exValue;
                        min[stringLength - 1] = exValue;
                    }
                    else{
                        //see if value iss a max or min
                        if(value[i] > max[i])
                        {
                            max[i] = value[i];
                        }
                        else if(value[i] < min[i])
                        {
                            min[i] = value[i];
                        }
                    }
                }
                //update prediction value
                if(exValue > max[stringLength - 1])
                {
                    max[stringLength - 1] = exValue;
                }
                else if(exValue < min[stringLength - 1])
                {
                    min[stringLength - 1] = exValue;
                }
  
                //assigningg to target value to exValue
                
                //transfer data in a class
                data[counter] = new TrainingData(
                        value,
                        exValue);
                data[counter].setDate(date);
                counter++;

            }  
            
            fr.close();   
            //upscale the min and max
            for (int i = 0; i < min.length; i++) {
                max[i] = max[i] * 2;
                min[i] = min[i] / 2 == 0?  min[i] / 2 : min[i];
                
            }            
            data[0].setMax(max);
            data[0].setMin(min);
            
        } catch (IOException ex) {
             Logger.getLogger(model.GAANN.NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
         }

        //reconstructing the data so it does not expty array
        TrainingData[] newdata = new TrainingData[counter];
        for (int i = 0; i < counter; i++) {
            newdata[i] = data[i];
        }
        return newdata;
    }

}
