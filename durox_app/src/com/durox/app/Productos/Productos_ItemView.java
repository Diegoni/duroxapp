package com.durox.app.Productos;

import com.example.durox_app.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Productos_model;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Productos_ItemView extends MenuActivity {
	// Declare Variables
	ImageView imgimagen;
	
	String nombre;
	String precio;
	String id;
	int imagen;
	
	SQLiteDatabase db;
	Productos_model mProducto;
	Cursor c;
	
	Config_durox config;
	
	private ProgressDialog prgDialog;
	public static final int progress_bar_type = 0;
	public static String file_url;
	String url = "";
	File file;
	String ip;
	
	String cortado;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos_itemview);
		
		setTitle("Productos - Registro");
        getActionBar().setIcon(R.drawable.menuproductos);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		// Locate the TextViews in singleitemview.xml
		//TextView id_back = (TextView) findViewById(R.id.txt_c);
		
		TextView txt_pNombre = (TextView) findViewById(R.id.txt_pNombre);
		TextView txt_pCodigo = (TextView) findViewById(R.id.txt_pCodigo);
		TextView txt_pCodigoLote = (TextView) findViewById(R.id.txt_pCodigoLote);
		TextView txt_pPrecio = (TextView) findViewById(R.id.txt_pPrecio);
		TextView txt_pPrecioIva = (TextView) findViewById(R.id.txt_pPrecioIva);
		TextView txt_pPrecioMin = (TextView) findViewById(R.id.txt_pPrecioMin);
		TextView txt_pPrecioMinIva = (TextView) findViewById(R.id.txt_pPrecioMinIva);
		TextView txt_pIva = (TextView) findViewById(R.id.txt_pIva);
		final TextView txt_pFichaTecnica = (TextView) findViewById(R.id.txt_pFichaTecnica);
		TextView txt_pDateAdd = (TextView) findViewById(R.id.txt_pDateAdd);
		TextView txt_pDateUpd = (TextView) findViewById(R.id.txt_pDateUpd);
		TextView txt_Moneda = (TextView) findViewById(R.id.txt_moneda);
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		
		// Intent para ClientesListView
		Intent i = getIntent();
		
		// Traigo los resultados 
		id = i.getStringExtra("id");
		nombre = i.getStringExtra("nombre");
		precio = i.getStringExtra("precio");
		imagen = i.getIntExtra("imagen", imagen);
		
		mProducto = new Productos_model(db);
		
		c = mProducto.getRegistro(id);
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				//1+ "productos.id_back, "
				txt_pCodigo.setText(c.getString(1)); 		//2+ "productos.codigo, "
				txt_pCodigoLote.setText(c.getString(2));	//3+ "productos.codigo_lote, "
				txt_pNombre.setText(c.getString(3));		//4+ "productos.nombre, "
				txt_pPrecio.setText(c.getString(4));		//5+ "productos.precio, "
				txt_pPrecioIva.setText(c.getString(5));		//6+ "productos.precio_iva, "
				txt_pPrecioMin.setText(c.getString(6));		//7+ "productos.precio_min, "
				txt_pPrecioMinIva.setText(c.getString(7));	//8+ "productos.precio_min_iva, "
				txt_pIva.setText(c.getString(8));			//9+ "productos.id_iva, "
				txt_pFichaTecnica.setText(c.getString(9));	//10+ "productos.ficha_tecnica, "
				txt_pDateAdd.setText(c.getString(10));		//11+ "productos.date_add, "
				txt_pDateUpd.setText(c.getString(11));		//12+ "productos.date_upd, "
				//13+ "productos.eliminado, "
				//14+ "productos.user_add, "
				//15+ "productos.user_upd, "
				txt_Moneda.setText(c.getString(15)+" "+c.getString(16)+" "+c.getString(17));		
															//16+ "monedas.moneda, "
															//17+ "monedas.abreviatura, "
															//18+ "monedas.simbolo "
				
			}
		}

		imgimagen.setImageResource(imagen);
		
		
		txt_pFichaTecnica.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		ip = config.getFichaProductos(db);
        		url = ip+txt_pFichaTecnica.getText().toString();
        		cortado = txt_pFichaTecnica.getText().toString();
        		file_url = url;
        			
        		file = new File(Environment.getExternalStorageDirectory().getPath()+"/"+cortado);
        		if (file.exists()) {
        			Toast.makeText(getApplicationContext(), "El archivo ya existe en la tarjeta SD", Toast.LENGTH_LONG).show();
        			abrirArchivo();
        		} else {
        			Toast.makeText(getApplicationContext(), "El archivo no existe en la tarjeta SD, comenzando descarga", Toast.LENGTH_LONG).show();
        			new DownloadArchivo().execute(url);
        		}
        		
        	}
		});
	}
	
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case progress_bar_type:
				prgDialog = new ProgressDialog(this);
				prgDialog.setMessage("Descargando, por favor espere ...");
				prgDialog.setIndeterminate(false);
				prgDialog.setMax(100);
				prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				prgDialog.setCancelable(false);
				prgDialog.show();
				return prgDialog;
			default:
				return null;
		}
	}
	
	
	
	class DownloadArchivo extends AsyncTask<String, String, String> {
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			Log.e("Paso", "1");
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		protected String doInBackground(String... f_url) {
			int count;
			try {
				Log.e("Paso", "2");
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				Log.e("Paso", "3");
				int lenghtOfFile = conection.getContentLength();
				Log.e("Paso", "lenghtOfFile "+lenghtOfFile);
				InputStream input = new BufferedInputStream(url.openStream(),10*1024);
				Log.e("Paso", "3a ");
				OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/"+cortado);
				Log.e("Paso", "3b ");
				byte data[] = new byte[1024];
				Log.e("Paso", "3c ");
				long total = 0;
				Log.e("Paso", "4");
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				Log.e("Paso", "5");
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return null;
		}

		
		protected void onProgressUpdate(String... progress) {
			Log.e("Paso", "6");
			prgDialog.setProgress(Integer.parseInt(progress[0]));
		}

		
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url) {
			Log.e("Paso", "7");
			dismissDialog(progress_bar_type);
			Toast.makeText(getApplicationContext(), "Descarga Completa", Toast.LENGTH_LONG).show();
			abrirArchivo();
		}
	}
	
			
	public void abrirArchivo(){
		File file = new File(Environment.getExternalStorageDirectory()+"/"+cortado);
		if (file .exists()){
			Log.e("Paso", "8");
			Uri path = Uri.fromFile(file );
			Log.e("Paso", "9");
			Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
			pdfIntent.setDataAndType(path , "application/pdf");
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Log.e("Paso", "10");
			try {
				startActivity(pdfIntent ); 
			} catch (ActivityNotFoundException e) {
			}  
		}
	}
}