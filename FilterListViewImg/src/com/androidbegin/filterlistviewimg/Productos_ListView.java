package com.androidbegin.filterlistviewimg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Productos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Productos> producto = null;
	private ArrayList<Productos> arraylist;

	public Productos_ListView(Context context,
			List<Productos> producto) 
	{
		mContext = context;
		this.producto = producto;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Productos>();
		this.arraylist.addAll(producto);
	}

	public class ViewHolder 
	{
		TextView nombre;
		TextView detalle;
		ImageView imagen;
	}

	public int getCount() 
	{
		return producto.size();
	}

	public Productos getItem(int position) 
	{
		return producto.get(position);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) 
	{
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.productos_listviewitem, null);
	
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.nombre);
			holder.detalle = (TextView) view.findViewById(R.id.detalle);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(producto.get(position).getNombre());
		holder.detalle.setText(producto.get(position).getDetalle());
		holder.imagen.setImageResource(producto.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Productos_ItemView.class);
				
				// Pasamos todos los datos de producto
				intent.putExtra("nombre", (producto.get(position).getNombre()));
				intent.putExtra("detalle", (producto.get(position).getDetalle()));
				intent.putExtra("imagen", (producto.get(position).getImagen()));
		
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) 
	{
		charText = charText.toLowerCase(Locale.getDefault());
		producto.clear();
		if (charText.length() == 0) 
		{
			producto.addAll(arraylist);
		}
		else 
		{
			for (Productos wp : arraylist) 
			{
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					producto.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
