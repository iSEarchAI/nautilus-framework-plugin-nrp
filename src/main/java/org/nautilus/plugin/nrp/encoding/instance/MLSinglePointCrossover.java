package org.nautilus.plugin.nrp.encoding.instance;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class MLSinglePointCrossover implements CrossoverOperator<MLBinarySolution> {
    private double crossoverProbability;
    private RandomGenerator<Double> crossoverRandomGenerator;
    private BoundedRandomGenerator<Integer> pointRandomGenerator;

    /**
     * Constructor
     */
    public MLSinglePointCrossover(double crossoverProbability) {
        this(crossoverProbability, () -> JMetalRandom.getInstance().nextDouble(), (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
    }

    /**
     * Constructor
     */
    public MLSinglePointCrossover(double crossoverProbability, RandomGenerator<Double> randomGenerator) {
        this(crossoverProbability, randomGenerator, BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
    }

    /**
     * Constructor
     */
    public MLSinglePointCrossover(double crossoverProbability, RandomGenerator<Double> crossoverRandomGenerator, BoundedRandomGenerator<Integer> pointRandomGenerator) {
        if (crossoverProbability < 0) {
            throw new JMetalException("Crossover probability is negative: " + crossoverProbability);
        }
        this.crossoverProbability = crossoverProbability;
        this.crossoverRandomGenerator = crossoverRandomGenerator;
        this.pointRandomGenerator = pointRandomGenerator;
    }

    /* Getter */
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    /* Setter */
    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public List<MLBinarySolution> execute(List<MLBinarySolution> solutions) {
        if (solutions == null) {
            throw new JMetalException("Null parameter");
        } else if (solutions.size() != 2) {
            throw new JMetalException("There must be two parents instead of " + solutions.size());
        }

        return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1));
    }

    /**
     * Perform the crossover operation.
     *
     * @param probability Crossover setProbability
     * @param parent1     The first parent
     * @param parent2     The second parent
     * @return An array containing the two offspring
     */
    public List<MLBinarySolution> doCrossover(double probability, MLBinarySolution parent1, MLBinarySolution parent2) {
        List<MLBinarySolution> offspring = new ArrayList<>(2);
        offspring.add((MLBinarySolution) parent1.copy());
        offspring.add((MLBinarySolution) parent2.copy());

        if (crossoverRandomGenerator.getRandomValue() < probability) {
            // 1. Get the total number of bits
            int totalNumberOfBits = parent1.getTotalNumberOfBits();

            // 2. Calculate the point to make the crossover
            int crossoverPoint = pointRandomGenerator.getRandomValue(0, totalNumberOfBits - 1);

            // 3. Compute the variable containing the crossover bit
            int variable = 0;
            int bitsAccount = parent1.getVariableValue(variable).getBinarySetLength();
            while (bitsAccount < (crossoverPoint + 1)) {
                variable++;
                bitsAccount += parent1.getVariableValue(variable).getBinarySetLength();
            }

            // 4. Compute the bit into the selected variable
            int diff = bitsAccount - crossoverPoint;
            int intoVariableCrossoverPoint = parent1.getVariableValue(variable).getBinarySetLength() - diff;

            // 5. Apply the crossover to the variable;
            MLBinarySet offspring1, offspring2;
            offspring1 = (MLBinarySet) parent1.getVariableValue(variable).clone();
            offspring2 = (MLBinarySet) parent2.getVariableValue(variable).clone();

            for (int i = intoVariableCrossoverPoint; i < offspring1.getBinarySetLength(); i++) {
                boolean swap = offspring1.get(i);
                offspring1.set(i, offspring2.get(i));
                offspring2.set(i, swap);
            }

            offspring.get(0).setVariableValue(variable, offspring1);
            offspring.get(1).setVariableValue(variable, offspring2);

            // 6. Apply the crossover to the other variables
            for (int i = variable + 1; i < parent1.getNumberOfVariables(); i++) {
                offspring.get(0).setVariableValue(i, (MLBinarySet) parent2.getVariableValue(i).clone());
                offspring.get(1).setVariableValue(i, (MLBinarySet) parent1.getVariableValue(i).clone());
            }

        }
        return offspring;
    }

    @Override
    public int getNumberOfRequiredParents() {
        return 2;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 2;
    }
}
