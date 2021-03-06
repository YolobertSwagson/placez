package wusadevelopment.albert.com.placez;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Albert on 22.08.2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Place> data;
    private ClickListener cListener;
    private Controller instance;

    public CustomAdapter(Context context, List<Place> data){

        this.inflater = LayoutInflater.from(context);
        this.data=data;
        this.instance = Controller.getInstance(context);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.customelement, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Place current = data.get(position);

        holder.placeName.setText(current.getName());
        holder.placeDescription.setText(current.getDescription());
        if(current.getPicture() != null) {
            holder.placeImage.setImageBitmap(instance.decodeBase64(current.getPicture()));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView placeName;
        TextView placeDescription;
        ImageView placeImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            placeName= (TextView) itemView.findViewById(R.id.PlaceName);
            placeDescription= (TextView) itemView.findViewById(R.id.PlaceDescription);
            placeImage= (ImageView) itemView.findViewById(R.id.PlaceImage);

        }

        @Override
        public void onClick(View v) {

            if (cListener != null){
                cListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

    public void setClickListener(ClickListener clickListener){
        this.cListener = clickListener;
    }

}
