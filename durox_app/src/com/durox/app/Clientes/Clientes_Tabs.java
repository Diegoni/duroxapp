package com.durox.app.Clientes;

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
import com.durox.app.Login;
import com.durox.app.MainActivity;
import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Productos.Productos_Main;
import com.durox.app.Vendedores.Mensajes_Main;
import com.durox.app.Visitas.Visitas_Main;
import com.example.durox_app.R;
@SuppressWarnings("deprecation")


public class Clientes_Tabs extends TabActivity {
	
	int imagen;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setTitle("Clientes - Registro");
        getActionBar().setIcon(R.drawable.menuclientes);
		
		Intent i = getIntent();
		
		// Traigo los resultados 
		String id = i.getStringExtra("id");
		imagen = i.getIntExtra("imagen", imagen);

		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
		
		// Android tab
		
		Intent intentCliente = new Intent().setClass(this, Clientes_ItemView.class);
		intentCliente.putExtra("id", id);
		intentCliente.putExtra("imagen", imagen);
		
		TabSpec tabSpecCliente = tabHost
			.newTabSpec("@string/perfil")
			.setIndicator("", ressources.getDrawable(R.drawable.users))
			.setContent(intentCliente);

		// Apple tab
		Intent intentTelefono = new Intent().setClass(this, Clientes_Telefonos.class);
		intentTelefono.putExtra("id", id);
		TabSpec tabSpecTelefono = tabHost
			.newTabSpec("@string/telefono")
			.setIndicator("", ressources.getDrawable(R.drawable.phonecell))
			.setContent(intentTelefono);
		
		// Windows tab
		Intent intentMail = new Intent().setClass(this, Clientes_Mails.class);
		intentMail.putExtra("id", id);
		TabSpec tabSpecMail = tabHost
			.newTabSpec("@string/mail")
			.setIndicator("", ressources.getDrawable(R.drawable.email))
			.setContent(intentMail);
		
		
		// Blackberry tab
		Intent intentDireccion = new Intent().setClass(this, Clientes_Direcciones.class);
		intentDireccion.putExtra("id", id);
		TabSpec tabSpecDireccion = tabHost
			.newTabSpec("@string/direccion")
			.setIndicator("", ressources.getDrawable(R.drawable.address))
			.setContent(intentDireccion);
	
		// add all tabs 
		tabHost.addTab(tabSpecCliente);
		tabHost.addTab(tabSpecTelefono);
		tabHost.addTab(tabSpecDireccion);
		tabHost.addTab(tabSpecMail);
		
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
	            
	        case R.id.item9: 
	        	Intent intentTest = new Intent(this, Login.class);
	        	intentTest.putExtra("no_back", "ok");
	        	startActivity(intentTest);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

}