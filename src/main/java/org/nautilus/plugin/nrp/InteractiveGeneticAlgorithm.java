package org.nautilus.plugin.nrp;

import org.nautilus.plugin.nrp.interactive.DataSet;
import org.nautilus.plugin.nrp.interactive.HumanSimulator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

public class InteractiveGeneticAlgorithm {
    private HumanSimulator simulator;
    private double[] parameters = {1, 1};
    private double maxIndividualScore;
    private Problem<BinarySolution> problem;
    private double budget;
    private Random random;
    private Classifier model;
    private Instances trainingData;
    private int evaluationCount;
    private int maxEvaluations;
    private double[] requirementsCost;
    public double[] requirementsScore;

    public InteractiveGeneticAlgorithm(Problem<BinarySolution> problem, HumanSimulator simulator, Classifier model, Instances trainingData, int maxEvaluations, double[] requirementsCost) {
        this.problem = problem;
        this.simulator = simulator;
        requirementsScore = new double[]{1.6228170746139177, 3.995106513241677, 4.622817074613917, 2.8852188912269874, 4.246329884931257, 3.0, 2.6228170746139177, 2.863557737083431, 2.759259337530486, 0.9999999999999999, 3.1252638179930785, 2.3715937029243372, 4.005589222461744, 2.496857520917416, 2.863557737083431, 3.011178444923489, 2.377182925386082, 3.2568125941513246, 2.880325404468665, 3.6228170746139177, 3.011178444923489, 2.874040446303498, 1.7543658507721638, 2.2512233716895804, 2.7599550732339084, 2.6284062970756623, 1.9999999999999998, 3.246329884931258, 3.502446743379161, 1.9999999999999998, 3.868451223841753, 2.2512233716895804, 3.2512233716895804, 2.869146959545176, 3.0, 2.503142479082583, 2.9888215550765103, 3.491268298455671, 4.125959553696501, 2.869146959545176, 3.8796296687652427, 3.6339955195374065, 2.994410777538255, 4.497553256620838, 2.8852188912269874, 1.7536701150687415, 2.880325404468665, 2.125959553696501, 4.497553256620838, 3.2400449267660907};
        this.random = new Random();
        this.model = model;
        this.trainingData = trainingData;
        this.evaluationCount = 0;
        this.maxEvaluations = maxEvaluations;
        this.requirementsCost = requirementsCost;
        this.budget = calculateBudget(60); // Example percentage
        this.buildResults();
    }

    private double[] getRequirementsScore(double[] customersImportance, double[][] requirementsImportance){
        double[] requirementsScore = new double[requirementsImportance[0].length];

        for (int i = 0; i <= customersImportance.length - 1; i++) {
            for (int j = 0; j <= requirementsImportance[0].length - 1; j++) {
                requirementsScore[j] += customersImportance[i] * requirementsImportance[i][j];
            }
        }

        return requirementsScore;
    }

    private void buildResults() {
        DataSet dataTest = new DataSet(maxEvaluations, 50);

        for (int i = 0; i < 50; i++) {
            double[] individual = getRandomIndividual(i);
            int she = simulator.getHumanEvaluation(individual);
            dataTest.insert(individual, she);
        }
    }


    private double[] getRandomIndividual(int requirementToBeIncluded){
        int numberOfRequirements = requirementsScore.length;
        double[] randomIndividual = new double[numberOfRequirements];
        int numberOfRequirementsToBeIncluded = random.nextInt(numberOfRequirements) + 1;
        int randomRequirement = 0;

        for(int i = 0; i <= numberOfRequirementsToBeIncluded - 1; i++){
            randomRequirement = random.nextInt(numberOfRequirements);

            while(randomIndividual[randomRequirement] == 1){
                randomRequirement = random.nextInt(numberOfRequirements);
            }

            randomIndividual[randomRequirement] = 1;
        }

        return randomIndividual;
    }

    private Instance createInstance(BinarySolution solution) {
        Instance instance = new DenseInstance(trainingData.numAttributes());
        instance.setDataset(trainingData);

        BinarySet binarySet = solution.getVariableValue(0);
        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
            instance.setValue(i, binarySet.get(i) ? 1.0 : 0.0);
        }

        instance.setClassMissing();
        return instance;
    }

    public void interactWithSimulator(BinarySolution solution) throws Exception {
        if (evaluationCount < maxEvaluations) {
            int she = simulator.getHumanEvaluation(convertSolutionToArray(solution));
            updateTrainingData(solution, she);
            evaluationCount++;
            if (evaluationCount >= maxEvaluations / 2) {
                model.buildClassifier(trainingData);
            }
        } else {
            int she = (int) model.classifyInstance(createInstance(solution));
            solution.setObjective(0, solution.getObjective(0) + she);
        }
    }

    private void updateTrainingData(BinarySolution solution, int she) {
        Instance instance = createInstance(solution);
        instance.setClassValue(she);
        trainingData.add(instance);
    }

    private double calculateBudget(double budgetPercentage) {
        double totalCost = 0;
        for (double cost : requirementsCost) {
            totalCost += cost;
        }
        return totalCost * budgetPercentage / 100.0;
    }

    private double[] convertSolutionToArray(BinarySolution solution) {
        BinarySet binarySet = solution.getVariableValue(0);
        double[] array = new double[binarySet.getBinarySetLength()];
        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
            array[i] = binarySet.get(i) ? 1 : 0;
        }
        return array;
    }
}
