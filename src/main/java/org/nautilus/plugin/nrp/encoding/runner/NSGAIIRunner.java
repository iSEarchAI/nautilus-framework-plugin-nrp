package org.nautilus.plugin.nrp.encoding.runner;

import br.otimizes.isearchai.learning.algorithms.binary.MLNSGAIIBinaryRunner;
import br.otimizes.isearchai.learning.encoding.binary.MLBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.problem.BinaryNRPProblem;
import org.nautilus.plugin.nrp.extension.problem.NRPProblemExtension;
import org.uma.jmetal.problem.Problem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class NSGAIIBinaryRunner extends MLNSGAIIBinaryRunner {

    private static Path path = Paths.get("src", "main", "resources", "instances", "r025.txt");

    public static void main(String[] args) {
        new NSGAIIBinaryRunner().run();
    }

    @Override
    public Problem<MLBinarySolution> getProblem() {
        List<AbstractObjective> objectives = new NRPProblemExtension().getObjectives();
        Instance instance = new NRPProblemExtension().getInstance(path);

        return new BinaryNRPProblem(instance, objectives);
    }
}
