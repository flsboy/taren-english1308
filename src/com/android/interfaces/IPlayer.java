package com.android.interfaces;

import java.util.List;

import com.android.gobang.Point;

/**
 * 参与下棋者的公共接口，所具备的基本功能
 * @author Administrator
 *
 */
public interface IPlayer {
	
	//下棋者的动作，如果是人只需要第二个参数，如果是AI需要知道对手下的棋子
	public void run(List<Point> enemyPoints,Point point);
	
	//判断自己是否赢了
	public boolean hasWin();
	
	//获取棋盘的信息
	public void setChessboard( IChessboard chessboard);
	
	//查看自己所下的棋子
	public List<Point> getMyPoints();

}
