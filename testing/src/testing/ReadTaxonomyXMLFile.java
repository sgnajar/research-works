package testing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadTaxonomyXMLFile {
	public static void main(String argv[]) {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			FileWriter fw = new FileWriter("output", true);
			BufferedWriter bw = new BufferedWriter(fw);

			DefaultHandler handler = new DefaultHandler() {

				boolean taxaId = false;
				boolean sciName = false;
				boolean ParentTaxId = false;
				boolean Rank = false;
				boolean Division = false;
				boolean Lineage = false;
				boolean LineageEx = false;
				boolean PubDate = false;
				

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					// System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("TaxId")) {
						taxaId = true;
					}

					if (qName.equalsIgnoreCase("ScientificName")) {
						sciName = true;
					}

					if (qName.equalsIgnoreCase("ParentTaxId")) {
						ParentTaxId = true;
					}

					if (qName.equalsIgnoreCase("Rank")) {
						Rank = true;
					}

					if (qName.equalsIgnoreCase("Division")) {
						Division = true;
					}

					if (qName.equalsIgnoreCase("Lineage")) {
						Lineage = true;
					}
					if (qName.equalsIgnoreCase("LineageEx")) {
						LineageEx = true;
					}
					if (qName.equalsIgnoreCase("PubDate")) {
						PubDate = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					// System.out.println("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length) throws SAXException {
					try {
						if (taxaId) {
							System.out.println("Tax Id: " + new String(ch, start, length));

							taxaId = false;
						}

						if (sciName) {
							System.out.println("ScientificName: " + new String(ch, start, length));
							sciName = false;
						}

						if (ParentTaxId) {
							System.out.println("ParentTaxId: " + new String(ch, start, length));
							ParentTaxId = false;
						}

						if (Rank) {
							System.out.println("Rank: " + new String(ch, start, length));
							Rank = false;
						}

						if (Division) {
							System.out.println("Division: " + new String(ch, start, length));
							Division = false;
						}

						if (Lineage) {
							System.out.println("Lineage: " + new String(ch, start, length));
							Lineage = false;
						}
						if (LineageEx) {
							System.out.println("LineageEx: " + new String(ch, start, length));
							LineageEx = false;
							System.out.println("Children\n");
						}
						if (PubDate) {
							System.out.println("\nPubDate: " + new String(ch, start, length));
							PubDate = false;
						}

						
						

						
						
						
						
						
					} catch (IOException e) {
						e.printStackTrace();

					}

				}

			};

			saxParser.parse("/Users/snajar/Desktop/taxa.xml", handler);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
