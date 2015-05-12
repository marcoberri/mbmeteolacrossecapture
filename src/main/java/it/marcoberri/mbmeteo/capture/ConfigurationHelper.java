package it.marcoberri.mbmeteo.capture;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.Configuration;

import org.apache.commons.io.FileUtils;

public class ConfigurationHelper {

	private static final Properties properties = initProperties();

	private ConfigurationHelper() {
	}

	private static Properties initProperties() {
		final Properties prop = new Properties();
		try {
			File[] files = getPropertiesFiles();
			for (File f : files) {
				Properties p = readPropertiesFile(f);
				prop.putAll(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private static File[] getPropertiesFiles() {
		final File baseDir = FileUtils.toFile(ConfigurationHelper.class.getResource("/"));
		return baseDir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".properties");
			}
		});
	}

	private static Properties readPropertiesFile(File f) throws IOException {
		final Properties p = new Properties();
		p.load(FileUtils.openInputStream(f));
		return p;
	}

	public static Properties readPropertiesFile(String f_name) throws IOException {
		final File baseDir = FileUtils.toFile(Configuration.class.getResource("/"));
		final Properties p = new Properties();
		p.load(FileUtils.openInputStream(new File(baseDir + "/" + f_name)));
		return p;
	}

	public static String getProperty(IProperty prop) {
		return getProperty(prop.getKey());
	}

	public static String getProperty(String name) {
		return properties.getProperty(name);
	}

	public static String getProperty(IProperty prop, String defaultValue) {
		return getProperty(prop.getKey(), defaultValue);
	}

	public static String getProperty(String name, String defaultValue) {
		return properties.getProperty(name, defaultValue);
	}

	public static Properties getProperties() {
		return properties;
	}

}
