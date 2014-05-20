package com.android.gobang;



import com.feiwo.appwall.AppWallManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GobangActivity extends Activity {
	
	private Button btn_ai = null;
	private Button btn_pvp = null;
	private Button btn_about = null;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        AppWallManager.init(getApplicationContext(), "5Ro3v1JlrG5K8v4U0ViiJ9t9");
        btn_ai=(Button) this.findViewById(R.id.btn_ai);
        btn_ai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(GobangActivity.this, StartActivity.class);
				intent.putExtra("type", "AI");
				startActivity(intent);
				
			}
		});
        
        btn_pvp=(Button) this.findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(GobangActivity.this, StartActivity.class);
				intent.putExtra("type", "pvp");
				startActivity(intent);
			}
		});
        
        btn_about=(Button) this.findViewById(R.id.btn_about);
        btn_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent=new Intent(GobangActivity.this, AboutActivity.class);			
				startActivity(intent);*/
				AppWallManager.showAppWall(GobangActivity.this);
			}
		});
        
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
    }
}