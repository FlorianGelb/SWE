package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Fahrzeug implements IDepictable, IPersistable {

	@Override
	public String getElementID() {
		return this.attArr[ Attributes.KENNZEICHEN.ordinal() ].getValue().toString();
	}

	@Override
	public Attribute[] getAttributeArray() {
		return this.attArr;
	}

	@Override
	public Object getPrimaryKey() {
		return this.attArr[ Attributes.KENNZEICHEN.ordinal() ].getValue();
	}

	public enum CSVPositions{
		KENNZEICHEN,
		MARKE,
		FARBE,
		KRAFTSTOFF,
		GETRIEBE;
	}


	public enum Farbe{
		ROT,
		GRÜN,
		WEIS,
		SCHWARZ;
	}

	public enum Kraftstoff{
		E10,
		BENZIN,
		DIESEL;
	}

	public enum Getriebe{
		MANUELL,
		AUTOMATIK;
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
		KENNZEICHEN( "Kennzeichen", String.class, true, true, true ),
		MARKE( "Marke", String.class, true, true, true ),
		FARBE( "FARBE", Farbe.class, true, true, true ),
		KRAFTSTOFF( "Kraftstoff", Kraftstoff.class, true, true, true ),
		GETRIEBE( "Getriebe", Getriebe.class, true, true, true );

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

	public Fahrzeug() {
		this( "", "", "", "", "");
	}
	public Fahrzeug(String kennzeichen, String marke, String farbe, String kraftstoff, String getriebe){
		boolean modifiable = true;
		LocalDateTime ldtStart = LocalDateTime.now();
		LocalDateTime ldtEnd = LocalDateTime.now();
		String randID = UUID.randomUUID().toString();
		this.attArr[ Attributes.KENNZEICHEN.ordinal() ] = Attributes.KENNZEICHEN.createAttribute( this, ( kennzeichen == null || kennzeichen.isEmpty() ? randID : kennzeichen ), randID );
		this.attArr[ Attributes.MARKE.ordinal() ] = Attributes.MARKE.createAttribute( this, marke, "Skoda");
		this.attArr[ Attributes.FARBE.ordinal() ] = Attributes.FARBE.createAttribute( this, farbe, Farbe.WEIS );
		this.attArr[ Attributes.KRAFTSTOFF.ordinal() ] = Attributes.KRAFTSTOFF.createAttribute(this, kraftstoff, Kraftstoff.E10);
		this.attArr[ Attributes.GETRIEBE.ordinal() ] = Attributes.GETRIEBE.createAttribute( this, getriebe, Getriebe.AUTOMATIK);
	}

	public String toString() {
		return this.attArr[Attributes.KENNZEICHEN.ordinal()].getValue() + ", "
				+ this.attArr[Attributes.MARKE.ordinal()].getValue() + ", "
				+ this.attArr[Attributes.FARBE.ordinal()].getValue() +","
				+ this.attArr[Attributes.GETRIEBE.ordinal()].getValue();
	}

}
