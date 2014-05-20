package com.android.gobang;

import java.util.HashMap;
import java.util.Map;

import com.android.interfaces.IPlayer;
import com.android.player.AiTaiNaAI;


//����AI������
public class AiFactory {
	
	private final static Map<Integer,IPlayer> ais = new HashMap<Integer, IPlayer>(2);
	
	//��������������Խ���Ѷ�Խ��
	public static IPlayer getInstance(int level){
		IPlayer ai = ais.get(level);
		if(ai==null){
			switch (level) {
			case 1:
				ais.put(level, new AiTaiNaAI());
				break;			
			}
		}
		return ais.get(level);
	}
	
}
