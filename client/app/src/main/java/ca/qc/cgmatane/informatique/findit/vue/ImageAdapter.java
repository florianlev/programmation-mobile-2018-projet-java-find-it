package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.qc.cgmatane.informatique.findit.accesseur.GalerieDAO;

public class ImageAdapter extends BaseAdapter {
    private GalerieDAO accesseurGalerie= GalerieDAO.getInstance();
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return listeUrl.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(270, 270));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.get().load(listeUrl[position]).into(imageView);

        return imageView;
    }

    //liste contenant nos url

    private String[] listeUrl= accesseurGalerie.recuperereListePourImageAdapteur();
}