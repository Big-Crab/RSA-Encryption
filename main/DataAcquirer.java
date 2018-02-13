package main;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;

import jdk.internal.org.xml.sax.SAXException;

public class DataAcquirer {

	NodeList nList;
	Document doc;
	String path = null;

	public void loadFile(){
		int maxRetries = 4;
		int retries = 0;
		while(true){
			path = null;
			try {
				path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
				path = new String(path + "\\AsymmCrypt/DataKeys.xml");
				File fXmlFile = new File(path);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(fXmlFile);

				//Optional I think?
				//stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				nList = doc.getElementsByTagName("slot");
				break;
			}
			catch (java.io.FileNotFoundException ioe){
				File file = new File(path);
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				if(++retries == maxRetries) break;
			}
			catch (Exception e) {
				e.printStackTrace();
				if(++retries == maxRetries) break;
			}
		}
	}

	public String[] giveKeys(int index, String result[]) {
		result = new String[5]; //ID, Pub, Priv, P, Q
		loadFile();

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE && i == 0) {
				Element eElement = (Element) nNode;
				result[0] = eElement.getAttribute("id");
				result[1] = eElement.getElementsByTagName("publickey").item(0).getTextContent();
				result[2] = eElement.getElementsByTagName("privatekey").item(0).getTextContent();
				result[3] = eElement.getElementsByTagName("pvalue").item(0).getTextContent();
				result[4] = eElement.getElementsByTagName("qvalue").item(0).getTextContent();
			}
		}
		return result;
	}
	public void saveKeys(int index, String inputs[]){
		//inputs = new String[5];

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE && i == 0) {
				Element eElement = (Element) nNode;
				eElement.getElementsByTagName("publickey").item(0).setTextContent(inputs[1]);
				eElement.getElementsByTagName("privatekey").item(0).setTextContent(inputs[2]);
				eElement.getElementsByTagName("pvalue").item(0).setTextContent(inputs[3]);
				eElement.getElementsByTagName("qvalue").item(0).setTextContent(inputs[4]);
			}
		}
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);
			System.out.println("Saved!");
		}
		catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}