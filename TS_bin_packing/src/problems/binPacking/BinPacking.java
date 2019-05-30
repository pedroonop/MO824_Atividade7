package problems.binPacking;

import problems.Evaluator;
import solutions.Solution;

public class BinPacking implements Evaluator<Alocation> {

	public final Integer c;
	
	public final Integer size;
	
	public final Integer items[];
	
	public BinPacking(Integer c, Integer size, Integer items[]) {
		this.c = c;
		this.size = size;
		this.items = items;
	}
	
	@Override
	public Integer getDomainSize() {
		return size;
	}

	@Override
	public Double evaluate(Solution<Alocation> sol) {
		
		Double maximum = Double.NEGATIVE_INFINITY;
		
		for (Alocation aloc : sol) {
			maximum = Math.max(maximum, aloc.bin);
		}
		
		return sol.cost = maximum;
	}

	@Override
	public Double evaluateInsertionCost(Alocation elem, Solution<Alocation> sol) {
		return null;
	}

	@Override
	public Double evaluateRemovalCost(Alocation elem, Solution<Alocation> sol) {
		return null;
	}

	@Override
	public Double evaluateExchangeCost(Alocation elemIn, Alocation elemOut, Solution<Alocation> sol) {
		Double maximum = evaluate(sol);
		Double newMaximum = 0.0 + elemIn.bin;
		boolean flag = false;
		for (Alocation aloc : sol) {
			if (flag || elemOut.equals(aloc)) maximum = Math.max(newMaximum, aloc.bin);
			else flag = true;
		}
		
		return maximum - newMaximum;
	}

}
