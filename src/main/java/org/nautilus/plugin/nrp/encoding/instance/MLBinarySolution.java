package org.nautilus.plugin.nrp.encoding.instance;

import br.otimizes.isearchai.learning.MLSolution;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.plugin.nrp.encoding.model.Requirement;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import java.util.Collections;
import java.util.List;

public class MLBinarySolution extends NBinarySolution implements MLSolution<Requirement> {
    private int userEvaluation;
    private Boolean clusterNoise;
    public Boolean evaluatedByUser;
    private Double clusterId;

    public MLBinarySolution() {
    }

    public MLBinarySolution(NBinarySolution solution) {
        super(solution);
    }

    public MLBinarySolution(int numberOfObjectives, int numberOfVariables, List<Integer> bitsPerVariable) {
        super(numberOfObjectives, numberOfVariables, bitsPerVariable);
    }

    public MLBinarySolution(int numberOfObjectives, int numberOfVariables, int bitsForVariables) {
        super(numberOfObjectives, numberOfVariables, bitsForVariables);
    }

    @Override
    public int numberOfObjectives() {
        return objectives.length;
    }

    @Override
    public void setClusterId(Double assignment) {
        this.clusterId = assignment;
    }

    @Override
    public void setClusterNoise(Boolean b) {
        this.clusterNoise = clusterNoise;
    }

    @Override
    public Double getClusterId() {
        return this.clusterId;
    }

    @Override
    public void setEvaluation(int i) {
        this.userEvaluation = i;
    }

    @Override
    public List getElements() {
        return Collections.emptyList();
    }

    @Override
    public boolean containsElementsEvaluation() {
        return false;
    }

    @Override
    public int getEvaluation() {
        return this.userEvaluation;
    }

    @Override
    public boolean getEvaluatedByUser() {
        return evaluatedByUser != null && evaluatedByUser;
    }

    @Override
    public Solution<BinarySet> copy() {
        return new MLBinarySolution(this);
    }
}
