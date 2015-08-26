package com.durox.app.Visitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Presupuestos.Presupuestos_Create;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Visitas_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Visitas> visitas = null;
	private ArrayList<Visitas> arraylist;

	public Visitas_ListView(Context context, ArrayList<Visitas> arraylist2) 
	{
		mContext = context;
		this.visitas = arraylist2;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Visitas>();
		this.arraylist.clear();
		this.arraylist.addAll(arraylist2);
	}

	public class ViewHolder 
	{
		TextView nombre;
		TextView epoca;
		TextView fecha;
		ImageView imagen;
	}
	
	public int getCount() 
	{
		return visitas.size();
	}

	public Visitas getItem(int position) 
	{
		return visitas.get(position);
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
			view = inflater.inflate(R.layout.visitas_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_vNombre);
			holder.epoca = (TextView) view.findViewById(R.id.txt_vEpoca);
			holder.fecha = (TextView) view.findViewById(R.id.txt_vFecha);
			holder.imagen = (ImageView) view.findViewById(R.id.imagen);
			
			view.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(visitas.get(position).getNombre());
		holder.epoca.setText(visitas.get(position).getEpoca());
		holder.fecha.setText(visitas.get(position).getFecha());
		holder.imagen.setImageResource(visitas.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Presupuestos_Create.class);
				
				// Pasamos toda la informacion
				intent.putExtra("id_visita", (visitas.get(position).getVisita()));
				intent.putExtra("nombre", (visitas.get(position).getNombre()));
				intent.putExtra("epoca", (visitas.get(position).getEpoca()));
				intent.putExtra("fecha", (visitas.get(position).getFecha()));
				intent.putExtra("imagen", (visitas.get(position).getImagen()));
				intent.putExtra("truncate", "truncate");
				
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
		visitas.clear();
		if (charText.length() == 0) 
		{
			visitas.addAll(arraylist);
		} 
		else 
		{
			for (Visitas wp : arraylist) 
			{
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					visitas.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}