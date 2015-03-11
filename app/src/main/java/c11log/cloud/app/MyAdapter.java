package c11log.cloud.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import c11log.cloud.R;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Item> items;
    private ViewHolder.ClickListener clickListener;

    public MyAdapter(ViewHolder.ClickListener clickListener) {
        super();
        this.clickListener = clickListener;
        items = new ArrayList<>();
    }

    public void addListOfItems(List<Item> items){
        this.items = items;
    }

    public void removeItemFromImage(Item item){
        new File(item.getImage()).delete(); // tabort bilden från bilder
        items.remove(item);
    }

    public void addItem(String location, String image){
        File file = new File(image);
        String lastModDate =null;
        if(file.exists()) //Extra check, Just to validate the given path
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d HH:mm");
            lastModDate = sdf.format(new Date(file.lastModified()));
        }
        if(!items.contains(new Item("Linus Oberg", location, lastModDate, image))) {
            items.add(new Item("Linus Oberg", location, lastModDate, image));
        }
    }

    public Item getItem(int position){
       return items.get(position);
    }

    public void removeItem(int position) {
        new File(items.get(position).getImage()).delete();
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout =  R.layout.item;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.location.setText(item.getLocation());
        holder.name.setText(item.getName());
        holder.date.setText(item.getDate());
        new LoadImage(holder.image, item.getImage()).execute();
    }

    private class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imv;
        private String path;

        public LoadImage(ImageView imv, String path) {
            this.imv = imv;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            int bwidth=bmp.getWidth();
            int bheight=bmp.getHeight();
            int swidth=imv.getWidth();
            int new_width=swidth;
            int new_height = (int) Math.floor((double) bheight *( (double) new_width / (double) bwidth));
            Bitmap bitmap = Bitmap.createScaledBitmap(bmp, new_width,new_height, true);
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null && imv != null){
                imv.setVisibility(View.VISIBLE);
                imv.setImageBitmap(result);
            }else{
                imv.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        TextView name;
        TextView date;
        TextView location;
        ImageView image;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            location = (TextView) itemView.findViewById(R.id.subtitle);
            image = (ImageView) itemView.findViewById(R.id.list_image);
            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getPosition());
            }
            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }
    }
}