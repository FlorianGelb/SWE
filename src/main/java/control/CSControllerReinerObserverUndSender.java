package control;

import java.io.IOException;
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
import de.dhbwka.swe.utils.model.IDepictable;
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
		SET_BUCHUNG_KUNDEN("Controller.setBuchungKunden", List.class);

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
	CSVWriter csvWriter = null;
	
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
			System.out.println(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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


		Map<Integer, String> paths = new TreeMap<>();
		paths.put(0, "/CSVFiles/Kunden.csv");
		paths.put(1, "/CSVFiles/Organisator.csv");
		paths.put(2, "/CSVFiles/Fahrzeug.csv");
		paths.put(3, "/CSVFiles/Buchungen.csv");


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

		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_BUCHUNG ) {
			logger.debug( ge.getData().toString() );
			String[] buchungAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				Kunde kunde = (Kunde)entityManager.find(Kunde.class, buchungAtts[Buchung.CSVPositions.KUNDENID.ordinal()]);
				Fahrzeug fahrzeug = (Fahrzeug)entityManager.find(Fahrzeug.class, buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()]);
				Organisator organisator = (Organisator)entityManager.find(Organisator.class, buchungAtts[Buchung.CSVPositions.ORGANISATORID.ordinal()]);

				if(kunde != null && fahrzeug != null && organisator != null) {
					elementFactory.createElement(Buchung.class, buchungAtts);
					buchungAtts[Buchung.CSVPositions.KUNDENID.ordinal()] = kunde.toString();
					buchungAtts[Buchung.CSVPositions.KENNZEICHEN.ordinal()] = fahrzeug.toString();
					buchungAtts[Buchung.CSVPositions.ORGANISATORID.ordinal()] = organisator.toString();

					fireUpdateEvent( new UpdateEvent(this, Commands.SET_BUCHUNG, entityManager.findAll( Buchung.class) ) );
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
