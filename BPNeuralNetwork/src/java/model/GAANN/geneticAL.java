/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.GAANN;
 
import java.util.ArrayList;
import java.util.Random;
import static model.GAANN.NeuralNetwork.fitnessFunction;
import model.TrainingData;

/**
 * Individual present a solution within a population
 * with getter and setter functions
 */
class Individual
{
    int geneNum = 0;
    double[] Gene;
    double fitness = 0.0;
    public Individual(int numGene) {
        //generate random gene between -1 and 1
        Random rand = new Random();
        double max = 1;
        double min = -1;
        this.geneNum = numGene;
        this.Gene = new double[geneNum];
        for(int i = 0; i < geneNum; i++){
            Gene[i] = (max - min) * rand.nextDouble() + min;
            //System.out.println(Gene[i]);
        }
        
    }
    
     public Individual(double [] genes) {
        //generate random gene between -1 and 1
        this.geneNum = genes.length;
        this.Gene = genes;
        
    }
    //fitness function get total network's error and use it as fitness of the indidvual
    public void setFitness(Individual population, TrainingData[] data)
    { 
        this.fitness = fitnessFunction(population, data);

    }
    //overload fitness to reduce computation power
    public void setFitness(double fitness)
    { 
        this.fitness = fitness;

    }
    public double getFitness()
    {
        return this.fitness;
    }
    public void setGene(double[] gene){
        for (int i = 0; i < geneNum; i++) {
            this.Gene[i] = gene[i];
        }
    }
    public double [] getGene(){
        return Gene;
    }
    
}

/**
 *
 * @author varut
 */

public class geneticAL {
    //global variable
    static int geneNum;
    //population size
    final static int SIZE = 100;
    //
    /**
    * Transfer the content of one class to another
    */
    
    /**
     * Selection
     * using roulette wheel selection favouring the stronger candidate
     */
    public static void selection(Individual[] population, Individual[] offspring)
    {
        Random rand = new Random();
        for(int i = 0; i < population.length; i++)
        {   //select two random parents
            int num1 = rand.nextInt(SIZE);
            int num2 = rand.nextInt(SIZE);
            //chance of the less fitness parent to be pick
            int chanceBadParent = rand.nextInt(10);
            double fitness1 = population[num1].getFitness();       
            double fitness2 = population[num2].getFitness();

            //selection will allow bad parent in 30% of the time
            //randomly pick less fit parent to next generation
            
            if(chanceBadParent > 7){
                if(fitness1 > fitness2)
                {
                    cloneParent(population[num1], offspring[i]);
                }
                else{
                    cloneParent(population[num2], offspring[i]);
                }
            }
            else if(fitness1 < fitness2 ){
                cloneParent(population[num1], offspring[i]);
            }
            else{
                cloneParent(population[num2], offspring[i]);
            }
            
 
        }
    }
    
      //
    /**
    * Transfer the content of one class to another
    */
    public static void cloneParent(Individual parent, Individual offspring)
    {
        offspring.setGene(parent.getGene());
        offspring.setFitness(parent.getFitness());
        
    }
    
    /**
     * compareFitness
     * getting the fitness of each generation and the best offspring 
     *  for the whole training cycle
     */
    public static double compareFitness(Individual[] offspring, Individual bestOffSpring)
    {
        //getting fitness for this generation to be display on a graph
        double bestFitness = 2000000000;
        for (int i = 0; i < SIZE; i++) {
            double currentFitness = offspring[i].getFitness();
            double offspringMin = bestOffSpring.getFitness();
            //if fitness less than clone 
            //getting best offspring for the whole run time

            if(currentFitness < offspringMin){
                 cloneParent(offspring[i], bestOffSpring);
            }
            //getting best offpsring this generation
            if(currentFitness < bestFitness)
            {
                bestFitness = currentFitness;
            
            }
        }
        return bestFitness;

    }
    
    /**
     * Crossover
     * Using single point crossover 
     */
    public static void crossover(Individual parent1, Individual parent2, TrainingData[] data)
    {
        Random rand = new Random();
        Individual offspring = new Individual(geneNum);
        //pick a random point close to the middle
        int randNum = rand.nextInt(geneNum);
        double[] gene1 = parent1.getGene();
        double[] gene2 = parent2.getGene();
        double[] temp = gene1;
        //switch genes
        for (int i = 0; i < randNum; i++) {
            gene1[i] = gene2[i];
            gene2[i] = temp[i];
        }
        //recalculate the new fitness
        parent1.setGene(gene1);
        parent1.setFitness(parent1, data);
        parent2.setGene(gene2);
        parent2.setFitness(parent2, data);
    }
   
   
    /**
    * mutation
    * pick at random if the gene will mutation and pick the mutation magnitude
    * at random
    */
    static double mutationRate = 970; 
   public static void mutation(Individual offspring, TrainingData[] data)
   {
       //uncapped mutation can go over the init genes
       //2% mutation rsate
       
       Random rand = new Random();
       double[] gene =  offspring.getGene();
       int randNum = 0; 
       double alter = 0;
       //for all gene
       for (int i = 0; i < geneNum; i++) {
           randNum = rand.nextInt(1000);
           //mutation size
           alter = rand.nextDouble()/ 10;
           if(randNum <= mutationRate)
           {
               //50 50 for increasing or decreasing the gene
              if(rand.nextInt(2) % 2 == 0)
              {
                  gene[i] += alter;
              }
              else{
                  gene[i] -= alter;

              }
           }
       }
       offspring.setGene(gene);
       offspring.setFitness(offspring, data);
       
       
   }
   
    /**
     * GenticAI
     * function that take in dataset, split it and train the network
     */
    
    public static Individual GeneticAI(int numOfGene, TrainingData[] data, TrainingData[] validationData, double desiredMinError, NetworkGA network
    , double mutation, double crossoverRate, int generationWithoutImprovement)
    {
        
        geneNum = numOfGene;
        mutationRate =  mutation * 1000;
        double fitness = 10000; 
        double LowestError = 100000;
        double crossover = crossoverRate * 1000;
        Random rand = new Random();
        int epochWithoutImprovement = 0;
        int epoch = 0;
        Individual[] population = new Individual[SIZE];
        Individual[] offspring = new Individual[SIZE];
        Individual bestOffSpring = new Individual(geneNum);
        bestOffSpring.setFitness(fitness);
        ArrayList<Double> trainingError = new ArrayList<>();
        ArrayList<Double> validationError = new ArrayList<>();
        
        //setting up the population
        for(int i = 0; i< SIZE; i++){
            population[i] = new Individual(geneNum);
            population[i].setFitness(population[i], data); 
            offspring[i] = new Individual(geneNum);

        }
        //loop for a 1000 generations
        while(fitness > desiredMinError)
        {
            selection(population, offspring);
            //crossover for the whole generation
            for (int j = 0; j < SIZE ; j+=2) {
                double randNum = rand.nextInt(1000);
                if(randNum <= crossover){
                    //System.out.println("crossover");
                    crossover(offspring[j], offspring[j + 1], data);
                }
            }
            //mutation for the whole generation
            for (int j = 0; j < SIZE; j++) {
                mutation(offspring[j], data);
            }
            
            double bestInGeneration = compareFitness(offspring, bestOffSpring);
            fitness = bestInGeneration;
            //replacce the current genertaion wit offspring
            for (int j = 0; j < population.length; j++) {
                cloneParent(offspring[j],  population[j]);
            }
            if(epochWithoutImprovement > generationWithoutImprovement )
            {
                //breakout
                fitness = 0; 
            }
            if(fitness < LowestError)
            {
                LowestError = fitness;
                epochWithoutImprovement= 0;
            }
            if(epoch > 101 || epoch == 0)
            {
                //adding training curve

                trainingError.add(fitness);
                double[] weighting = bestOffSpring.getGene();
                NeuralNetwork.genesToWeights(network, weighting);
                NeuralNetwork.forward(validationData, network);
                double vError = network.getNetworkError();
                validationError.add(vError);
 
                //breakout if validation more error than traininng butt not first epoch
                if(vError > fitness && epoch != 0){
                    fitness = 0;
                }
                epoch = 1;
            }
            epoch++;
            epochWithoutImprovement++;

        }
        trainingError.add(LowestError);
        NeuralNetwork.forward(validationData, network);
        double vError = network.getNetworkError();
        validationError.add(vError);
        network.setTrainingError(trainingError);
        network.setValidationError(validationError);
        //return the good offspring
        return bestOffSpring;
    }
    
   
    

    
}


