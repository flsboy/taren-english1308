package com.android.player;

import java.util.List;

import com.android.base.BasePlayer;
import com.android.gobang.Point;
import com.android.interfaces.IPlayer;

/**
 * �������
 * @author Administrator
 *
 */
public class HumanPlayer extends BasePlayer implements IPlayer{

	@Override
	public void run(List<Point> enemyPoints,Point p) {
		
		getMyPoints().add(p);//������µ�����
		allFreePoints.remove(p);
		
	}
}
