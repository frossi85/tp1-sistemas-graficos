package actionSquare;

abstract public class actionSquare {
	float x, y, xm, ym;
	
	public actionSquare(float x, float y, float xm, float ym){
		this.x = x;
		this.y = y;
		this.xm = xm;
		this.ym = ym;
	}
	
	public boolean estaDentro(float x, float y){
		return x > this.x && x < this.xm && y > this.y && y < this.ym;
	}
	
	abstract public void actuar();
	
}
