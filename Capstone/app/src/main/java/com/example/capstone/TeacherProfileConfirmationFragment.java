package com.example.capstone;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.JsonReader;
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
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import com.example.capstone.asynctasks.ImageDownloader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    AlertDialog.Builder alert;
    Teacher selectedTeacher;
    Availability selectedAvailability;
    String selectedClassType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_profile_confirmation_layout, container, false);

        geocoder = new Geocoder(this.getActivity());

        alert = new AlertDialog.Builder(this.getActivity());
        alert.setTitle("Class confirmed!");
        alert.setMessage("Your class has been successfully scheduled and the teacher will be notified soon.");
        alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Navigation.findNavController(getActivity(),R.id.navHostFragment).navigate(R.id.action_teacher_profile_confirmation_to_student_home_search);
            }
        });

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

        selectedTeacher = ((MainActivity)getActivity()).getTeacher();
        selectedAvailability = ((MainActivity)getActivity()).getAvailability();
        // Checking if selectedClassType is not null here.

        selectedClassType = ((MainActivity)getActivity()).getClassTypes();

        Log.d("POST_Date", selectedAvailability.getDate());
        Log.d("POST_TimeSlots", selectedAvailability.getTimeSlots().toString());
        Log.d("POST_ClassType", selectedClassType);
        Log.d("POST_RetrievingTeacher:","Retrieved Teacher is: " + selectedTeacher.getName());


        // TODO: the following data should come from the database:
        ImageDownloader imgDownloader = new ImageDownloader(userAvatar, selectedTeacher.getId());
        imgDownloader.execute("");
        String name = selectedTeacher.getName();
        String teacherType = "Community Teacher";
        String location = selectedTeacher.getCity();
        int rating = 4;
        String time = selectedAvailability.getTimeSlots().get(0) + ", " + selectedAvailability.getDate();
        String language = selectedTeacher.getLanguage(0);
        double cost = selectedTeacher.getHourlyRate();

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
        mSpot = mMap.addMarker(new MarkerOptions().position(initialPosition).title("Meeting Spot").draggable(false));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        locationCoordinates[0] = mSpot.getPosition().latitude;
        locationCoordinates[1] = mSpot.getPosition().longitude;

        moveCamera();

        mMap.setOnMarkerClickListener(this);
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
        // Defining search scope/context
        String searchContext = " Vancouver, BC, Canada";
        if (userInputName.equals("")) {
            userInputName = "4700 Kingsway, Burnaby, BC V5H 4N2"; // Placeholder address, for testing purposes.
        }
        try {
            addresses = geocoder.getFromLocationName(userInputName + searchContext, 1);
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest (Double[] locationCoordinates, String locationName, String inputMessage) {

        // Checking if the parameters we need were initialized. If not, send an empty string.
        // If the parameter type is String, it will default to the empty string so there is no need to check it here.
        // Note: selectedClassType has already been checked.
        String teacherIdParam = selectedTeacher.getId() == 0 ? "" : Integer.toString(selectedTeacher.getId());
        String timeSlotParam = (selectedAvailability.getTimeSlots() == null || selectedAvailability.getTimeSlots().size() == 0) ? "" : selectedAvailability.getTimeSlots().get(0);
        String locationLatParam = "", locationLongParam = "";
        if (locationCoordinates.length == 2) {
            locationLatParam = locationCoordinates[0].toString();
            locationLongParam = locationCoordinates[1].toString();
        } else {
            Log.e("Error", "Location coordinates array doesn't have two elements");
        }
        String priceParam = selectedTeacher.getHourlyRate() == 0.0f ? "" : Float.toString(selectedTeacher.getHourlyRate());

        Map<String, String> params = new HashMap<String, String>();
        params.put("studentId", "1");
        params.put("teacherId", teacherIdParam);
        params.put("date", selectedAvailability.getDate());
        params.put("startTime", timeSlotParam);
        params.put("duration", "01:00");
        params.put("classType", selectedClassType);
        params.put("location", locationName);
        params.put("locationLat", locationLatParam);
        params.put("locationLong", locationLongParam);
        params.put("price", priceParam);
        params.put("message", inputMessage);

        Log.d("POST_Map", params.toString());

        try {
            APICaller.Post("class", params, new APICallBack() {
                @Override
                public void callBack(JsonReader jsonObject) {
                    // TODO: parse response and log it?
                    Log.d("TEST POST REQUEST","This is try");
                    // TODO: error handling
//                    if (we got an error) {
//                        alert.setTitle("Error");
//                        alert.setMessage("An exception has occurred..." );
//                    }
                }
            });
        } catch (Exception e) {
            Log.d("TEST POST REQUEST","An exception has occurred: " + e);
        } finally {
            Log.d("TEST POST REQUEST","This is finally");
        }

        alert.show();
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
