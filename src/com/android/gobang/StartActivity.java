package com.android.gobang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class StartActivity extends Activity {
	
	public Chessboard chessboard;
	private static final String GAME_OVER = "com.ws.gobang.gameover";
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ws", "StartActivity-onCreate");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        chessboard=(Chessboard) this.findViewById(R.id.chessboard);
        
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        
        if (type.equals("pvp")){
        	chessboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.pvp_bg));
        }else {
        	chessboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.ai_bg));
        }
       
        chessboard.receiver(type,this);
        
    }
    
   //����Menu
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		super.onCreateOptionsMenu(menu);
  		menu.add(0, 1, 0, "���¿�ʼ"); 
  		return true;
  	}
  	
  	//Menu����¼�
  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  		
  		switch (item.getItemId()){
  			case 1:
  				chessboard.restart_Gobang();
  				break;
  			
  			default:
  				break;
  		}
  		return true;
  	}
  	
  	@Override
	protected void onStart() {
		super.onStart();
		
		//ע��㲥
		IntentFilter filter = new IntentFilter();
        filter.addAction(GAME_OVER);
		registerReceiver(changeItem, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//ע���㲥
		unregisterReceiver(changeItem);
		
	}
  	
    // ȷ�����˳����Ǽ���
 	@Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		
 		if (keyCode == KeyEvent.KEYCODE_BACK) {
 			
 			if( chessboard.player1.getMyPoints().size()>0){
 				
 				Builder dialog = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher);
 				dialog.setTitle("��ʾ��");
 				dialog.setMessage("��ȷ��Ҫ����������");
 				
 				dialog.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
 					
 					@Override
 					public void onClick(DialogInterface dialog, int which) { 					
 						finish();
 					}
 				}); 			
 				
 				dialog.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
 					
 					@Override
 					public void onClick(DialogInterface dialog, int which) {
 					}
 				});
 				
 				dialog.create();
 				dialog.show();
 			}

 		}
 		return super.onKeyDown(keyCode, event);
 	}
 	
 	//�㲥
 	private BroadcastReceiver changeItem = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		
			if (intent.getAction().equals(GAME_OVER)){				
				finish();				
			}
			
		}
 		
 	};
    
    
}