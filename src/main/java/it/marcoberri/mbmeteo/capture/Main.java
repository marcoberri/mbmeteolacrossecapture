package it.marcoberri.mbmeteo.capture;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		Options options = new Options();
		options.addOption("s", false, "send data with command: [" + ConfigurationHelper.getProperties().getProperty("app.command.single") + "] to url: [" + ConfigurationHelper.getProperties().getProperty("app.target.url.single") + "]");
		options.addOption("d", false, "send data with command: [" + ConfigurationHelper.getProperties().getProperty("app.command.dump") + "] to url: [" + ConfigurationHelper.getProperties().getProperty("app.target.url.dump")+ "]");
		options.addOption("h", false, "this help");

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
					HttpHelper.sendPostData(ConfigurationHelper.getProperties().getProperty("app.target.url.single"), output);
				} catch (final Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				
			} else if (cmd.hasOption("d")) {
				System.out.println("start with -d options");
				String output = obj.executeCommand(ConfigurationHelper.getProperties().getProperty("app.command.dump"));
				String data[] = output.split("\n");
				if(data.length <= 1)
					data = output.split(" ");
				
				for(String d : data){
					if(d == null || d.equalsIgnoreCase(""))
						continue;
					try {
						HttpHelper.sendPostData(ConfigurationHelper.getProperties().getProperty("app.target.url.dump"), d);
					} catch (final Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
				
			} else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "java -jar <jar_name>" + "\nVersion:" + ConfigurationHelper.getProperties().getProperty("app.version") + "\nbuild: " + ConfigurationHelper.getProperties().getProperty("app.build"), options);
			}
		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
