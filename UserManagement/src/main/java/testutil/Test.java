package testutil;

import org.apache.commons.io.FileUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


public class Test {

    // static final String XML_FILE_PATH = "C:\\fstp-integration\\bytestream\\cost-center\\request-converter.xsl";
    static final String XML_FILE_PATH = "/mnt/a7d909f7-388e-402b-ac1f-eb60b5f3fa88/MyRepository/CasBinNew/UserManagement/src/main/resources/xsd/asset_change.xsl";


    public static void main(String[] args) throws IOException, ParserConfigurationException {

        Map<String,String> parsedResult = XSLParser.parse(XML_FILE_PATH);


       // String storagePath = getStoragePath(XML_FILE_PATH);
        //writeCSVFile(parsedResult, storagePath);

    }

    private static void writeCSVFile(Map<String, String> parsedResult, String storagePath) throws IOException {

        StringBuilder builder = new StringBuilder();
        builder.append("XML-KEY,JSON-KEY\n");


        parsedResult.forEach((key, value) -> {
            builder.append(key).append(",").append(value).append("\n");
        });

        System.out.println("File Stored at the path: " + storagePath);
        FileUtils.writeStringToFile(new File(storagePath), builder.toString());
    }

    private static String getStoragePath(String xmlFilePath) {

        String USER_HOME = System.getProperty("user.home");

        Path path = Paths.get(xmlFilePath);
        Path parentDir = path.getParent();
        String lastFolder = parentDir.getFileName().toString();

        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex);
        }

        String storagePath = USER_HOME + File.separator + "Documents" + File.separator + "XML-JSON-MAPPINGS" + File.separator + lastFolder + File.separator + fileName + ".csv";

        System.out.println(storagePath);
        return storagePath;

    }
}
