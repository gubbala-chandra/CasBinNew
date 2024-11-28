package testutil;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XSDParser {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // Load XSD file
        try {
            // Load the XSD file
            File file = new File("/mnt/a7d909f7-388e-402b-ac1f-eb60b5f3fa88/MyRepository/CasBinNew/UserManagement/src/main/resources/xsd/asset_chnage.xsd");

            // Create DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XSD file into a Document
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            // Get all the elements in the XSD
            NodeList elements = document.getElementsByTagName("xs:element");

            // Loop through the elements and print the required/default attributes
            for (int i = 0; i < elements.getLength(); i++) {
                Node node = elements.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String elementName = element.getAttribute("name");
                    String defaultValue = element.getAttribute("default");
                    String use = element.getAttribute("use");

                    System.out.println("Element: " + elementName);
                    if (!defaultValue.isEmpty()) {
                        System.out.println(" - Default: " + defaultValue);
                    }
                    if ("required".equals(use)) {
                        System.out.println(" - Required: true");
                    } else {
                        System.out.println(" - Required: false");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

