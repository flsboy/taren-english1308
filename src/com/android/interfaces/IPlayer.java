package com.android.interfaces;

import java.util.List;

import com.android.gobang.Point;

/**
 * ���������ߵĹ����ӿڣ����߱��Ļ�������
 * @author Administrator
 *
 */
public interface IPlayer {
	
	//�����ߵĶ������������ֻ��Ҫ�ڶ��������������AI��Ҫ֪�������µ�����
	public void run(List<Point> enemyPoints,Point point);
	
	//�ж��Լ��Ƿ�Ӯ��
	public boolean hasWin();
	
	//��ȡ���̵���Ϣ
	public void setChessboard( IChessboard chessboard);
	
	//�鿴�Լ����µ�����
	public List<Point> getMyPoints();

}
