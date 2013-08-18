package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.google.common.base.Objects;

import pl.edu.agh.ki.grieg.processing.core.config.PropertyDefinition;

@XmlType
public abstract class XmlProperty<T> implements PropertyDefinition<T> {

    @XmlAttribute
    private String name;

    @XmlValue
    private String contentValue;

    @XmlAttribute(name = "value")
    private String attributeValue;

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return {@code true} if the value specification is correct, i.e. value is
     *         defined precisely in one place
     */
    protected boolean isValid() {
        return contentValue != null ^ attributeValue != null;
    }

    /**
     * Checks if the value specification is correct, throws an exception if it
     * is not.
     */
    protected void checkValidity() throws DuplicateValueException {
        if (!isValid()) {
            throw new DuplicateValueException(name);
        }
    }

    /**
     * Returns string representing the property contentValue. If the value is
     * specified both as the attribute {@code value} and as a content of the
     * property tag, an exception is thrown.
     * 
     * @return String defining property contentValue
     * @throws DuplicateValueException
     *             If both attributeValue {@code value} and content of the
     *             {@code <property>} tag are present.
     */
    protected String getString() throws DuplicateValueException {
        checkValidity();
        return uncheckedGetString();
    }

    /**
     * Returns string value without checking validity
     */
    private String uncheckedGetString() {
        return Objects.firstNonNull(contentValue, attributeValue);
    }

    /**
     * Returns string representing the value (or both values in case of such
     * error) regardless of property correctness. Can be used e.g. for
     * implementing {@link #toString()}.
     */
    protected String safeGetString() {
        return isValid() ? uncheckedGetString() 
                : String.format("{'%s'|'%s'}", contentValue, attributeValue);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", name, safeGetString());
    }

}
