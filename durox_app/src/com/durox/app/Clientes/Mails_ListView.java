package com.durox.app.Clientes;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.durox_app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class Mails_ListView extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private List<Mails> mails = null;
	private ArrayList<Mails> arraylist;

	public Mails_ListView(Context context, List<Mails> mails) {
		mContext = context;
		this.mails = mails;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Mails>();
		this.arraylist.clear();
		this.arraylist.addAll(mails);
	}

	public class ViewHolder {
		TextView nombre;
		TextView direccion;
		TextView id;
		ImageView imagen;
	}
	
	public int getCount() {
		return mails.size();
	}

	public Mails getItem(int position) {
		return mails.get(position);
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
		holder.nombre.setText(mails.get(position).getTipo());
		holder.id.setText(mails.get(position).getIdCliente());
		holder.direccion.setText(mails.get(position).getMail());
		holder.imagen.setImageResource(mails.get(position).getImagen());
		
		
		view.setOnLongClickListener(new OnLongClickListener(){	
			public boolean onLongClick(View v) {
				Intent intent = new Intent(mContext, Clientes_Edit_Mail.class);
				
				Log.e("Paso ", "id "+mails.get(position).getID());
				Log.e("Paso ", "mails "+mails.get(position).getMail());
				Log.e("Paso ", "tipo "+mails.get(position).getTipo());
				Log.e("Paso ", "id_cliente "+mails.get(position).getIdCliente());
				Log.e("Paso ", "imagen "+mails.get(position).getImagen());
				
				// Pasamos toda la informacion
				intent.putExtra("id", (mails.get(position).getID()));
				intent.putExtra("mails", (mails.get(position).getMail()));
				intent.putExtra("tipo", (mails.get(position).getTipo()));
				intent.putExtra("id_cliente", (mails.get(position).getIdCliente()));
				intent.putExtra("imagen", (mails.get(position).getImagen()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
			
				return false;
			}
		});

		// Detecta que item de la lista le hicieron clic
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
				i.putExtra(Intent.EXTRA_TEXT   , "body of email");
				try {
					mContext.startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    //Toast.makeText(Mails_ListView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				    //Toast.makeText(this, "Documentos", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return view;
	}


	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mails.clear();
		if (charText.length() == 0) {
			mails.addAll(arraylist);
		} else {
			for (Mails wp : arraylist) {
				if (wp.getTipo().toLowerCase(Locale.getDefault()).contains(charText)) {
					mails.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
}
