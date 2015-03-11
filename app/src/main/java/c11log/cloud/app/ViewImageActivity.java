package c11log.cloud.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import c11log.cloud.R;


public class ViewImageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Lägg till menyknappar i actionbaren
        getMenuInflater().inflate(R.menu.menu_view_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_delete:

                Item removeItem = (Item)getIntent().getParcelableExtra("item");
                if(removeItem != null) {
                    AlertDialog.Builder removeConvDlg = new AlertDialog.Builder(this);

                    removeConvDlg.setTitle(R.string.deleteConversation);
                    removeConvDlg.setMessage(getResources().getString(R.string.confirmDelete) + " " + removeItem.getName() + "?");
                    removeConvDlg.setCancelable(true);
                    removeConvDlg.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Item removeItem = (Item) getIntent().getParcelableExtra("item");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("removeItem", removeItem);
                            startActivity(intent);
                        }
                    });
                    removeConvDlg.setNegativeButton("No", null);
                    removeConvDlg.create().show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Gör så att up-knappen i actionbaren fungerar som bakåtknappen.
        onBackPressed();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private Item item;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);
            Intent intent = getActivity().getIntent();
            item = (Item)intent.getParcelableExtra("item");
            if(item != null) {
                String imgPath = item.getImage();
                String location = item.getLocation();
                String userName = item.getName();
                ImageView img = (ImageView) rootView.findViewById(R.id.picture);
                TextView loc = (TextView) rootView.findViewById(R.id.location);
                TextView uname = (TextView) rootView.findViewById(R.id.username);
                loc.setText(location);
                uname.setText(userName);
                if (img != null) {
                    img.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                }

            }
            FloatingActionButton button;
            button = (FloatingActionButton) rootView.findViewById(R.id.setter);
            button.setSize(FloatingActionButton.SIZE_NORMAL);
            button.setColorNormalResId(R.color.accent);
            button.setColorPressedResId(R.color.primary_dark);
            button.setIcon(R.drawable.reply);
            button.setStrokeVisible(true);
            button.setOnClickListener(new FabClickListener(this));
                return rootView;
        }

        public void onActivityResult(int requestCode,
                                     int resultCode,
                                     Intent data) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity().getApplicationContext(), "Your picture was sent to " + item.getName(),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("removeItem",item);
                    startActivity(intent);
                }
            }
        }

    }

    private static class FabClickListener implements View.OnClickListener {
        private PlaceholderFragment placeholderFragment;

        public FabClickListener(PlaceholderFragment placeholderFragment) {
            this.placeholderFragment = placeholderFragment;
        }

        @Override
        public void onClick(View v) {

            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /* Bestäm vart filen ska sparas
             * Observera att Kamera-appen måste kunna skriva till platsen
             * där filen finns
             */
            SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_HH_mm_ss");
            Date now = new Date();
            File photosDir = placeholderFragment.getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
            String fileName = photosDir +"/"+formatter.format(now) + ".jpg";
            System.out.println(fileName);
            File file = new File(fileName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            //Starta aktiviteten
            placeholderFragment.startActivityForResult(
                    i, 1);

        }
    }
}