package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.dhbwka.swe.utils.model.Gruppe;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.model.Person;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import model.*;


public class ElementFactory {

//  now in Class Person:
//	public enum CSVPosPerson{
//		ID,
//		NACHNAME,
//		VORNAME,
//		GEBURTSJAHR,
//		GRUPPEN,
//		BESCHREIBUNG
//	}
	
//  now in Class Gruppe:
//	public enum CSVPosGruppe{
//		ID,
//		NAME,
//		MITGLIEDER,
//		BESCHREIBUNG
//	}
	
	/**
	 * Map mit allen Referenzen (als String == CSV-Eintrag), die nicht zueordnet werden konnten
	 */
	private HashMap<String, String> mapOfUnreferencedElements = new HashMap<>(); 

	private CommonEntityManager entityManager = null;
	
	private IPersistable persistableElement = null;

	public ElementFactory( CommonEntityManager em) {
		this.entityManager = em;
	};
	
	/**
	 * Die createElement-Methode anpassen, Kunde ist als Beispiel realisiert, nun die Factory für die
	 * anderen Klassen genau wie Person (s.u.) anpassen
	 */
	/**
	 * 
	 * @param c the type of the class to be created
	 * @param csvData the data read from a CSV file
	 * @return the instance of the element just created
	 * @throws Exception
	 */
	public IPersistable createElement(Class<?> c, String[] csvData) throws Exception {

		if (c == null) {
			throw new IllegalArgumentException("Klasse muss angegeben werden ( Klasse ist null )!");
		}


		else if (c == Organisator.class){
			String ID = csvData[Organisator.CSVPositions.ID.ordinal()];
			String Vorname = csvData[Organisator.CSVPositions.VORNAME.ordinal()];
			String Nachname = csvData[Organisator.CSVPositions.NACHNAME.ordinal()];

			persistableElement = new Organisator(ID, Vorname, Nachname);
		}

		else if(c == Fahrzeug.class){
			String kennzeichen = csvData[Fahrzeug.CSVPositions.KENNZEICHEN.ordinal()];
			String marke = csvData[Fahrzeug.CSVPositions.MARKE.ordinal()];
			String farbe = csvData[Fahrzeug.CSVPositions.FARBE.ordinal()];
			String kraftstoff = csvData[Fahrzeug.CSVPositions.KRAFTSTOFF.ordinal()];
			String getriebe =  csvData[Fahrzeug.CSVPositions.GETRIEBE.ordinal()];

			persistableElement = new Fahrzeug(kennzeichen, marke, farbe, kraftstoff, getriebe);
		}

		else if(c == Rechnung.class){
			String rechnungsnummer = csvData[Rechnung.CSVPositions.RECHNUNGSNUMMER.ordinal()];
			float rechnungsbetrag = Float.parseFloat(csvData[Rechnung.CSVPositions.RECHNUNGSBETRAG.ordinal()]);
			String frist = csvData[Rechnung.CSVPositions.FRIST.ordinal()];
			String anmerkung = csvData[Rechnung.CSVPositions.ANMERKUNG.ordinal()];

			persistableElement = new Rechnung(rechnungsnummer, rechnungsbetrag, frist, anmerkung);
		}

		else if(c == Buchung.class){
			String id = csvData[Buchung.CSVPositions.ID.ordinal()];
			String benennung = csvData[Buchung.CSVPositions.BENENNUNG.ordinal()];
			String start = csvData[Buchung.CSVPositions.STARTDATUM.ordinal()];
			String ende = csvData[Buchung.CSVPositions.ENDDATUM.ordinal()];
			String beschreibung = csvData[Buchung.CSVPositions.BESCHREIBUNG.ordinal()];
			String kundenid = csvData[Buchung.CSVPositions.KUNDENID.ordinal()];
			String kennzeichen = csvData[Buchung.CSVPositions.KENNZEICHEN.ordinal()];
			String organisatorid = csvData[Buchung.CSVPositions.ORGANISATORID.ordinal()];
			Kunde kunde = (Kunde)entityManager.find(Kunde.class, kundenid);
			Fahrzeug fahrzeug = (Fahrzeug)entityManager.find(Fahrzeug.class, kennzeichen);
			Organisator organisator = (Organisator)entityManager.find(Organisator.class, organisatorid);
			System.out.println(organisatorid);
			System.out.println(organisator);

			persistableElement = new Buchung(id, benennung, start, ende, beschreibung, kunde, fahrzeug, organisator);
		}

		else if( c == Kunde.class ) {
			String id = csvData[ Kunde.CSVPositions.ID.ordinal() ];
			String nachName = csvData[ Kunde.CSVPositions.NACHNAME.ordinal() ];
			String vorName = csvData[ Kunde.CSVPositions.VORNAME.ordinal() ];
			String beschreibung = csvData[ Kunde.CSVPositions.BESCHREIBUNG.ordinal() ];
			String engagiert = csvData[ Kunde.CSVPositions.ENGAGIERT.ordinal() ];
			String perso = csvData[Kunde.CSVPositions.PERSO.ordinal()];
			String fuehrerschein = csvData[Kunde.CSVPositions.FUERERSCHEIN.ordinal()];
			
			persistableElement = new Kunde( id, vorName, nachName, beschreibung, Boolean.getBoolean(engagiert),
					perso, fuehrerschein);
			/**
			 * hier kämen dann die N:M-Beziehungen hin, s.u. bei Person und Gruppe
			 */
		}
		else if( c == Person.class ) {
			String id = csvData[ Person.CSVPositions.ID.ordinal() ];
			String nachName = csvData[ Person.CSVPositions.NACHNAME.ordinal() ];
			String vorName = csvData[ Person.CSVPositions.VORNAME.ordinal() ];
			String beschreibung = csvData[ Person.CSVPositions.BESCHREIBUNG.ordinal() ];
			
			persistableElement = new Person( id, vorName, nachName, beschreibung );
			String gJahr = csvData[ Person.CSVPositions.GEBURTSJAHR.ordinal() ];
			if (gJahr != null  &&  ! gJahr.isEmpty() ) {
				int gebJahr  = Integer.parseInt( gJahr );
				((Person)persistableElement).setGeburtsjahr( gebJahr );
			}

			String listOfGruppen = csvData[ Person.CSVPositions.GRUPPEN.ordinal() ];
			
			try{
				List<IPersistable> realRefs = getReferences(Gruppe.class, listOfGruppen);
				for( IPersistable iP: realRefs ){
					((Person)persistableElement).addGroup( (Gruppe)iP );
				}
			}
			catch( Exception e ){
				mapOfUnreferencedElements.put( id, listOfGruppen );
			}
		}
		else if (c == Gruppe.class) {
			String id = csvData[ Gruppe.CSVPositions.ID.ordinal() ];
			String name = csvData[ Gruppe.CSVPositions.NAME.ordinal() ];
			String beschreibung = csvData[ Gruppe.CSVPositions.BESCHREIBUNG.ordinal() ];
			
			persistableElement = new Gruppe( id, name, beschreibung );

			String listOfMitglieder = csvData[ Gruppe.CSVPositions.MITGLIEDER.ordinal() ];
			
			try{
				List<IPersistable> realRefs = getReferences(Person.class, listOfMitglieder );
				for( IPersistable iP: realRefs ){
					((Gruppe)persistableElement).addMitglied( (Person)iP );
				}
			}
			catch( Exception e ){
				mapOfUnreferencedElements.put( id, listOfMitglieder );
			}
		}
		entityManager.persist( persistableElement );

		return persistableElement;
	}

	/**
	 * Ab hier alles so lassen ***********************************************************************
	 */
	
	/**
	 * 
	 * @param c
	 * @param stringIDs
	 * @return
	 * @throws Exception
	 */
	private List<IPersistable> getReferences(Class<?> c, String stringIDs) throws Exception {
		List<IPersistable> refs = new ArrayList<>();
		if( stringIDs == null  ||  stringIDs.isEmpty() ) throw new RuntimeException( "List of refs is empty or null" );
		
		String[] arrIDs = stringIDs.split(",");
		
		for( String sId: arrIDs ){
			if( sId.isEmpty() || sId.indexOf( ' ' ) >= 0 ) throw new RuntimeException( "a reference is empty" );
//			List<IPersistable> ae = entityManager.find(c, "getPrimaryKey", sId );
			IPersistable ae = entityManager.find(c, sId );
			if( ae == null )
				throw new RuntimeException( "Reference '" + sId + "' not found!" );
			refs.add( ae );
		}
		
		return refs;
	}
	
	/**
	 * for compatibility reasons, this method simply delegates to {@link #resolveUnreferencedReferences()}
	 * @throws Exception
	 */
	public void resolveUnresolvedReferences() throws Exception {
		this.resolveUnreferencedReferences();
	}
	
	/**
	 * resolve the unresolved references stored in the local HashMap
	 * @throws Exception
	 */
	public void resolveUnreferencedReferences() throws Exception {
		for( String key : this.mapOfUnreferencedElements.keySet() ){
			IPersistable ip = this.entityManager.find( key );
			String refs = this.mapOfUnreferencedElements.get( key );
			
			if( ip instanceof Person ) {
				List<IPersistable> refList = getReferences(Gruppe.class, refs);
				refList.forEach( e -> ((Person)ip).addGroup( (Gruppe)e ) );
			}
			else if( ip instanceof Gruppe ) {
				List<IPersistable> refList = getReferences(Person.class, refs);
				refList.forEach( e -> ((Gruppe)ip).addMitglied( (Person)e ) );
			}
		}
	}

}
