package engine.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String adaptedDate) throws Exception {
        return LocalDate.parse(adaptedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
