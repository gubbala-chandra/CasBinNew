package testutil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.user.util.Utils.isEmpty;

public class XSLParser {

    public static Map<String, String> parse(String xmlFilePath) {
        Map<String,String> map = new LinkedHashMap<>();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFilePath);

            NodeList templateNodes = document.getElementsByTagName("xsl:template");

            // Iterate through each xsl:template element
            for (int i = 0; i < templateNodes.getLength(); i++) {
                Element templateElement = (Element) templateNodes.item(i);
                String xmlKey = templateElement.getAttribute("match");

                String xml = elementToString(templateElement);
                String jsonKey = getMatcher(xml);

                map.put(xmlKey, jsonKey);
            }

            map.forEach((key,value) -> System.out.println(key + "," + value));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String elementToString(Element element) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(element), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMatcher(String input) {
        String regex = "\"([^\"]+)\":";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(matcher.group(1)).append(",");
        }

        if(!isEmpty(result)) {
            result.deleteCharAt( result.length() - 1 );
        }
        return result.toString();
    }
}
