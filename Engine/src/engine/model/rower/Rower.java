package engine.model.rower;

import engine.api.EngineContext;
import engine.model.Model;
import engine.model.boat.Boat;
import engine.utils.RegexHandler;
import engine.xml.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@XmlRootElement(name = "Rower")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rower extends Model implements Serializable {

    @XmlAttribute
    @XmlID
    private final String serialNumber;
    @XmlList
    private final List<String> notes;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private final LocalDate joinDate;
    @XmlList
    private final Set<String> privateBoatsSerialNumbers;
    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private String password;
    private eRowerRank rank;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate expirationDate;
    private boolean isAdmin;

    public Rower() {
        this.serialNumber = null;
        this.notes = null;
        this.joinDate = null;
        this.privateBoatsSerialNumbers = null;
    }

    public Rower(String serialNumber, String name, int age, eRowerRank rank, String password,
                 boolean isAdmin, String email, String phoneNumber) {

        this.serialNumber = serialNumber;
        this.joinDate = LocalDate.now();
        this.expirationDate = LocalDate.now().plusYears(1);
        this.name = name;
        this.age = age;
        this.password = password;
        this.isAdmin = isAdmin;
        this.rank = rank;
        setMail(email);
        setPhoneNumber(phoneNumber);
        this.privateBoatsSerialNumbers = new HashSet<>();
        this.notes = new ArrayList<>();
    }

    public Rower(String serialNumber, String name, int age, eRowerRank rank, String password, boolean isAdmin,
                 String email, String phoneNumber, String... privateBoatsSerialNumbers) {

        this(serialNumber, name, age, rank, password, isAdmin, email, phoneNumber);
        Collections.addAll(this.privateBoatsSerialNumbers, privateBoatsSerialNumbers);
    }

    // For XML adapter only
    public Rower(String serialNumber, String name, int age, eRowerRank rank, String password,
                 boolean isAdmin, String email, String phoneNumber, LocalDate joinDate, LocalDate expirationDate) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.age = age;
        this.password = password;
        this.isAdmin = isAdmin;
        this.rank = rank;
        setMail(email);
        setPhoneNumber(phoneNumber);
        this.privateBoatsSerialNumbers = new HashSet<>();
        this.notes = new ArrayList<>();
        this.expirationDate = expirationDate;
        this.joinDate = joinDate;
    }


    // ONLY for the XML input/output
    public Rower(String serialNumber, String name, int age, eRowerRank rank, String password,
                 boolean isAdmin, String email, String phone, LocalDate joinDate,
                 LocalDate expirationDate, List<String> privateBoatId) {
        this(serialNumber, name, age, rank, password, isAdmin, email, phone, joinDate, expirationDate);

        if (privateBoatId != null) {
            this.privateBoatsSerialNumbers.addAll(privateBoatId);
        }
    }


    private Rower(String id, String serialNumber, String name, int age, eRowerRank rank, String password, boolean isAdmin, String email, String phoneNumber, String[] ids) {
        this(serialNumber, name, age, rank, password, isAdmin, email, phoneNumber, ids);
        this.setId(id);
    }


    public String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    void setAge(int age) {
        this.age = age;
    }

    public int getBoatsCount() {
        return this.privateBoatsSerialNumbers.size();
    }

    public Set<String> getPrivateBoatsSerialNumbers() {
        return Collections.unmodifiableSet(this.privateBoatsSerialNumbers);
    }

    Set<String> getModifiablePrivateBoatsSerials() {
        return this.privateBoatsSerialNumbers;
    }

    public List<String> getNotes() {
        return Collections.unmodifiableList(this.notes);
    }

    List<String> getModifiableNotes() {
        return this.notes;
    }

    public StringBuilder getNotesAsStringBuilder() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < this.notes.size(); i++) {
            result.append(String.format("%d. %s%n", i, this.notes.get(i)));
        }
        return result;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    void setPhoneNumber(String phoneNumber) throws InvalidParameterException {
        if (RegexHandler.isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new InvalidParameterException("Invalid phone number received.");
        }
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public eRowerRank getRank() {
        return this.rank;
    }

    void setRank(eRowerRank rank) {
        this.rank = rank;
    }

    public LocalDate getJoinDate() {
        return this.joinDate;
    }

    public String getFormattedJoiningDate() {
        return this.joinDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getFormattedExpirationDate() {
        return this.expirationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public boolean hasPrivateBoat() {
        return !this.privateBoatsSerialNumbers.isEmpty();
    }

    public boolean addPrivateBoatAndChangeItsOwner(Boat boatToAdd) throws Exception {
        if (boatToAdd.hasOwner()) {
            return false;
        } else {
            this.privateBoatsSerialNumbers.add(boatToAdd.getSerialNumber());
            EngineContext.getInstance().getBoatModifier(boatToAdd, null).setOwner(this);
            return true;
        }
    }

    public void addPrivateBoat(Boat boatToAdd) {
        this.privateBoatsSerialNumbers.add(boatToAdd.getSerialNumber());
    }

    public void removePrivateBoat(Boat boatToRemove) {
        this.getModifiablePrivateBoatsSerials().remove(boatToRemove.getSerialNumber());
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    void changeIsAdminStatus(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    void setMail(String email) throws InvalidParameterException {
        if (RegexHandler.isEmailAddressValid(email)) {
            this.email = email;
        } else {
            throw new InvalidParameterException("Invalid email received.");
        }
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public Rower clone() {
        return clone(false);
    }


    public Rower clone(boolean keepId) {
        List<String> privateArr = new ArrayList<>(this.privateBoatsSerialNumbers);
        String[] ids = new String[this.privateBoatsSerialNumbers.size()];

        for (int i = 0; i < this.privateBoatsSerialNumbers.size(); i++) {
            ids[i] = privateArr.get(i);
        }

        if (!keepId) {
            return new Rower(this.serialNumber, this.name, this.age, this.rank, this.password, this.isAdmin, this.email, this.phoneNumber, ids);
        } else {
            return new Rower(getId(), this.serialNumber, this.name, this.age, this.rank, this.password, this.isAdmin, this.email, this.phoneNumber, ids);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rower rower = (Rower) o;
        return serialNumber.equals(rower.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    public enum eRowerRank {
        BEGINNER, AVERAGE, PRO;

        public static eRowerRank getFromInt(int number) {
            switch (number) {
                case 0:
                    return BEGINNER;
                case 1:
                    return AVERAGE;
                case 2:
                    return PRO;
            }
            return null;
        }

        public String getRankName() {
            String result = null;

            if (this == BEGINNER) {
                result = "Beginner";
            }

            if (this == AVERAGE) {
                result = "Average";
            }

            if (this == PRO) {
                result = "Professional";
            }

            return result;
        }
    }
}
