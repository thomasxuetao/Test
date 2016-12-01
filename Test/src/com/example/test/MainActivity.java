package com.example.test;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button actButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","onCreate");
        actButton = (Button)findViewById(R.id.button1);
        actButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//1.拨打电话
				// 给移动客服10086拨打电话
				Uri uri = Uri.parse("tel:10086");
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
				
			}
		});
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}).start();
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
