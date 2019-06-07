package problems.binPacking.solvers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayDeque;
import java.util.ArrayList;

import metaheuristics.tabusearch.AbstractTS;
import problems.binPacking.Alocation;
import problems.binPacking.BinPacking;
import solutions.Solution;

public class TS_BinPacking extends AbstractTS<Alocation> {
	
	private final Alocation fake = new Alocation(-1, -1);
	
	private Integer c;
	
	private Integer items[];
	
	private Integer bin[];
	
	public TS_BinPacking(Integer tenure, Integer iterations, Integer c, Integer size, Integer items[], Integer timeLimit) {
		super(new BinPacking(c, size, items), tenure, iterations, timeLimit);
		this.c = c;
		this.items = items;
		bin = new Integer[ObjFunction.getDomainSize()];
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			bin[i] = 0;
		}
	}

	@Override
	public ArrayList<Alocation> makeCL() {
		
		ArrayList<Alocation> _CL = new ArrayList<Alocation>();
		
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			for (int j = 0; j < ObjFunction.getDomainSize(); j++) {
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
			for (int j = 0; j < ObjFunction.getDomainSize(); j++) {
				Alocation aloc = new Alocation(items[i], j);
				if (c - bin[aloc.bin] >= aloc.item && bin[aloc.bin] > 0) _CL.add(aloc);		
			}
		}
		
		CL = _CL;
		
	}

	@Override
	public Solution<Alocation> createEmptySol() {

		Solution<Alocation> sol = new Solution<Alocation>();
		
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			sol.add(new Alocation(items[i], i));
			bin[i] = items[i];
		}
		
		return sol;
	}

	@Override
	public Solution<Alocation> neighborhoodMove() {

		Double minDeltaCost = Double.POSITIVE_INFINITY;
		Alocation bestCandIn = null, bestCandOut = null;
		
		updateCL();
		
		for (Alocation candIn : CL) {
			if (minDeltaCost <= -0.99999) {
				break;
			}
			for (Alocation candOut : incumbentSol) {
				if (!candIn.item.equals(candOut.item)) continue;
				if (candIn.bin.equals(candOut.bin)) continue;
				Double folga = (double) (c - bin[candOut.bin] + candOut.item);
				Double aux = (folga/c);
				Double deltaCost = -aux*aux;
				if (incumbentSol.cost+deltaCost <= bestSol.cost - 0.99999 || (!TL.contains(candIn) && !TL.contains(candOut))) {
					if (deltaCost < minDeltaCost) {
						minDeltaCost = deltaCost;
						bestCandIn = candIn;
						bestCandOut = candOut;
					}
				}
				if (minDeltaCost <= -0.99999) break;
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
		
//		String filename = args[0];
//		Integer timeLimit = Integer.parseInt(args[1]);
		
		String filename = "/home/pedro/git/MO824_Atividade7/bpp_instances/instance9.bpp";
		Integer timeLimit = 1000*60*10;

		Integer c = 0;
		
		Integer n = 0;
		Integer items[] = new Integer[0];
		
		try {
			Reader fileInst = new BufferedReader(new FileReader(filename));
			StreamTokenizer stok = new StreamTokenizer(fileInst);

			stok.nextToken();
			n = (int) stok.nval;
			items = new Integer[n];
			
			stok.nextToken();
			c = (int) stok.nval;

			for (int i = 0; i < n; i++) {
				stok.nextToken();
				items[i] = (int) stok.nval;
			}			
		}
		catch (IOException e) {
			System.out.println("Problema na leitura do arquivo. :(");
			System.exit(1);
		}
		
		long startTime = System.currentTimeMillis();
		TS_BinPacking tabusearch = new TS_BinPacking(20, 1000000, c, n, items, timeLimit);
		Solution<Alocation> bestSol = tabusearch.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = "+(double)totalTime/(double)1000+" seg");
	}

}
