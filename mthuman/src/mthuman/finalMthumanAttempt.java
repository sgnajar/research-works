package mthuman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.stream.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class finalMthumanAttempt {
	String mtHumanIdList;
	String curId;
	String gbAccId;
	String countrytag;
	
	FileWriter fw; 
	BufferedWriter bw; 
	
	private String getIDs() {
		return mtHumanIdList;
	}

	public finalMthumanAttempt(String searchTerm) {
		xmlFromMtHuman(searchTerm);
	}

	public JSONObject xmlFromMtHuman(String searchTerm) {

		String searchURLjson = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=nuccore&term="
				+ searchTerm + "&retmode=json";

		try {
			URL request = new URL(searchURLjson);
			HttpURLConnection connection = (HttpURLConnection) request.openConnection();

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String text = "";
			String lineInText = "";
			while ((lineInText = br.readLine()) != null) {
				text += lineInText + "\n";
			}

			br.close();
			JSONObject json = new JSONObject(text);

			int retMax = 500;
			int iteration = 500;
			ArrayList<String> IdList = new ArrayList<String>();
			JSONObject esearchResult = json.getJSONObject("esearchresult");
			int totalCount = esearchResult.getInt("count");
			System.out.println("Total counts: " + totalCount);
			// totalCount
			for (int retstart = 0; retstart < totalCount; retstart += iteration) {
				String test = "&retMax=" + retMax + "&retstart=" + retstart;
				String test2 = searchURLjson + test;
				// System.out.println("This is test2 :" + test2);
				URL request2 = new URL(test2);
				HttpURLConnection connection2 = (HttpURLConnection) request2.openConnection();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
				String text2 = "";
				String lineInText2 = "";
				while ((lineInText2 = br2.readLine()) != null) {
					text2 += lineInText2 + "\n";
				}
				br2.close();
				JSONObject json2 = new JSONObject(text2);

				JSONArray searchIdList = json2.getJSONObject("esearchresult").getJSONArray("idlist");

				System.out.println("preparing IdList from retstart: " + retstart);
				//
				for (int cId = 0; cId < searchIdList.length(); cId++) {
					mtHumanIdList = searchIdList.get(cId).toString();
					if (mtHumanIdList.length() > 1) {
						IdList.add(mtHumanIdList);
					}
				}

			}
			System.out.println(IdList.size());
			String pullData = null;

			System.out.println(IdList.size());
			int count = 1;
			fw = new FileWriter("mtGenome_idPlace", true);
			bw = new BufferedWriter(fw);
			bw.write("genBankId\tcountry\n");
			
			for (int i = 0; i < IdList.size(); i++) {
				// System.out.print("this is the line" + IdList.get(i));
				curId = IdList.get(i);
				System.out.println("###################################");
				System.out.println(count + ". current Id: " + curId);
				count++;
				// System.out.println("curId length: " + curId.length());
				pullData = getxml(curId);
			}

			System.out.println("Processing finished.");
			bw.close();

		} catch (Exception e) {
			System.out.println("An error was thrown: " + e);
		}
		return null;
	}

	private String getxml(String stringID) throws IOException {
		String fetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=" + stringID
				+ "&retmode=xml";


		// System.out.println("Downloading xml File: " + stringID);
		// try {
		//
		//
		// downloadUsingNIO(fetchURL, "/Volumes/G-DRIVE mobile USB/xml_data/" +
		// stringID + ".xml");
		// System.out.println(stringID + ".xml downloaded.\n");
		//
		// } catch (Exception ex) {
		// System.out.println("An error was thrown2: " + ex);
		// }

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();


			DefaultHandler handler = new DefaultHandler() {

				boolean GBSeq_locus = false;
				boolean GBQualifier_name = false;
				boolean GBQualifier_value = false;

				boolean organism = false;
				boolean organelle = false;
				boolean mol_type = false;
				boolean isolate = false;
				boolean db_xref = false;
				boolean haplotype = false;
				boolean country = false;
				boolean PubDate = false;
				

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					// System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("GBSeq_locus")) {
						GBSeq_locus = true;
					}

					if (qName.equalsIgnoreCase("GBQualifier_name")) {
						GBQualifier_name = true;

					}
					if (qName.equalsIgnoreCase("GBQualifier_value")) {
						GBQualifier_value = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					// System.out.pees rintln("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (GBSeq_locus) {
						gbAccId = new String(ch, start, length);
						System.out.println("Accession ID: " + gbAccId);
						GBSeq_locus = false;
					}

					if (GBQualifier_name) {
						String tempValue = new String(ch, start, length);
						String tempValue2 = new String(ch, start, length);

						//System.out.println("GBQualifier_name: " + new String(ch, start, length));

						if (tempValue.equals("organism")) {
							organism = true;
							GBQualifier_name = false;

						} else if (tempValue2.equals("organelle")) {
							organelle = true;
							GBQualifier_name = false;

						} else if (tempValue.equals("mol_type")) {
							mol_type = true;

						} else if (tempValue.equals("isolate")) {
							isolate = true;

						} else if (tempValue.equals("db_xref")) {
							db_xref = true;

						} else if (tempValue.equals("haplotype")) {
							haplotype = true;

						} else if (tempValue.equals("country")) {
							country = true;

						}

						GBQualifier_name = false;
					}

					if (GBQualifier_value) {
						if (organism == true) {
							//System.out.println("Organism; " + new String(ch, start, length));
							organism = false;

						} else if (organelle == true) {
							//System.out.println("organelle; " + new String(ch, start, length));
							organelle = false;
							
						} else if (mol_type == true) {
							//System.out.println("mol_type; " + new String(ch, start, length));
							mol_type = false;
							
						} else if (isolate == true) {
							//System.out.println("isolate; " + new String(ch, start, length));
							isolate = false;
							
						} else if (db_xref == true) {
							//System.out.println("db_xref; " + new String(ch, start, length));
							db_xref = false;
							
						} else if (haplotype == true) {
							//System.out.println("haplotype; " + new String(ch, start, length));
							haplotype = false;

						} else if (country == true) {
							countrytag = new String(ch, start, length);
							System.out.println(curId + " country; " + countrytag);

							country = false;
							try {
								System.out.println("writing to output file ... ");
								bw.write(gbAccId + "\t" + countrytag + "\n");
								
								
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
						
						 //System.out.println("GBQualifier_value: " + new String(ch, start, length));
						 GBQualifier_value = false;
					}
				}

			};

			saxParser.parse(fetchURL, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fetchURL;
	}

	private static void downloadUsingNIO(String urlStr, String file) throws IOException {
		URL fetchURL = new URL(urlStr);
		ReadableByteChannel rbc = Channels.newChannel(fetchURL.openStream());

		File yourFile = new File(file);
		yourFile.createNewFile();

		// two lines above and added false below
		FileOutputStream fos = new FileOutputStream(file, false);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}

	public static void main(String[] args) {
		String searchTerm = ("(015400%5BSLEN%5D%3A016600%5BSLEN%5D)AND+Homo%5BOrganism%5D+AND+mitochondrion%5BFILT%5D+NOT+(Homo+sp.+Altai+OR+Denisova+hominin+OR+neanderthalensis+OR+heidelbergensis+OR+consensus+OR+ancient+human+remains)");
		;
		finalMthumanAttempt test = new finalMthumanAttempt(searchTerm);
	}
}
