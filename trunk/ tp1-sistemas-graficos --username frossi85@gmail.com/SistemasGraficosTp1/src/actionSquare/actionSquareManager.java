package actionSquare;
import java.util.ArrayList;


public class actionSquareManager {
	private ArrayList<actionSquare> actionSquareList;
	
	public actionSquareManager(){
		this.actionSquareList = new ArrayList<actionSquare>();
	}

	public void add(actionSquare aS){
		this.actionSquareList.add(aS);
	}
	
	public void trata(float x, float y){
		for(actionSquare aS : this.actionSquareList){
			if(aS.estaDentro(x,y)){
				aS.actuar();
			}
		}
	}
}
