package com.flushd.flushd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static User currentUser;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance(position + 1, new User("nullrefexc", "nullrefexc@gmail.com")))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SignInFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        EditText email;
        EditText password;
        Button signIn;
        Button signUp;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SignInFragment newInstance(int sectionNumber) {
            SignInFragment fragment = new SignInFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SignInFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            email = (EditText) rootView.findViewById(R.id.editTextEmail);
            password = (EditText) rootView.findViewById(R.id.editTextPass);
            signIn = (Button) rootView.findViewById(R.id.buttonSignIn);
            signUp = (Button) rootView.findViewById(R.id.buttonSignUp);
            
            signIn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//This assumes that they are both filled for now...
					//******************************FIX LATER*******************************
					if(SignIn(email.getText().toString(), password.getText().toString()))
					{
						Toast.makeText(getActivity(), "Whoot it workded", Toast.LENGTH_LONG);
					}
					else
					{
						Toast.makeText(getActivity(), "It didnt work", Toast.LENGTH_LONG);
					}
					
				}
			});
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        private boolean SignIn(String email, String password){
        	UserTask userT = new UserTask(getActivity());
        	userT.execute(email, password);
        	return true;
        }
        
        class UserTask extends AsyncTask<String, Void, String> {

        	Context context;
        	User tuser;
        	UserTask(Context context){
        		this.context = context;
        	}
            protected String doInBackground(String... urls) {
                //Put your getServerData-logic here
            	SignIn(urls[0], urls[1]);
            	return null;
            	
                

            }
            //This Method is called when Network-Request finished
            protected void onPostExecute(String serverData) {
                //Nav
            	Fragment newFragment = HomeFragment.newInstance(2,tuser);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
            }
            
            private void SignIn(String email, String password)
            {
            	String result = "";
            	//the year data to send
            	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            	nameValuePairs.add(new BasicNameValuePair("user_email",email));
            	nameValuePairs.add(new BasicNameValuePair("user_password",password));
            	InputStream ist = null;
            	//http post
            	try{
            	        HttpClient httpclient = new DefaultHttpClient();
            	        HttpPost httppost = new HttpPost("http://nullrefexc.com/flushd/user.php");
            	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            	        HttpResponse response = httpclient.execute(httppost);
            	        HttpEntity entity = response.getEntity();
            	        ist = entity.getContent();
            	}catch(Exception e){
            	        Log.e("log_tag", "Error in http connection "+e.toString());
            	}
            	//convert response to string
            	try{
            	        BufferedReader reader = new BufferedReader(new InputStreamReader(ist,"iso-8859-1"),8);
            	        StringBuilder sb = new StringBuilder();
            	        String line = null;
            	        while ((line = reader.readLine()) != null) {
            	                sb.append(line + "\n");
            	        }
            	        ist.close();
            	 
            	        result=sb.toString();
            	}catch(Exception e){
            	        Log.e("log_tag", "Error converting result "+e.toString());
            	}
            	 
            	//parse json data
            	try{
            	        JSONArray jArray = new JSONArray(result);
            	        for(int i=0;i<jArray.length();i++){
            	                JSONObject json_data = jArray.getJSONObject(i);
            	                Log.i("log_tag","id: "+json_data.getInt("user_id")+
            	                        ", name: "+json_data.getString("user_name")+
            	                        ", sex: "+json_data.getString("user_email")
            	                );
            	        }
            	        JSONObject json_data = jArray.getJSONObject(0);
            	        tuser = new User(json_data.getString("user_name"), json_data.getString("user_email"));
            	        Log.e("log_tag", "it Worked");
            	}
            	catch(JSONException e){
            	        Log.e("log_tag", "Error parsing data "+e.toString());
            	}
            	
            	
            }
        }
        
        
    }

}


