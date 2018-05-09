import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: xsl_file xml_file output_file [file_encoding]");
            return;
        }
        try {
            String encoding = args.length == 4 ? args[3] : DEFAULT_ENCODING;

            InputStreamReader xslISR = new InputStreamReader(new FileInputStream(args[0]), encoding);
            StreamSource xslStreamSource = new StreamSource(new BufferedReader(xslISR));

            InputSource xmlIS = new InputSource(new InputStreamReader(new FileInputStream(args[1]), encoding));
            xmlIS.setEncoding(encoding);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); // disable DTD validation
            Document document = dbFactory.newDocumentBuilder().parse(xmlIS);
            Source xmlSource = new DOMSource(document.getFirstChild().getParentNode());  // get root node

            StreamResult out = new StreamResult(new FileOutputStream(args[2]));

            System.out.println("Using encoding: " + encoding);
            System.out.println("Using XSL file: " + args[0]);
            System.out.println("Using XML file: " + args[1]);

            Transformer transformer = TransformerFactory.newInstance().newTransformer(xslStreamSource);
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.transform(xmlSource, out);

            System.out.println("Transformation completed. Result stored in file: " + args[2]);
        } catch (Exception ex) {
            System.err.println("An error occurred while transforming XML file\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
