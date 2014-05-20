package com.android.gobang;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.android.interfaces.IChessboard;
import com.android.interfaces.IPlayer;
import com.android.player.HumanPlayer;


public class Chessboard extends View implements IChessboard {

	//当前状态，默认为可开局状态
	private int currentMode = READY;
	//已准备好，可开局
    private static final int READY = 1;
    //已开局
    private static final int RUNNING = 2;
    //已结束
    private static final int PLAYER_TWO_LOST = 3;
    private static final int PLAYER_ONE_LOST = 4;
    
    //画笔对象
  	private final Paint paint = new Paint();
  	
  	//代表黑子
  	private static final int BLACK = 0;
  	private static final int NEW_BLACK= 1;
  	
  	//代表白子
  	private static final int WHITE = 2;	
  	private static final int NEW_WHITE = 3;
  	
  	//点大小
    private static int pointSize = 42;
  	
    //------------------------------------------------------------//
    
    //不同颜色的数组
  	private Bitmap[] pointArray = new Bitmap[4];
  	
  	//屏幕右下角的坐标值，即最大坐标值
    private static int maxX;
    private static int maxY;
      
    //第一点偏离左上角从像数，为了棋盘居中
  	private static int yOffset;
  	private static int xOffset;
  	
    //------------------------------------------------------------//
  	
  	//第一个玩家默认为人类玩家
  	public IPlayer player1 = new HumanPlayer();
  	//第二个则根据选择来初始化
  	private IPlayer player2;

  	//电脑玩家[玩家二 人工智能]
  	private  IPlayer computer = AiFactory.getInstance(1);
  	//人类玩家
  	private IPlayer human = new HumanPlayer();
  	
  	// 所有未下的空白点
  	private final List<Point> allFreePoints = new ArrayList<Point>();	
  	  
    //画棋盘
    private List<Line> lines = new ArrayList<Line>();	
    
    private static final String GAME_OVER = "com.ws.gobang.gameover";
    private Context context=null;
     
    //------------------------------------------------------------//
    
    //线类
    class Line{
    	float xStart,yStart,xStop,yStop;
    	
		public Line(float xStart, float yStart, float xStop, float yStop) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
    }
    
    //构造函数
   	public Chessboard(Context context, AttributeSet attrs) {
   		
		super(context, attrs);
		Log.d("ws", "Chessboard");
		setFocusable(true);//使View获取焦点
		
		//把四个颜色的点准备好，并放入数组
        Resources r = this.getContext().getResources();
        
        fillPointArrays(BLACK,r.getDrawable(R.drawable.black_point));
        fillPointArrays(NEW_BLACK,r.getDrawable(R.drawable.new_black_point));
        fillPointArrays(WHITE,r.getDrawable(R.drawable.white_point));
        fillPointArrays(NEW_WHITE,r.getDrawable(R.drawable.new_white_point));
	        
        //设置画线时用的颜色
        paint.setColor(Color.BLACK);
	}
   	
   	//自动匹配不同分辨率下的棋子大小
    public void autoChangeSize (int h){

		if (1800 < h && h < 2000) {// 1920
			pointSize = 95;
		} else if (1100 < h && h < 1800) {// 1280
			pointSize = 70;
		} else if (900 < h && h < 1100) {// 960
			pointSize = 55;
		} else if (700 < h && h < 900) {// 800
			pointSize = 45;
		} else {
			pointSize = 30;
		}
    }
   	
	
	//接收Activity传过来的数据
	public void receiver (String type ,Context context){
		Log.d("ws", "receiver");
		
		this.context =context;
		
		if(type.equals("pvp")) {
			player2 = human;
		}else {
			player2 = computer;
		}		
		
	}
	
	//重新开始
	public void restart_Gobang(){
		Log.d("ws", "restart_Gobang");
		
		player1.getMyPoints().clear();
		if (null !=player2) {
			player2.getMyPoints().clear();
		}
		
		restart();
      	setMode(RUNNING);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		 Log.d("ws", "onSizeChanged"+ "w="+w+"h= "+h);
		 
		 autoChangeSize(h);	//自动匹配不同分辨率下的棋子大小
		 
		 maxX = (int) Math.floor(w / pointSize);
	     maxY = (int) Math.floor(h / pointSize);

        //设置X、Y座标微调值，目的整个框居中
        xOffset = ((w - (pointSize * maxX)) / 2);
        yOffset = ((h - (pointSize * maxY)) / 2);
        //创建棋盘上的线条
        createChssboardLines();
        //初始化棋盘上所有空白点
        createPoints();
        
        restart_Gobang();
	}
	
	//初始化好四种颜色的点
    public void fillPointArrays(int color,Drawable drawable) {
    	
        Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, pointSize-5, pointSize-5);
        drawable.draw(canvas);
        
        pointArray[color] = bitmap;//放入Bitmap数组数组
    }
    
    //产生棋盘上所有的线
    private void createChssboardLines(){
    	
    	for (int i = 0; i <= maxX; i++) {//竖线
    		lines.add(new Line(xOffset+i*pointSize-pointSize/2, yOffset+pointSize-pointSize/2, 
    										  xOffset+i*pointSize-pointSize/2, yOffset+maxY*pointSize-pointSize/2));
		}
    	
    	for (int i = 0; i <= maxY; i++) {//横线
    		lines.add(new Line(xOffset+pointSize-pointSize/2, 		   yOffset+i*pointSize-pointSize/2, 
    										  xOffset+maxX*pointSize-pointSize/2, yOffset+i*pointSize-pointSize/2));
		}    	
    }
    
    //画棋盘上的线
    private void drawChessboardLines(Canvas canvas){
    	for (Line line : lines) {
    		canvas.drawLine(line.xStart, line.yStart, line.xStop, line.yStop, paint);
		}
    }
    
    //初始化空白点集合
	private void createPoints(){
		allFreePoints.clear();
		
		for (int i = 0; i < maxX; i++) {			
			for (int j = 0; j < maxY; j++) {				
				allFreePoints.add(new Point(i, j));
			}
		}
	}
	
	private void refressCanvas(){
		//触发onDraw函数
        Chessboard.this.invalidate();
	}
	
	//重新开始
	private void restart() {
		createPoints();
		player1.setChessboard(this);
		player2.setChessboard(this);
		setPlayer1Run();
		//刷新一下
		refressCanvas();
	}
	
	//默认第一个玩家先行
	private int whoRun = 1;
	
	private void setPlayer1Run(){
		whoRun = 1;
	}
	private void setOnProcessing(){
		whoRun = -1;
	}
	
	//是否轮到第一个玩家下棋
	private boolean isPlayer1Run(){
		return whoRun==1;
	}
	
	//是否轮到第二个玩家下棋
	private boolean isPlayer2Run(){
		return whoRun==2;
	}
	
	private void setPlayer2Run(){
		whoRun = 2;
	}
	
	//如果是AI下棋时需要较长的计算时间，这期间一定不可以再响应触摸事件
	private boolean onProcessing() {
		return whoRun == -1;
	}
	
	//设置运行状态
	public void setMode(int newMode) {
		
		currentMode = newMode;
		if(currentMode==PLAYER_TWO_LOST){
			//提示玩家2输了		
			currentMode = READY;
			
			createDialog("黑棋获胜");
			
		}else if(currentMode==RUNNING){
		
		}else if(currentMode==READY){
			
		}else if(currentMode==PLAYER_ONE_LOST){
			//提示玩家1输了		
			currentMode = READY;
			
			createDialog("白棋获胜");
		}
	}
	
	private void createDialog (String str){
		Builder dialog1 = new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher);
			dialog1.setTitle("提示：");
			dialog1.setMessage(str);
			
			dialog1.setPositiveButton("继续", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					restart_Gobang();
				}
			}); 			

			dialog1.setNegativeButton("退出", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(GAME_OVER);					
					context.sendBroadcast(i);
				}
			});

			dialog1.create();
			dialog1.show();
	}
	
	private void createDialog1 (){
		Builder dialog1 = new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher);
			dialog1.setTitle("提示：");
			dialog1.setMessage("女神认输了");
			
			dialog1.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					restart_Gobang();
				}
			});
			
			dialog1.create();
			dialog1.show();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//画棋盘上的线
		drawChessboardLines(canvas);
    	//画第一个玩家的棋
    	drawPlayer1Point(canvas);
    	//画第二个玩家的棋
    	drawPlayer2Point(canvas);
	}
	
	//画第一个玩家的棋
	private void drawPlayer1Point(Canvas canvas){
		int size = player1.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player1.getMyPoints().get(i), BLACK);
		}
		//最后下的一个点标成新黑色
		drawPoint(canvas, player1.getMyPoints().get(size), NEW_BLACK);
	}
	
	//画第二个玩家的棋
	private void drawPlayer2Point(Canvas canvas){
		if(player2==null){
			return ;
		}
		int size = player2.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player2.getMyPoints().get(i), WHITE);		
		}
	
		//最后下的一个点标成新白色
		drawPoint(canvas, player2.getMyPoints().get(size), NEW_WHITE);
	}
	
	 //画棋子
    private void drawPoint(Canvas canvas,Point p,int color){
		canvas.drawBitmap(pointArray[color], p.x * pointSize + xOffset, p.y * pointSize + yOffset, paint);
    }
	
    //是否已开局
	private boolean hasStart(){
		return currentMode==RUNNING;
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		//还没有开局，或者是按下事件则不处理，只处理开局后的触摸弹起事件
		if (!hasStart() || event.getAction() != MotionEvent.ACTION_UP) {
			return true;
		}
		
		//是否是AI正在下棋的过程中
		if(onProcessing()){
			return true;
		}
		
		playerRun(event);//进行下棋
		
		return true;
	}
	
	//进行下棋，此处需要加上synchronized
	private synchronized void playerRun(MotionEvent event){
		
		if (isPlayer1Run()) {
			player1Run(event);
			
		} else if (isPlayer2Run()) {
			player2Run(event);			
		}
	}
	
	// 第一玩家下棋
	private void player1Run(MotionEvent event) {
		Point point = newPoint(event.getX(), event.getY());
		
		if (allFreePoints.contains(point)) {// 此棋是否可下
			setOnProcessing();
			player1.run(player2.getMyPoints(), point);
			
			// 刷新一下棋盘
			refressCanvas();
			
			// 判断第一个玩家是否已经下了
			if (!player1.hasWin()) {// 我还没有赢
				if (player2 == computer) {
					// 如果第二玩家是电脑 10豪秒后才给玩家2下棋
					refreshHandler.computerRunAfter(10);
				} else {
					setPlayer2Run();
				}
			} else {
				// 否则，提示游戏结束
				setMode(PLAYER_TWO_LOST);
			}
		}
	}
		
	// 第二玩家下棋
	private void player2Run(MotionEvent event) {
		Point point = newPoint(event.getX(), event.getY());
		
		if (allFreePoints.contains(point)) {// 此棋是否可下
			setOnProcessing();
			player2.run(player1.getMyPoints(), point);
			
			// 刷新一下棋盘
			refressCanvas();
			
			// 判断我是否赢了
			if (!player2.hasWin()) {
				setPlayer1Run();
			} else {
				// 否则，提示游戏结束
				setMode(PLAYER_ONE_LOST);
			}
		}
	}
	
	//根据触摸点坐标找到对应点
	private Point newPoint(Float x, Float y){		
		Point p = new Point(0, 0);
		
		for (int i = 0; i < maxX; i++) {
			if ((i * pointSize + xOffset) <= x && x < ((i + 1) * pointSize + xOffset)) {
				p.setX(i);
			}
		}
		
		for (int i = 0; i < maxY; i++) {
			if ((i * pointSize + yOffset) <= y && y < ((i + 1) * pointSize + yOffset)) {
				p.setY(i);
			}
		}
		return p;
	}
	
	private RefreshHandler refreshHandler = new RefreshHandler();
	
	class RefreshHandler extends Handler {

		//这个方法主要在指定的时刻发一个消息
        public void computerRunAfter(long delayMillis) {
        	this.removeMessages(0);
        	//发消息触发handleMessage函数
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
        
        //收到消息
        @Override
        public void handleMessage(Message msg) {
        	//AI走一步棋子
    		player2.run(player1.getMyPoints(),null);
    		
    		//判断最后一个棋子返回的是否为NUll,如果是AI认输
			int size = player2.getMyPoints().size()-1;
			if ( null == player2.getMyPoints().get(size)){
				createDialog1();
				return;
			}
    		
    		//刷新一下
    		refressCanvas();
    		
    		if(!player2.hasWin()){
    			setPlayer1Run();//人下
    		}else{
    			setMode(PLAYER_ONE_LOST);//AI赢了
    		}
        }
    };

	@Override
	public int getMaxX() {
		
		return maxX;
	}

	@Override
	public int getMaxY() {
		
		return maxY;
	}

	@Override
	public List<Point> getFreePoints() {
		
		return allFreePoints;
	}

}
