package org.nautilus.plugin.nrp.encoding.instance;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.util.InstanceReader;
import org.nautilus.plugin.nrp.encoding.model.Requirement;
import org.nautilus.plugin.nrp.encoding.model.Task;

public class TXTInstance extends Instance {

    protected int sumOfSolution;

    protected double sumOfCosts;

    protected double sumOfProfits;

    protected double sumOfImportances;

    protected double sumOfItem;

    protected List<Double> solutionCost;

    protected List<Double> solutionProfit;

    protected List<Double> solutionImportance;

    protected List<Integer> numberOfItem;

    protected List<Requirement> solutions;

    public TXTInstance(Path path) {

        this.solutions = new ArrayList<>();
        this.solutionCost = new ArrayList<>();
        this.solutionProfit = new ArrayList<>();
        this.solutionImportance = new ArrayList<>();

        InstanceReader reader = new InstanceReader(path, " ");

        reader.ignoreLine();
        this.sumOfSolution = reader.readIntegerValue();

        reader.ignoreLine();
        this.numberOfItem = reader.readIntegerValues();

        for (int i = 0; i < sumOfSolution; i++) {

            reader.ignoreLine();

            List<Task> items = new ArrayList<>();

            for (int j = 0; j < numberOfItem.get(i); j++) {

                List<Double> values = reader.readDoubleValues();

                Task task = new Task(
                    values.get(0),
                    values.get(1),
                    values.get(2)
                );

                items.add(task);
            }

            this.solutions.add(new Requirement(items));
        }

        for (Requirement requirement : solutions) {
            this.solutionCost.add(requirement.getCost());
            this.solutionProfit.add(requirement.getProfit());
            this.solutionImportance.add(requirement.getImportance());
        }

        this.sumOfCosts = this.solutionCost.stream().mapToDouble(e -> e).sum();
        this.sumOfProfits = this.solutionProfit.stream().mapToDouble(e -> e).sum();
        this.sumOfImportances = this.solutionImportance.stream().mapToDouble(e -> e).sum();
        this.sumOfItem = this.numberOfItem.stream().mapToDouble(e -> e).sum();
    }

    public int getSumOfSolution() {
        return sumOfSolution;
    }

    public double getSumOfCosts() {
        return this.sumOfCosts;
    }

    public double getSumOfProfits() {
        return this.sumOfProfits;
    }

    public double getSumOfImportances() {
        return this.sumOfImportances;
    }

    public double getSumOfItem() {
        return this.sumOfItem;
    }

    public double getCost(int requirementId) {
        return this.solutionCost.get(requirementId);
    }

    public double getProfit(int requirementId) {
        return this.solutionProfit.get(requirementId);
    }

    public double getImportance(int requirementId) {
        return this.solutionImportance.get(requirementId);
    }

    public List<Task> getTasks(int requirementId) {
        return this.solutions.get(requirementId).items;
    }

    public Requirement getSolution(int index) {
        return this.solutions.get(index);
    }

    @Override
    public List<Tab> getTabs(Instance data) {

        TXTInstance c = (TXTInstance) data;

        List<Tab> tabs = new ArrayList<>();

        tabs.add(getRequirementsTab(c));

        return tabs;
    }

    protected Tab getRequirementsTab(TXTInstance data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Cost", "Profit", "Importance"));

        for (int i = 0; i < data.getSumOfSolution(); i++) {
            table.getRows().add(Arrays.asList(
                "" + data.getCost(i),
                "" + data.getProfit(i),
                "" + data.getImportance(i)
            ));
        }

        return new Tab("Requirements", table);
    }
}
