package problems.binPacking;

import problems.Evaluator;
import solutions.Solution;

public class BinPacking implements Evaluator<Alocation> {

	public final Integer c;
	
	public final Integer size;
	
	public final Integer items[];
	
	public int a = 0;
	
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
		
		Double bin[] = new Double[size];
		
		for (int i = 0; i < size; i++) {
			bin[i] = 0.0;
		}
		
		for (Alocation aloc : sol) {
			bin[aloc.bin] += aloc.item;
		}
	
		if (bin[elemIn.bin] <= 0.001) return Double.POSITIVE_INFINITY;
		
		bin[elemIn.bin] += elemIn.item;
		bin[elemOut.bin] -= elemOut.item;
		
		double folga = c - (bin[elemOut.bin]);
		
		return -(folga * folga) / (c * c);
	}
	
}
