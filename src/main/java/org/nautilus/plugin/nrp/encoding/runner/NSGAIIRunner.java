package org.nautilus.plugin.nrp.encoding.runner;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.learning.MLSolutionSet;
import br.otimizes.isearchai.learning.impl.MLNSGAIIBuilder;
import br.otimizes.isearchai.learning.nautilus.*;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.problem.NRPProblem;
import org.nautilus.plugin.nrp.extension.problem.NRPProblemExtension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class NSGAIIRunner {

    private static Path path = Paths.get("src", "main", "resources", "instances", "r025.txt");

    public static void main(String[] args) {

        System.out.println("Loading...");

        List<AbstractObjective> objectives = new NRPProblemExtension().getObjectives();
        Instance instance = new NRPProblemExtension().getInstance(path);

        Problem problem = new NRPProblem(instance, objectives);

        CrossoverOperator<MLBinarySolution> crossover = new MLSinglePointCrossover(0.9);
        MutationOperator<MLBinarySolution> mutation = new MLBitFilpMutation(0.005);
        SelectionOperator<List<MLBinarySolution>, MLBinarySolution> selection = new BinaryTournamentSelection<MLBinarySolution>(
            new RankingAndCrowdingDistanceComparator<MLBinarySolution>());


        Algorithm<List<MLBinarySolution>> algorithm = new MLNSGAIIBuilder<MLBinarySolution>(problem, crossover, mutation,
            100, new InteractiveConfig().setInteractiveFunction(solutionSet -> {
            MLSolutionSet<MLBinarySolution, MLBinarySet> solutions = solutionSet;
            for (MLBinarySolution solution : solutions) {
                if (solution.getObjective(0) < .2) {
                    solution.setEvaluation(4);
                } else if (solution.getObjective(1) < .4) {
                    solution.setEvaluation(3);
                } else {
                    solution.setEvaluation(2);
                }
            }
            return solutions;
        }).setFirstInteraction(3).setIntervalInteraction(3).setMaxInteractions(3), new MLBinarySolutionSet())
            .setSelectionOperator(selection)
            .setMaxEvaluations(10000)
            .build();

        System.out.println("Optimizing...");

        new AlgorithmRunner.Executor(algorithm).execute();

        List<MLBinarySolution> population = algorithm.getResult();

        for (MLBinarySolution solution : population) {
            System.out.println(Arrays.toString(solution.getObjectives()));
        }

        System.out.println("Done");
    }
}
