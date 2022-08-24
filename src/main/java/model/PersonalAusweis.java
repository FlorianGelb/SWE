package model;

import de.dhbwka.swe.utils.model.Attribute;

import java.util.List;
import java.util.Vector;

public class PersonalAusweis extends AusweisDokument{

    public enum CSVPositions{

    }


    @Override
    public Class<?> getElementClass() {
        return super.getElementClass();
    }

    @Override
    public String getElementID() {

    }

    @Override
    public String getVisibleText() {
        return super.getVisibleText();
    }

    @Override
    public Attribute[] getAttributeArray() {
        return new Attribute[0];
    }

    @Override
    public Attribute[] setAttributeValues(Attribute[] attributeArray) {
        return super.setAttributeValues(attributeArray);
    }

    @Override
    public List<Attribute> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public List<Attribute> getCopiedAttributes() {
        return super.getCopiedAttributes();
    }

    @Override
    public Vector<Attribute> getAttributesAsVector() {
        return super.getAttributesAsVector();
    }

    @Override
    public Vector<Attribute> getCopiedAttributesAsVector() {
        return super.getCopiedAttributesAsVector();
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }
}
