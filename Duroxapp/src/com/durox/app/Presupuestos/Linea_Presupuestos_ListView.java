package com.durox.app.Presupuestos;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
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


public class Linea_Presupuestos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Linea_Presupuestos> linea_presupuestos = null;
	private ArrayList<Linea_Presupuestos> arraylist;

	public Linea_Presupuestos_ListView(Context context, List<Linea_Presupuestos> linea_presupuestos) 
	{
		mContext = context;
		this.linea_presupuestos = linea_presupuestos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Linea_Presupuestos>();
		this.arraylist.clear();
		this.arraylist.addAll(linea_presupuestos);
	}

	public class ViewHolder 
	{
		TextView producto;
		TextView cantidad;
		TextView comentario;
	}
	
	public int getCount() 
	{
		return linea_presupuestos.size();
	}

	public Linea_Presupuestos getItem(int position) 
	{
		return linea_presupuestos.get(position);
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
			view = inflater.inflate(R.layout.linea_presupuestos_listviewitem, null);
			
			// Locate the TextViews in listview_item.xml
			holder.producto = (TextView) view.findViewById(R.id.linea_presupuesto_producto);
			holder.cantidad = (TextView) view.findViewById(R.id.linea_presupuesto_cantidad);
			holder.comentario = (TextView) view.findViewById(R.id.linea_presupuesto_comentario);
			
			view.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.producto.setText(linea_presupuestos.get(position).getProducto());
		holder.cantidad.setText(linea_presupuestos.get(position).getCantidad());
		holder.comentario.setText(linea_presupuestos.get(position).getComentario());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, Linea_Presupuestos_ItemView.class);
				
				// Pasamos toda la informacion
				intent.putExtra("producto", (linea_presupuestos.get(position).getProducto()));
				intent.putExtra("cantidad", (linea_presupuestos.get(position).getCantidad()));
				intent.putExtra("comentario", (linea_presupuestos.get(position).getComentario()));
				
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
		linea_presupuestos.clear();
		if (charText.length() == 0) 
		{
			linea_presupuestos.addAll(arraylist);
		} 
		else 
		{
			for (Linea_Presupuestos wp : arraylist) 
			{
				if (wp.getProducto().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					linea_presupuestos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
