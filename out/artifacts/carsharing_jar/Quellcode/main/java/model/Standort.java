package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Standort implements IDepictable, IPersistable {


	@Override
	public Object getPrimaryKey() {
		return this.attArr[ Standort.Attributes.ID.ordinal() ].getValue();
	}

	public enum CSVPositions{
		ID,
		NAME,
		ANZAHL_PLAETZE
	}

	public enum Attributes {

		/**
		 * Attribute erzeugen, die folgende Einstellungen haben:
		 * Name, Klasse (Typ), sichtbar, aenderbar, editierbar
		 */
		ID( "ID", String.class, true, true, true ),
		NAME( "Name", String.class, true, true, true ),
		ANZAHL_PLAETZE( "Anzahl Plätze", Integer.class, true, true, true );

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

	private Attribute[] attArr = new Attribute[ Standort.Attributes.values().length ];


	/*public Standort.csv(String id, String name, int anzahlPlaetze) {
		super();
		this.id = id;
		this.name = name;
		this.anzahlPlaetze = anzahlPlaetze;
	}*/

	public Standort() {
		this( "", "", 0);
	}

	public Standort(String iD, String name, int anzahlPlaetze) {
		boolean modifiable = true;
		String randID = UUID.randomUUID().toString();
		this.attArr[ Standort.Attributes.ID.ordinal() ] = Standort.Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
		this.attArr[ Standort.Attributes.NAME.ordinal() ] = Standort.Attributes.NAME.createAttribute( this, name, "--" );
		this.attArr[ Standort.Attributes.ANZAHL_PLAETZE.ordinal() ] = Standort.Attributes.ANZAHL_PLAETZE.createAttribute( this, anzahlPlaetze, "--" );
	}

	@Override
	public Attribute[] getAttributeArray() {
		// TODO Auto-generated method stub
		return this.attArr;
	}

	@Override
	public String getElementID() {
		return this.attArr[ Standort.Attributes.ID.ordinal() ].getValue().toString();
	}


	@Override
	public String toString() {
		return this.attArr[Attributes.ID.ordinal()].getValue() + ", "
				+ this.attArr[Attributes.NAME.ordinal()].getValue() + ", "
				+ this.attArr[Attributes.ANZAHL_PLAETZE.ordinal()].getValue();
	}

}
