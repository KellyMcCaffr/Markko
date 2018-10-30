package bob.markkouserloginsignup;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by manel on 9/5/2017.
 * Modified by Kelly McCaffrey
 */


public class arrayAdapter extends ArrayAdapter{

    ArrayList<Drawable>itemList;

    public arrayAdapter(Context context, int resourceId, ArrayList<Drawable> items){

        super(context, resourceId, items);

        itemList=items;

    }
    public int getCount(){
        return super.getViewTypeCount();
    }
    public void add(Drawable D){
        itemList.add(D);
    }
    public void clear(){
        itemList=new ArrayList<Drawable>();
    }
    public void remove(int index){
        itemList.remove(index);
    }
    public View getView(int position, View convertView, ViewGroup parent){

        //Takes whatever image is in the list and displays it in the view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        ImageView image =  convertView.findViewById(R.id.image);

        if(itemList.size()>0) {
            image.setImageDrawable(itemList.get(position));
        }

        return convertView;
    }

}

