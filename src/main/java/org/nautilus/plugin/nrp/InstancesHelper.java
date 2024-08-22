package org.nautilus.plugin.nrp;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

import java.util.ArrayList;

public class InstancesHelper {

    public static Instances createTrainingDataStructure(int numberOfRequirements) {
        ArrayList<Attribute> attributes = new ArrayList<>();

        // Add binary attributes for each requirement
        for (int i = 0; i < numberOfRequirements; i++) {
            attributes.add(new Attribute("Requirement_" + i));
        }

        // Add class attribute (subjective human evaluation)
        attributes.add(new Attribute("HumanEvaluation"));

        // Create Instances object with the defined attributes
        Instances trainingData = new Instances("TrainingData", attributes, 0);

        // Set the class index (last attribute)
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        return trainingData;
    }
}
