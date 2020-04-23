package net.arcoflexdroid.panels.devices;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

public class ArcoFlexDeviceArrayAdapter extends ArrayAdapter<ArcoFlexDeviceItem> {

    private Context c;
    private int id;
    private List<ArcoFlexDeviceItem> items;
    int listPosititon;


    public ArcoFlexDeviceArrayAdapter(Context context, int textViewResourceId, List<ArcoFlexDeviceItem> objects) {
        super(context, textViewResourceId, objects);

        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public ArcoFlexDeviceItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listPosititon = position;

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);


            final ArcoFlexDeviceItem o = items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                ImageView _devIMG = (ImageView) v.findViewById(R.id.fd_Icon1);
                final EditText edT = (EditText) v.findViewById(R.id.EditText01);

                edT.setTag(position);
                edT.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int _positionEd = (Integer) edT.getTag();
                        System.out.println("*************CHANGED: " + _positionEd);

                        System.out.println(items.get(_positionEd).getName());
                        items.get(_positionEd).setValue(s.toString());
                        System.out.println(items.get(_positionEd).getValue());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                _devIMG.setTag(position);
                _devIMG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        System.out.println("Position: " + v.getTag());
                        try {
                            //o.setInlay( ArcoFlexRemoteSearchExplorer.getWOSInlay( o.getName() ) );
                            /*TODO*///                        ZXSpectrumFavorites.addFavorite(o);
                            System.out.println("IMPLEMENTAR addFavourite!!!!");
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                    }
                });

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
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }

                if (t1 != null)
                    t1.setText(o.getName());
            /*if(t2!=null)
                t2.setText(o.getData()+" "+o.getDate());

             */
                /*if(t3!=null)
                    t3.setText(o.getDate());*/

                if (_devIMG != null) {
                    _devIMG.setTag(new Integer(position));
                }

                if (edT != null) {
                    edT.setTag(new Integer(position));
                }
            }

        }
        return v;

    }
}


