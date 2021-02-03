package engine.xml.model.util.member;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import engine.api.EngineContext;
import engine.api.EngineInterface;
import engine.database.collection.RowersCollectionManager;
import engine.model.boat.Boat;
import engine.model.boat.BoatModifier;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import engine.utils.RegexHandler;
import jaxb.schema.engine.generated.member.Member;
import jaxb.schema.engine.generated.member.Members;
import jaxb.schema.engine.generated.member.RowingLevel;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberUtils {
    private static EngineInterface engine;
    private static RowersCollectionManager rowers;

    static {
        try {
            engine = EngineContext.getInstance();
            rowers = engine.getRowersCollectionManager();
        } catch (Exception ignored) {

        }
    }

    public static boolean verifyAdmin(Members members) {
        List<Member> membersList = members.getMember();

        for (Member member : membersList) {
            if (memberIsValid(member, null, false) && member.isManager() != null && member.isManager()) {
                return true;
            }
        }

        return false;
    }

    public static int addValidMembers(Members members, StringBuilder stringBuilder) {
        List<Member> membersList = members.getMember();
        int res = 0;

        for (Member member : membersList) {
            if (memberIsValid(member, stringBuilder)) {
                if (rowers.isSerialNumberAvailable(member.getId()) && !rowers.emailExist(member.getEmail())) {
                    Rower rowerToAdd = convertMemberToRower(member);
                    rowers.add(rowerToAdd);
                    res++;
                }
            }
        }

        return res;
    }

    private static boolean memberIsValid(Member member, StringBuilder stringBuilder) {
        return memberIsValid(member, stringBuilder, true);
    }

    private static boolean memberIsValid(Member member, StringBuilder stringBuilder, boolean checkEmailExist) {
        boolean result = true;
        StringBuilder errors = new StringBuilder();

        if (member.getName().isEmpty()) {
            result = false;
            errors.append("'Name' can't be empty\n");
        }

        List<String> privateBoatsIds = new ArrayList<>();
        if (member.getPrivateBoatId() != null) {
            String[] importedIds = member.getPrivateBoatId().split(",").clone();
            for (String id : importedIds) {
                if (!id.trim().isEmpty()) {
                    privateBoatsIds.add(id);
                }
            }
        }
        if (privateBoatsIds.isEmpty()) {
            privateBoatsIds = null;
        }

        if (!checkIfHasPrivateBoatAndPrivateBoatIdMatch(member.isHasPrivateBoat(), privateBoatsIds)) {
            result = false;
            errors.append("'Has private boat' and 'boat id' are not match\n");
        }
        if (!datesAreMatch(member.getJoined(), member.getMembershipExpiration())) {
            result = false;
            errors.append("'join date' and 'expiration date' are not match\n");
        }
        boolean isEmailExist = checkEmailExist && rowers.emailExist(member.getEmail());
        if (member.getEmail().isEmpty()
                || !RegexHandler.isEmailAddressValid(member.getEmail())
                || isEmailExist) {
            errors.append("'Email' is not valid or already exists\n");
        }
        if (member.getPhone() != null) {
            if (!RegexHandler.isPhoneNumberValid(member.getPhone())) {
                errors.append("'Phone number' is not valid\n");
                result = false;
            }
        }
        if (member.getPassword().length() < 4) {
            errors.append("'Password' is not valid\n");
            result = false;
        }

        if (errors.length() != 0 && stringBuilder != null) {
            stringBuilder.append(String.format("Rower: %s\nID: %s\nerrors:\n%s\n",
                    member.getName(), member.getId(), errors.toString()));
        }
        return result;
    }

    private static boolean datesAreMatch(XMLGregorianCalendar joined, XMLGregorianCalendar membershipExpiration) {
        boolean res = true;

        if (joined != null && membershipExpiration != null) {
            LocalDate join = joined.toGregorianCalendar().toZonedDateTime().toLocalDate();
            LocalDate expired = membershipExpiration.toGregorianCalendar().toZonedDateTime().toLocalDate();
            if (join.isAfter(expired)) {
                res = false;
            }
        }

        return res;
    }

    private static boolean checkIfHasPrivateBoatAndPrivateBoatIdMatch(Boolean hasPrivateBoat, List<String> privateBoatIds) {

        if (hasPrivateBoat == null && privateBoatIds == null) {
            return true;
        }
        if (hasPrivateBoat == null) {
            return privateBoatIds.isEmpty();
        }
        if (privateBoatIds == null) {
            return !hasPrivateBoat;
        }

        if (!hasPrivateBoat) {
            return privateBoatIds.isEmpty();
        } else {
            for (String id : privateBoatIds) {
                if (engine.getBoatsCollectionManager().findBySerialNumber(id) == null) {
                    return false;
                }
            }
            return true;
        }
    }


    public static Member convertRowerToMember(Rower rower) {
        Member member = new Member();
        member.setAge(rower.getAge());
        member.setEmail(rower.getEmail());
        member.setHasPrivateBoat(rower.hasPrivateBoat());
        member.setId(rower.getSerialNumber());
        member.setJoined(localDateToXmlDate(rower.getJoinDate()));
        member.setMembershipExpiration(localDateToXmlDate(rower.getExpirationDate()));
        member.setLevel(getMemberRank(rower.getRank()));
        member.setManager(rower.isAdmin());
        member.setName(rower.getName());
        member.setPassword(rower.getPassword());
        member.setPhone(rower.getPhoneNumber());
        StringBuilder ids = new StringBuilder();
        rower.getPrivateBoatsSerialNumbers().forEach(privateBoat -> ids.append(privateBoat).append(','));
        member.setPrivateBoatId(ids.toString());
        member.setComments(notesToString(rower.getNotes()));

        return member;
    }

    private static XMLGregorianCalendar localDateToXmlDate(LocalDate date) {
        XMLGregorianCalendar result = new XMLGregorianCalendarImpl();
        result.setDay(date.getDayOfMonth());
        result.setMonth(date.getMonthValue());
        result.setYear(date.getYear());
        result.setHour(0);
        result.setMinute(0);
        result.setSecond(0);
        result.setMillisecond(0);
        return result;
    }

    private static String notesToString(List<String> notes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String note : notes) {
            stringBuilder.append(note).append("\n");
        }

        return stringBuilder.toString();
    }

    private static Rower convertMemberToRower(Member member) {
        String serialNumber = member.getId();//required
        String name = member.getName();//required
        String email = member.getEmail();//required
        String password = member.getPassword();//required
        int age = member.getAge() == null ? member.getAge() : 20;

        String phone = member.getPhone() == null ? "050-0000000" : member.getPhone();
        boolean isAdmin = member.isManager() != null && member.isManager();
        boolean hasPrivateBoat = member.isHasPrivateBoat() != null && member.isHasPrivateBoat();
        List<String> privateBoatIds = null;

        if (hasPrivateBoat) {
            privateBoatIds = new ArrayList<>(Arrays.asList(member.getPrivateBoatId().split(",")));
        }

        Rower.eRowerRank rank = member.getLevel() == null ? Rower.eRowerRank.BEGINNER : getRowerRank(member.getLevel());

        LocalDate joinDate = member.getJoined() == null ?
                LocalDate.now() : member.getJoined().toGregorianCalendar().toZonedDateTime().toLocalDate();

        LocalDate expirationDate = member.getJoined() == null ?
                LocalDate.now().plusYears(1) : member.getMembershipExpiration().toGregorianCalendar().toZonedDateTime().toLocalDate();

        Rower result = new Rower(serialNumber, name, age, rank, password, isAdmin, email, phone, joinDate, expirationDate, privateBoatIds);

        try {
            updateBoatsOwner(result);
        } catch (Exception ignored) {
        }

        if (member.getComments() != null) {
            try {
                String[] notes = member.getComments().split("\n");
                RowerModifier modifier = EngineContext.getInstance().getRowerModifier(result, null);
                for (String note : notes) {
                    if (!note.isEmpty()) {
                        modifier.addNewNote(note);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static void updateBoatsOwner(Rower rower) throws Exception {
        for (String id : rower.getPrivateBoatsSerialNumbers()) {
            Boat boat = EngineContext.getInstance().getBoatsCollectionManager().findBySerialNumber(id);
            BoatModifier modifier = EngineContext.getInstance().getBoatModifier(boat, null);
            modifier.setOwner(rower);
        }
    }


    private static Rower.eRowerRank getRowerRank(RowingLevel level) {
        Rower.eRowerRank res = null;

        switch (level) {
            case ADVANCED:
                res = Rower.eRowerRank.PRO;
                break;

            case INTERMEDIATE:
                res = Rower.eRowerRank.AVERAGE;
                break;

            case BEGINNER:
                res = Rower.eRowerRank.BEGINNER;
                break;
        }

        return res;
    }

    private static RowingLevel getMemberRank(Rower.eRowerRank level) {
        RowingLevel result = null;

        switch (level) {
            case PRO:
                result = RowingLevel.ADVANCED;
                break;

            case AVERAGE:
                result = RowingLevel.INTERMEDIATE;
                break;

            case BEGINNER:
                result = RowingLevel.BEGINNER;
                break;
        }

        return result;
    }
}
