package org.nautilus.plugin.nrp;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InteractiveNSGAII<S extends Solution<?>> extends NSGAII<S> {
    private InteractiveGeneticAlgorithm iga;

    public InteractiveNSGAII(Problem<S> problem, int maxEvaluations, int populationSize, int matingPoolSize, int offspringPopulationSize, CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator, SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator, InteractiveGeneticAlgorithm iga) {
        super(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize, crossoverOperator, mutationOperator, selectionOperator, evaluator);
        this.iga = iga;
    }

    @Override
    protected void initProgress() {
        this.evaluations = this.getMaxPopulationSize();
    }

    @Override
    protected void updateProgress() {
        this.evaluations += this.offspringPopulationSize;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return this.evaluations >= this.maxEvaluations;
    }

    @Override
    protected List<S> evaluatePopulation(List<S> population) {
        population = this.evaluator.evaluate(population, this.getProblem());

        for (S solution : population) {
            try {
                iga.interactWithSimulator((BinarySolution) solution);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return population;
    }

    @Override
    protected List<S> selection(List<S> population) {
        List<S> matingPopulation = new ArrayList(population.size());

        for (int i = 0; i < this.matingPoolSize; ++i) {
            S solution = (S) this.selectionOperator.execute(population);
            matingPopulation.add(solution);
        }

        return matingPopulation;
    }

    @Override
    protected List<S> reproduction(List<S> matingPool) {
        int numberOfParents = this.crossoverOperator.getNumberOfRequiredParents();
        this.checkNumberOfParents(matingPool, numberOfParents);
        List<S> offspringPopulation = new ArrayList(this.offspringPopulationSize);

        for (int i = 0; i < matingPool.size(); i += numberOfParents) {
            List<S> parents = new ArrayList(numberOfParents);

            for (int j = 0; j < numberOfParents; ++j) {
                parents.add(this.population.get(i + j));
            }

            List<S> offspring = (List) this.crossoverOperator.execute(parents);
            Iterator var7 = offspring.iterator();

            while (var7.hasNext()) {
                S s = (S) var7.next();
                this.mutationOperator.execute(s);
                offspringPopulation.add(s);
                if (offspringPopulation.size() >= this.offspringPopulationSize) {
                    break;
                }
            }
        }

        return offspringPopulation;
    }

    @Override
    protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
        List<S> jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);
        RankingAndCrowdingSelection<S> rankingAndCrowdingSelection = new RankingAndCrowdingSelection(this.getMaxPopulationSize(), this.dominanceComparator);
        return rankingAndCrowdingSelection.execute(jointPopulation);
    }

    @Override
    public List<S> getResult() {
        return SolutionListUtils.getNondominatedSolutions(this.getPopulation());
    }

    @Override
    public String getName() {
        return "InteractiveNSGAII";
    }

    @Override
    public String getDescription() {
        return "Nondominated Sorting Genetic Algorithm version II with Human Interaction";
    }
}

