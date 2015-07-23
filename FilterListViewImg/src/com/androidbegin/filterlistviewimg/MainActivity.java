package com.androidbegin.filterlistviewimg;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	private Button btn_cliente;
	Context mContext;
	
	@Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        btn_cliente = (Button) findViewById(R.id.cliente);
       
        
        btn_cliente.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(mContext, Clientes_Main.class);
        		
        		startActivity(intent);
        	}
        });
       
    }    
}