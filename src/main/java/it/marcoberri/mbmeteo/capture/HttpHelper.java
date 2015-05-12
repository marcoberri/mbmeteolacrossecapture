package it.marcoberri.mbmeteo.capture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpHelper {

	final static String USER_AGENT = "Mozilla/5.0";

	public static boolean sendPostData(String url, String data) {

		CloseableHttpClient httpclient = null;
		HttpResponse response = null;
		final File backupFile = new File(ConfigurationHelper.getProperties().getProperty("app.file.save.backup"));
		try {
			FileUtils.writeStringToFile(backupFile, data + "\n", true);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			httpclient = HttpClients.createMinimal();
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			data = data.trim();
			formparams.add(new BasicNameValuePair("data", data));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("send " + data);

			response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			System.out.println("result --> " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200)
				return true;
			return false;

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null)
				try {
					httpclient.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}

		}
		return false;

	}
}