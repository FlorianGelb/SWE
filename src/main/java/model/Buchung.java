package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Buchung implements IDepictable, IPersistable {

    @Override
    public String getElementID() {
        return this.attArr[ Buchung.Attributes.ID.ordinal() ].getValue().toString();
    }

    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Buchung.Attributes.ID.ordinal() ].getValue();
    }

    public enum CSVPositions{
        ID,
        BENENNUNG,
        STARTDATUM,
        ENDDATUM,
        BESCHREIBUNG,
        KUNDENID,
        KENNZEICHEN,

        ORGANISATORID;
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
        ID( "ID", String.class, true, true, true ),
        BENENNUNG( "Benennung", String.class, true, true, true ),
        STARTDATUM( "Start", LocalDate.class, true, true, true ),
        ENDDATUM( "Ende", LocalDate.class, true, true, true ),
        BESCHREIBUNG( "Beschreibung", String.class, true, true, true ),
        KUNDE("Kunde", Kunde.class, true, true, true),

        FAHRZEUG("Auto", Fahrzeug.class, true, true, true),

        ORGANISATOR("Organisator", Organisator.class, true, true, true);

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
        Attributes[] pA = Attributes.values();

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

    private Attribute[] attArr = new Attribute[ Buchung.Attributes.values().length ];
    public Buchung(String iD, String benennung,  String description, Kunde kunde, Fahrzeug fahrzeug, Organisator organisator){
        this(iD, benennung, LocalDate.now().toString(), LocalDate.now().toString(), description, kunde, fahrzeug, organisator);
    }

    public Buchung() {
        this( "", "", "", "", "", null, null, null);
    }
    public Buchung(String iD, String benennung, String start, String end, String description, Kunde kunde, Fahrzeug fahrzeug, Organisator organisator){
        boolean modifiable = true;
        LocalDateTime ldtStart = LocalDateTime.now();
        LocalDateTime ldtEnd = LocalDateTime.now();
        String randID = UUID.randomUUID().toString();
        this.attArr[ Attributes.ID.ordinal() ] = Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.BENENNUNG.ordinal() ] = Attributes.BENENNUNG.createAttribute( this, benennung, "--" );
        this.attArr[ Attributes.STARTDATUM.ordinal() ] = Attributes.STARTDATUM.createAttribute( this, ldtStart, "--" );
        this.attArr[ Attributes.ENDDATUM.ordinal() ] = Attributes.ENDDATUM.createAttribute(this, ldtEnd, Boolean.TRUE );
        this.attArr[ Attributes.BESCHREIBUNG.ordinal() ] = Attributes.BESCHREIBUNG.createAttribute( this, description, "--" );
        this.attArr[Attributes.KUNDE.ordinal()] = Attributes.KUNDE.createAttribute(this, kunde, null);
        this.attArr[Attributes.FAHRZEUG.ordinal()] = Attributes.FAHRZEUG.createAttribute(this, fahrzeug, null);
        this.attArr[Attributes.ORGANISATOR.ordinal()] = Attributes.ORGANISATOR.createAttribute(this, organisator, null);
    }

    public String toString() {
        return this.attArr[Attributes.BENENNUNG.ordinal()].getValue() + ", "
                + this.attArr[Attributes.STARTDATUM.ordinal()].getValue() + ", "
                + this.attArr[Attributes.ENDDATUM.ordinal()].getValue() +","
                + this.attArr[Attributes.BESCHREIBUNG.ordinal()].getValue();
    }

}
