package com.durox.app.Presupuestos;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

import com.durox.app.Config_vista_durox;
import com.durox.app.MainActivity;
import com.durox.app.Clientes.Clientes_Main;
import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Productos.Productos_Main;
import com.durox.app.Vendedores.Mensajes_Main;
import com.durox.app.Visitas.Visitas_Main;
import com.example.durox_app.R;
@SuppressWarnings("deprecation")


public class Presupuestos_Tabs extends TabActivity {
	
	int imagen;
	
	String cNombre;
	String cIdVisita;
	String truncate;
	String id_presupuesto;
	String id_linea;
	String id;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setTitle("Presupuestos - Crear");
        getActionBar().setIcon(R.drawable.menupresupuesto);
		
		Intent i = getIntent();
		cNombre = i.getStringExtra("nombre");
		cIdVisita = i.getStringExtra("id_visita");
		id_presupuesto = i.getStringExtra("id_presupuesto");
		id = i.getStringExtra("id");
		id_linea = i.getStringExtra("id_linea");
		truncate = i.getStringExtra("truncate");
		
		// Traigo los resultados 
		String id = i.getStringExtra("id");
		imagen = i.getIntExtra("imagen", imagen);

		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
		
		// Productos
		Intent intent = new Intent().setClass(this, Presupuestos_Create.class);
		
		intent.putExtra("nombre", cNombre);
		intent.putExtra("id_visita", cIdVisita);
		intent.putExtra("id_presupuesto", id_presupuesto);
		intent.putExtra("id", id);
		intent.putExtra("id_linea", id_linea);
		intent.putExtra("truncate", truncate);
		
		TabSpec tabSpecCliente = tabHost
			.newTabSpec("@string/perfil")
			.setIndicator("", ressources.getDrawable(R.drawable.users))
			.setContent(intent);
		

		// Datos
		Intent intentDatos = new Intent().setClass(this, Presupuestos_Datos.class);
		
		intentDatos.putExtra("nombre", cNombre);
		intentDatos.putExtra("id_visita", cIdVisita);
		intentDatos.putExtra("id_presupuesto", id_presupuesto);
		intentDatos.putExtra("id", id);
		intentDatos.putExtra("id_linea", id_linea);
		intentDatos.putExtra("truncate", truncate);
		
		TabSpec tabSpecTelefono = tabHost
			.newTabSpec("@string/datos")
			.setIndicator("", ressources.getDrawable(R.drawable.phonecell))
			.setContent(intentDatos);
		
		
		// add all tabs 
		tabHost.addTab(tabSpecCliente);
		tabHost.addTab(tabSpecTelefono);
		
		
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}
	
	
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
	            Toast.makeText(this, "Mi Perfil", Toast.LENGTH_SHORT).show(); 
	            Intent intentVendedores = new Intent(this, Mensajes_Main.class);
	     		startActivity(intentVendedores);
	    		
	            return true;            
	        
	        case R.id.item7: 
	            Toast.makeText(this, "Config", Toast.LENGTH_SHORT).show(); 
	            Intent intentLogin = new Intent(this, Config_vista_durox.class);
	     		startActivity(intentLogin);
	    		
	            return true;   
	            
	        case R.id.item8: 
	            Toast.makeText(this, "Actualizar", Toast.LENGTH_SHORT).show(); 
	            Intent intentActualizar = new Intent(this, MainActivity.class);
	            intentActualizar.putExtra("actualizar", "ok");
	            startActivity(intentActualizar);
	    		
	            return true;               
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

}