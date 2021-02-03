package engine.xml.adapter;


import engine.api.EngineContext;
import engine.model.rower.AdaptedRower;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RowerAdapter extends XmlAdapter<AdaptedRower, Rower> {

    @Override
    public Rower unmarshal(AdaptedRower adaptedRower) throws Exception {
        String serialNumber = adaptedRower.getSerialNumber();
        String name = adaptedRower.getName();
        int age = adaptedRower.getAge();
        List<String> notes;
        if (adaptedRower.getNotes() == null) {
            notes = new ArrayList<>();
        } else {
            notes = adaptedRower.getNotes();
        }
        String phoneNumber = adaptedRower.getPhoneNumber();
        String email = adaptedRower.getEmail();
        String password = adaptedRower.getPassword();
        Rower.eRowerRank rank = adaptedRower.getRank();
        LocalDate joiningDate = LocalDate.parse(adaptedRower.getJoinDate(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate expirationDate = LocalDate.parse(adaptedRower.getExpirationDate(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] privateBoatsSerials = adaptedRower.getPrivateBoatsSerialNumbers();
        boolean isAdmin = adaptedRower.isAdmin();

        if (privateBoatsSerials == null || privateBoatsSerials.length == 0) {
            Rower res = new Rower(serialNumber, name, age, rank, password, isAdmin, email,
                    phoneNumber, joiningDate, expirationDate);
            res.getNotes().addAll(notes);
            return res;
        } else {
            Rower res = new Rower(serialNumber, name, age, rank, password, isAdmin, email,
                    phoneNumber, joiningDate, expirationDate);
            RowerModifier modifier = EngineContext.getInstance().getRowerModifier(res, null);
            Collections.addAll(modifier.getModifiablePrivateBoats(), privateBoatsSerials);
            res.getNotes().addAll(notes);
            return res;
        }
    }

    @Override
    public AdaptedRower marshal(Rower rower) throws Exception {
        AdaptedRower adaptedRower = new AdaptedRower();
        adaptedRower.setAdmin(rower.isAdmin());
        adaptedRower.setAge(rower.getAge());
        adaptedRower.setEmail(rower.getEmail());
        adaptedRower.setExpirationDate(rower.getFormattedExpirationDate());
        adaptedRower.setJoinDate(rower.getFormattedJoiningDate());
        adaptedRower.setSerialNumber(rower.getSerialNumber());
        adaptedRower.setName(rower.getName());
        adaptedRower.setNotes(rower.getNotes());
        adaptedRower.setPhoneNumber(rower.getPhoneNumber());
        adaptedRower.setPassword(rower.getPassword());
        adaptedRower.setPrivateBoatsSerialNumbers(rower.getPrivateBoatsSerialNumbers().toArray(new String[0]));
        adaptedRower.setRank(rower.getRank());

        return adaptedRower;
    }
}
