package com.example.tutorialbuild;

import android.os.Bundle;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView fuelEfficiency = view.findViewById(R.id.fuelEfficiencyTxt);
        TextView recommendations = view.findViewById(R.id.recommendationsTxt);
        TextView welcomeText = view.findViewById(R.id.textView5);

        FetchUsersTask fetcher = new FetchUsersTask(this);
        fetcher.execute("http://192.168.1.42:5000/obd-data");

        Bundle args = getArguments();
        welcomeText.setText("Welcome, " + args.getString("name"));
        fuelEfficiency.setText("Fuel Efficiency: " + args.getString("fuelEfficiency") + " L / 100 km");

        return view;
    }
}

/**
 * Allows for fetching of data
 */
class FetchUsersTask extends AsyncTask<String, Void, String> {

    private Fragment fragment;

    public FetchUsersTask(Fragment fragment){
        this.fragment = fragment;
    }

    /**
     * Does operations in background
     *
     * @param urls - the url that it is reading
     * @return - The result in the url in a String format
     */
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            response = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {

        // Runs on UI thread — can now safely update the UI
        try {
//            JSONArray jsonArray = new JSONArray(result);
//            StringBuilder userNames = new StringBuilder();

            String timestamp;
            double speed;
            double totalDistance;
            double totalFuel;
            double fuelEfficiency;

            JSONObject user = new JSONObject(result);

            timestamp = user.getString("timestamp");
            speed = user.getDouble("speed_kph");
            totalDistance = user.getDouble("total_distance_km");
            totalFuel = user.getDouble("total_fuel_liters");
            fuelEfficiency = user.getDouble("fuel_efficiency_l_per_100km");

            Bundle bundle = new Bundle();
            bundle.putString("timestamp", timestamp);
            bundle.putDouble("speed_kph", speed);
            bundle.putDouble("total_distance_km", totalDistance);
            bundle.putDouble("total_fuel_liters", totalFuel);
            bundle.putDouble("fuelEfficiency", fuelEfficiency);
            fragment.setArguments(bundle);

//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject user = jsonArray.getJSONObject(i);
//                timestamp = user.getString("timestamp");
//                speed = user.getDouble("speed_kph");
//                totalDistance = user.getDouble("total_distance_km");
//                totalFuel = user.getDouble("total_fuel_liters");
//                fuelEfficiency = user.getDouble("fuel_efficiency_l_per_100km");
//            }

            // TODO modifications of the information displayed on the website
            //textViewResult.setText(userNames.toString());
        } catch (JSONException e) {
            // TODO errors display on any textview
            //textViewResult.setText("Error parsing data.");
            e.printStackTrace();
        }
    }

    public void setFragment(Fragment frag){
        this.fragment = frag;
    }
}
