package com.android.interfaces;

import java.util.List;

import com.android.gobang.Point;

/**
 * �������̻����ķ������õ�������л�ȡ��Ϣ
 * @author Administrator
 *
 */
public interface IChessboard {
	
	//ȡ��������������
	public int getMaxX();
	
	//���������
	public int getMaxY();
	
	//ȡ�õ�ǰ���пհ׵㣬��Щ��ſ�������
	public List<Point> getFreePoints();
	
}
