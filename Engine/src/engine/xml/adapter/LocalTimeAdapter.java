package engine.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String adaptedDate) throws Exception {
        return LocalTime.parse(adaptedDate, DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String marshal(LocalTime date) throws Exception {
        return date.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
