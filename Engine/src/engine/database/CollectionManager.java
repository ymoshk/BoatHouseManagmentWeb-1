package engine.database;


import engine.api.EngineInterface;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class CollectionManager<T> implements Iterable<T>, Serializable {
    protected final List<T> data;
    protected final EngineInterface engine;

    public CollectionManager(EngineInterface engine) {
        this.data = new ArrayList<>(10);
        this.engine = engine;
    }

    public CollectionManager(EngineInterface engine, List<T> data) {
        this.data = data;
        this.engine = engine;
    }

    // XML converter only
    public CollectionManager() {
        this.data = new ArrayList<>(10);
        this.engine = null;
    }

    public abstract boolean add(T toAdd);

    public abstract boolean remove(T toRemove) throws Exception;

    public void clean() throws Exception {
        for (T data : this.data) {
            remove(data);
        }
    }

    public T get(int index) {
        return this.data.get(index);
    }

    public int size() {
        return this.data.size();
    }


    public List<T> toArrayList() {
        return Collections.unmodifiableList(this.data);
    }

    @Override
    public Iterator<T> iterator() {
        return this.data.iterator();
    }

    public List<T> filter(Predicate<T> condition) {
        List<T> result = new ArrayList<>();

        for (T item : this) {
            if (condition.test(item)) {
                result.add(item);
            }
        }

        return Collections.unmodifiableList(result);
    }

    public abstract void importFromXml(String xmlAsString, boolean cleanAll, StringBuilder stringBuilder) throws JAXBException;

    public abstract String exportToXml() throws JAXBException;

    protected Unmarshaller initializeJAXB(Class<?> classToUnmarshall, String sourceName) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classToUnmarshall);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = sf.newSchema(getClass().getClassLoader().getResource(sourceName));
        } catch (SAXException ignored) {

        }
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);

        return unmarshaller;
    }

    public T find(T objectToFind){
        for(T obj : this.data){
            if(obj.equals(objectToFind)){
                return obj;
            }
        }

        return null;
    }
}
