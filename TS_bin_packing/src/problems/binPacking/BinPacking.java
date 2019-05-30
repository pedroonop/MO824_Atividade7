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
		
		Double cont = 0.0;
		
		boolean usedBin[] = new boolean[size];
		
		for (int i = 0; i < size; i++) {
			usedBin[i] = false;
		}
		
		for (Alocation aloc : sol) {
			if (!usedBin[aloc.bin]) {
				cont++;
				usedBin[aloc.bin]= true;
			}
		}
		
		return sol.cost = cont;
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

		Double old = evaluate(sol);
		
		Double cont = 1.0;
		
		boolean usedBin[] = new boolean[size];
		
		for (int i = 0; i < size; i++) {
			usedBin[i] = false;
		}
		usedBin[elemIn.bin] = true;
		
		boolean flag = true;
		for (Alocation aloc : sol) {
			if (flag && aloc.equals(elemOut)) {
				flag = false;
				continue;
			}
			if (!usedBin[aloc.bin]) {
				cont++;
				usedBin[aloc.bin] = true;
			}
		}
		
		return cont - old;
	}

}
