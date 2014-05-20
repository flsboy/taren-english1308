package com.android.base;

import java.util.ArrayList;
import java.util.List;

import com.android.gobang.Point;
import com.android.interfaces.IChessboard;
import com.android.interfaces.IPlayer;

public abstract class BasePlayer implements IPlayer {
	
	// �������µ�����
	public List<Point> points = new ArrayList<Point>(500);
		
	// ��������������ݱ꣬
	protected int maxX;
	protected int maxY;

	// ���пհ�����
	protected List<Point> allFreePoints;
	
	// ��ʱװ���ӵ�λ��
	private final Point temp = new Point(0, 0);

	@Override
	public List<Point> getMyPoints() {
		return points;
	}

	@Override
	public void setChessboard(IChessboard chessboard) {
		this.maxX = chessboard.getMaxX();
		this.maxY = chessboard.getMaxY();
		this.allFreePoints = chessboard.getFreePoints();

	}
	
	@Override
	public boolean hasWin() {
		
		if(points.size()<5){
			return false;
		}
				
		Point point=points.get(points.size()-1);//�õ���ǰ�µ�����λ��
		int count=1;
		int x=point.getX(),y=point.getY();
		
		//����
		temp.setX(x).setY(y);
		while ( points.contains(temp.setX(temp.getX()-1 )) && temp.getX()>=0 &&count<5 ) {
			count++;
		}
		if (count >=5) {
			return true;
		}
		temp.setX(x).setY(y);
		while ( points.contains(temp.setX(temp.getX()+1)) && temp.getX()<maxX  &&count<5 ) {
			count++;
		}
		if (count >=5) {
			return true;
		}
	
		//����
		count = 1;
		temp.setX(x).setY(y);
		while (points.contains(temp.setY(temp.getY()-1)) && temp.getY()>=0  &&count<5 ) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (points.contains(temp.setY(temp.getY()+1)) && temp.getY()<maxY && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		
		//��б�� /
		count =1;
		temp.setX(x).setY(y);
		while (points.contains(temp.setX(temp.getX()-1).setY(temp.getY()+1)) && temp.getX()>=0 && temp.getY()<maxY  && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (points.contains(temp.setX(temp.getX()+1).setY(temp.getY()-1)) && temp.getX()<maxX && temp.getY()>=0 && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}		
		
		//��б�� \
		count = 1;
		temp.setX(x).setY(y);
		while (points.contains(temp.setX(temp.getX()-1).setY(temp.getY()-1)) && temp.getX()>=0 && temp.getY()>=0 && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (points.contains(temp.setX(temp.getX()+1).setY(temp.getY()+1)) && temp.getX()<maxX && temp.getY()<maxY && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		return false;
		
	}
}
