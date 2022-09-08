package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Rechnung implements IDepictable, IPersistable {
    @Override
    public String getElementID() {
        return this.attArr[ Attributes.RECHNUNGSNUMMER.ordinal() ].getValue().toString();
    }

    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Attributes.RECHNUNGSNUMMER.ordinal() ].getValue();
    }

    public enum CSVPositions{
        RECHNUNGSNUMMER,
        RECHNUNGSBETRAG,
        FRIST,
        ANMERKUNG;
    }

    /**
     * Enum zur Definition der Attribut-Instanzen
     *
     * Hier müssen lediglich die Attribute und deren Eigenschaften deklariert werden
     *
     */
    public enum Attributes {

        /**
         * Attribute erzeugen, die folgende Einstellungen haben:
         * Name, Klasse (Typ), sichtbar, aenderbar, editierbar
         */
        RECHNUNGSNUMMER( "Rechnungsnummer", String.class, true, true, true ),
        RECHNUNGSBETRAG( "Rechnungsbetrag", Float.class, true, true, true ),
        FRIST( "Frist", Date.class, true, true, true ),
        ANMERKUNG( "Anmerkung", String.class, true, true, true );

        private String name;
        private boolean visible;
        private boolean modifiable, editable;
        private Class<?> clazz;

        private Attributes( String name, Class<?> clazz, boolean visible, boolean modifyable, boolean editable ) {
            this.name = name;
            this.visible = visible;
            this.clazz = clazz;
            this.modifiable = modifyable;
            this.editable = editable;
        }

        private Attribute createAttribute(Object dedicatedInstance, Object value, Object defaultValue ) {
            return new Attribute( this.name, dedicatedInstance, this.clazz, value,
                    defaultValue, this.visible, this.modifiable, this.editable, false );  // mandatory
        }
    }

    /**
     * get all names of the attributes for the Person
     *
     * @return the complete list of attribute names
     */
    public final static String[] getAllAttributeNames() {
        return getAttributeNames( false );
    }

    /**
     * get the names of all attributes for the Person
     *
     * @param  filterVisibleAttributes if true, only names of visible attributes are
     *                                     listed, if false, all attribute names are
     *                                     listed.
     * @return                         the list of attribute names
     */
    public final static String[] getAttributeNames( boolean filterVisibleAttributes ) {
        List<String> names = new ArrayList<>();
        Rechnung.Attributes[] pA = Rechnung.Attributes.values();

        for( int i = 0 ; i < pA.length ; i++ ){

            if( !filterVisibleAttributes ){
                names.add( pA[ i ].name );
            }
            else if( pA[ i ].visible ){
                names.add( pA[ i ].name );
            }
        }
        return names.toArray( new String[ names.size() ] );
    }

    private Attribute[] attArr = new Attribute[ Rechnung.Attributes.values().length ];

    public Rechnung() {
        this("", 0.0F, null, "");
    }
    public Rechnung(String rechnungsnummer, float rechnungsbetrag, String frist, String anmerkung){
        boolean modifiable = true;
        LocalDateTime ldtStart = LocalDateTime.now();
        LocalDateTime ldtEnd = LocalDateTime.now();
        String randID = UUID.randomUUID().toString();
        this.attArr[ Rechnung.Attributes.RECHNUNGSNUMMER.ordinal() ] = Rechnung.Attributes.RECHNUNGSNUMMER.createAttribute( this, ( rechnungsnummer == null || rechnungsnummer.isEmpty() ? randID : rechnungsnummer ), randID );
        this.attArr[ Rechnung.Attributes.RECHNUNGSBETRAG.ordinal() ] = Rechnung.Attributes.RECHNUNGSBETRAG.createAttribute( this, rechnungsbetrag, 0.0F);
        this.attArr[ Rechnung.Attributes.FRIST.ordinal() ] = Rechnung.Attributes.FRIST.createAttribute( this, frist, LocalDateTime.now() );
        this.attArr[ Rechnung.Attributes.ANMERKUNG.ordinal() ] = Rechnung.Attributes.ANMERKUNG.createAttribute(this, anmerkung, "--");
    }

    public String toString() {
        return this.attArr[Attributes.RECHNUNGSNUMMER.ordinal()].getValue() + ", "
                + this.attArr[Attributes.RECHNUNGSBETRAG.ordinal()].getValue() + ", "
                + this.attArr[Attributes.FRIST.ordinal()].getValue() +","
                + this.attArr[Attributes.ANMERKUNG.ordinal()].getValue();
    }

}

