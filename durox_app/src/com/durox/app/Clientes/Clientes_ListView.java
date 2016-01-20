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


@SuppressLint("InflateParams")
public class Clientes_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Clientes> clientes = null;
	private ArrayList<Clientes> arraylist;

	
	public Clientes_ListView(Context context, List<Clientes> clientes) {
		mContext = context;
		this.clientes = clientes;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Clientes>();
		this.arraylist.clear();
		this.arraylist.addAll(clientes);
	}
	

	public class ViewHolder {
		TextView razon_social;
		TextView nombre;
		TextView id;
		ImageView imagen;
	}
	
	
	public int getCount() {
		return clientes.size();
	}
	

	public Clientes getItem(int position) {
		return clientes.get(position);
	}
	

	public long getItemId(int position) {
		return position;
	}
	

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.clientes_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.razon_social = (TextView) view.findViewById(R.id.txt_cRazon_social);
			holder.nombre = (TextView) view.findViewById(R.id.txt_cNombre);
			holder.id = (TextView) view.findViewById(R.id.txt_cIdback);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.razon_social.setText(clientes.get(position).getRazonSocial());
		holder.nombre.setText(clientes.get(position).getNombre());
		holder.id.setText(clientes.get(position).getID());
		holder.imagen.setImageResource(clientes.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Clientes_Tabs.class);
				
				// Pasamos toda la informacion
				intent.putExtra("razon_social", (clientes.get(position).getRazonSocial()));
				intent.putExtra("nombre", (clientes.get(position).getNombre()));
				intent.putExtra("id", (clientes.get(position).getID()));
				intent.putExtra("imagen", (clientes.get(position).getImagen()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});

		return view;
	}
	
	
	public void filter(String charText, CharSequence filtro) {
		charText = charText.toLowerCase(Locale.getDefault());
		clientes.clear();
		if (charText.length() == 0) {
			clientes.addAll(arraylist);
		} else {
			for (Clientes wp : arraylist) {
				if(filtro.equals("") || filtro.equals("razon social") || filtro.equals("Razón Social")){
					if (wp.getRazonSocial().toLowerCase(Locale.getDefault()).contains(charText)) {
						clientes.add(wp);
					}
				} else if(filtro.equals("nombre") || filtro.equals("apellido") || filtro.equals("Nombre") || filtro.equals("Apellido")){
					if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
						clientes.add(wp);
					}
				} else {
					if (wp.getID().toLowerCase(Locale.getDefault()).contains(charText)) {
						clientes.add(wp);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
