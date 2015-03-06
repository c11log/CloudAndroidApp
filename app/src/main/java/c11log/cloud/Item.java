package c11log.cloud;

import android.graphics.Bitmap;

/**
 * Created by Maria on 2015-03-04.
 */
public class Item {

    private Bitmap image;

    private String name;
    private String date;

    Item(String name, String date, Bitmap imageUrl) {
        this.name=name;
        this.date=date;
        this.image=imageUrl;
    }


    public Bitmap getImageUrl() {
        return image;
    }

    public void setImageUrl(Bitmap imageUrl) {
        this.image = imageUrl;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
