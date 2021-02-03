package engine.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Model implements Serializable {

    private String id;

    protected Model() {
        this.id = generateNewId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Model model = (Model) o;
        return id.equals(model.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    private String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public enum eImportExportModelType implements Serializable {
        BOATS, ROWERS, WEEKLY_ACTIVITIES;

        public static Class<?> getClass(eImportExportModelType type) {
            return type.getDeclaringClass();
        }
    }
}
