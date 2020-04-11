package net.arcoflexdroid.panels.filebrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;

import net.arcoflexdroid.R;

import java.util.List;

public class ArcoFlexFileArrayAdapter  extends ArrayAdapter<ArcoFlexFileItem> {


    private Context c;
    private int id;
    private List<ArcoFlexFileItem> items;

    public ArcoFlexFileArrayAdapter(Context context, int textViewResourceId,
                                  List<ArcoFlexFileItem> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }
    public ArcoFlexFileItem getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        final ArcoFlexFileItem o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            //TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
            /* Take the ImageView from layout and set the city's image */
            ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
            //LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.iconLayout, null);
            ImageView favoriteStar = (ImageView) v.findViewById(R.id.file_add_favorites);
            favoriteStar.setTag(position);
            favoriteStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    //System.out.println("Position: "+v.getTag());
                    try {
                        o.setInlay( ArcoFlexRemoteSearchExplorer.getWOSInlay( o.getName() ) );
/*TODO*///                        ZXSpectrumFavorites.addFavorite(o);
                        System.out.println("IMPLEMENTAR addFavourite!!!!");
                    } catch (Exception e) {
                        e.printStackTrace( System.out );
                    }
                }});

            //CheckBox favoriteStar = (CheckBox) v.findViewById(R.id.btn_star);
            //favoriteStar.setVisibility(View.GONE);

            String uri = "drawable/" + o.getImage();
            int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            //Drawable image = c.getResources().getDrawable(imageResource);
            //imageCity.setImageDrawable(image);
            //imageCity.setImageDrawable(ContextCompat.getDrawable(c, R.id.fd_Icon1));

            //System.out.println("1: "+c);

            try {
                if (o.getImage().equals("directory_icon")) {
                    imageCity.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.folderxxhdpi));
                } else if (o.getImage().equals("file_icon")) {
                    imageCity.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.filexxhdpi));
                    favoriteStar.setVisibility(View.VISIBLE);
                } else {
                    imageCity.setImageDrawable(ContextCompat.getDrawable(c, android.R.drawable.ic_menu_revert));
                    favoriteStar.setVisibility(View.GONE);
                }
            } catch (Exception e){
                e.printStackTrace(System.out);
            }

            if(t1!=null)
                t1.setText(o.getName());
            if(t2!=null)
                t2.setText(o.getData()+" "+o.getDate());
                /*if(t3!=null)
                    t3.setText(o.getDate());*/

            if (favoriteStar != null) {
                favoriteStar.setTag(new Integer(position));
            }
        }
        return v;
    }


}

