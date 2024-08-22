package org.nautilus.plugin.nrp;

import org.nautilus.plugin.nrp.interactive.HumanSimulator;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Example data
        int numberOfRequirements = 10;
        double[] requirementsCost = {1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5, 1.2, 2.2, 1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5, 1.2, 2.2, 1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5, 1.2, 2.2, 1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5, 1.2, 2.2, 1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5, 1.2, 2.2};
        double[] requirementsScore = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        double budget = 10.0;

        // Initialize HumanSimulator
        HumanSimulator simulator = new HumanSimulator();
        simulator.setCostValues(requirementsCost);
        simulator.setScoreValues(requirementsScore);
        simulator.setHumanSimulatorProfile("MANUALLY");



        // Initialize Weka Classifier
        Classifier model = new MultilayerPerceptron();
        Instances trainingData = InstancesHelper.createTrainingDataStructure(numberOfRequirements);

        // Define the problem
        InteractiveNextReleaseProblem problem = new InteractiveNextReleaseProblem(
            numberOfRequirements, requirementsCost, requirementsScore, budget, simulator, model
        );

        // Initialize the interactive genetic algorithm
        InteractiveGeneticAlgorithm iga = new InteractiveGeneticAlgorithm(problem, simulator, model, trainingData, 20000, requirementsCost);

        // Initialize the interactive NSGA-II algorithm
        InteractiveNSGAII algorithm = new InteractiveNSGAII(
            problem,
            20000, // max evaluations
            100, // population size
            100, // mating pool size
            100, // offspring size
            new SinglePointCrossover(0.9),
            new BitFlipMutation(0.01),
            new BinaryTournamentSelection<>(),
            new SequentialSolutionListEvaluator<>(),
            iga
        );

        // Run the algorithm
        algorithm.run();

        // Retrieve and display results
        List<BinarySolution> result = algorithm.getResult();
        BinarySolution bestSolution = result.get(0); // Assuming the first one is the best for simplicity

        HashMap<String, String> results = new HashMap<>();
        results.put("bestIndividual", bestSolution.getVariableValue(0).toString());
        results.put("bestIndividualScore", String.valueOf(bestSolution.getObjective(0)));

        System.out.println("Best Individual: " + results.get("bestIndividual"));
        System.out.println("Best Individual Score: " + results.get("bestIndividualScore"));
    }
}
