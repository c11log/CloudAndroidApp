package c11log.cloud.app;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import c11log.cloud.R;


public class ListFragment  extends Fragment implements  MyAdapter.ViewHolder.ClickListener {
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
    private File photosDir;


    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File dir=this.getActivity().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        photosDir =new File(dir+"/");

        if(savedInstanceState != null ){
            List<Item> items = savedInstanceState.getParcelableArrayList("items");
            if(items != null) {
                adapter.addListOfItems(items);
                adapter.notifyDataSetChanged();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {

            Toast.makeText(this.getActivity().getApplicationContext(), "Your picture was sent to three random people",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (photosDir.exists()) {
                    File[] files = photosDir.listFiles();
                    for (int i = 0; i < files.length; ++i) {
                        File file = files[i];
                        System.out.println(file.getAbsoluteFile());
                        if (!file.isDirectory()) {
                            adapter.addItem(getLocation(), file.getAbsolutePath());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_layout, parent, false);
        adapter = new MyAdapter(this);

        Item removeItem = (Item)getActivity().getIntent().getParcelableExtra("removeItem");
        if(removeItem != null){
            adapter.removeItemFromImage(removeItem);
            adapter.notifyDataSetChanged();
        }

        FloatingActionButton button;
        button = (FloatingActionButton) v.findViewById(R.id.setter);
        button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setColorNormalResId(R.color.accent);
        button.setColorPressedResId(R.color.primary_dark);
        button.setIcon(R.drawable.camera2);
        button.setStrokeVisible(true);
        button.setOnClickListener(new FabClickListener());


        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        new LoadImagesTask().execute();
        return v;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra("item", adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        adapter.removeItem(position);
        return true;
    }


    private String getLocation(){
        String finalAddress ="Not Found";
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null)
            {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();



                List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
                int maxLines = address.get(0).getMaxAddressLineIndex();

                if(maxLines > 0){
                    String city = address.get(0).getLocality();
                    builder.append(city);
                    builder.append(", ");
                    String country = address.get(0).getCountryName();
                    builder.append(country);
                    builder.append(" ");
                }
                //This is the complete address (city, country).
                finalAddress= builder.toString();
            }
        } catch (IOException e) {
        }

        return finalAddress;
    }

    private class LoadImagesTask extends AsyncTask<Integer, Void, Void> {


        private LoadImagesTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (photosDir.exists()) {
                File[] files = photosDir.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if (!file.isDirectory()) {
                        if(adapter != null) {
                            adapter.addItem(getLocation(), file.getAbsolutePath());
                        }
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            super.onPostExecute(response);
            if(adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class FabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /* Bestäm vart filen ska sparas
             * Observera att Kamera-appen måste kunna skriva till platsen
             * där filen finns
             */
            SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_HH_mm_ss");
            Date now = new Date();
            String fileName = photosDir +"/"+formatter.format(now) + ".jpg";
            System.out.println(fileName);
            File file = new File(fileName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            //Starta aktiviteten
            startActivityForResult(i, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }
}

