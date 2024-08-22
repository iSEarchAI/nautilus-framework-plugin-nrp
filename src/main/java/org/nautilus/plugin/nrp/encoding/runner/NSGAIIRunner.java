package org.nautilus.plugin.nrp.encoding.runner;

import br.otimizes.isearchai.interactive.InteractiveConfig;
import br.otimizes.isearchai.learning.impl.MLNSGAIIBuilder;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.MLBinarySolution;
import org.nautilus.plugin.nrp.encoding.instance.MLBitFilpMutation;
import org.nautilus.plugin.nrp.encoding.instance.MLSinglePointCrossover;
import org.nautilus.plugin.nrp.encoding.instance.BinarySolutionSet;
import org.nautilus.plugin.nrp.encoding.problem.NRPProblem;
import org.nautilus.plugin.nrp.extension.problem.NRPProblemExtension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

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
            return solutionSet;
        }).setFirstInteraction(3).setIntervalInteraction(3).setMaxInteractions(3),
            new BinarySolutionSet()
        )
            .setSelectionOperator(selection)
            .setMaxEvaluations(10000)
            .build();

        new NSGAII<>(
            problem,
            20000, // max evaluations
            100, // population size
            100, // mating pool size
            100, // offspring size
            new SinglePointCrossover(0.9),
            new BitFlipMutation(0.01),
            new BinaryTournamentSelection<>(),
            new SequentialSolutionListEvaluator<>()
        );

        System.out.println("Optimizing...");

        new AlgorithmRunner.Executor(algorithm).execute();

        List<MLBinarySolution> population = algorithm.getResult();

        for (MLBinarySolution solution : population) {
            System.out.println(Arrays.toString(solution.getObjectives()));
        }

        System.out.println("Done");
    }
}
