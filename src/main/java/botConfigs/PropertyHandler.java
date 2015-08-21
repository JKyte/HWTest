package botConfigs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 
 * @author JKyte, based largely on code from http://www.mkyong.com/java/java-properties-file-examples/
 *
 */
public class PropertyHandler {

	//	User should change production default to point at production properties
	public final String PRODUCTION_DEFAULT = "./properties/production.properties";
	public final String TEST_DEFAULT = "./properties/default.properties";

	public static void writePropertyFile(String filename, Properties prop){

		OutputStream output = null;
		try {

			output = new FileOutputStream(filename);
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Properties readPropertyFile(String filename){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(filename);
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static Properties updateExistingPropertyFile(String filename, String key, String value){
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(filename);
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		prop.setProperty(key, value);

		OutputStream output = null;
		try {

			output = new FileOutputStream(filename);
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
