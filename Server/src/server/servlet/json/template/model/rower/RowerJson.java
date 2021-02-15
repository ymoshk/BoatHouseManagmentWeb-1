package server.servlet.json.template.model.rower;

import engine.model.rower.Rower;

import java.util.List;
import java.util.Set;

public class RowerJson {
    public final String serialNumber;
    public final String name;
    public final int age;
    public final String phone;
    public final String email;
    public final int rank;
    public final boolean isAdmin;
    public final Set<String> boatsId;
    public final List<String> notes;
    public final String joiningDate;
    public final String expirationDate;
    private final String id;

    public RowerJson(Rower rower){
        this.age = rower.getAge();
        this.rank = Rower.eRowerRank.getIntFromRank(rower.getRank());
        this.serialNumber = rower.getSerialNumber();
        this.id = rower.getId();
        this.name = rower.getName();
        this.phone = rower.getPhoneNumber();
        this.email = rower.getEmail();
        this.isAdmin = rower.isAdmin();
        this.boatsId = rower.getPrivateBoatsSerialNumbers();
        this.notes = rower.getNotes();
        this.joiningDate = rower.getFormattedJoiningDate();
        this.expirationDate = rower.getFormattedExpirationDate();
    }
}
