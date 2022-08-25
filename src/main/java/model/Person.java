package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

public  class Person implements IDepictable, IPersistable {


    @Override
    public Class<?> getElementClass() {
        return IDepictable.super.getElementClass();
    }

    @Override
    public String getElementID() {
        return null;
    }

    @Override
    public String getVisibleText() {
        return IDepictable.super.getVisibleText();
    }

    @Override
    public Attribute[] getAttributeArray() {
        return new Attribute[0];
    }

    @Override
    public Attribute[] setAttributeValues(Attribute[] attributeArray) {
        return IDepictable.super.setAttributeValues(attributeArray);
    }

    @Override
    public List<Attribute> getAttributes() {
        return IDepictable.super.getAttributes();
    }

    @Override
    public List<Attribute> getCopiedAttributes() {
        return IDepictable.super.getCopiedAttributes();
    }

    @Override
    public Vector<Attribute> getAttributesAsVector() {
        return IDepictable.super.getAttributesAsVector();
    }

    @Override
    public Vector<Attribute> getCopiedAttributesAsVector() {
        return IDepictable.super.getCopiedAttributesAsVector();
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }
}
