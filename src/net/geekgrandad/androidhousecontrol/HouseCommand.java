package net.geekgrandad.androidhousecontrol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;

public class HouseCommand extends AsyncTask<String, Integer, Long> {
	
	private final String host = "192.168.0.100";
	private final int port = 50000;
	private String response = "Unknown";
	private int index, section;

	@Override
	protected Long doInBackground(String... cmd) {
		Socket socket = null;
		index = Integer.parseInt(cmd[1]);
		section = Integer.parseInt(cmd[2]);
		try {
		    socket = new Socket(host, port);
		    
		    PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    
		    out.println(cmd[0]);
		    
		    response = in.readLine();
		    MainActivity.DataTableFragment.setResponse(section, index, response);
		    
		    in.close();
		    out.close();
		    socket.close();
		    
		} catch (Exception e) {
			response = "Error";
			return 1l;
		} 
		    
		return 0l;
	}
		
	@Override
	protected void onPostExecute(Long result) {
	    super.onPostExecute(result);
	}

}
