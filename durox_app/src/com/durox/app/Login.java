package com.durox.app;
/*CREADO POR SEBASTIAN CIPOLAT*/
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.durox.app.Httppostaux;
import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Models.Vendedores_model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.durox_app.R;

public class Login extends Activity {
    /** Called when the activity is first created. */
    
    EditText user;
    EditText pass;
    Button blogin;
    Button btnSalir;
    Button btn_Sin;
    
    Httppostaux post;
    // String URL_connect="http://www.scandroidtest.site90.com/acces.php";
    String IP_Server = "192.168.1.207"; //IP DE NUESTRO PC
    String URL_connect = "http://"+IP_Server+"/durox/index.php/actualizaciones/getLogin/"; //ruta en donde estan nuestros archivos
  
    boolean result_back;
    private ProgressDialog pDialog;
    Vendedores_model mVendedores;
    
    int logstatus;
    int intentos;
    int segundos;
    
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.login);
        post = new Httppostaux();
        
        user = (EditText) findViewById(R.id.edusuario);
        pass = (EditText) findViewById(R.id.edpassword);
        blogin = (Button) findViewById(R.id.Blogin);
        btnSalir = (Button) findViewById(R.id.btn_Salir);
        btn_Sin = (Button) findViewById(R.id.btn_Sin);
               
        intentos = 3;
        segundos = 15;
        
        //Login button action
        btnSalir.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		Intent salida = new Intent( Intent.ACTION_MAIN); //Llamando a la activity principal
        		finish(); 
        	}
        });
        
        
        //Login button action
        btn_Sin.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		String usuario = user.getText().toString();
        		String passw   = pass.getText().toString();
        		
        		SQLiteDatabase db;
    			Config_durox config = new Config_durox();
    			db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
    			
    			Vendedores_model mVendedor = new Vendedores_model(db);
    			mVendedor.truncate();
    			
    			new asynclogin().execute(usuario,passw); 
        	}
        });
        
        
        //Login button action
        blogin.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		//Extreamos datos de los EditText
        		String usuario = user.getText().toString();
        		String passw   = pass.getText().toString();
        		
        		//verificamos si estan en blanco
        		if( checklogindata( usuario , passw ) == true){
        			
        			SQLiteDatabase db;
        			Config_durox config = new Config_durox();
        			db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        			
        			Vendedores_model mVendedor = new Vendedores_model(db);
        			Cursor c = mVendedor.getRegistros();
        			
        			if(c.getCount() > 0){
        				while(c.moveToNext()){
        					if(new String (usuario).equals(c.getString(2)) && new String (passw).equals(c.getString(3))){
        						Toast.makeText(getApplicationContext(),
            							config.msjOkLogin(usuario), Toast.LENGTH_SHORT).show();
        						okLogin();
        					}else{
        						Toast.makeText(getApplicationContext(),
            							config.msjError("Login incorrecto, pass y/o user incorrectos"), Toast.LENGTH_SHORT).show();
        					}
        				}
        			}else{
        				//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
            			new asynclogin().execute(usuario,passw);  
        			}
        		}else{
        			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
        			err_login();
        		}
        	}
        });
        
        
    }
    
    public void okLogin(){
    	Intent intentMain = new Intent(this, MainActivity.class);
 		startActivity(intentMain);
    }
    
    
    public void err_login(){
    	intentos = intentos - 1;
    	if(intentos > 0){
    		Toast toast1 = Toast.makeText(getApplicationContext(),
    	    		"Error: Nombre de usuario o password incorrectos, le quedan "+intentos+" intentos", 
    	    		Toast.LENGTH_LONG);
     	    toast1.show();
    	} else {
    		Toast toast1 = Toast.makeText(getApplicationContext(),
    	    		"Se acabaron los intentos", 
    	    		Toast.LENGTH_LONG);
     	    toast1.show();
     	    blogin.setEnabled(false);
     	   
     	    Handler handler = new Handler();
     	    handler.postDelayed(new Runnable() {
     	    	public void run() {
     	    		blogin.setEnabled(true);
     	    		intentos = 3;
     	    	}
     	    }, 1000 * segundos);
     	}
    }
    
    
    public void ok_login(String id_vendedor){
    	//Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    //vibrator.vibrate(200);
	   Toast toast1 = Toast.makeText(getApplicationContext(),
	    		"Login correcto ", Toast.LENGTH_SHORT);
 	   toast1.show(); 
 	   Intent intentLogin = new Intent(this, MainActivity.class);
 	   
 	   intentLogin.putExtra("user", user.getText().toString());
 	   intentLogin.putExtra("pass", pass.getText().toString());
 	   intentLogin.putExtra("id_vendedor", id_vendedor);
 	   
 	   startActivity(intentLogin);
    }
    
    
    // Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String username ,String password ) {
    	logstatus = -1;
    	// Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	// y enviarlo mediante POST a nuestro sistema para relizar la validacion 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    	postparameters2send.add(new BasicNameValuePair("usuario", username));
    	postparameters2send.add(new BasicNameValuePair("password", password));
    	
    	// realizamos una peticion y como respuesta obtenes un array JSON
    	JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

      	// como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      	// para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      	// observar el progressdialog
      	// la podemos eliminar si queremos
      	
    	SystemClock.sleep(950);
		    		
    	// si lo que obtuvimos no es null
    	if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data; //creamos un objeto JSON
    		try {
    			json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
    			logstatus = json_data.getInt("logstatus");//accedemos al valor 
    			Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}		            
		             
    		//validamos el valor obtenido
    		if (logstatus == 0){	// [{"logstatus":"0"}] 
    			Log.e("loginstatus ", "invalido "+logstatus);
    			return false;
    		} else{				// [{"logstatus":"1"}]
    			Log.e("loginstatus ", "valido "+logstatus);
    			
    			return true;
    		}
		    		 
    	}else{	//json obtenido invalido verificar parte WEB.
    		Log.e("JSON  ", "ERROR");
    		return false;
    	}
    }
    
          
    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){
    	
	    if 	(username.equals("") || password.equals("")){
	    	Log.e("Login ui", "checklogindata user or pass error");
	    	return false;
	    }else{
	    	return true;
	    }
    }           
    
    // CLASE ASYNCTASK
  
	// usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
	// podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
	// si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
	// ademas observariamos el mensaje de que la app no responde.     
 
    
    class asynclogin extends AsyncTask< String, String, String > {
    	 
    	String user,pass;
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			user = params[0];
			pass = params[1];
			
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (loginstatus(user,pass)==true){    		    		
    			return "ok"; //login valido
    		}else{    		
    			return "err"; //login invalido     	          	  
    		}
        	
		}
       
		// Una vez terminado doInBackground segun lo que halla ocurrido 
		// pasamos a la sig. activity
		// o mostramos error
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute="," "+result+" "+logstatus);
           
           if (result.equals("ok")){
        	   ok_login(""+logstatus);		
           }else{
        	   err_login();
           }
        }
	}
}