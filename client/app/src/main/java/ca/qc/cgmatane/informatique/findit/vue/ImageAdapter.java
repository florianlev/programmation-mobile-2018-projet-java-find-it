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

    //liste contenant nos url
    private String[] listeUrl= accesseurGalerie.recuperereListePourImageAdapteur();

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

    //créé un ImageView pour tous les éléments référencés par l'Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            //si ce n'est pas recyclé, initialise des attributs
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
}