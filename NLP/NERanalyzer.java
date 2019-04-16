package NameEntityRecognition;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class NERanalyzer {
	private String articleAbs;
	private String[] locations;

	public NERanalyzer(String pmid) {
		getAbstract(pmid);
	}

	public String getAbs() {
		return articleAbs;
	}

	public String[] getLocations() {
		return locations;
	}

	private String getAbstract(String pmid) {

		String searchURL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + pmid
				+ "&retmode=xml";

		System.out.println("Running: " + searchURL);
		try {
			URL request = new URL(searchURL);
			HttpURLConnection connection = (HttpURLConnection) request.openConnection();
			connection.setRequestMethod("GET");
			//
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder hammerNNails = factory.newDocumentBuilder();
			org.w3c.dom.Document results = hammerNNails.parse(connection.getInputStream());

			DOMSource domSource = new DOMSource(results);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			// System.out.println("XML IN String format is: \n" +
			// writer.toString());
			/*
			 * somewhere here parseXML should be functioning
			 */
			NodeList nList = results.getElementsByTagName("PubmedArticle");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				// String NodeName = nNode.getNodeName();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					pmid = eElement.getElementsByTagName("PMID").item(0).getTextContent();

					articleAbs = eElement.getElementsByTagName("AbstractText").item(0).getTextContent();
					System.out.println("PubMed ID " + pmid + " selected.");
					System.out.println("Abstract for this PubMed ID ( " + pmid + " )\n\n " + articleAbs + "\n");
					locations = NameEntityRecognition(articleAbs);
					System.out.println("******************************************************************");
				}
			}
		} catch (Exception e) {
			System.out.println("An error was thrown: " + e);
		}
		return searchURL;

	}

	private String[] NameEntityRecognition(String inputString) {
		System.out.println("Looking for location(s) started...");
		String serializedClassifier = "/Users/snajar/Documents/Tools/stanford-ner-2015-12-09/classifiers/english.all.3class.distsim.crf.ser.gz";
		try {
			AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
			// System.out.println("this is your base string: " + str);
			String test = classifier.classifyToString(inputString, "tabbedEntities", false);
			//System.out.println("This is the result: " + test);
			String[] results = test.split("\t");
			ArrayList<Integer> ofInterest = new ArrayList<Integer>();
			for (int i = 0; i < results.length; i++) {
				if (results[i].equals("LOCATION")) {
					ofInterest.add(i - 1);
				}
			}
			// System.out.println("Location(s) found in this abstract with PMID#
			// " + pmid);
			String[] output = new String[ofInterest.size()];
			for (int i = 0; i < ofInterest.size(); i++) {
				output[i] = results[ofInterest.get(i)].split("\n")[1];
			}
			return output;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
