package it.marcoberri.mbmeteo.capture;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

public class Main {

	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	public static void main(String[] args) {
		
		if(ConfigurationHelper.getProperties() == null){
			System.out.println("no properties loaded");
			System.exit(1);
		}
		Options options = new Options();
		options.addOption("s", "post-line", false, "post data with command: [" + ConfigurationHelper.getProperties().getProperty("app.command.single") + "] to url: [" + ConfigurationHelper.getProperties().getProperty("app.target.url.single") + "] store backup post in [" + ConfigurationHelper.getProperties().getProperty("app.file.save.backup") + "]");
		options.addOption("d", "post-dump", false, "post data with command: [" + ConfigurationHelper.getProperties().getProperty("app.command.dump") + "] to url: [" + ConfigurationHelper.getProperties().getProperty("app.target.url.dump") + "] store backup post in [" + ConfigurationHelper.getProperties().getProperty("app.file.save.backup") + "]");
		options.addOption("f", "post-from-file", true, "post file to url: [" + ConfigurationHelper.getProperties().getProperty("app.target.url.dump") + "] no store backup in file");
		options.addOption("h", "help", false, "this help");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);

			// comando singolo
			Main obj = new Main();

			if (cmd.hasOption("s")) {
				System.out.println("start with -s options");
				String output = obj.executeCommand(ConfigurationHelper.getProperties().getProperty("app.command.single"));
				System.out.println(output);

				try {
					HttpHelper.sendPostData(ConfigurationHelper.getProperties().getProperty("app.target.url.single"), output, true);
				} catch (final Exception e) {
					e.printStackTrace();
					System.exit(1);
				}

			} else if (cmd.hasOption("d")) {
				System.out.println("start with -d options");
				String output = obj.executeCommand(ConfigurationHelper.getProperties().getProperty("app.command.dump"));
				String data[] = output.split("\n");
				if (data.length <= 1)
					data = output.split(" ");

				for (String d : data) {
					if (d == null || d.equalsIgnoreCase(""))
						continue;
					try {
						HttpHelper.sendPostData(ConfigurationHelper.getProperties().getProperty("app.target.url.dump"), d, true);
					} catch (final Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
				}

			} else if (cmd.hasOption("f")) {
				try {
					System.out.println("start with -f options with file [" + cmd.getOptionValue("f") + "]");
				} catch (final Exception e) {
					e.printStackTrace();
					System.exit(1);
				}

				List<String> data = null;
				try {
					data = FileUtils.readLines(new File(cmd.getOptionValue("f")), "UTF8");
				} catch (final IOException e1) {
					e1.printStackTrace();
					System.exit(1);
				}
				if (data == null || data.isEmpty()) {
					System.out.println("No data found in file [" + cmd.getOptionValue("f") + "]");
					System.exit(1);
				}
				for (String d : data) {
					if (d == null || d.equalsIgnoreCase(""))
						continue;
					try {
						HttpHelper.sendPostData(ConfigurationHelper.getProperties().getProperty("app.target.url.dump"), d, false);
					} catch (final Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
				}

			} else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar <jar_name>" + "\nVersion:" + ConfigurationHelper.getProperties().getProperty("app.version") + "\nbuild: " + ConfigurationHelper.getProperties().getProperty("app.build"), options);
			}
		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
