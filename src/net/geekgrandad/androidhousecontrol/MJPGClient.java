package net.geekgrandad.androidhousecontrol;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;


public class MJPGClient implements Runnable {
	private static final String CONTENT_LENGTH = "Content-Length: ";
	private static final int TIMEOUT = 10000;
	private String url;
	private InputStream urlStream;
	private MJPGViewer viewer;
	
	public MJPGClient(MJPGViewer viewer, String url) throws IOException {

		this.url = url;
		this.viewer = viewer;
	}
	
	public void setViewer(MJPGViewer viewer) {
		this.viewer=viewer;
	}
	
	@Override
	public void run() {
		while(true) {
			URLConnection urlConn ;
			try {
				urlConn = new URL(url).openConnection();
				urlConn.setReadTimeout(TIMEOUT);
				urlConn.connect();
				urlStream = urlConn.getInputStream();
			} catch (Exception e) {
				viewer.error("Exception: " + e);
				return;
			}
	
			while (true) {
				try {						
					viewer.setImage(receiveImage());
				} catch (Exception e) {
					viewer.error("Exception: " + e);
					break;
				}
			}
			
			try {
				urlStream.close();
			} catch (Exception e) {}
			
			try {
				Thread.sleep(TIMEOUT);
			} catch (InterruptedException e) {}
		}
	}

	private byte[] receiveImage() throws IOException {
		int b;
		StringWriter sw = new StringWriter(128);
		
		// Get header
		while ((b = urlStream.read()) > -1) {	
			if (b == 255) break;
			else sw.write(b);
		}
		
		int contentLength = contentLength(sw.toString());
		byte[] imageBytes = new byte[contentLength + 1];
		int offset = 1;
        int numRead = 0;
        
		imageBytes[0] = (byte) 255; // Put 255 back

		// Get image bytes
        while (offset < imageBytes.length
               && (numRead=urlStream.read(imageBytes, offset, imageBytes.length-offset)) >= 0) {
            offset += numRead;
        }
		
		return imageBytes;
	}

	private static int contentLength(String header) {
		int start = header.indexOf(CONTENT_LENGTH) + CONTENT_LENGTH.length();
		int end = header.indexOf('\n', start);	
		return Integer.parseInt(header.substring(start, end).trim());	
	}
}
