package com.android.interfaces;

import java.util.List;

import com.android.gobang.Point;

/**
 * 定义棋盘基本的方法，让调用类从中获取信息
 * @author Administrator
 *
 */
public interface IChessboard {
	
	//取得棋盘最大横坐标
	public int getMaxX();
	
	//最大纵坐标
	public int getMaxY();
	
	//取得当前所有空白点，这些点才可以下棋
	public List<Point> getFreePoints();
	
}
