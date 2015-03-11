package c11log.cloud.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String image;
    private String location;
    private String name;
    private String date;

    Item( String name, String location, String date, String imageUrl) {
        this.name=name;
        this.date=date;
        this.location=location;
        this.image=imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImageUrl(String imageUrl) {
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

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Item)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Item c = (Item) o;

        // Compare the data members and return accordingly
            return c.getImage().equalsIgnoreCase(this.getImage());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(image);

    }
    /**
     * Retrieving Die data from Parcel object
     **/
    private Item(Parcel in){

        this.name = in.readString();
        this.date = in.readString();
        this.location = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
