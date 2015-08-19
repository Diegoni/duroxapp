package com.durox.app.Presupuestos;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Clientes.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class Presupuestos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Presupuesto> presupuestos = null;
	private ArrayList<Presupuesto> arraylist;

	public Presupuestos_ListView(Context context, List<Presupuesto> presupuestos) 
	{
		mContext = context;
		this.presupuestos = presupuestos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Presupuesto>();
		this.arraylist.clear();
		this.arraylist.addAll(presupuestos);
	}

	public class ViewHolder 
	{
		TextView nombre;
		TextView direccion;
		TextView id;
		ImageView imagen;
	}
	
	public int getCount() 
	{
		return presupuestos.size();
	}

	public Presupuesto getItem(int position) 
	{
		return presupuestos.get(position);
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
		holder.nombre.setText(presupuestos.get(position).getNombre());
		holder.id.setText(presupuestos.get(position).getID());
		holder.direccion.setText(presupuestos.get(position).getDireccion());
		holder.imagen.setImageResource(presupuestos.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Presupuestos_ItemView.class);
				
				// Pasamos toda la informacion
				intent.putExtra("id", (presupuestos.get(position).getID()));
				intent.putExtra("nombre", (presupuestos.get(position).getNombre()));
				intent.putExtra("direccion", (presupuestos.get(position).getDireccion()));
				intent.putExtra("imagen", (presupuestos.get(position).getImagen()));
				
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
		presupuestos.clear();
		if (charText.length() == 0) 
		{
			presupuestos.addAll(arraylist);
		} 
		else 
		{
			for (Presupuesto wp : arraylist) 
			{
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					presupuestos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
