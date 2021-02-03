package engine.model.rower;

import engine.api.EngineInterface;
import engine.model.Modifier;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Consumer;

public class RowerModifier extends Modifier<Rower> {
    public RowerModifier(EngineInterface engineContext, Rower objectToEdit, Runnable collectionSaveFunc, Consumer<String> callBack) {
        super(engineContext, objectToEdit, collectionSaveFunc, callBack);
    }

    public void setRowerName(String newName) {
        if (newName != null && !newName.isEmpty()) {
            this.objectToEdit.setName(newName);
            this.markModify();
            invokeCallBack("Rower name successfully changed.");
        } else {
            invokeCallBack("Changing rower name failed.");
        }
    }

    public void setRowerAge(int newAge) {
        if (newAge > 0) {
            this.objectToEdit.setAge(newAge);
            this.markModify();
            invokeCallBack("Rower age successfully changed.");
        } else {
            invokeCallBack("Changing rower age failed. The new age must be greater than 0.");
        }
    }

    public void setRowerPhoneNumber(String newPhone) {
        try {
            this.objectToEdit.setPhoneNumber(newPhone);
            this.markModify();
            invokeCallBack("Rower phone number successfully changed.");
        } catch (InvalidParameterException e) {
            invokeCallBack("Rower phone number change failed.");
        }
    }

    public void setRowerEmail(String newEmail) {
        if (!this.getEngine().getRowersCollectionManager().emailExist(newEmail)) {
            try {
                this.objectToEdit.setMail(newEmail);
                this.markModify();
                invokeCallBack("Rower email successfully changed.");
            } catch (InvalidParameterException e) {
                invokeCallBack("Changing rower email failed.");
            }
        } else {
            invokeCallBack("Rower email change failed. You can't use an email address that's already exist.");
        }
    }

    public void setRowerExpirationDate(LocalDate newExpirationDate) {
        if (newExpirationDate != null && newExpirationDate.isAfter(this.objectToEdit.getJoinDate())) {
            this.objectToEdit.setExpirationDate(newExpirationDate);
            this.markModify();
            invokeCallBack("Rower expiration date successfully changed.");
        } else {
            invokeCallBack("Changing rower expiration date failed.");
        }
    }

    public void setRowerRank(Rower.eRowerRank newRank) {
        this.objectToEdit.setRank(newRank);
        this.markModify();
        invokeCallBack("Rower rank successfully changed.");
    }

    public void setRowerPassword(String password) {
        this.objectToEdit.setPassword(password);
        this.markModify();
        invokeCallBack("Rower password successfully changed.");
    }

    public void setIsAdminStatus(boolean isAdmin) {
        this.objectToEdit.changeIsAdminStatus(isAdmin);
        this.markModify();
        invokeCallBack("Rower status successfully changed.");
    }

    public void addNewNote(String note) {
        this.objectToEdit.getModifiableNotes().add(note);
        this.markModify();
        invokeCallBack("Note successfully added.");
    }

    public void removeNote(String note) {
        if (this.objectToEdit.getModifiableNotes().remove(note)) {
            this.markModify();
            invokeCallBack("Note successfully deleted.");
        } else {
            invokeCallBack("Deleting note failed.");
        }
    }

    public void cleanNotes() {
        this.objectToEdit.getModifiableNotes().clear();
        this.markModify();
        invokeCallBack("Notes list is now empty.");
    }

    public Set<String> getModifiablePrivateBoats(){
        return objectToEdit.getModifiablePrivateBoatsSerials();
    }
}
