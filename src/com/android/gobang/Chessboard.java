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

	//��ǰ״̬��Ĭ��Ϊ�ɿ���״̬
	private int currentMode = READY;
	//��׼���ã��ɿ���
    private static final int READY = 1;
    //�ѿ���
    private static final int RUNNING = 2;
    //�ѽ���
    private static final int PLAYER_TWO_LOST = 3;
    private static final int PLAYER_ONE_LOST = 4;
    
    //���ʶ���
  	private final Paint paint = new Paint();
  	
  	//�������
  	private static final int BLACK = 0;
  	private static final int NEW_BLACK= 1;
  	
  	//�������
  	private static final int WHITE = 2;	
  	private static final int NEW_WHITE = 3;
  	
  	//���С
    private static int pointSize = 42;
  	
    //------------------------------------------------------------//
    
    //��ͬ��ɫ������
  	private Bitmap[] pointArray = new Bitmap[4];
  	
  	//��Ļ���½ǵ�����ֵ�����������ֵ
    private static int maxX;
    private static int maxY;
      
    //��һ��ƫ�����ϽǴ�������Ϊ�����̾���
  	private static int yOffset;
  	private static int xOffset;
  	
    //------------------------------------------------------------//
  	
  	//��һ�����Ĭ��Ϊ�������
  	public IPlayer player1 = new HumanPlayer();
  	//�ڶ��������ѡ������ʼ��
  	private IPlayer player2;

  	//�������[��Ҷ� �˹�����]
  	private  IPlayer computer = AiFactory.getInstance(1);
  	//�������
  	private IPlayer human = new HumanPlayer();
  	
  	// ����δ�µĿհ׵�
  	private final List<Point> allFreePoints = new ArrayList<Point>();	
  	  
    //������
    private List<Line> lines = new ArrayList<Line>();	
    
    private static final String GAME_OVER = "com.ws.gobang.gameover";
    private Context context=null;
     
    //------------------------------------------------------------//
    
    //����
    class Line{
    	float xStart,yStart,xStop,yStop;
    	
		public Line(float xStart, float yStart, float xStop, float yStop) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
    }
    
    //���캯��
   	public Chessboard(Context context, AttributeSet attrs) {
   		
		super(context, attrs);
		Log.d("ws", "Chessboard");
		setFocusable(true);//ʹView��ȡ����
		
		//���ĸ���ɫ�ĵ�׼���ã�����������
        Resources r = this.getContext().getResources();
        
        fillPointArrays(BLACK,r.getDrawable(R.drawable.black_point));
        fillPointArrays(NEW_BLACK,r.getDrawable(R.drawable.new_black_point));
        fillPointArrays(WHITE,r.getDrawable(R.drawable.white_point));
        fillPointArrays(NEW_WHITE,r.getDrawable(R.drawable.new_white_point));
	        
        //���û���ʱ�õ���ɫ
        paint.setColor(Color.BLACK);
	}
   	
   	//�Զ�ƥ�䲻ͬ�ֱ����µ����Ӵ�С
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
   	
	
	//����Activity������������
	public void receiver (String type ,Context context){
		Log.d("ws", "receiver");
		
		this.context =context;
		
		if(type.equals("pvp")) {
			player2 = human;
		}else {
			player2 = computer;
		}		
		
	}
	
	//���¿�ʼ
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
		 
		 autoChangeSize(h);	//�Զ�ƥ�䲻ͬ�ֱ����µ����Ӵ�С
		 
		 maxX = (int) Math.floor(w / pointSize);
	     maxY = (int) Math.floor(h / pointSize);

        //����X��Y����΢��ֵ��Ŀ�����������
        xOffset = ((w - (pointSize * maxX)) / 2);
        yOffset = ((h - (pointSize * maxY)) / 2);
        //���������ϵ�����
        createChssboardLines();
        //��ʼ�����������пհ׵�
        createPoints();
        
        restart_Gobang();
	}
	
	//��ʼ����������ɫ�ĵ�
    public void fillPointArrays(int color,Drawable drawable) {
    	
        Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, pointSize-5, pointSize-5);
        drawable.draw(canvas);
        
        pointArray[color] = bitmap;//����Bitmap��������
    }
    
    //�������������е���
    private void createChssboardLines(){
    	
    	for (int i = 0; i <= maxX; i++) {//����
    		lines.add(new Line(xOffset+i*pointSize-pointSize/2, yOffset+pointSize-pointSize/2, 
    										  xOffset+i*pointSize-pointSize/2, yOffset+maxY*pointSize-pointSize/2));
		}
    	
    	for (int i = 0; i <= maxY; i++) {//����
    		lines.add(new Line(xOffset+pointSize-pointSize/2, 		   yOffset+i*pointSize-pointSize/2, 
    										  xOffset+maxX*pointSize-pointSize/2, yOffset+i*pointSize-pointSize/2));
		}    	
    }
    
    //�������ϵ���
    private void drawChessboardLines(Canvas canvas){
    	for (Line line : lines) {
    		canvas.drawLine(line.xStart, line.yStart, line.xStop, line.yStop, paint);
		}
    }
    
    //��ʼ���հ׵㼯��
	private void createPoints(){
		allFreePoints.clear();
		
		for (int i = 0; i < maxX; i++) {			
			for (int j = 0; j < maxY; j++) {				
				allFreePoints.add(new Point(i, j));
			}
		}
	}
	
	private void refressCanvas(){
		//����onDraw����
        Chessboard.this.invalidate();
	}
	
	//���¿�ʼ
	private void restart() {
		createPoints();
		player1.setChessboard(this);
		player2.setChessboard(this);
		setPlayer1Run();
		//ˢ��һ��
		refressCanvas();
	}
	
	//Ĭ�ϵ�һ���������
	private int whoRun = 1;
	
	private void setPlayer1Run(){
		whoRun = 1;
	}
	private void setOnProcessing(){
		whoRun = -1;
	}
	
	//�Ƿ��ֵ���һ���������
	private boolean isPlayer1Run(){
		return whoRun==1;
	}
	
	//�Ƿ��ֵ��ڶ����������
	private boolean isPlayer2Run(){
		return whoRun==2;
	}
	
	private void setPlayer2Run(){
		whoRun = 2;
	}
	
	//�����AI����ʱ��Ҫ�ϳ��ļ���ʱ�䣬���ڼ�һ������������Ӧ�����¼�
	private boolean onProcessing() {
		return whoRun == -1;
	}
	
	//��������״̬
	public void setMode(int newMode) {
		
		currentMode = newMode;
		if(currentMode==PLAYER_TWO_LOST){
			//��ʾ���2����		
			currentMode = READY;
			
			createDialog("�����ʤ");
			
		}else if(currentMode==RUNNING){
		
		}else if(currentMode==READY){
			
		}else if(currentMode==PLAYER_ONE_LOST){
			//��ʾ���1����		
			currentMode = READY;
			
			createDialog("�����ʤ");
		}
	}
	
	private void createDialog (String str){
		Builder dialog1 = new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher);
			dialog1.setTitle("��ʾ��");
			dialog1.setMessage(str);
			
			dialog1.setPositiveButton("����", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					restart_Gobang();
				}
			}); 			

			dialog1.setNegativeButton("�˳�", new android.content.DialogInterface.OnClickListener() {

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
			dialog1.setTitle("��ʾ��");
			dialog1.setMessage("Ů��������");
			
			dialog1.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
				
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
		//�������ϵ���
		drawChessboardLines(canvas);
    	//����һ����ҵ���
    	drawPlayer1Point(canvas);
    	//���ڶ�����ҵ���
    	drawPlayer2Point(canvas);
	}
	
	//����һ����ҵ���
	private void drawPlayer1Point(Canvas canvas){
		int size = player1.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player1.getMyPoints().get(i), BLACK);
		}
		//����µ�һ�������º�ɫ
		drawPoint(canvas, player1.getMyPoints().get(size), NEW_BLACK);
	}
	
	//���ڶ�����ҵ���
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
	
		//����µ�һ�������°�ɫ
		drawPoint(canvas, player2.getMyPoints().get(size), NEW_WHITE);
	}
	
	 //������
    private void drawPoint(Canvas canvas,Point p,int color){
		canvas.drawBitmap(pointArray[color], p.x * pointSize + xOffset, p.y * pointSize + yOffset, paint);
    }
	
    //�Ƿ��ѿ���
	private boolean hasStart(){
		return currentMode==RUNNING;
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		//��û�п��֣������ǰ����¼��򲻴���ֻ�����ֺ�Ĵ��������¼�
		if (!hasStart() || event.getAction() != MotionEvent.ACTION_UP) {
			return true;
		}
		
		//�Ƿ���AI��������Ĺ�����
		if(onProcessing()){
			return true;
		}
		
		playerRun(event);//��������
		
		return true;
	}
	
	//�������壬�˴���Ҫ����synchronized
	private synchronized void playerRun(MotionEvent event){
		
		if (isPlayer1Run()) {
			player1Run(event);
			
		} else if (isPlayer2Run()) {
			player2Run(event);			
		}
	}
	
	// ��һ�������
	private void player1Run(MotionEvent event) {
		Point point = newPoint(event.getX(), event.getY());
		
		if (allFreePoints.contains(point)) {// �����Ƿ����
			setOnProcessing();
			player1.run(player2.getMyPoints(), point);
			
			// ˢ��һ������
			refressCanvas();
			
			// �жϵ�һ������Ƿ��Ѿ�����
			if (!player1.hasWin()) {// �һ�û��Ӯ
				if (player2 == computer) {
					// ����ڶ�����ǵ��� 10�����Ÿ����2����
					refreshHandler.computerRunAfter(10);
				} else {
					setPlayer2Run();
				}
			} else {
				// ������ʾ��Ϸ����
				setMode(PLAYER_TWO_LOST);
			}
		}
	}
		
	// �ڶ��������
	private void player2Run(MotionEvent event) {
		Point point = newPoint(event.getX(), event.getY());
		
		if (allFreePoints.contains(point)) {// �����Ƿ����
			setOnProcessing();
			player2.run(player1.getMyPoints(), point);
			
			// ˢ��һ������
			refressCanvas();
			
			// �ж����Ƿ�Ӯ��
			if (!player2.hasWin()) {
				setPlayer1Run();
			} else {
				// ������ʾ��Ϸ����
				setMode(PLAYER_ONE_LOST);
			}
		}
	}
	
	//���ݴ����������ҵ���Ӧ��
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

		//���������Ҫ��ָ����ʱ�̷�һ����Ϣ
        public void computerRunAfter(long delayMillis) {
        	this.removeMessages(0);
        	//����Ϣ����handleMessage����
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
        
        //�յ���Ϣ
        @Override
        public void handleMessage(Message msg) {
        	//AI��һ������
    		player2.run(player1.getMyPoints(),null);
    		
    		//�ж����һ�����ӷ��ص��Ƿ�ΪNUll,�����AI����
			int size = player2.getMyPoints().size()-1;
			if ( null == player2.getMyPoints().get(size)){
				createDialog1();
				return;
			}
    		
    		//ˢ��һ��
    		refressCanvas();
    		
    		if(!player2.hasWin()){
    			setPlayer1Run();//����
    		}else{
    			setMode(PLAYER_ONE_LOST);//AIӮ��
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
