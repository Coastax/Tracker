package com.example.tracker.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tracker.DisplayPathEvent;
import com.example.tracker.R;
import com.example.tracker.SavePathEvent;
import com.example.tracker.TravelPath;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;


public class MapsFragment extends Fragment {

    private InfoViewModel mPageViewModel;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location mNewLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private TravelPath currentPath = new TravelPath();
    private TravelPath displayPath;
    private ExtendedFloatingActionButton startBtn;
    private FloatingActionButton saveBtn;
    private FloatingActionButton clearBtn;
    private FloatingActionButton shareBtn;
    private boolean recording = false;
    private boolean displaying = false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String m_Text = "";
    DateFormat df = new SimpleDateFormat("MM/dd HH:mm:ss");
    DateFormat timeStampFormat = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(500); // 0.5s
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                    checkLocationPermission();
                }
            } else {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    };

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                //Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mNewLocation = location;


                float delta = mNewLocation.distanceTo(mLastLocation);
                if (recording) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    currentPath.addLocation(location);
                    if(!displaying) {
                        mPageViewModel.setDisplayPath(currentPath);
                        redrawPolyLine(currentPath);
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    }
                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPageViewModel = ViewModelProviders.of(requireActivity()).get(InfoViewModel.class);
        mPageViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startBtn = getView().findViewById(R.id.startBtn);
        saveBtn = getView().findViewById(R.id.saveBtn);
        clearBtn = getView().findViewById(R.id.clearBtn);
        shareBtn = getView().findViewById(R.id.shareBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!recording) {
                mGoogleMap.clear();
                //currentPath.clearPath();
                currentPath = new TravelPath();
                Snackbar.make(view, "Start Recording Path", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //startBtn.getDrawable().mutate().setTint(ContextCompat.getColor(getContext(), R.color.red));
                startBtn.setText("  STOP");
                startBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                startBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C62828")));
                saveBtn.setVisibility(View.INVISIBLE);
                clearBtn.setVisibility(View.INVISIBLE);
                shareBtn.setVisibility(View.INVISIBLE);
                recording = true;
                //String time = df.format(new Date());
                //Toast.makeText(getContext(),time,Toast.LENGTH_LONG).show();
                currentPath.setStartTime(df.format(new Date()));
                if(mNewLocation != null)
                    currentPath.addLocation(mNewLocation);


            } else {
                recording = false;
                Snackbar.make(view, "Recording Stopped", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                clearBtn.setVisibility(View.VISIBLE);
                //startBtn.getDrawable().mutate().setTint(ContextCompat.getColor(getContext(), R.color.gray));
                startBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFFFF")));
                startBtn.setText("  START");
                startBtn.setTextColor(Color.parseColor("#FF000000"));
                saveBtn.setVisibility(View.VISIBLE);
                shareBtn.setVisibility(View.VISIBLE);
                currentPath.setEndTime(df.format(new Date()));
                currentPath.setEndPoint();
            }
            mPageViewModel.setDisplayPath(currentPath);
            redrawPolyLine(currentPath);
        }

        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter the path name");
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_name, (ViewGroup) getView(), false);
                // Set up the input
                final EditText input = viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);
                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        currentPath.setSavedName(m_Text);
                        saveCurrentPath();
                        //Toast.makeText(getContext(),m_Text,Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("PathToSave", currentPath);
                        //SavedPathFragment savedPathFragment = (SavedPathFragment) getParentFragmentManager().findFragmentById(R.id.list);
                        //savedPathFragment.setArguments(bundle);
                        getParentFragmentManager().setFragmentResult("save", bundle);
                        EventBus.getDefault().post(new SavePathEvent(currentPath));
                        saveBtn.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.clear();
                clearBtn.setVisibility(View.INVISIBLE);
                if(displaying){
                    displaying = false;
                    redrawPolyLine(currentPath);
                    startBtn.setVisibility(View.VISIBLE);
                    if(!recording){
                        if(currentPath.ended()){
                            saveBtn.setVisibility(View.VISIBLE);
                            shareBtn.setVisibility(View.VISIBLE);
                            redrawPolyLine(currentPath);
                        }
                        else
                            shareBtn.setVisibility(View.INVISIBLE);
                    }
                    else {
                        redrawPolyLine(currentPath);
                        shareBtn.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    if(!recording && currentPath.ended()){
                        currentPath.clearPath();
                        saveBtn.setVisibility(View.INVISIBLE);
                        shareBtn.setVisibility(View.INVISIBLE);
                    }
                }
                mPageViewModel.setDisplayPath(currentPath);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if(displaying) {
                    message = "I have travelled for " + displayPath.getTravelledDistance() + "m!";
                }
                else{
                    message = "I have travelled for " + currentPath.getTravelledDistance() + "m!";
                }
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.putExtra("sms_body", message);
                    intent.setData(Uri.parse("sms:"));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Log.d("Error" , "Error");
                }
            }
        });

        mapFrag =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) {
            mapFrag.getMapAsync(callback);
        }

       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient((Context)getActivity());
        mLastLocation = new Location(LocationManager.GPS_PROVIDER);
        mLastLocation.setLatitude(49.276330);
        mLastLocation.setLongitude(-122.920268);
        currentPath = new TravelPath();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveCurrentPath(){
        Gson gson = new Gson();
        String json = gson.toJson(currentPath);
        Context context = getActivity();

        String timeStamp = timeStampFormat.format(new Date());
        currentPath.setSavedFileName(currentPath.getSavedName() + timeStamp + ".json");

        String path = context.getFilesDir() + "/SavedPath/";
        Log.i("PATH", path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
            //Toast.makeText(getActivity().getBaseContext(), "Directory DOE, making one", Toast.LENGTH_LONG).show();
        }
        try {
            Writer output = null;
            File file = new File(dir.getAbsolutePath() + "/" + currentPath.getSavedFileName());
            //Log.i("FULL PATH", dir.getAbsolutePath() + "/" + currentPath.getSavedName() + timeStamp+ ".json");

            output = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            output.write(json);
            output.close();
            Toast.makeText(context, "Path saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void redrawPolyLine(TravelPath path) {
        mGoogleMap.clear();

        PolylineOptions options = new PolylineOptions().width(10).color(Color.CYAN).geodesic(true);
        for (int i = 0; i < path.getSize(); i++){
            LatLng point = new LatLng(path.getLocationList().get(i).getLatitude(),
                    path.getLocationList().get(i).getLongitude());
            options.add(point);
        }
        mGoogleMap.addPolyline(options);

        if(path.getStartPoint() != null) {
            MarkerOptions startMaker = new MarkerOptions();
            Location startPoint = path.getStartPoint();
            startMaker.position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude()));
            startMaker.title("Start");
            startMaker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mGoogleMap.addMarker(startMaker);
        }


        if (path.getEndPoint() != null) {
            Location endPoint = path.getEndPoint();
            MarkerOptions endMaker = new MarkerOptions();
            endMaker.position(new LatLng(endPoint.getLatitude(), endPoint.getLongitude()));
            endMaker.title("End");
            endMaker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mGoogleMap.addMarker(endMaker);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisplayPath(DisplayPathEvent received){
        displayPath = received.getPathToDisplay();
        Log.i("DisplayPath", valueOf(displayPath.getStartPoint().getLatitude()));

        mGoogleMap.clear();
        redrawPolyLine(displayPath);
        mPageViewModel.setDisplayPath(displayPath);
        Toast.makeText(getContext(), "Path Loaded", Toast.LENGTH_SHORT).show();
        displaying = true;
        startBtn.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);
        clearBtn.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(View.VISIBLE);
        LatLng latLng = new LatLng(displayPath.getStartPoint().getLatitude(), displayPath.getStartPoint().getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        //adapter.notifyDataSetChanged();
    }
}
