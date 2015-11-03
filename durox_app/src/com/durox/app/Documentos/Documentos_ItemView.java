package com.durox.app.Documentos;

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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.widget.Toast;

public class Documentos_ItemView extends MenuActivity {
	SQLiteDatabase db;
	Config_durox config;
	
	String nombre;
	String id;
	String url;
	
	private ProgressDialog prgDialog;
	public static final int progress_bar_type = 0;
	public static String file_url;
	
	String ip;
	String cortado;
	
	File file;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		config = new Config_durox();		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		ip = config.getDocumentos(db);
						
		Intent i = getIntent();
			
	 	nombre = i.getStringExtra("nombre");
		id = i.getStringExtra("id");
		url = i.getStringExtra("url");
			
		cortado = url.substring(ip.length());
			
		file_url = url;
			
		Log.e("Paso ","nombre= "+nombre);
		Log.e("Paso ","id= "+id);
		Log.e("Paso ","url= "+url);
		Log.e("Paso ","cortado= "+cortado);
			
		file = new File(Environment.getExternalStorageDirectory().getPath()+cortado);
			
		if (file.exists()) {
			Toast.makeText(getApplicationContext(), "El archivo ya existe en la tarjeta SD", Toast.LENGTH_LONG).show();
			abrirArchivo();
		} else {
			Toast.makeText(getApplicationContext(), "El archivo no existe en la tarjeta SD, comenzando descarga", Toast.LENGTH_LONG).show();
			new DownloadArchivo().execute(url);
		}
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
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),10*1024);
				OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+cortado);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return null;
		}

		
		protected void onProgressUpdate(String... progress) {
			prgDialog.setProgress(Integer.parseInt(progress[0]));
		}

		
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url) {
			dismissDialog(progress_bar_type);
			Toast.makeText(getApplicationContext(), "Descarga Completa", Toast.LENGTH_LONG).show();
			abrirArchivo();
		}
	}
	
			
	public void abrirArchivo(){
		File file = new File(Environment.getExternalStorageDirectory()+cortado);
		if (file .exists()){
			Uri path = Uri.fromFile(file );
			Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
			pdfIntent.setDataAndType(path , "application/pdf");
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try {
				startActivity(pdfIntent ); 
			} catch (ActivityNotFoundException e) {
			}  
		}
	}
			
}