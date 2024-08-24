package org.nautilus.plugin.nrp.encoding.problem;

import br.otimizes.isearchai.learning.encoding.binary.MLBinaryProblem;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

public class NRPProblem extends MLBinaryProblem {

    private static final long serialVersionUID = 1233594822179588853L;

    public NRPProblem(Instance instance, List<AbstractObjective> objectives) {
        super(instance, objectives);

        setNumberOfVariables(1);

        List<Integer> bitsPerVariable = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            bitsPerVariable.add(((TXTInstance) getInstance()).getNumberOfRequirements());
        }

        setBitsPerVariable(bitsPerVariable);
    }

    @Override
    public void evaluate(MLBinarySolution solution) {

        // Change if it is invalid

        BinarySet binarySet = (BinarySet) solution.getVariableValue(0);

        if (binarySet.isEmpty()) {

            int pos = JMetalRandom.getInstance().nextInt(0, binarySet.getBinarySetLength() - 1);

            binarySet.set(pos, true);
        }

        super.evaluate(solution);
    }
}
