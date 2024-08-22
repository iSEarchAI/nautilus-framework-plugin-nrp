package org.nautilus.plugin.nrp.encoding.instance;

import br.otimizes.isearchai.learning.MLSolutionSet;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.plugin.nrp.encoding.model.Requirement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BinarySolutionSet extends MLSolutionSet<MLBinarySolution, Requirement> {

    public BinarySolutionSet() {
    }

    @Override
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        return this.solutions.stream().map(NSolution::getObjectives).toArray(double[][]::new);
    }


    @Override
    public double[] writeObjectivesFromElements(Requirement MLElement, MLBinarySolution MLSolution) {
        return new double[0];
    }

    @Override
    public double[] writeCharacteristicsFromElement(Requirement MLElement, MLBinarySolution MLSolution) {
        return new double[0];
    }

    @Override
    public List<Requirement> getAllElementsFromSolution(MLBinarySolution MLSolution) {
        return Collections.emptyList();
    }

    @Override
    public Iterator<MLBinarySolution> iterator() {
        return solutions.iterator();
    }
}
