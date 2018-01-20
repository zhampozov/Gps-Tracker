package com.example.nurgali.gps_tracker.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.R;
import com.example.nurgali.gps_tracker.UserRelations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzbmn;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    DatabaseReference refDatabase;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Marker marker;
    Double markerLatitude = 0.0;
    Double userLatitude = 0.0;
    Double markerLongitude = 0.0;
    Double userLongitude = 0.0;
    String foundUid;
    Toast mToast;
    Circle circle;
    private static final int MY_PERMISSION_FINE_LOCATION_REQUEST = 101;
    private boolean permissionIsGranted = false;
    Boolean clicked = false;
    Button btnSetRadius, btnSetMapNormal, btnSetMapTerrain, btnSetMapSatellite, btnSetMapHybrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        foundUid = intent.getStringExtra(FindUserActivity.EXTRA_MESSAGE);

        btnSetRadius = (Button) findViewById(R.id.btnSetRadius);
        btnSetMapNormal = (Button) findViewById(R.id.btnSetMapNormal);
        btnSetMapTerrain = (Button) findViewById(R.id.btnSetMapTerrain);
        btnSetMapSatellite = (Button) findViewById(R.id.btnSetMapSatellite);
        btnSetMapHybrid = (Button) findViewById(R.id.btnSetMapHybrid);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refDatabase = FirebaseDatabase.getInstance().getReference().child("location");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSetMapNormal:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.btnSetMapTerrain:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case R.id.btnSetMapSatellite:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.btnSetMapHybrid:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case R.id.btnSetRadius:
                        if (markerLatitude != 0 && markerLongitude != 0){
                            circle = mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(markerLatitude, markerLongitude))
                                    .radius(800)
                                    .strokeColor(Color.RED)
                            );
                            clicked = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Выберите маркер", Toast.LENGTH_SHORT).show();
                        }

                        break;

                }
            }
        };

        btnSetMapNormal.setOnClickListener(onClickListener);
        btnSetMapTerrain.setOnClickListener(onClickListener);
        btnSetMapSatellite.setOnClickListener(onClickListener);
        btnSetMapHybrid.setOnClickListener(onClickListener);
        btnSetRadius.setOnClickListener(onClickListener);
    }

    @Override
    public synchronized void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        System.out.println("/////////************ " + foundUid);
        final Map<String, Marker> markers = new HashMap();

        if (foundUid != null) {

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    FirebaseDatabase.getInstance().getReference().child("location").child(foundUid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            System.out.println("ds /*//*/*/*/*/*:  " + dataSnapshot);

                            Location l = null;

                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                l = d.getValue(Location.class);
                                System.out.println("lat====: " + l.getLatitude() + "longi=====: " + l.getLongitude());
                            }

                            userLongitude = l.getLongitude();
                            userLatitude = l.getLatitude();

                            System.out.println("userLat: " + userLatitude + " ulong" + userLongitude);

                            LatLng newLocation = new LatLng(userLatitude ,userLongitude);
                            System.out.println("new ;" + newLocation);
                            Marker uAmarker = mMap.addMarker(new MarkerOptions()
                                    .title("Friend")
                                    .position(newLocation));
                            markers.put(dataSnapshot.getKey(), uAmarker);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            };

            Thread br = new Thread(r);
            br.start();

        }

       /*     findfriend();

            final Map<String, Marker> markers = new HashMap();

                    LatLng newLocation = new LatLng(markerLatitude, markerLongitude);
                    System.out.println("new ;" + newLocation);
                    Marker uAmarker = mMap.addMarker(new MarkerOptions()
                            .title(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .position(newLocation));
                    markers.put("foundMarker", uAmarker);
                }*/
    }

    /*public void findfriend(){
    }*/


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Местоположение не найдено", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            /*CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 13);*/
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                refDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latitude").setValue(ll.latitude);
                refDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("longitude").setValue(ll.longitude);
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            /*mMap.animateCamera(update);*/
            if (marker != null) {
                marker.remove();
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

                        /*CIRCLE*/
            if (clicked) {
                float[] distance = new float[2];
                Location.distanceBetween(latLng.latitude, latLng.longitude, circle.getCenter()
                        .latitude, circle.getCenter().longitude, distance);

                if (distance[0] <= circle.getRadius()) {
                    mToast.makeText(getApplicationContext(), "inside", Toast.LENGTH_LONG).show();

                } else {
                    //Toast.makeText(getApplicationContext(), "outside", Toast.LENGTH_LONG).show();
                }
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerLatitude = marker.getPosition().latitude;
                markerLongitude = marker.getPosition().longitude;
                System.out.println("marker clicked1 : " + markerLatitude);
                return false;
            }
        });
    }


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    /*Geocoding*/
    public void onClickShowPlace(View view) throws IOException {
        EditText editText = (EditText) findViewById(R.id.editText);
        String location = editText.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        if (!list.isEmpty()) {
            Address address = list.get(0);
            String locality = address.getLocality();

            Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocationZoom(lat, lng, 15);
            setMarker(locality, lat, lng);
        } else {
            Toast.makeText(this, "Адрес не найден", Toast.LENGTH_SHORT).show();
        }
    }


    /*Marker*/
    private void setMarker(String locality, double lat, double lng) {
        if (marker != null) {
            removeEverything();
        }
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(new LatLng(lat, lng));
        marker = mMap.addMarker(options);

        circle = drawCircle(new LatLng(lat, lng));

    }
    /*Circle*/
    private Circle drawCircle(LatLng latLng) {

        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(200)
                .fillColor(0x33FF0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);
        return mMap.addCircle(options);
    }

    private void removeEverything() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        if (circle != null) {
            circle.remove();
            circle = null;
        }
    }


    /*Menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeRadius:
                if (circle != null) {
                    circle.remove();
                }
                break;
            case R.id.btnAbout:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                finish();
                Toast.makeText(this, "Информация о разработчике", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnLogOut:
                logout();
                break;
            case R.id.btnSettings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
                Toast.makeText(this, "Настройки", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnFindUser:
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
                finish();
                Toast.makeText(this, "Поиск пользователя", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnUserRelations:
                startActivity(new Intent(getApplicationContext(), UserRelations.class));
                finish();
                Toast.makeText(this, "Друзья", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из системы")
                .setMessage("Вы уверены что хотите выйти из системы?")
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseAuth.getInstance().signOut();
                        Log.d("Logout", "Clicked");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        finish();

                    }
                }).create().show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        markerLatitude = marker.getPosition().latitude;
        markerLongitude = marker.getPosition().longitude;
        System.out.println("marker is pressed" + markerLatitude);
        return false;
    }

    public String getMarkerName(){
        String mData = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        System.out.println("=====:  " + mData);
        return mData;
    }
}


