package com.schoolstuff.adrian.koshertracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity {
    List<RestAddress> geopoints;
    Toolbar toolbar;
    GoogleMap gMap;
    ListView list;
    Spinner type_pref, cert_pref, prox_pref;
    MapFragment map_fragment;
    static Location current_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init_components();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public void init_components() {
        this.toolbar = findViewById(R.id.toolbar2View);
        this.type_pref = findViewById(R.id.type_pref);
        this.cert_pref = findViewById(R.id.cert_pref);
        this.prox_pref = findViewById(R.id.prox_pref);
        this.list = findViewById(R.id.listView);

        DBThread thread=new DBThread();
        this.geopoints=thread.init_thread();

        setSupportActionBar(toolbar);
        init_spinners();
        init_map();
        init_list();

    }

    public void init_spinners() {
        //spinner data according to the appropriate spinner
        String[] types = {"Choose Type", "Milk", "Meat", "Parve", "Whatever"};
        String[] certs = {"Choose Certification", "Kosher", "Unimportant"};
        String[] prox = {"Choose Proximity", "1 KM", "5 KM", "10 KM", "20 KM", "50 KM", "100 KM"};

        ArrayAdapter<String> types_adapter = new ArrayAdapter<>(MapActivity.this, android.R.layout.simple_spinner_dropdown_item, types);
        ArrayAdapter<String> certs_adapter = new ArrayAdapter<>(MapActivity.this, android.R.layout.simple_spinner_dropdown_item, certs);
        ArrayAdapter<String> prox_adapter = new ArrayAdapter<>(MapActivity.this, android.R.layout.simple_spinner_dropdown_item, prox);

        this.type_pref.setAdapter(types_adapter);
        this.cert_pref.setAdapter(certs_adapter);
        this.prox_pref.setAdapter(prox_adapter);
    }
    public void init_map() {
        map_fragment=((MapFragment)getFragmentManager().findFragmentById(R.id.fragment));
        final Context context=MapActivity.this;
        map_fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            //initializes the map to show the user where he currently is
            //TODO the next step is to get the long and lat of every restaurant I have, and show markers of the ones that fit the users preferences
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions((MapActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                MapActivity.current_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i("#LocationStatus", current_location == null ? "Null" : "Not");
                double lng,lat;
                if (current_location != null) {
                    lng = current_location.getLongitude();
                    lat = current_location.getLatitude();
                }
                else  {
                    //popestii romania
                    lng=51.5033640;
                    lat=-0.1276250;
                }
                gMap.setMyLocationEnabled(true);
                gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                if (geopoints==null)
                    Log.i("#GeoPoints","Its null");
                if (geopoints.size()==0)
                    Log.i("#SizeStatus","Its empty god damn thread");
                else {Log.i("#SizeStatus","" +geopoints.size());}
                for (RestAddress add:geopoints){
                    if (add==null)
                        Log.i("#It is null","Its null");
                    gMap.addMarker(new MarkerOptions().position(new LatLng(add.latitude,add.longitude)));
                    Log.i("#MarkerCreation",add.toString());
                }
            }
        });
    }
    public void init_list(){
        String typePref=type_pref.getSelectedItem().toString();
        String certPref=cert_pref.getSelectedItem().toString();
        String distPref=prox_pref.getSelectedItem().toString();


    }
    public List<RestAddress> getPreferredRest(String typePref,String certPref,String distPref){
        List<RestAddress> preferred;
        for (RestAddress add:geopoints){
            if(add.)
        }
    }
    //start of custom classes
    public class RestAddress {
        //necessery fields for geopoint searching
        String city;
        String address_name;
        String address_num;
        static final String COUNTRY="Israel";
        double longitude;
        double latitude;
        //extra fields to provide extra information about the reaurant
        String name;
        int cert;

        public RestAddress(String city, String address_name,String address_num) {
            this.city=city;
            this.address_name=address_name;
            this.address_num=address_num;
            getGeopoint();
        }

        public void getGeopoint() {
            String full_address=this.address_num+" "+this.address_name+" "+this.city+" "+RestAddress.COUNTRY;
            Geocoder geoCode=new Geocoder(MapActivity.this, Locale.getDefault());
            try{
                //assigns the longitude and the latitude of the address of the restaurant
                List<Address> addresses=geoCode.getFromLocationName(full_address,5);
                if  (addresses.size()>0){
                    this.longitude=addresses.get(0).getLongitude();
                    this.latitude=addresses.get(0).getLatitude();
                    Log.i("#Geopoint",full_address+" "+this.latitude+" "+this.longitude);
                }
            }
            catch (Exception e){
                Toast.makeText(MapActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                Log.i("#Geopoint",e.getMessage());
            }
        }
        //calculates the distance between the restaurant and the given location
        //using Haversine formula to calculate the distance between 2 geographic points on the globe using earth's radius
        public double getDistance(Location current_location){

            double loc_long=current_location.getLongitude();
            double loc_lat=current_location.getLatitude();
            final double radius=6378.1;
            //the delta values of longitudes and lattitudes
            double dlong=Math.toRadians(this.longitude-loc_long);
            double dlat=Math.toRadians(this.latitude-loc_lat);
            //mathemtical constants related to haversine formula
            //second expression in the formula
            double a=Math.sin(dlat/2)*Math.sin(dlat/2)+Math.cos(Math.toRadians(loc_lat))*Math.cos(this.latitude)*Math.sin(dlong/2)*Math.sin(dlong/2);
            //first expression in the formula, dependant on the first expression
            double c = 2*Math.asin(Math.sqrt(a));
            //result in kilometres
            double result=radius*c;
            Log.i("#Distance",""+result);
            return result;
        }
        @Override
        public String toString(){
            return this.city+" "+this.address_name+" "+this.address_num+" "+RestAddress.COUNTRY+" "+this.longitude+" "+this.latitude+"\n";
        }
    }
    public class DBThread{
        private List<RestAddress> rests;
        public DBThread(){ }

        //TODO get all restaurants from the database
        public void do_it(){
            DBTools database=new DBTools(MapActivity.this);
            database.openRead();
            int size=database.get_count();
            for (int i=0;i<size;i++) {
                DataIndexer data=database.fetch_data(i+1);
                String the_address=data.address;
                String[] split=the_address.split(" ");
                RestAddress rest=new RestAddress(data.city,split.length==2 ? split[0]:split[0]+" "+split[1],split[split.length-1]);
                rest.name=data.name;
                rest.cert=data.cert;
                rests.add(rest);
            }
            database.close();
        }

        public List<RestAddress> init_thread(){
            rests=new ArrayList<>();
            do_it();
            return rests;
        }
    }
    public class CustomAdapter extends ArrayAdapter{
        private int layout;
        private Context mContext;
        private List<RestAddress> mylist;
        public CustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.layout=resource;
            mContext=context;
            mylist=objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder views=new ViewHolder();
            if (convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(this.layout,null);
                views.name=convertView.findViewById(R.id.restName);
                views.city=convertView.findViewById(R.id.cityName);
                views.address=convertView.findViewById(R.id.distText);
                views.dist=convertView.findViewById(R.id.distText);
                views.cert=convertView.findViewById(R.id.certText);
                convertView.setTag(views);
            }
            else {
                views=(ViewHolder)convertView.getTag();
            }
            views.name.setText(this.mylist.get(position).name);
            views.city.setText(this.mylist.get(position).city);
            views.address.setText(this.mylist.get(position).address_name+" "+this.mylist.get(position).address_num);
            views.dist.setText(Double.toString(this.mylist.get(position).getDistance(MapActivity.current_location)));
            views.cert.setText(this.mylist.get(position).cert);

            return convertView;
        }
        public class ViewHolder{
            TextView name,city,address,dist,cert;
        }
    }

}

