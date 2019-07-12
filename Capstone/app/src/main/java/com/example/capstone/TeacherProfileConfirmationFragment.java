package com.example.capstone;

import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.asynctasks.ImageDownloader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class TeacherProfileConfirmationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, View.OnClickListener {
    private GoogleMap mMap;
    MapView mMapView;
    private Marker mSpot;
    private Double[] locationCoordinates = new Double[2];
    private String locationName;
    List<Address> addresses;
    Geocoder geocoder;
    Button findLocationButton, confirmButton;
    EditText inputLocationName, inputMessage;
    ImageView userAvatar;
    RatingBar userRating;
    TextView userName, userDescription, classTime, classLanguage, classCost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_profile_confirmation_layout, container, false);
        geocoder = new Geocoder(this.getActivity());

        findLocationButton = view.findViewById(R.id.findLocationButton);
        inputLocationName = view.findViewById(R.id.inputLocationName);
        inputMessage = view.findViewById(R.id.inputMessage);
        confirmButton = view.findViewById(R.id.confirmButton);
        userAvatar = view.findViewById(R.id.userAvatar);
        userName = view.findViewById(R.id.userName);
        userDescription = view.findViewById(R.id.userDescription);
        userRating = view.findViewById(R.id.userRating);
        classTime = view.findViewById(R.id.classTime);
        classLanguage = view.findViewById(R.id.classLanguage);
        classCost = view.findViewById(R.id.classCost);

        findLocationButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        // TODO: the following data should come from the database:
        // placeholder_avatar from https://pravatar.cc/
        int avatar = R.drawable.placeholder_avatar;
        ImageDownloader imgDownloader = new ImageDownloader(userAvatar, 3);
        imgDownloader.execute("");
        String name = "Jean Coutu";
        String teacherType = "Community Teacher";
        String location = "Surrey, Canada";
        int rating = 4;
        String time = "3:00 PM - 4:00 PM Sat, Jun 3";
        String language = "English";
        double cost = 15.00;

        // Populating UI
        // userAvatar.setImageResource(avatar);
        userName.setText(name);
        userDescription.setText(teacherType + " from " + location);
        userRating.setRating(rating);
        classTime.setText(time);
        classLanguage.setText(language);
        classCost.setText("CA$ " + cost);


        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // TODO: get this from the database?
        LatLng initialPosition = new LatLng(49.224206, -123.108453);
        // Drag and drop doesn't work well with ScrollView, so I'm disabling it.
        mSpot = mMap.addMarker(new MarkerOptions().position(initialPosition).title("Meeting Spot").draggable(false));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        locationCoordinates[0] = mSpot.getPosition().latitude;
        locationCoordinates[1] = mSpot.getPosition().longitude;

        moveCamera();

        mMap.setOnMarkerClickListener(this);
        // Drag and drop doesn't work well with ScrollView, so I'm disabling it.
        // mMap.setOnMarkerDragListener(this);
    }

    public void moveCamera () {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mSpot.getPosition()));
    }

    public void moveMarker () {
        LatLng updatedPosition = new LatLng(locationCoordinates[0], locationCoordinates[1]);
        mSpot.setPosition(updatedPosition);

        moveCamera();
    }

    public void updateUI () {
        if(addresses.size() > 0) {
            Address address = addresses.get(0);

            locationCoordinates[0] = address.getLatitude();
            locationCoordinates[1] = address.getLongitude();
            locationName = address.getAddressLine(0);

            moveMarker();
            inputLocationName.setText(locationName);
        }
    }

    public void locationFromCoordinates (Double[] userInputCoordinates) {
        try {
            addresses = geocoder.getFromLocation(userInputCoordinates[0], userInputCoordinates[1], 1);
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void locationFromName (String userInputName) {
        if (userInputName.equals("")) {
            userInputName = "4700 Kingsway, Burnaby, BC V5H 4N2"; // Placeholder address, for testing purposes.
        }
        try {
            addresses = geocoder.getFromLocationName(userInputName, 1);
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest (Double[] locationCoordinates, String locationName, String inputMessage) {
        // TODO: send data to database.
        Toast.makeText(this.getActivity(), locationName + "\n" + locationCoordinates[0] + ", " + locationCoordinates[1] + "\n" + inputMessage, Toast.LENGTH_LONG).show();
        Log.d("To Database", "locationName: " + locationName);
        Log.d("To Database", "locationCoordinates: " + locationCoordinates[0] + ", " + locationCoordinates[1]);
        Log.d("To Database", "inputMessage: " + inputMessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.findLocationButton:
                Log.d("Find Location Button", "Clicked");
                locationFromName(inputLocationName.getText().toString());
                InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputLocationName.getWindowToken(), 0);
                inputLocationName.clearFocus();
                break;
            case R.id.confirmButton:
                Log.d("Confirm Button", "Clicked");
                sendRequest(locationCoordinates, locationName, inputMessage.getText().toString());
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this.getContext(), marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
        Log.d("Current Position", marker.getPosition().toString());
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Double[] newCoordinates = {marker.getPosition().latitude, marker.getPosition().longitude};
        locationFromCoordinates(newCoordinates);
    }
}
