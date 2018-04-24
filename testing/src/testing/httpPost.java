package testing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class httpPost {
	//private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {
		httpPost http = new httpPost();

		System.out.println("\nTesting 2 - Send Http POST request");
		http.sendPost();
	}

	private void sendPost() throws Exception {
		String url = "http://bioinformatics.ua.pt/becas/api/pubmed/annotate/28575076?email=snajar@uncc.edu&tool=test";
		String url2 = "http://bioinformatics.ua.pt/becas/api/text/annotate?email=snajar@uncc.edu&tool=test";


		try {
			URL request = new URL(url);
			HttpURLConnection con = (HttpURLConnection) request.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");

			//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			//wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("\nSending 'POST' request to URL : " + url2);

			//System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();


			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result

			String result = response.toString();

			int PRETTY_PRINT_INDENT_FACTOR = 4;

			JSONObject json = new JSONObject(result);
			System.out.println("\n" + json.getClass().getName());

//			System.out.println("HERE is the original json... " + json);

			String jsonPrettyPrintString = json.toString(PRETTY_PRINT_INDENT_FACTOR);
			System.out.println(jsonPrettyPrintString);



		} catch (Exception e) {
			System.out.println("ERROR thrown: " + e);
		}

	}

	private String result(int pRETTY_PRINT_INDENT_FACTOR) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
