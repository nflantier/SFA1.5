package noelflantier.sfartifacts.common.helpers;

public class Coord4 {
	
	public int x;
	public int y;
	public int z;
	public int dimension;
	
	public Coord4 (int x, int y, int z, int dimension){
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}
	public Coord4 (int[] c){
		this.x = c[0];
		this.y = c[1];
		this.z = c[2];
	}
	public Coord4 (int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
