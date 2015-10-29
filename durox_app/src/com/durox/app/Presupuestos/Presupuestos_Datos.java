package com.durox.app.Presupuestos;

import com.example.durox_app.R;

import java.util.ArrayList;
import java.util.List;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Condiciones_pago_model;
import com.durox.app.Models.Modos_pago_model;
import com.durox.app.Models.Presupuestos_temp_model;
import com.durox.app.Models.Tiempos_entrega_model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("ShowToast")
public class Presupuestos_Datos extends MenuActivity {
	// Declare Variables
	
	SQLiteDatabase db;
	Config_durox config;
	
	private Spinner sp_condiciones_pago;
	private Spinner sp_modos_pago;
	private Spinner sp_tiempo_entrega;
	
	Presupuestos_temp_model mTemp;
	String id;
	EditText et_nota_privada;
	EditText et_nota_publica;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presupuestos_datos);
		
		config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		sp_condiciones_pago = (Spinner) findViewById(R.id.sp_condiciones_pago);
		List<String> listCondiciones = new ArrayList<String>();
		Condiciones_pago_model mCondiciones = new Condiciones_pago_model(db);
		Cursor cCondiciones = mCondiciones.getRegistros();
		if(cCondiciones.getCount() > 0){
			while(cCondiciones.moveToNext()){
				listCondiciones.add(cCondiciones.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterCondiciones = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listCondiciones);
		adapterCondiciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_condiciones_pago.setAdapter(adapterCondiciones);
		
		
		sp_modos_pago = (Spinner) findViewById(R.id.sp_modos_pago);
		List<String> listModos = new ArrayList<String>();
		Modos_pago_model mModos = new Modos_pago_model(db);
		Cursor cModos = mModos.getRegistros();
		if(cModos.getCount() > 0){
			while(cModos.moveToNext()){
				listModos.add(cModos.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterModos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listModos);
		adapterModos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_modos_pago.setAdapter(adapterModos);
		
		
		sp_tiempo_entrega = (Spinner) findViewById(R.id.sp_tiempo_entrega);
		List<String> listTiempos = new ArrayList<String>();
		Tiempos_entrega_model mTiempos = new Tiempos_entrega_model(db);
		Cursor cTiempos = mTiempos.getRegistros();
		if(cTiempos.getCount() > 0){
			while(cTiempos.moveToNext()){
				listTiempos.add(cTiempos.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterTiempos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listTiempos);
		adapterTiempos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_tiempo_entrega.setAdapter(adapterTiempos);
		
		
		mTemp = new Presupuestos_temp_model(db);
		mTemp.createTable();
		mTemp.limpiarOption();
		id = mTemp.newOption();
		String compareValue;
		
		compareValue = mTemp.getOption("id_condicion_pago", id);
		if (!compareValue.equals(null)) {
		    int spinnerPosition = adapterCondiciones.getPosition(compareValue);
		    sp_condiciones_pago.setSelection(spinnerPosition);
		}
		compareValue = mTemp.getOption("id_modo_pago", id);
		if (!compareValue.equals(null)) {
		    int spinnerPosition = adapterModos.getPosition(compareValue);
		    sp_modos_pago.setSelection(spinnerPosition);
		}
		compareValue = mTemp.getOption("id_tiempo_entrega", id);
		if (!compareValue.equals(null)) {
		    int spinnerPosition = adapterTiempos.getPosition(compareValue);
		    sp_tiempo_entrega.setSelection(spinnerPosition);
		}	
		et_nota_publica = (EditText) findViewById(R.id.et_nota_publica);
		et_nota_privada = (EditText) findViewById(R.id.et_nota_privada);		
		et_nota_publica.setText(mTemp.getOption("nota_publica", id));
		et_nota_privada.setText(mTemp.getOption("nota_privada", id));
		
		sp_condiciones_pago.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id_lista) {
				String imc_met = sp_condiciones_pago.getSelectedItem().toString();
				mTemp.setOption("id_condicion_pago", imc_met, id);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		sp_modos_pago.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id_lista) {
				String imc_met = sp_modos_pago.getSelectedItem().toString();
				mTemp.setOption("id_modo_pago", imc_met, id);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		sp_tiempo_entrega.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id_lista) {
				String imc_met = sp_tiempo_entrega.getSelectedItem().toString();
				mTemp.setOption("id_tiempo_entrega", imc_met, id);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		et_nota_publica.addTextChangedListener(new TextWatcher(){
		    public void afterTextChanged(Editable s) {
		    	String imc_met = et_nota_publica.getText().toString();
				mTemp.setOption("nota_publica", imc_met, id);
		    }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 	
		et_nota_privada.addTextChangedListener(new TextWatcher(){
		    public void afterTextChanged(Editable s) {
		    	String imc_met = et_nota_privada.getText().toString();
				mTemp.setOption("nota_privada", imc_met, id);
		    }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 		
	}
}