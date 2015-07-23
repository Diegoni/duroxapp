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

public class Clientes_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Clientes> clientes = null;
	private ArrayList<Clientes> arraylist;

	public Clientes_ListView(Context context, List<Clientes> clientes) 
	{
		mContext = context;
		this.clientes = clientes;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Clientes>();
		this.arraylist.clear();
		this.arraylist.addAll(clientes);
	}

	public class ViewHolder 
	{
		TextView nombre;
		TextView direccion;
		ImageView imagen;
	}
	
	public int getCount() 
	{
		return clientes.size();
	}

	public Clientes getItem(int position) 
	{
		return clientes.get(position);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) 
	{
		final ViewHolder holder;
		if (view == null) 
		{
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.clientes_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.nombre);
			holder.direccion = (TextView) view.findViewById(R.id.direccion);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(clientes.get(position).getNombre());
		holder.direccion.setText(clientes.get(position).getDireccion());
		holder.imagen.setImageResource(clientes.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Clientes_ItemView.class);
				
				// Pasamos toda la informacion
				intent.putExtra("nombre", (clientes.get(position).getNombre()));
				intent.putExtra("direccion", (clientes.get(position).getDireccion()));
				intent.putExtra("imagen", (clientes.get(position).getImagen()));
				
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
		clientes.clear();
		if (charText.length() == 0) 
		{
			clientes.addAll(arraylist);
		} 
		else 
		{
			for (Clientes wp : arraylist) 
			{
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					clientes.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
