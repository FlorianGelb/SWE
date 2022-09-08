package control;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.URL;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IGUIEventSender;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CSVReader;
import de.dhbwka.swe.utils.util.CSVWriter;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.GenericEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;
import gui.MainComponent;
import gui.MainComponentMitTabbedPane;
import model.*;
import util.ElementFactory;

import javax.naming.ldap.Control;
import javax.swing.*;

public class CSControllerReinerObserverUndSender implements IGUIEventListener, IUpdateEventSender {
	
	/**
	 * Folgende Commands deklarieren! Es sind die Commands, welche der Controller 
	 * z.B. an die MainGUI als UpdateEvent sendet
	 * Die Commands müssen nur oben deklariert werden, der Rest des enums ist copy/paste (s. SWE-utils-GUIs)
	 */
	public enum Commands implements EventCommand {

		/**
		 * Command:  ID + gelieferter Payload-Typ
		 */
		SET_KUNDEN( "Controller.setKunden", List.class ),
		SET_BUCHUNG("Controller.setBuchung", List.class),
		SET_BUCHUNG_KUNDEN("Controller.setBuchungKunden", List.class),

		SET_FAHRZEUG("Controller.setFahrzeug",List.class),

		SET_STANDORT("Controller.setStandrot", List.class),
		SET_RECHNUNG("Controller.setFahrzeug",List .class);


		public final Class<?> payloadType;
		public final String cmdText;

		private Commands( String cmdText, Class<?> payloadType ) {
			this.cmdText = cmdText;
			this.payloadType = payloadType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getCmdText() {
			return this.cmdText;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getPayloadType() {
			return this.payloadType;
		}
	}

	CSVWriter csvWriter = null;
	
	/**
	 * alle Listener, die auf Updates warten
	 */
	List<EventListener> allListeners = new ArrayList<>();
	
	/**
	 * entityManager für die Elemente (können auch mehrere EntityManager sein ...
	 */
	CommonEntityManager entityManager = new CommonEntityManager();

	/**
	 * entityFactory für die Elemente 
	 */
	ElementFactory elementFactory = new ElementFactory( entityManager );
	
	/**
	 * Lesen und schreiben der Elemente. Attribut kann (muss nicht) 
	 * zum Lesen/Schreiben wiederverwendet werden (csvReader = new CSVReader() ...)
	 */
	CSVReader csvReader = null;

	IAppLogger logger = AppLogger.getInstance();

	public CSControllerReinerObserverUndSender() {
		logger.setSeverity(  IAppLogger.Severity.DEBUG_LOW );
	}
	
	/**
	 * "start" the controller.
	 * Das muss separat gemacht werden, da beim Verknüpfen der Observer (Controller+MainGUI)
	 * das Laden der Daten bereits durchgeführt wurde und somit der UpdateEvent "ins Leere" ging
	 */
	public void init() {
		try {
			loadCSVData();
			fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );
			fireUpdateEvent(new UpdateEvent(this, Commands.SET_BUCHUNG, entityManager.findAll(Buchung.class)));
			fireUpdateEvent(new UpdateEvent(this, Commands.SET_FAHRZEUG, entityManager.findAll(Fahrzeug.class)));
			fireUpdateEvent(new UpdateEvent(this, Commands.SET_RECHNUNG, entityManager.findAll(Rechnung.class)));
			fireUpdateEvent(new UpdateEvent(this, Commands.SET_STANDORT, entityManager.findAll(Standort.class)));
			System.out.println(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeCSVData() throws IOException {
		String header = "ID;BENENNUNG;STARTDATUM;ENDDATUM;BESCHREIBUNG;KUNDENID;KENNZEICHEN;ORGANISATORID;";
		String[] headerArg = header.split(";");
		List<IPersistable> buchungen = entityManager.findAll(Buchung.class);
		List<Object[]> data = new ArrayList<>();
		buchungen.forEach(buchung -> {
			Attribute[] attributeArray = ((Buchung) buchung).getAttributeArray();
			Object[] attributes = new Object[attributeArray.length];
			for (int i = 0; i < attributeArray.length; i++) {
				attributes[i] = attributeArray[i].getValue().toString();
				System.out.println(0);
			}
			data.add((Object[]) attributes);
		});

		csvWriter = new CSVWriter(Objects.requireNonNull(this.getClass().getResource("/CSVFiles/Buchungen.csv")).getPath(), false);
		csvWriter.writeDataToFile(data, headerArg);
	}

	private void loadCSVData() throws IOException {
		/**
		 * Hier sollen alle CSV-Daten gelesen werden 
		 */
		/**
		 * exemplarisch für Kunden: Daten lesen, in den EntityManager speichern
		 * und dann im UpdateEvent an die Main-GUI senden
		 */

		HashMap<Integer, Class> pathKeysClass = new HashMap<>();
		pathKeysClass.put(0, Kunde.class);
		pathKeysClass.put(1, Organisator.class);
		pathKeysClass.put(2, Fahrzeug.class);
		pathKeysClass.put(3, Buchung.class);
		pathKeysClass.put(4, Rechnung.class);
		pathKeysClass.put(5, Standort.class);


		Map<Integer, String> paths = new TreeMap<>();
		paths.put(0, "/CSVFiles/Kunden.csv");
		paths.put(1, "/CSVFiles/Organisator.csv");
		paths.put(2, "/CSVFiles/Fahrzeug.csv");
		paths.put(3, "/CSVFiles/Buchungen.csv");
		paths.put(4, "/CSVFiles/Rechnungen.csv");
		paths.put(5, "/CSVFiles/Standort.csv");


		for(int key: paths.keySet()){
			String path = paths.get(key);
			Class clazz = pathKeysClass.get(key);
			String filePath = this.getClass().getResource(path).getPath();  // ohne "file:" am Anfang
			CSVReader csvReader = new CSVReader( filePath );
			List<String[]> csvData = csvReader.readData();
			csvData.forEach( e -> {
				try {
					if (e.length == 7 && clazz == Kunde.class){
						e[5] =  this.getClass().getResource(e[5]).getPath();
						e[6] =  this.getClass().getResource(e[6]).getPath();
					}

					elementFactory.createElement(clazz, e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});


		}


	}

	// fuer alle GUI-Elemente, die aktualisiert werden sollen:
	@Override
	public boolean addObserver(EventListener eL) {
		return this.allListeners.add(eL);
	}

	@Override
	public boolean removeObserver(EventListener eL) {
		return this.allListeners.remove(eL);
	}

	// die GUI-Events verarbeiten, die von den GUIs kommen:
	@Override
	public void processGUIEvent(GUIEvent ge) {

		logger.debug("Hier ist der Controller!   Event: " + ge);
		
		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_KUNDE ) {
			logger.debug( ge.getData().toString() );
			String[] kundenAtts = (String[])ge.getData(); 
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Kunde.class, kundenAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_FAHRZEUG ) {
			logger.debug( ge.getData().toString() );
			String[] fahrzeugAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Fahrzeug.class, fahrzeugAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_FAHRZEUG, entityManager.findAll( Fahrzeug.class) ) );

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_RECHNUNG ) {
			logger.debug( ge.getData().toString() );
			String[] rechnungAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Rechnung.class, rechnungAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_RECHNUNG, entityManager.findAll( Rechnung.class) ) );

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_BUCHUNG ) {
			logger.debug( ge.getData().toString() );
			String[] buchungAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				Kunde kunde = (Kunde)entityManager.find(Kunde.class, buchungAtts[Buchung.CSVPositions.KUNDENID.ordinal()]);
				Fahrzeug fahrzeug = (Fahrzeug)entityManager.find(Fahrzeug.class, buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()]);
				Organisator organisator = (Organisator)entityManager.find(Organisator.class, buchungAtts[Buchung.CSVPositions.ORGANISATORID.ordinal()]);
				// Rechnung

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String startS = buchungAtts[Buchung.CSVPositions.STARTDATUM.ordinal()].replace("T", " ");
				String endS = buchungAtts[Buchung.CSVPositions.ENDDATUM.ordinal()].replace("T", " ");
				LocalDateTime startNew = LocalDateTime.parse(startS, formatter);
				LocalDateTime endNew = LocalDateTime.parse(endS, formatter);


				List<IPersistable> buchungen = entityManager.findAll(Buchung.class);
				Boolean notCollision = endNew.isAfter(startNew);
				for(IPersistable b : buchungen){
					Attribute[] attrArr = ((Buchung)b).getAttributeArray();
					LocalDateTime start = (LocalDateTime)attrArr[Buchung.CSVPositions.STARTDATUM.ordinal()].getValue();
					LocalDateTime end = (LocalDateTime)attrArr[Buchung.CSVPositions.ENDDATUM.ordinal()].getValue();
					String kennzeichen = (String)((Fahrzeug)attrArr[Buchung.CSVPositions.KENNZEICHEN.ordinal()].getValue()).getAttributeArray()[Fahrzeug.CSVPositions.KENNZEICHEN.ordinal()].getValue();

					notCollision = notCollision && !(((startNew.isAfter(start) && startNew.isBefore(end)) || (endNew.isAfter(start) && endNew.isBefore(end))) && kennzeichen.equals(buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()]));

					System.out.println(0);
				}


				if(kunde != null && fahrzeug != null && organisator != null && notCollision) {
					elementFactory.createElement(Buchung.class, buchungAtts);
					buchungAtts[Buchung.CSVPositions.KUNDENID.ordinal()] = kunde.toString();
					buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()] = fahrzeug.toString();
					buchungAtts[Buchung.CSVPositions.ORGANISATORID.ordinal()] = organisator.toString();

					fireUpdateEvent( new UpdateEvent(this, Commands.SET_BUCHUNG, entityManager.findAll( Buchung.class) ) );
					writeCSVData();
				}
				else{
					if(kunde == null){
						JOptionPane.showMessageDialog(null, "Ungültige KundenID: " +  buchungAtts[Buchung.CSVPositions.KUNDENID.ordinal()], "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					else if(fahrzeug == null){
						JOptionPane.showMessageDialog(null, "Ungültiges Fahrzeug: " +  buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()], "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					else if(organisator == null){
						JOptionPane.showMessageDialog(null, "Ungültige OrganisatorID: " +  buchungAtts[Buchung.CSVPositions.ORGANISATORID.ordinal()], "ERROR", JOptionPane.ERROR_MESSAGE);
					}

					else if(!notCollision){
						JOptionPane.showMessageDialog(null, "Das Fahrzeug ist in diesem Zeitraum schon gebucht " +  buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()], "ERROR", JOptionPane.ERROR_MESSAGE);
					}

				}



			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * zum Senden von UpdateEvents an entsprechende Listener (IUpdateEventListener)
	 * @param ue
	 */
	private void fireUpdateEvent( UpdateEvent ue ) {
		for (EventListener eventListener : allListeners) {
			if( eventListener instanceof IUpdateEventListener ) {
				((IUpdateEventListener)eventListener).processUpdateEvent(ue);
			}
		}
	}
		

}
