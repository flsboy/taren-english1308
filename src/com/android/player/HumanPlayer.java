package com.android.player;

import java.util.List;

import com.android.base.BasePlayer;
import com.android.gobang.Point;
import com.android.interfaces.IPlayer;

/**
 * 人类玩家
 * @author Administrator
 *
 */
public class HumanPlayer extends BasePlayer implements IPlayer{

	@Override
	public void run(List<Point> enemyPoints,Point p) {
		
		getMyPoints().add(p);//添加我下的棋子
		allFreePoints.remove(p);
		
	}
}
