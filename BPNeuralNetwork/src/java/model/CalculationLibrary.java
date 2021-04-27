/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author varut
 */
public class CalculationLibrary {
    /**
     *
     * sigmoid transfer function
     */
    public static double sigmoid(double x)
    {
        //sigmoid transfer 1/1 + e^-1
        //alway one and zero
        
        return (1.0/(1.0 + Math.exp(-x)));
    }
    
    //calculating the of each node
    public static double squaredError(double output, double target)
    {
        return (0.5 * Math.pow(target - output, 2));
    }
    //function for getting aboslute error
    public static double absoluteError(double output, double target)
    {
        return Math.abs(target - output);
    }
 
    
 
}
