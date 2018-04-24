package taxonomy;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class pullTaxonomy {
	private String taxoIdList;
	
	
	public pullTaxonomy(String searchTerm){
		getTreeFromTaxo(searchTerm);
	}
	
	
	public void getTreeFromTaxo(String searchTerm){
		String searchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=taxonomy&term=" + searchTerm + "&retmode=xml";
		System.out.println(searchURL);
		
		try {
			// File outputFile = new File("newSearch_" + searchTerm + ".txt");
			// // if file doesnot exist, then create it
			// if (outputFile.exists()) {
			// outputFile.createNewFile();
			// }
			// FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
			// BufferedWriter bw = new BufferedWriter(fw);
			URL request = new URL(searchURL);
			HttpURLConnection connection = (HttpURLConnection) request.openConnection();
			connection.setRequestMethod("GET");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder hammerNNails = factory.newDocumentBuilder();
			org.w3c.dom.Document results = hammerNNails.parse(connection.getInputStream());
			DOMSource domSource = new DOMSource(results);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			NodeList nList = results.getElementsByTagName("eSearchResult");
			NodeList children = results.getChildNodes();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				String NodeName = nNode.getNodeName();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String counts = eElement.getElementsByTagName("Count").item(0).getTextContent();
					System.out.println("Found: " + counts + " hits.");
				}
			}
			//System.out.println("******************************************************************\n");
			//System.out.println("List of Taxo ID for this searchTerm(s): " + searchTerm);
			// bw.write("List of taxo ID for this search: " + term);
			Node parent = results.getElementsByTagName("IdList").item(0);
			NodeList child = parent.getChildNodes();
			List<String> IdList = new ArrayList<String>();
			String pullAbstractArticle;
			for (int i = 0; i < child.getLength(); i++) {
				Node current = child.item(i);
				// System.out.print(current.getTextContent());
				taxoIdList = current.getTextContent();
				// bw.write(current.getTextContent());
				if(taxoIdList.length() > 1){
					IdList.add(taxoIdList);
					
				}
				//System.out.print(taxoIdList);
			}
//			for (int i = 0; i < IdList.size(); i++) {
//				// System.out.print("this is the line" + IdList.get(i));
//				String curId = IdList.get(i);
//				//System.out.println("Current Id in for loop: " + curId);
//				//System.out.println("curId length: " + curId.length());
//				pullSubTree = getSubTree(curId);
//			}
			// do a single search
			// pullAbstractArticle = getSubTree(27749938);

		} catch (Exception e) {
			System.out.println("An error was thrown: " + e);
		}
	}
		
	private String getSubTree(String string){
		String fetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=taxonomy&id=" + string + "&retmode=xml";
		System.out.println(fetchURL);
		try{
//			URL request = new URL(fetchURL);
//			HttpURLConnection connection = (HttpURLConnection) request.openConnection();
//			connection.setRequestMethod("GET");
			//
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder hammerNNails = factory.newDocumentBuilder();
//			org.w3c.dom.Document results = hammerNNails.parse(connection.getInputStream());
//
//			DOMSource domSource = new DOMSource(results);
//			StringWriter writer = new StringWriter();
//			StreamResult result = new StreamResult(writer);
//			TransformerFactory tf = TransformerFactory.newInstance();
//			javax.xml.transform.Transformer transformer = tf.newTransformer();
//			transformer.transform(domSource, result);
			
			
			
			
		} catch (Exception ex){
			System.out.println("An error was thrown2: " + ex);
		}
		return fetchURL;
	}	
		
	
	
	
	
	
	public static void main(String[] args) {
		String searchTerm = "txid10239[Subtree]"; //for viruses superkingdom"
		pullTaxonomy test = new pullTaxonomy(searchTerm);
	}

}
