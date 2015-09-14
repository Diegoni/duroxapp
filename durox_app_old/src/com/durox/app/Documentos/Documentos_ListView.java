package com.durox.app.Documentos;

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
public class Documentos_ListView extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Documentos> documentos = null;
	private ArrayList<Documentos> arraylist;
	
	public Documentos_ListView(Context context,
			List<Documentos> documentos) 
	{
		mContext = context;
		this.documentos = documentos;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Documentos>();
		this.arraylist.addAll(documentos);
	}

	public class ViewHolder 
	{
		TextView txt_pID;
		TextView nombre;
		TextView detalle;
		ImageView imagen;
	}

	public int getCount() 
	{
		return documentos.size();
	}

	public Documentos getItem(int position) 
	{
		return documentos.get(position);
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
			view = inflater.inflate(R.layout.documentos_listviewitem, null);
	
			// Locate the TextViews in listview_item.xml
			holder.nombre = (TextView) view.findViewById(R.id.txt_dDocumento);
			holder.detalle = (TextView) view.findViewById(R.id.txt_cFecha);
			holder.imagen = (ImageView) view.findViewById(R.id.imgDocumentos);
			
			view.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) view.getTag();
		}
		
		// Set the results into TextViews
		holder.nombre.setText(documentos.get(position).getNombre());
		holder.detalle.setText(documentos.get(position).getDetalle());
		holder.imagen.setImageResource(documentos.get(position).getImagen());

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				//Intent intent = new Intent(mContext, Productos_ItemView.class); // Arreglar esto
				Intent intent = new Intent(mContext, Documentos_ItemView.class);
				
				// Pasamos todos los datos de producto
				intent.putExtra("nombre", (documentos.get(position).getNombre()));
				intent.putExtra("id", (documentos.get(position).getID()));
				intent.putExtra("url", (documentos.get(position).getDetalle()));// esto esta mal
				
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
		documentos.clear();
		if (charText.length() == 0) 
		{
			documentos.addAll(arraylist);
		}
		else 
		{
			for (Documentos wp : arraylist) 
			{
				if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					documentos.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	
	

}
