package com.durox.app.Documentos;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Productos_model;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Documentos_ItemView extends MenuActivity {
	SQLiteDatabase db;
	Config_durox config;
	
	String nombre;
	String id;
	String url;
	
	// Button to download and play Music
	private Button btnPlayMusic;
	// Media Player Object
	private MediaPlayer mPlayer;
	// Progress Dialog Object
	private ProgressDialog prgDialog;
	// Progress Dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;
	// Music resource URL
	//private static String file_url = "http://android.programmerguru.com/wp-content/uploads/2014/01/jai_ho.mp3";
	public static String file_url;
	
	String ip;
	String cortado;
	
	File file;

	
	
		@Override
			public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			config = new Config_durox();
			
			ip = config.getDocumentos();
			
			
			// Downloaded Music File path in SD Card
			
			Intent i = getIntent();
			
			// Traigo los resultados 
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
			// If the Music File doesn't exist in SD card (Not yet downloaded)
			} else {
				Toast.makeText(getApplicationContext(), "El archivo no existe en la tarjeta SD, comenzando descarga", Toast.LENGTH_LONG).show();
				// Trigger Async Task (onPreExecute method)
				new DownloadMusicfromInternet().execute(url);
			}
				
				
			}

		    // Show Dialog Box with Progress bar
			@Override
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

			// Async Task Class
			class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

				// Show Progress bar before downloading Music
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					// Shows Progress Bar Dialog and then call doInBackground method
					showDialog(progress_bar_type);
				}

				// Download Music File from Internet
				@Override
				protected String doInBackground(String... f_url) {
					int count;
					try {
						URL url = new URL(f_url[0]);
						URLConnection conection = url.openConnection();
						conection.connect();
						// Get Music file length
						int lenghtOfFile = conection.getContentLength();
						// input stream to read file - with 8k buffer
						InputStream input = new BufferedInputStream(url.openStream(),10*1024);
						// Output stream to write file in SD card
						OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+cortado);
						byte data[] = new byte[1024];
						long total = 0;
						while ((count = input.read(data)) != -1) {
							total += count;
							// Publish the progress which triggers onProgressUpdate method
							publishProgress("" + (int) ((total * 100) / lenghtOfFile));

							// Write data to file
							output.write(data, 0, count);
						}
						// Flush output
						output.flush();
						// Close streams
						output.close();
						input.close();
					} catch (Exception e) {
						Log.e("Error: ", e.getMessage());
					}
					return null;
				}

				// While Downloading Music File
				protected void onProgressUpdate(String... progress) {
					// Set progress percentage
					prgDialog.setProgress(Integer.parseInt(progress[0]));
				}

				// Once Music File is downloaded
				@Override
				protected void onPostExecute(String file_url) {
					
					//Log.e("Archivo : ", file_url);
					// Dismiss the dialog after the Music file was downloaded
					dismissDialog(progress_bar_type);
					Toast.makeText(getApplicationContext(), "Descarga Completa", Toast.LENGTH_LONG).show();
					// Play the music
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
		             try 
		               {
		              startActivity(pdfIntent ); }
		              catch (ActivityNotFoundException e) 
		                {
		                   //Toast.makeText(EmptyBlindDocumentShow.this,"No"Toast.LENGTH_SHORT).show();
		                }  
	            }
				
			}
			
}