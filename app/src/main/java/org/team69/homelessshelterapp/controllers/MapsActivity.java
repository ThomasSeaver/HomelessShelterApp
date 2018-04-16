package org.team69.homelessshelterapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.ShelterList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"ALL", "FeatureEnvy"})
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap viewMap;
    private final ShelterList list = new ShelterList();
    private Map<String, String> restrictionsMap;
    private String userID;
    private Button listButton;
    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        restrictionsMap = (HashMap<String, String>) intent.getSerializableExtra("restrictionsMap");
        userID = intent.getStringExtra("userID");

        searchButton =  findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSearchActivity();
            }
        });

        listButton =  findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToShelterListActivity();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        viewMap = googleMap;
        //recenter camera initially
        LatLng atlanta = new LatLng(33.753, -84.390);
        viewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atlanta, 11));
        viewMap.setInfoWindowAdapter(new ShelterMapAdapter(this));

        //read shelter file then create shelter markers
        readShelterFile();
        if (restrictionsMap == null) {
            makeMarkers(ShelterList.getMap());
        } else {
            makeMarkers(list.getByRestriction(restrictionsMap.get("Gender"), restrictionsMap.get("AgeRange"), restrictionsMap.get("ShelterName")));
        }
    }

    @SuppressWarnings("FeatureEnvy")
    private void makeMarkers(Map<String, Shelter> map) {
        for (Shelter shelter : map.values()) {
            viewMap.addMarker(new MarkerOptions()
                    .position(shelter.getCoordinates())
                    .title(shelter.getName())
                    .snippet(shelter.getInfo()));
        }
    }

    class ShelterMapAdapter implements GoogleMap.InfoWindowAdapter{

        private final View shelterView;

        ShelterMapAdapter(Context context){
            LayoutInflater inflater = LayoutInflater.from(context);
            shelterView = inflater.inflate(R.layout.map_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView title = shelterView.findViewById(R.id.title);
            title.setText(marker.getTitle());
            TextView info = shelterView.findViewById(R.id.info);
            info.setText(marker.getSnippet());

            return shelterView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private void backToShelterListActivity() {
        Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void backToSearchActivity() {
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    @SuppressWarnings("FeatureEnvy")
    private void readShelterFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/homeless_shelter_database.csv";
            File csv = new File(filePath);
            if (!csv.exists()) {
                try {
                    InputStream is = getResources().openRawResource(R.raw.homeless_shelter_database);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String line;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] traits = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        for (int i = 0; i < traits.length; i++) {
                            if((traits[i] == null) || traits[i].isEmpty()) {
                                traits[i] = "Not available";
                            } else if ((traits[i].charAt(0) == '"') && (traits[i].charAt(traits[i].length() - 1) == '"')){
                                traits[i] = traits[i].substring(1, traits[i].length() - 1);
                            }
                        }
                        list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4], traits[5], traits[6], traits[8], (traits.length > 9) ? traits[9] : "Not available"));
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //make writer, append set to true
                    CSVWriter writer = new CSVWriter(new FileWriter(filePath));
                    for (Map.Entry<String, Shelter> shelter : ShelterList.getMap().entrySet())
                    {
                        //form
                        String [] record = (shelter.getKey() + "," + shelter.getValue().getRecord()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        writer.writeNext(record);
                    }
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
                CSVReader csvReader = new CSVReader(reader);
                String traits[];
                while ((traits = csvReader.readNext()) != null) {
                    list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4], traits[5], traits[6], traits[7], (traits.length > 8) ? traits[8] : "Not available"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
