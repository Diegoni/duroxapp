package com.durox.app.Clientes;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class Direcciones_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Direcciones> direcciones = null;
	private ArrayList<Direcciones> arraylist;

	public Direcciones_ListView(Context context, List<Direcciones> direcciones) {
		mContext = context;
		this.direcciones = direcciones;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Direcciones>();
		this.arraylist.clear();
		this.arraylist.addAll(direcciones);
	}

	public class ViewHolder {
		TextView nombre;
		TextView direccion;
		TextView id;
		ImageView imagen;
	}
	
	public int getCount() {
		return direcciones.size();
	}

	public Direcciones getItem(int position) {
		return direcciones.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.clientes_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_cRazon_social);
			holder.id = (TextView) view.findViewById(R.id.txt_cIdback);
			holder.direccion = (TextView) view.findViewById(R.id.txt_cNombre);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(direcciones.get(position).getTipo());
		holder.id.setText(direcciones.get(position).getIdDepartamento()+" -");
		holder.direccion.setText(direcciones.get(position).getDireccion());
		holder.imagen.setImageResource(direcciones.get(position).getImagen());
		
		
		view.setOnLongClickListener(new OnLongClickListener(){	
			public boolean onLongClick(View v) {
				Intent intent = new Intent(mContext, Clientes_Edit_Direccion.class);
			
				// Pasamos toda la informacion
				intent.putExtra("id", (direcciones.get(position).getID()));
				intent.putExtra("direccion", (direcciones.get(position).getDireccion()));
				intent.putExtra("departamento", (direcciones.get(position).getIdDepartamento()));
				intent.putExtra("tipo", (direcciones.get(position).getTipo()));
				intent.putExtra("id_cliente", (direcciones.get(position).getIdCliente()));
				intent.putExtra("imagen", (direcciones.get(position).getImagen()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
			
				return false;
			}
		});

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
			}
		});

		return view;
	}


	
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		direcciones.clear();
		if (charText.length() == 0) {
			direcciones.addAll(arraylist);
		} else {
			for (Direcciones wp : arraylist) {
				if (wp.getTipo().toLowerCase(Locale.getDefault()).contains(charText)) {
					direcciones.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
