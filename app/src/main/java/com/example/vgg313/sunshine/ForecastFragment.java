package com.example.vgg313.sunshine;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends android.app.Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            FetchWeatherTask weatherTask= new com.example.vgg313.sunshine.ForecastFragment.FetchWeatherTask();
                    weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
            android.os.Bundle savedInstanceState) {

        String[] forecastArray ={

                "Today - sunny - 99/22",
                "tomorrow - sunny - 99/22",
                "Today - sunny - 99/22",
                "Today - sunny - 99/22",
                "Today - sunny - 99/22",
                "Today - sunny - 99/22",
                "Sunday - sunny - 99/22",

         };

        java.util.List<String> weekForecast = new java.util.ArrayList<String>(java.util.Arrays.asList(forecastArray));


        android.widget.ArrayAdapter<String> mForecastAdapter;
        mForecastAdapter = new android.widget.ArrayAdapter<String>(
                getActivity(),
                com.example.vgg313.sunshine.R.layout.list_item_forecast,
                com.example.vgg313.sunshine.R.id.list_item_forecast_textview,
                weekForecast);

        android.view.View rootView = inflater.inflate(com.example.vgg313.sunshine.R.layout.fragment_main, container, false);



        // Get a reference to the ListView, and attach this adapter to it.
        android.widget.ListView listView = (android.widget.ListView) rootView.findViewById(com.example.vgg313.sunshine.R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);



        return rootView;
    }

    public class FetchWeatherTask extends android.os.AsyncTask <Void, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
    protected Void doInBackground(Void... params) {

        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        java.net.HttpURLConnection urlConnection = null;
        java.io.BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            java.net.URL url = new java.net.URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            java.io.InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            android.util.Log.v(LOG_TAG, "ForecastJsonstring:"+  forecastJsonStr);
        } catch (java.io.IOException e) {
            android.util.Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final java.io.IOException e) {
                    android.util.Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
    return  null;
    }}}
