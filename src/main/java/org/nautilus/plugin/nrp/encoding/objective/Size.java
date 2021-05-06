package org.nautilus.plugin.nrp.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.uma.jmetal.solution.Solution;

public class Size extends AbstractObjective {
	
	protected int requirements;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.requirements = 0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
	    requirements++;
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		return (double) requirements / (double) instance.getNumberOfRequirements();
	}
	
	public boolean isMaximize() {
		return false;
	}
	
	@Override
	public String getName() {
		return "Size";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
