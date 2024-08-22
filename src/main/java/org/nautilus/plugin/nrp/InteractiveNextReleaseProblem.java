package org.nautilus.plugin.nrp;

import org.nautilus.plugin.nrp.interactive.HumanSimulator;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import weka.classifiers.Classifier;

public class InteractiveNextReleaseProblem implements BinaryProblem {

    private int numberOfRequirements;
    private double[] requirementsCost;
    private double[] requirementsScore;
    private double budget;
    private HumanSimulator simulator;
    private Classifier model;
    private boolean useSimulator;

    public InteractiveNextReleaseProblem(int numberOfRequirements, double[] requirementsCost, double[] requirementsScore, double budget, HumanSimulator simulator, Classifier model) {
        this.numberOfRequirements = numberOfRequirements;
        this.requirementsCost = requirementsCost;
        this.requirementsScore = requirementsScore;
        this.budget = budget;
        this.simulator = simulator;
        this.model = model;
        this.useSimulator = true;
    }

    public void useSimulator(boolean useSimulator) {
        this.useSimulator = useSimulator;
    }

    @Override
    public int getNumberOfVariables() {
        return numberOfRequirements;
    }

    @Override
    public int getNumberOfObjectives() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 1;
    }

    @Override
    public String getName() {
        return "InteractiveNextReleaseProblem";
    }

    @Override
    public void evaluate(BinarySolution solution) {
        BinarySet binarySet = solution.getVariableValue(0);
        double totalCost = 0;
        double totalScore = 0;

        for (int i = 0; i < numberOfRequirements; i++) {
            if (binarySet.get(i)) {
                totalCost += requirementsCost[i];
                totalScore += requirementsScore[i];
            }
        }

        solution.setObjective(0, -totalScore); // We want to maximize the score, hence minimizing the negative score
        solution.setObjective(0, totalCost <= budget ? 0 : budget - totalCost); // Constraint satisfaction

        if (useSimulator) {
            int she = simulator.getHumanEvaluation(solution.getObjectives());
            // Incorporate subjective human evaluation in fitness
            solution.setObjective(0, solution.getObjective(0) + she);
        }
    }

    @Override
    public BinarySolution createSolution() {
        return new DefaultBinarySolution(this);
    }

    @Override
    public int getNumberOfBits(int i) {
        return 0;
    }

    @Override
    public int getTotalNumberOfBits() {
        return 0;
    }
}
