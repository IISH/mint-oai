package gr.ntua.ivml.mint.oai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletContext;

public class Config {
	public final static String PROPERTIES = "pi.properties";
	public static Properties properties = new Properties(System.getProperties());
	
	public static String get( String key ) {
		checkAndRead();		
		return properties.getProperty(key);
	}
	
	public static boolean getBoolean( String key ) {
		String result = Config.get(key);
		
		if(result != null) {
			if(result.equalsIgnoreCase("true") || result.equalsIgnoreCase("yes") || result.equalsIgnoreCase("1")) {
				return true;
			}
		}
		
		return false;
	}
	
	public static int getInt( String key ) {
		String result = Config.get(key);
		
		if(result != null) {
			int i = Integer.parseInt(result);
			return i;
		}
		
		return 0;
	}
	
	public static boolean has( String key ) {
		checkAndRead();
		return properties.containsKey(key);
	}
	
	public static String get( String key, String defaultValue ) {
		checkAndRead();
		return properties.getProperty( key, defaultValue );
	}
	
	private static void checkAndRead() {
		readProperties();
	}
	
	private static void readProperties() {
	    try {
	    		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(PROPERTIES);
	    		if(inputStream == null) {
	    			System.err.println(PROPERTIES + " not loaded");
	    		}
	        properties.load(inputStream);
	    } catch(Exception e) {
	    		throw new Error( "Configuration file " + PROPERTIES + " not found in CLASSPATH", e);
	    }
	}

	private static String[] decomposeJsonQuery(String query){return query.split("\\.");}
	
	private static String convertStreamToString(InputStream is) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				reader.close();
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
}
