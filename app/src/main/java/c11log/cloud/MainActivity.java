package c11log.cloud;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity  implements MyAdapter.ViewHolder.ClickListener {

    private MyAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
    private File f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File dir=MainActivity.this.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        f=new File(dir+"/"+"mypic.jpg");


        FloatingActionButton button;
        button = (FloatingActionButton) findViewById(R.id.setter);
        button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setColorNormalResId(R.color.accent);
        button.setColorPressedResId(R.color.primary_dark);
        button.setIcon(R.drawable.ic_action_camera_big);
        button.setStrokeVisible(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Floating Action Button", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    			/* Bestäm vart filen ska sparas
	    			 * Observera att Kamera-appen måste kunna skriva till platsen
	    			 * där filen finns
	    			 */
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                //Starta aktiviteten
                MainActivity.this.startActivityForResult(

                        i, CAPTURE_IMAGE_REQUEST_CODE);


            }
        });
        adapter = new MyAdapter(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // /You will setup the action bar with pull to refresh layout
       mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

      }



    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Bilden sparad till den plats vi angav i intentetIntent

                updateImageViewFromFile();

	           /* Hade vi inte angivit platsen så hade vi istället kunnat
	            * göra som följer. Vi kan dock få en bild av sämre kvalitet
	            * imView.setImageBitmap((Bitmap) data.getExtras().get("data"));
	            */
            } else if (resultCode == RESULT_CANCELED) {
                // Användaren valde att inte ta en bild
            } else {
                // Något gick fel
            }
        }
    }


    private void updateImageViewFromFile() {
       // ImageView imView=(ImageView) findViewById(R.id.imageView1);

        //Läs in bilden som nu bör finnas där vi sa att den skulle placeras
        Bitmap bm= BitmapFactory.decodeFile(f.getAbsolutePath());
        adapter.addItem(bm);
        adapter.notifyDataSetChanged();


        Uri uri = Uri.fromFile(f);
        System.out.println(uri);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(MainActivity.this, viewImage.class);
        intent2.putExtra("photoUri", adapter.getItem(position).getImageUrl());
        startActivity(intent2);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        adapter.removeItem(position);
        return true;
    }
}
