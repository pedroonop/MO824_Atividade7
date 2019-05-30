package problems.binPacking.solvers;

import java.util.ArrayDeque;
import java.util.ArrayList;

import metaheuristics.tabusearch.AbstractTS;
import problems.binPacking.Alocation;
import problems.binPacking.BinPacking;
import solutions.Solution;

public class TS_BinPacking extends AbstractTS<Alocation> {
	
	private final Double EPS = 0.01;
	
	private final Alocation fake = new Alocation(-1, -1);
	
	private Integer c;
	
	private Integer items[];
	
	private Integer bin[];

	public TS_BinPacking(Integer tenure, Integer iterations, Integer c, Integer size, Integer items[]) {
		super(new BinPacking(c, size, items), tenure, iterations);
		this.c = c;
		this.items = items;
		bin = new Integer[ObjFunction.getDomainSize() + 1];
		for (int i = 1; i <= ObjFunction.getDomainSize(); i++) {
			bin[i] = 0;
		}
	}

	@Override
	public ArrayList<Alocation> makeCL() {
		
		ArrayList<Alocation> _CL = new ArrayList<Alocation>();
		
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			for (int j = 1; j <= ObjFunction.getDomainSize(); j++) {
				_CL.add(new Alocation(items[i], j));
			}
		}
		
		return _CL;
	}

	@Override
	public ArrayList<Alocation> makeRCL() {
		return new ArrayList<Alocation>();
	}

	@Override
	public ArrayDeque<Alocation> makeTL() {
		ArrayDeque<Alocation> _TS = new ArrayDeque<Alocation>(2*tenure);
		for (int i=0; i<2*tenure; i++) {
			_TS.add(fake);
		}

		return _TS;
	}

	@Override
	public void updateCL() {
		
		ArrayList<Alocation> _CL = new ArrayList<Alocation>();
		
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			for (int j = 1; j <= ObjFunction.getDomainSize(); j++) {
				Alocation aloc = new Alocation(items[i], j);
				if (c - bin[aloc.bin] >= aloc.item && !this.incumbentSol.contains(aloc)) _CL.add(aloc);				
			}
		}
		
		CL = _CL;
		
	}

	@Override
	public Solution<Alocation> createEmptySol() {

		Solution<Alocation> sol = new Solution<Alocation>();
		
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			sol.add(new Alocation(items[i], i + 1));
			bin[i + 1] = items[i];
		}
		
		return sol;
	}

	@Override
	public Solution<Alocation> neighborhoodMove() {

		Double minDeltaCost = Double.POSITIVE_INFINITY;
		Alocation bestCandIn = null, bestCandOut = null;
		
		updateCL();
		
		
		
		for (Alocation candIn : CL) {
			for (Alocation candOut : incumbentSol) {
				if (!candIn.item.equals(candOut.item)) continue;
				Double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, incumbentSol);
				if ((!TL.contains(candIn) && !TL.contains(candOut)) || incumbentSol.cost+deltaCost < bestSol.cost) {
					if (deltaCost < minDeltaCost) {
						minDeltaCost = deltaCost;
						bestCandIn = candIn;
						bestCandOut = candOut;
					}
				}
			}
		}
		
		TL.poll();
		TL.poll();
		if (bestCandIn != null) {
			
			bin[bestCandOut.bin] -= bestCandOut.item;
			incumbentSol.remove(bestCandOut);
			CL.add(bestCandOut);
			TL.add(bestCandOut);
			
			bin[bestCandIn.bin] += bestCandIn.item;
			incumbentSol.add(bestCandIn);
			CL.remove(bestCandIn);
			TL.add(bestCandIn);
		}
		else {
			TL.add(fake);
			TL.add(fake);
		}
		
		ObjFunction.evaluate(incumbentSol);
		
		int i = 1;
		while (i < incumbentSol.cost) {
			if (bin[i] == 0) {
				for (Alocation aloc : incumbentSol) {
					if (Math.abs(aloc.bin - incumbentSol.cost) < EPS) aloc.bin = i;
				}
				bin[i] = bin[(int)(incumbentSol.cost + EPS)];
				bin[(int)(incumbentSol.cost + EPS)] = 0;
				incumbentSol.cost -= 1;
			}
			i++;
		}

		return null;
	}
	
	@Override
	public Solution<Alocation> constructiveHeuristic() {
		
		CL = makeCL();
		incumbentSol = createEmptySol();
		ObjFunction.evaluate(incumbentSol);
		incumbentCost = incumbentSol.cost;
		
		return incumbentSol;
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
//		Integer items[] = {9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1};
		Integer items[] = {9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 5, 4, 3, 2, 1};
//		Integer items[] = {9, 8, 7, 6, 5, 5, 4, 3, 2, 1};
		TS_BinPacking tabusearch = new TS_BinPacking(20, 1000, 2500, 50, items);
		Solution<Alocation> bestSol = tabusearch.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = "+(double)totalTime/(double)1000+" seg");
	}

}
