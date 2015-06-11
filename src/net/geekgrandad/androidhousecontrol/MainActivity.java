package net.geekgrandad.androidhousecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.R.drawable;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	
	private static DisplayMetrics dm = new DisplayMetrics();
	private static boolean speechSupported;
	private static final int FIRST_DATA_TABLE_SECTION = 0;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
        // Find out whether speech recognition is supported
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities
        		(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        
        speechSupported = (intActivities.size() > 0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 4) return TextCommandFragment.newInstance(position);
			else if (position == 5) return SpeechCommandFragment.newInstance(position);
			else if (position == 6) return StreamingFragment.newInstance(position);
			else if (position == 7) return MapFragment.newInstance(position);
			else if (position == 8) return TVRemoteFragment.newInstance(position);
			else if (position == 9) return RoomFragment.newInstance(position);
			else return DataTableFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			int[] ids = {R.string.living_room, R.string.electricity, R.string.master_bedroom, 
					R.string.hall, R.string.text_control, R.string.speech_control, R.string.front_door, 
					R.string.first_floor, R.string.tv_remote,R.string.temperature};
			Locale l = Locale.getDefault();
			return getString(ids[position]).toUpperCase(l);
		}
	}

	/**
	 * Data table fragment.
	 */
	public static class DataTableFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		private static String[][] livingRoom = {
			{"Temperature (front)", "temperature 3",""},
			{"Temperature (back)", "temperature 2", ""},
			{"Light level (front)","lightlevel 3", ""},
			{"Light level (back)","lightlevel 2", ""},
			{"Occupied (front)","occupied 3",""},
			{"Occupied (back)","occupied 2",""},
			{"Front light","pollock status",""},
			{"Back light","picasso status",""},
			{"Couch weight","humidity 8",""},
			{"Camera socket","spy status",""},
			{"Back window","switch 2 status",""},
			{"Table socket","coffee status",""},
			{"Computer socket","print status",""},
			{"Guitar socket","guitar status",""},
			{"Banjolele socket","banjolele status",""},
			{"Fan socket","fan status",""},
			{"Bench socket","bench status",""},
			{"Tablet socket","trans status",""}};
	
		private static String[][] electricity = {
			{"Total power", "power",""},
			{"Washer", "washer value", ""},
			{"Dish washer","dishwasher value", ""},
			{"Dryer","dryer value", ""},
		    {"Games", "xbox value", ""},
		    {"Bedroom media", "bedmedia value", ""}};
	
		private static String[][] masterBedroom = {
			{"Temperature", "temperature 1",""},
			{"Humidity", "humidity 1", ""},
			{"Occupied","occupied 1", ""},
			{"Light level","lightlevel 1", ""},
		    {"Left light", "world status", ""},
		    {"Right light", "periodic status", ""}};
	
		private static String[][] hall = {
			{"Letterbox", "switch 1 status",""}};
		
		private static String[][] command = {
			{"Command", "",""}};
		
		static String[][][] cmds = {livingRoom, electricity, masterBedroom, hall, command};

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static DataTableFragment newInstance(int sectionNumber) {
			DataTableFragment fragment = new DataTableFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public DataTableFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			
			HouseCommand house = null;
			int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
			int tableNumber =  sectionNumber - FIRST_DATA_TABLE_SECTION;
			String[][] data = cmds[tableNumber];
			
			for(int i=0;i<data.length;i++) {
		        house = (HouseCommand) new HouseCommand().execute(data[i][1],"" + i, "" + tableNumber);
			}
			
			try {
				house.get();
			} catch (InterruptedException | ExecutionException e) {}
			
			refreshTable((TableLayout) rootView.findViewById(R.id.tableLayout1),container.getContext(),rootView, cmds[tableNumber]);
				
			return rootView;
		}
		
		public static void setResponse(int tableNumber, int i, String r) {
			cmds[tableNumber][i][2] = r;
		}
		
		private void refreshTable(TableLayout ll, final Context context, View rootView, String[][] cmds) {
			
			// Remove all rows other than header
			int l = ll.getChildCount();
			if (l > 1) ll.removeViewsInLayout(1,l-1);
			
			final TableRow[] rows = new TableRow[cmds.length];
		    for (int i = 0; i <cmds.length; i++) {
		        final TableRow row= new TableRow(context);
		        rows[i] = row;
		        row.setBackgroundResource(drawable.screen_background_light_transparent);
		        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
		        row.setLayoutParams(lp);
		        TextView tv1 = new TextView(context);
		        tv1.setText(cmds[i][0]);
		        row.addView(tv1);
		        TextView tv2 = new TextView(context);
		        tv2.setText(cmds[i][2]);
		        row.addView(tv2);
		        ll.addView(row);
		    }  
		    ll.invalidate();
		}
	}
	
	/**
	 * Text command fragment.
	 */
	public static class TextCommandFragment extends Fragment implements OnClickListener {
		private static final String ARG_SECTION_NUMBER = "section_number";
		private Button go;
		private Context context;
		private TextView command;
		
		public static TextCommandFragment newInstance(int sectionNumber) {
			TextCommandFragment fragment = new TextCommandFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.text_command_main, container,
					false);
			context = container.getContext();
			
			go = (Button) rootView.findViewById(R.id.go);
			go.setOnClickListener(this);
			
			command = (TextView) rootView.findViewById(R.id.command);
				
			return rootView;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.go) {
				String value = command.getText().toString();
				HouseCommand house = (HouseCommand) new HouseCommand().execute(value,"0", "4");
				
				try {
					house.get();
				} catch (InterruptedException | ExecutionException e) {}
				
				Toast.makeText(context, "Reply to " + value + " is " + DataTableFragment.cmds[4][0][2], Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Speech command fragment.
	 */
	public static class SpeechCommandFragment extends Fragment implements OnClickListener {
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		//variable for checking Voice Recognition support on user device
		private static final int VR_REQUEST = 999;
		
	    //ListView for displaying suggested words
		private ListView wordList;
		
		//Log tag for output information
		private final String LOG_TAG = "SpeechControlActivity";
		
		private Context context;
		
		public static SpeechCommandFragment newInstance(int sectionNumber) {
			SpeechCommandFragment fragment = new SpeechCommandFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.speech_main, container,
					false);
			
			context = container.getContext();
			
		    //gain reference to speak button
	        Button speechBtn = (Button) rootView.findViewById(R.id.speech_btn);
	        //gain reference to word list
	        wordList = (ListView) rootView.findViewById(R.id.word_list);
	        
	        if (speechSupported) {
	        	//speech recognition is supported - detect user button clicks
	            speechBtn.setOnClickListener(this);
	        }
	        else 
	        {
	        	//speech recognition not supported, disable button and output message
	            speechBtn.setEnabled(false);
	            Toast.makeText(context, "Oops - Speech recognition not supported!", Toast.LENGTH_LONG).show();
	        }
	        
	        //detect user clicks of suggested words
	        wordList.setOnItemClickListener(new OnItemClickListener() {
	        	
	        	//click listener for items within list
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	            {
	            	//cast the view
	            	TextView wordView = (TextView)view;
	            	//retrieve the chosen word
	            	String wordChosen = (String) wordView.getText();
	            	//output for debugging
	            	Log.v(LOG_TAG, "chosen: "+wordChosen);
	            	
	    			HouseCommand house = (HouseCommand) new HouseCommand().execute(wordChosen,"0", "4");
	    			
	    			try {
	    				house.get();
	    			} catch (InterruptedException | ExecutionException e) {}
	    			
	    			Toast.makeText(context, "Reply is " + DataTableFragment.cmds[4][0][2], Toast.LENGTH_LONG).show();            }
	        });

			return rootView;
		}

		@Override
		public void onClick(View v) {
	        if (v.getId() == R.id.speech_btn) {
	            listenToSpeech();
	        }
		}
		
	    /**
	     * Instruct the app to listen for user speech input
	     */
	    private void listenToSpeech() {
	    	
	    	//start the speech recognition intent passing required data
	    	Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    	//indicate package
	    	listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
	    	//message to display while listening
	    	listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
	    	//set speech model
	    	listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    	//specify number of results to retrieve
	    	listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

	    	//start listening
	        startActivityForResult(listenIntent, VR_REQUEST);
	    }
	    
	    /**
	     * onActivityResults handles:
	     *  - retrieving results of speech recognition listening
	     */
	    @Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	//check speech recognition result 
	        if (requestCode == VR_REQUEST && resultCode == RESULT_OK) 
	        {
	        	//store the returned word list as an ArrayList
	            ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	            //set the retrieved list to display in the ListView using an ArrayAdapter
	            wordList.setAdapter(new ArrayAdapter<String> (context, R.layout.word, suggestedWords));
	        }

	        //call superclass method
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	/**
	 * Streaming fragment.
	 */
	public static class StreamingFragment extends Fragment implements MJPGViewer {
		private static final String ARG_SECTION_NUMBER = "section_number";
		private static MJPGClient client;
		private static Thread clientThread; 
		private Context context;
		private byte[] data;
		
		public static StreamingFragment newInstance(int sectionNumber) {
			StreamingFragment fragment = new StreamingFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.streaming_main, container,
					false);	
			context = container.getContext();
			if (clientThread == null)
				try {
					clientThread = new Thread(client = new MJPGClient(this, "http://192.168.0.102:8081"));
					clientThread.setDaemon(true);
					//Toast.makeText(context, "Client created", Toast.LENGTH_LONG).show();
					clientThread.start();
				} catch (Exception e) {
					Toast.makeText(context, "Failed to create thread: " + e, Toast.LENGTH_LONG).show();
				}
			if (data != null) {
		        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		        ImageView imgViewer = (ImageView) rootView.findViewById(R.id.img3);
		        imgViewer.setMinimumHeight(dm.heightPixels);
		        imgViewer.setMinimumWidth(dm.widthPixels);
		        imgViewer.setImageBitmap(bm);
			}
			client.setViewer(this);
			return rootView;
		}
		
		private void displayImage(byte[] image) {
			data = image;
			try {
				getFragmentManager().beginTransaction().detach(this).attach(this).commit();
			} catch (Exception e) {}
		}

		@Override
		public void setImage(final byte[] image) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
			    @Override
			    public void run() {
			    	displayImage(image);
			    }
			});
		}

		@Override
		public void error(final String msg) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
			    @Override
			    public void run() {
			    	Toast.makeText(context, "MJPG Streamer error " + msg, Toast.LENGTH_LONG).show();
			    }
			});
		}
	}
	
	/**
	 * Map fragment.
	 */
	public static class MapFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		public static MapFragment newInstance(int sectionNumber) {
			MapFragment fragment = new MapFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.first_floor_main, container,
					false);	
			
			ImageView imageView = (ImageView) rootView.findViewById(R.id.img1);
	        //Get a drawable from the parsed SVG and apply to ImageView
	        imageView.setBackgroundColor(Color.WHITE);
	        imageView.setMinimumHeight(dm.heightPixels);
	        imageView.setMinimumWidth(dm.widthPixels);
			SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.floor);
	        imageView.setImageDrawable(svg.createPictureDrawable());
	        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			return rootView;
		}
	}
	
	/**
	 * TV Remote fragment.
	 */
	public static class TVRemoteFragment extends Fragment implements OnClickListener {
		private static final String ARG_SECTION_NUMBER = "section_number";
		private Button select, onOff, mute, channelUp, channelDown, volumeUp, volumeDown, tvOnOff, ok, stop, 
		               tv, home, clear, back, shows, info, guide, skipf, skipb, record, subtitles;
		private ImageButton left, right, up, down, play, pause, ff, fb;
		private Spinner spinner;
		Context context;
		
		public static TVRemoteFragment newInstance(int sectionNumber) {
			TVRemoteFragment fragment = new TVRemoteFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.tv_remote_main, container,
					false);
			
			select = (Button) rootView.findViewById(R.id.select);
			select.setOnClickListener(this);
			onOff = (Button) rootView.findViewById(R.id.onOff);
			onOff.setOnClickListener(this);
			mute = (Button) rootView.findViewById(R.id.mute);
			mute.setOnClickListener(this);
			volumeUp = (Button) rootView.findViewById(R.id.volumeUp);
			volumeUp.setOnClickListener(this);
			volumeDown = (Button) rootView.findViewById(R.id.volumeDown);
			volumeDown.setOnClickListener(this);
			channelUp = (Button) rootView.findViewById(R.id.channelUp);
			channelUp.setOnClickListener(this);
			channelDown = (Button) rootView.findViewById(R.id.channelDown);
			channelDown.setOnClickListener(this);
			ok = (Button) rootView.findViewById(R.id.ok);
			ok.setOnClickListener(this);
			left = (ImageButton) rootView.findViewById(R.id.left);
			left.setOnClickListener(this);
			right = (ImageButton) rootView.findViewById(R.id.right);
			right.setOnClickListener(this);
			up = (ImageButton) rootView.findViewById(R.id.up);
			up.setOnClickListener(this);
			down = (ImageButton) rootView.findViewById(R.id.down);
			down.setOnClickListener(this);
			tvOnOff = (Button) rootView.findViewById(R.id.tvOnOff);
			tvOnOff.setOnClickListener(this);
			play = (ImageButton) rootView.findViewById(R.id.play);
			play.setOnClickListener(this);
			pause = (ImageButton) rootView.findViewById(R.id.pause);
			pause.setOnClickListener(this);
			stop = (Button) rootView.findViewById(R.id.stop);
			stop.setOnClickListener(this);
			ff = (ImageButton) rootView.findViewById(R.id.ff);
			ff.setOnClickListener(this);
			fb = (ImageButton) rootView.findViewById(R.id.fb);
			fb.setOnClickListener(this);
			tv = (Button) rootView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			home = (Button) rootView.findViewById(R.id.home);
			home.setOnClickListener(this);
			clear = (Button) rootView.findViewById(R.id.clear);
			clear.setOnClickListener(this);
			back = (Button) rootView.findViewById(R.id.back);
			back.setOnClickListener(this);
			shows = (Button) rootView.findViewById(R.id.shows);
			shows.setOnClickListener(this);
			info = (Button) rootView.findViewById(R.id.info);
			info.setOnClickListener(this);
			guide = (Button) rootView.findViewById(R.id.guide);
			guide.setOnClickListener(this);
			skipf = (Button) rootView.findViewById(R.id.skipf);
			skipf.setOnClickListener(this);
			skipb = (Button) rootView.findViewById(R.id.skipb);
			skipb.setOnClickListener(this);
			record = (Button) rootView.findViewById(R.id.record);
			record.setOnClickListener(this);
			subtitles = (Button) rootView.findViewById(R.id.subtitles);
			subtitles.setOnClickListener(this);
			
			
			context = container.getContext();
			
			spinner = (Spinner) rootView.findViewById(R.id.spinner1);
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
			        R.array.channels_array, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			
			return rootView;
		}

		@Override
		public void onClick(View v) {
			String value="";
			if (v.getId() == R.id.select) {
				value = (String)spinner.getSelectedItem();
				int index = value.indexOf(' ');
				value = "channel " + value.substring(0,index);
			} else if (v.getId() == R.id.channelDown) {
				value = "channel down";
			} else if (v.getId() == R.id.channelUp) {
				value = "channel up";
			} else if (v.getId() == R.id.down) {
				value = "vt down";
			} else if (v.getId() == R.id.up) {
				value = "vt up";
			} else if (v.getId() == R.id.left) {
				value = "vt left";
			} else if (v.getId() == R.id.right) {
				value = "vt right";
			} else if (v.getId() == R.id.mute) {
				value = "av mute";
			} else if (v.getId() == R.id.ok) {
				value = "vt ok";
			} else if (v.getId() == R.id.volumeUp) {
				value = "volume up";
			} else if (v.getId() == R.id.volumeDown) {
				value = "volume down";
			} else if (v.getId() == R.id.onOff) {
				value = "vt on";
			} else if (v.getId() == R.id.tvOnOff) {
				value = "tv on";
			} else if (v.getId() == R.id.play) {
				value = "vt play";
			} else if (v.getId() == R.id.pause) {
				value = "vt pause";
			} else if (v.getId() == R.id.stop) {
				value = "vt stop";
			} else if (v.getId() == R.id.ff) {
				value = "vt ff";
			} else if (v.getId() == R.id.fb) {
				value = "vt fb";
			} else if (v.getId() == R.id.tv) {
				value = "vt tv";
			} else if (v.getId() == R.id.home) {
				value = "vt home";
			} else if (v.getId() == R.id.clear) {
				value = "vt clear";
			} else if (v.getId() == R.id.back) {
				value = "vt back";
			} else if (v.getId() == R.id.shows) {
				value = "vt shows";
			} else if (v.getId() == R.id.info) {
				value = "vt info";
			} else if (v.getId() == R.id.guide) {
				value = "vt guide";
			} else if (v.getId() == R.id.skipf) {
				value = "vt skipf";
			} else if (v.getId() == R.id.skipb) {
				value = "vt skipb";
			} else if (v.getId() == R.id.record) {
				value = "vt record";
			} else if (v.getId() == R.id.subtitles) {
				value = "vt subtitles";
			}
				
			HouseCommand house = (HouseCommand) new HouseCommand().execute(value,"0", "4");
			
			try {
				house.get();
			} catch (InterruptedException | ExecutionException e) {}
			
			Toast.makeText(context, "Reply is " + DataTableFragment.cmds[4][0][2], Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Room fragment.
	 */
	public static class RoomFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";
		private Context context;
		private int[] temps = new int[9];
		
		public static RoomFragment newInstance(int sectionNumber) {
			RoomFragment fragment = new RoomFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.room_main, container,
					false);	
			context = container.getContext();
			
			temps[0] = Integer.parseInt(MainActivity.DataTableFragment.cmds[2][0][2]);
			temps[4] = Integer.parseInt(MainActivity.DataTableFragment.cmds[0][0][2]);
			temps[5] = Integer.parseInt(MainActivity.DataTableFragment.cmds[0][1][2]);
			
			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		    gridview.setAdapter(new TextAdapter(context, dm, temps));
			
			return rootView;
		}
	}
}
