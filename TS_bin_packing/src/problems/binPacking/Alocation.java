package problems.binPacking;

public class Alocation {

	public Integer item;
	
	public Integer bin;
	
	public Alocation(Integer item, Integer bin) {
		this.item = item;
		this.bin = bin;
	}
	
	public boolean equals(Alocation aloc) {
		return this.item.equals(aloc.item) && this.bin.equals(aloc.bin);
	}
	
	public String toString() {
		return "(" + item + "->" + bin + ")";
	}
	
}
