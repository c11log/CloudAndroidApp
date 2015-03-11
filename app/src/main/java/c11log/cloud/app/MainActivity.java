package c11log.cloud.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import c11log.cloud.R;
import c11log.cloud.sign.SignInActivity;

public class MainActivity extends FragmentActivity {

    private   MyAdapter adapter;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new ListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
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
            case R.id.action_settings:
                displaySignOutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displaySignOutDialog() {
        AlertDialog.Builder signOutDlg  = new AlertDialog.Builder(this);
        signOutDlg.setTitle(R.string.signout);
        signOutDlg.setMessage(R.string.confirmSignOut);
        signOutDlg.setCancelable(true);
        signOutDlg.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        signOutDlg.setNegativeButton("No", null);
        signOutDlg.create().show();
    }

    public void onBackPressed() {
            moveTaskToBack(true);
    }
}

//
//import android.content.Context;
//import android.content.Intent;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.getbase.floatingactionbutton.FloatingActionButton;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//
//public class MainActivity extends ActionBarActivity  implements MyAdapter.ViewHolder.ClickListener {
//
//    private MyAdapter adapter;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
//    private File photosDir;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        File dir=MainActivity.this.getExternalFilesDir(
//                Environment.DIRECTORY_PICTURES);
//        photosDir =new File(dir+"/");
//        adapter = new MyAdapter(this);
//        Intent intent = getIntent();
//
//        Item removeItem = (Item)intent.getParcelableExtra("removeItem");
//        if(removeItem != null){
//            System.out.println("removeItem");
//            adapter.removeItemFromImage(removeItem);
//
//            adapter.notifyDataSetChanged();
//
//        }
//
//        if(savedInstanceState == null){
//            if (photosDir.exists()) {
//                File[] files = photosDir.listFiles();
//                for (int i = 0; i < files.length; ++i) {
//                    File file = files[i];
//                    System.out.println(file.getAbsoluteFile());
//                    if (!file.isDirectory()) {
//
//                        adapter.addItem(getLocation(), file.getAbsolutePath());
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//        }
//
//        FloatingActionButton button;
//        button = (FloatingActionButton) findViewById(R.id.setter);
//        button.setSize(FloatingActionButton.SIZE_NORMAL);
//        button.setColorNormalResId(R.color.accent);
//        button.setColorPressedResId(R.color.primary_dark);
//        button.setIcon(R.drawable.camera2);
//        button.setStrokeVisible(true);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//	    			/* Bestäm vart filen ska sparas
//	    			 * Observera att Kamera-appen måste kunna skriva till platsen
//	    			 * där filen finns
//	    			 */
//                SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_HH_mm_ss");
//                Date now = new Date();
//                String fileName = photosDir +"/"+formatter.format(now) + ".jpg";
//                System.out.println(fileName);
//                File file = new File(fileName);
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//
//                //Starta aktiviteten
//                MainActivity.this.startActivityForResult(
//                        i, CAPTURE_IMAGE_REQUEST_CODE);
//
//            }
//        });
//
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        // /You will setup the action bar with pull to refresh layout
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (photosDir.exists()) {
//                    File[] files = photosDir.listFiles();
//                    for (int i = 0; i < files.length; ++i) {
//                        File file = files[i];
//                        System.out.println(file.getAbsoluteFile());
//                        if (!file.isDirectory()) {
//                            adapter.addItem(getLocation(), file.getAbsolutePath());
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//        if(savedInstanceState != null ){
//
//            List<Item> items = savedInstanceState.getParcelableArrayList("items");
//            if(items != null) {
//                adapter.addListOfItems(items);
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//
//    }
//    protected void onActivityResult(int requestCode,
//                                    int resultCode,
//                                    Intent data) {
//        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(getApplicationContext(), "Your picture was sent to three random people",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private String getLocation(){
//        String finalAddress ="Not Found";
//        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
//        StringBuilder builder = new StringBuilder();
//        try {
//            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null)
//            {
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//
//
//
//                List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
//                int maxLines = address.get(0).getMaxAddressLineIndex();
//
//                if(maxLines > 0){
//                    String city = address.get(0).getLocality();
//                    builder.append(city);
//                    builder.append(", ");
//                    String country = address.get(0).getCountryName();
//                    builder.append(country);
//                    builder.append(" ");
//                }
//                //This is the complete address (city, country).
//                finalAddress= builder.toString();
//            }
//        } catch (IOException e) {
//        }
//
//        return finalAddress;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onItemClicked(int position) {
//        Intent intent = new Intent(this, ViewImageActivity.class);
//        intent.putExtra("item", adapter.getItem(position));
//        startActivity(intent);
//
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//
//
//        savedInstanceState.putParcelableArrayList("items", (java.util.ArrayList<? extends android.os.Parcelable>) adapter.getListOfItem());
//        // etc.
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public boolean onItemLongClicked(int position) {
//        adapter.removeItem(position);
//        return true;
//    }
//
//    public void onBackPressed() {
//            moveTaskToBack(true);
//    }
//}
