package com.durox.app.Clientes;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class Telefonos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Telefonos> telefonos = null;
	private ArrayList<Telefonos> arraylist;

	public Telefonos_ListView(Context context, List<Telefonos> telefonos) 
	{
		mContext = context;
		this.telefonos = telefonos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Telefonos>();
		this.arraylist.clear();
		this.arraylist.addAll(telefonos);
	}

	public class ViewHolder {
		TextView nombre;
		TextView direccion;
		TextView id;
		ImageView imagen;
	}
	
	public int getCount() {
		return telefonos.size();
	}

	public Telefonos getItem(int position) {
		return telefonos.get(position);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup parent) 
	{
		final ViewHolder holder;
		if (view == null) 
		{
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.clientes_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_cRazon_social);
			holder.id = (TextView) view.findViewById(R.id.txt_cIdback);
			holder.direccion = (TextView) view.findViewById(R.id.txt_cNombre);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(telefonos.get(position).getTipo());
		holder.id.setText(telefonos.get(position).getCodArea());
		holder.direccion.setText(telefonos.get(position).getTelefono());
		holder.imagen.setImageResource(telefonos.get(position).getImagen());
		
		
		view.setOnLongClickListener(new OnLongClickListener(){	
			public boolean onLongClick(View v) {
				Intent intent = new Intent(mContext, Clientes_Edit_Telefono.class);
				
				Log.e("Paso ", "id "+telefonos.get(position).getID());
				Log.e("Paso ", "telefono "+telefonos.get(position).getTelefono());
				Log.e("Paso ", "cod_area "+telefonos.get(position).getCodArea());
				Log.e("Paso ", "tipo "+telefonos.get(position).getTipo());
				Log.e("Paso ", "id_cliente "+telefonos.get(position).getIdCliente());
				Log.e("Paso ", "imagen "+telefonos.get(position).getImagen());
				
				// Pasamos toda la informacion
				intent.putExtra("id", (telefonos.get(position).getID()));
				intent.putExtra("telefono", (telefonos.get(position).getTelefono()));
				intent.putExtra("cod_area", (telefonos.get(position).getCodArea()));
				intent.putExtra("tipo", (telefonos.get(position).getTipo()));
				intent.putExtra("id_cliente", (telefonos.get(position).getIdCliente()));
				intent.putExtra("imagen", (telefonos.get(position).getImagen()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
			
				return false;
			}
		});

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String tel = telefonos.get(position).getCodArea()+telefonos.get(position).getTelefono();
				
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
				mContext.startActivity(intent);
			}
		});

		return view;
	}


	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		telefonos.clear();
		if (charText.length() == 0) {
			telefonos.addAll(arraylist);
		} else {
			for (Telefonos wp : arraylist) {
				if (wp.getTipo().toLowerCase(Locale.getDefault()).contains(charText)) {
					telefonos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
