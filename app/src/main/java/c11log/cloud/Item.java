package c11log.cloud;

/**
 * Created by Maria on 2015-03-04.
 */
public class Item {

        private int imageUrl;

        private String title;
        private String subtitle;

        Item(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }


}
