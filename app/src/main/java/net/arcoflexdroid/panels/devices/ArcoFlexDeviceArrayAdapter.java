package net.arcoflexdroid.panels.devices;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexRemoteSearchExplorer;

import java.util.List;

import static mess056.messH.*;

public class ArcoFlexDeviceArrayAdapter extends ArrayAdapter<ArcoFlexDeviceItem> implements TextWatcher {

    private Context c;
    private int id;
    private List<ArcoFlexDeviceItem> items;
    int listPosititon;

    static class ViewHolder {
        protected TextView text;
        protected ImageView image;
        protected EditText editText;
        //protected CheckBox checkbox;
    }

    public ArcoFlexDeviceArrayAdapter(Context context, int textViewResourceId, List<ArcoFlexDeviceItem> objects) {
        super(context, textViewResourceId, objects);

        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public ArcoFlexDeviceItem getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listPosititon = position;
        ViewHolder viewHolder = null;

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);


        // INI
        viewHolder = new ViewHolder();
        viewHolder.text = (TextView) v.findViewById(R.id.TextView01);
        viewHolder.editText = (EditText) v.findViewById(R.id.EditText01);
        viewHolder.image = (ImageView) v.findViewById(R.id.fd_Icon1);

        viewHolder.editText.addTextChangedListener(this);

        v.setTag(viewHolder);
        v.setTag(R.id.EditText01, viewHolder.text);
        v.setTag(R.id.fd_Icon1, viewHolder.image);
        v.setTag(R.id.TextView01, viewHolder.text);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.editText.setTag(position);
        viewHolder.editText.setText(items.get(position).getValue());
        viewHolder.text.setText(items.get(position).getName());

        // END


        final ArcoFlexDeviceItem o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            ImageView _devIMG = (ImageView) v.findViewById(R.id.fd_Icon1);

            _devIMG.setTag(position);
            _devIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    System.out.println("Position: "+v.getTag());
                    try {
                        //o.setInlay( ArcoFlexRemoteSearchExplorer.getWOSInlay( o.getName() ) );
                        /*TODO*///                        ZXSpectrumFavorites.addFavorite(o);
                        System.out.println("IMPLEMENTAR addFavourite!!!!");
                    } catch (Exception e) {
                        e.printStackTrace( System.out );
                    }
                }});

            //CheckBox favoriteStar = (CheckBox) v.findViewById(R.id.btn_star);
            //favoriteStar.setVisibility(View.GONE);

            //String _img = "disk_image";

            //String uri = "drawable/" + o.getImage();
            //int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            //Drawable image = c.getResources().getDrawable(imageResource);
            //imageCity.setImageDrawable(image);
            //imageCity.setImageDrawable(ContextCompat.getDrawable(c, R.id.fd_Icon1));

            //System.out.println("1: "+c);

            try {
                //if (o.getImage().equals("directory_icon")) {
                _devIMG.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.disk_image));
                /*} else if (o.getImage().equals("file_icon")) {
                    imageCity.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.disk_image));
                    favoriteStar.setVisibility(View.VISIBLE);
                } else {
                    imageCity.setImageDrawable(ContextCompat.getDrawable(c, android.R.drawable.ic_menu_revert));
                    favoriteStar.setVisibility(View.GONE);
                }*/
            } catch (Exception e){
                e.printStackTrace(System.out);
            }

            if(t1!=null)
                t1.setText(o.getName());
            /*if(t2!=null)
                t2.setText(o.getData()+" "+o.getDate());

             */
                /*if(t3!=null)
                    t3.setText(o.getDate());*/

            if (_devIMG != null) {
                _devIMG.setTag(new Integer(position));
            }
        }
        return v;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //System.out.println("beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //System.out.println("onTextChanged");
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //System.out.println("afterTextChanged: "+items.get(listPosititon).getName()+"="+editable.toString());
        items.get(listPosititon).setValue(editable.toString());
    }
}


