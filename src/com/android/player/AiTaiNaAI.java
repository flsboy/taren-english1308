package com.android.player;

import com.android.base.BaseComputerAI;
import com.android.gobang.Point;


//AI-°¬Ì©ÄÉ
public class AiTaiNaAI  extends BaseComputerAI{

	protected Point getBestPoint(){
		
		Point mostBest = getBestPoint(computer3Alive_4Half, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human3Alive_4Half, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computerDouble3Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(humanDouble3Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer3Alive_2Alive, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human3Alive_2Alive, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer3Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human3Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human4HalfAlives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;

		mostBest = getBestPoint(computerDouble2Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(humanDouble2Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer2Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human2Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
				
		mostBest = getBestPoint(computer3HalfAlives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;				
		
		mostBest = getBestPoint(human3HalfAlives, computerSencodResults);
		return mostBest;
	}

}
