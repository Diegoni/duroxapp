package com.durox.app;


import com.example.durox_app.R;
import com.durox.app.Clientes.Clientes_Main;
import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Productos.Productos_Main;
import com.durox.app.Visitas.Visitas_Main;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
 
public class MenuActivity extends Activity {
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.item1:
        	Toast.makeText(this, "Clientes", Toast.LENGTH_SHORT).show();
        	Intent intentClientes = new Intent(this, Clientes_Main.class);
    		startActivity(intentClientes);
    		return true;
    		
        case R.id.item2: 
        	Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show(); 
        	Intent intentProductos = new Intent(this, Productos_Main.class);
    		startActivity(intentProductos);
            return true;
        
        case R.id.item3: 
        	Toast.makeText(this, "Visitas", Toast.LENGTH_SHORT).show();
        	Intent intentVisitas = new Intent(this, Visitas_Main.class);
    		startActivity(intentVisitas);
        	 
            return true;            
        case R.id.item4: 
            Toast.makeText(this, "Presupuestos", Toast.LENGTH_SHORT).show();
            Intent intentPresupuestos = new Intent(this, Presupuestos_Main.class);
    		startActivity(intentPresupuestos);
            
            return true;
        case R.id.item5: 
        	Toast.makeText(this, "Documentos", Toast.LENGTH_SHORT).show();
        	Intent intentDocumentos = new Intent(this, Documentos_Main.class);
     		startActivity(intentDocumentos);
        	  
            return true;
        case R.id.item6: 
            Toast.makeText(this, "Config", Toast.LENGTH_SHORT).show(); 
            Intent intentLogin = new Intent(this, Config_vista_durox.class);
     		startActivity(intentLogin);
    		
            return true;            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}