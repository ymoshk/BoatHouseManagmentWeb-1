package engine.xml;

import engine.api.AdaptedEngineContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XmlConverter {

    public static String convertObjectToXMLString(Object objectToConvert) throws JAXBException {
        if (objectToConvert != null) {
            JAXBContext contextObj = JAXBContext.newInstance(objectToConvert.getClass());

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectToConvert, outputStream);
            return outputStream.toString();
        }
        return "Error exporting data";
    }

    public static void convertObjectToXML(Object objectToConvert, String path) throws JAXBException, FileNotFoundException {
        if (objectToConvert != null && path != null) {
            JAXBContext contextObj = JAXBContext.newInstance(objectToConvert.getClass());

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(objectToConvert, new FileOutputStream(path));
        }
    }

    public static AdaptedEngineContext parseEngineFromXML(String path) {
        try {
            File xmlFile = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(AdaptedEngineContext.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (AdaptedEngineContext) jaxbUnmarshaller.unmarshal(xmlFile);

        } catch (JAXBException e) {
            return null;
        }
    }
}
