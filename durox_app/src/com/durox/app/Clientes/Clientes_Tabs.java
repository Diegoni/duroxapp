package com.durox.app.Clientes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.example.durox_app.R;
import com.durox.app.Clientes.Clientes_Main;

public class Clientes_Tabs extends TabActivity {
	
	int imagen;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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
		Intent intentDireccion = new Intent().setClass(this, Clientes_Main.class);
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

}