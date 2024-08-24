package org.nautilus.plugin.nrp.encoding.model;

import org.nautilus.core.util.Converter;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    protected static JMetalRandom random = JMetalRandom.getInstance();

    public List<Task> items;

    public Requirement(List<Task> tasks) {
        this.items = tasks;
    }

    public static Requirement getRandom() {

        int numberOfTasks = JMetalRandom.getInstance().nextInt(1, 5);

        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(Task.getRandom());
        }

        return new Requirement(tasks);
    }

    public double getCost() {

        double sum = 0.0;

        for (Task task : items) {
            sum += task.cost;
        }

        return sum;
    }

    public double getProfit() {

        double sum = 0.0;

        for (Task task : items) {
            sum += task.profit;
        }

        return sum;
    }

    public double getImportance() {

        double sum = 0.0;

        for (Task task : items) {
            sum += task.importance;
        }

        return sum;
    }

    public String toString() {
        return Converter.toJson(this);
    }
}
