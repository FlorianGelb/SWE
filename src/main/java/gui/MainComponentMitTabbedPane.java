package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.gui.*;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IAttributed;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.IAppLogger;
import de.dhbwka.swe.utils.util.IPropertyManager;
import model.*;

public class MainComponentMitTabbedPane extends ObservableComponent 
		implements IGUIEventListener, IUpdateEventListener{

	/**
	 * Folgende Commands deklarieren! Es sind die Commands, welche z.B. die MainGUI als 
	 * GUIEvent sendet
	 * Die Commands müssen nur oben deklariert werden, der Rest des enums ist copy/paste (s. SWE-utils-GUIs)
	 */
	public enum Commands implements EventCommand {

		/**
		 * Command:  ID + gelieferter Payload-Typ
		 */
		SAVE_KUNDEN( "MainComponent.saveAllKunden", null ), // reiner Befehl ohne Payload 
		ADD_KUNDE( "MainComponent.addKunde", String[].class ),
		ADD_BUCHUNG("MainComponent.addBuchung", String[].class),
		ADD_FAHRZEUG("MainComponent.addFahrzeug", String[].class),
		ADD_RECHNUNG("MainComponent.addRechnung", String[].class),
		REMOVE_KUNDE( "MainComponent.removeKunde", IDepictable.class );

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

	private IPropertyManager propManager = null;
	
	private IAppLogger logger = AppLogger.getInstance();
	
	private List<IDepictable> allElements = new ArrayList<>();
	private List<AttributeElement> allAttributeElements = new ArrayList<>();
	
	private final static String BTN_ADD_LL = "AddElement2LeftList";
	private final static String BTN_REMOVE_LL = "RemoveElementFromLeftList";

	private final static String BTN_ADD_LL_BUCHUNG = "AddElement2LeftListB";
	private final static String BTN_REMOVE_LL_BUCHUNG = "RemoveElementFromLeftListB";
	private final static String BTN_ADD_LL_STANDORT = "AddElement2LeftListB";

	private final static String BTN_ADD_LL_FAHRZEUG = "AddElement2LeftListB";

	private final static String BTN_ADD_LL_RM = "AddElement2LeftListB";


	private final static String BTN_REMOVE_LL_STANDORT = "RemoveElementFromLeftListB";

	private final static String BTN_REMOVE_LL_FAHRZEUG = "RemoveElementFromLeftListB";

	private final static String BTN_REMOVE_LL_RM = "RemoveElementFromLeftListB";


	private final static String BTN_CHANGE_ATR = "ChangeAttrCustomer";
	
	public final static String SLC = "SimpleListComponent-1";

	public final static String SLC_BUCHUNG = "SimpleListComponent-2";
	public final static String SLC_STANDORT = "SimpleListComponent-2";

	public final static String SLC_FAHRZEUG = "SimpleListComponent-2";

	public final static String SLC_RM= "SimpleListComponent-2";


	public final static String ATTC = "AttributeComponent-1";
	public final static String ATTCB = "AttributeComponent-1";
	public final static String ATTCS = "AttributeComponent-1";
	public final static String BTC = "ButtonComponent-1";
	public final static String LBL_ATTC_KUNDE = "Attribute des Kunden";

	public final static String LBL_ATTC_BUCHUNG = "Attribute der Buchung";

	public final static String LBL_ATTC_STANDORT = "Attribute des Standortes";

	public final static String LBL_ATTC_FAHRZEUG = "Attribute des Standortes";

	public final static String LBL_ATTC_RM = "Attribute der Rechnungen und Mahnungen";



	public final static String LBL_SLC_KUNDE = "Alle Kunden";

	public final static String LBL_SLC_BUCHUNG = "Alle Buchungen";

	public final static String LBL_SLC_STANDORT = "Alle Standorte";

	public final static String LBL_SLC_FAHRZEUG = "Alle Fahrzeuge";

	public final static String LBL_SLC_RM = "Alle Rechnungen und Mahnungen";


	public final static String TAB_KUNDE = "Kunden";
	public final static String TAB_BUCHUNG = "Buchungen";
	public final static String TAB_STANDORT = "Standorte";

	public final static String TAB_FAHRZEUG = "Fahrzeuge";

	public final static String TAB_RM = "Rechnungen/Mahnungen";



	private final static Dimension attCompSize =new Dimension(350,500);

	private SimpleListComponent slc = null;

	private SimpleListComponent slc_buchung = null;

	private SimpleListComponent slc_standort = null;

	private SimpleListComponent slc_fahrzeug = null;

	private SimpleListComponent slc_rm = null;



	private AttributeComponent attComp = null;

	private AttributeComponent attCompBuchung = null;
	private AttributeComponent attCompStandort = null;

	private AttributeComponent attCompFahrzeug = null;

	private AttributeComponent  attCompRM = null;


	private ButtonComponent btnComp = null;

	private ButtonComponent btnCompB = null;

	private ButtonComponent btnCompS = null;

	private ButtonComponent btnCompRM = null;


	private ButtonComponent btnCompF = null;


	private  ButtonComponent btnChng = null;
	private JTabbedPane tabbedPane = new JTabbedPane();

	private final String TTL_PRS = "Personalausweis";
	private final String TTL_FHR = "Führerschein";

	private DocumentComponent dcComp;

	
	public MainComponentMitTabbedPane( IPropertyManager propManager ) {
//		if( propManager == null ) throw new IllegalArgumentException( "PropManager must not be null!");
		this.propManager = propManager;
		initUI();
	}

	private void initUI() {

		/**
		 * hier wird ein JTabbedPane verwendet. Dann werden alle Inhalte (Tabs)
		 * in einer eigenen Methode erzeugt und zugewiesen.
		 * Wenn einTab selbst ein JPanel oder eine JComponent ist, dann kann sie auch in eine
		 * eigene Klasse ausgelagert werden. In diesem Fall ist jedoch die Kommunikation 
		 * zusätzlich aufzubauen (z.B. Observer, was ja auch sinnvoll wäre).
		 */

		// wir nehmen trotzdem ein Borderlayout, da am flexibelsten
		this.setLayout( new BorderLayout() );
		
		this.tabbedPane.add( TAB_KUNDE, createKundenTab() );
		this.tabbedPane.add( TAB_BUCHUNG, createBuchungTab() );
		this.tabbedPane.add( TAB_STANDORT, createStandortTab() );
		this.tabbedPane.add(TAB_FAHRZEUG, createFahrzeugTab());
		this.tabbedPane.add(TAB_RM, createRechnungUndMahnungTab());

		this.add(tabbedPane);
		
	}
	
	
	private JPanel createKundenTab() {
		// Basispanel mit BorderLayout
		JPanel pnlKunde = new JPanel(new BorderLayout());
		
		slc = SimpleListComponent.builder( SLC )
				.propManager( this.propManager )
				.title( LBL_SLC_KUNDE )
				.build();
		slc.setPreferredSize( new Dimension(200, 500) );
		
		btnComp = createButtonComponentForLeftList( slc, BTN_ADD_LL, BTN_REMOVE_LL );
		pnlKunde.add( btnComp, BorderLayout.WEST );
		slc.addObserver( this );
		Kunde initKunde = new Kunde();
		initKunde.getAttributeArray();
		attComp = createAttributeComponent( ATTC, LBL_ATTC_KUNDE, createAttributeElementsFor( initKunde ) ); 
		pnlKunde.add( attComp, BorderLayout.EAST );
		attComp.setPreferredSize( attCompSize );
		attComp.addObserver( this );
		btnChng = createButtonComponentForAttr( attComp );
		pnlKunde.add( btnChng, BorderLayout.EAST);
		dcComp = createDocumentComponent();
		dcComp.addObserver(this);
		pnlKunde.add(dcComp, BorderLayout.CENTER);

		return pnlKunde;
	}


	private DocumentComponent createDocumentComponent(){
		return DocumentComponent.builder()
				.documents(new ArrayList<>(Arrays.asList(new DocumentElement(TTL_PRS, "").initUI(), new DocumentElement(TTL_FHR, "").initUI())))
				.build();
	}

	private JPanel createBuchungTab() {
		// Basispanel mit BorderLayout
		JPanel pnlBuchung = new JPanel(new BorderLayout());
		slc_buchung = SimpleListComponent.builder( SLC_BUCHUNG)
				.propManager( this.propManager )
				.title( LBL_SLC_BUCHUNG )
				.build();
		slc_buchung.setPreferredSize( new Dimension(200, 500) );

		slc_buchung.addObserver(this);

		btnCompB = createButtonComponentForLeftList( slc_buchung, BTN_ADD_LL_BUCHUNG, BTN_REMOVE_LL_BUCHUNG );
		pnlBuchung.add( btnCompB, BorderLayout.WEST );
		Buchung initBuchung = new Buchung();

		attCompBuchung =  createAttributeComponent( ATTCB, LBL_ATTC_BUCHUNG, createAttributeElementsFor( initBuchung ) );
		pnlBuchung.add(attCompBuchung, BorderLayout.EAST);

		return pnlBuchung;
	}

	private JPanel createStandortTab() {
		// Basispanel mit BorderLayout
		JPanel pnlStandort = new JPanel(new BorderLayout());
		slc_standort = SimpleListComponent.builder( SLC_STANDORT)
				.propManager( this.propManager )
				.title( LBL_SLC_STANDORT )
				.build();
		slc_standort.setPreferredSize( new Dimension(200, 500) );

		slc_standort.addObserver(this);

		btnCompS = createButtonComponentForLeftList( slc_standort, BTN_ADD_LL_STANDORT, BTN_REMOVE_LL_STANDORT );
		pnlStandort.add( btnCompS, BorderLayout.WEST );
		Standort initStandort = new Standort();

		attCompStandort =  createAttributeComponent( ATTCS, LBL_ATTC_STANDORT, createAttributeElementsFor( initStandort ) );
		pnlStandort.add(attCompStandort, BorderLayout.EAST);

		return pnlStandort;
	}

	private JPanel createFahrzeugTab() {
		// Basispanel mit BorderLayout
		JPanel pnlFahrzeug = new JPanel(new BorderLayout());
		slc_fahrzeug = SimpleListComponent.builder( SLC_FAHRZEUG)
				.propManager( this.propManager )
				.title( LBL_SLC_FAHRZEUG )
				.build();
		slc_fahrzeug.setPreferredSize( new Dimension(200, 500) );

		slc_fahrzeug.addObserver(this);

		btnCompF = createButtonComponentForLeftList( slc_fahrzeug, BTN_ADD_LL_FAHRZEUG, BTN_REMOVE_LL_FAHRZEUG );
		pnlFahrzeug.add( btnCompF, BorderLayout.WEST );
		Fahrzeug initFahrzeug = new Fahrzeug();

		attCompFahrzeug =  createAttributeComponent( ATTCS, LBL_ATTC_FAHRZEUG, createAttributeElementsFor( initFahrzeug ) );
		pnlFahrzeug.add(attCompFahrzeug, BorderLayout.EAST);

		return pnlFahrzeug;
	}

	private JPanel createRechnungUndMahnungTab() {
		// Basispanel mit BorderLayout
		JPanel pnlRM = new JPanel(new BorderLayout());
		slc_rm = SimpleListComponent.builder( SLC_RM)
				.propManager( this.propManager )
				.title( LBL_SLC_RM )
				.build();
		slc_rm.setPreferredSize( new Dimension(200, 500) );

		slc_rm.addObserver(this);

		btnCompRM = createButtonComponentForLeftList( slc_rm, BTN_ADD_LL_RM, BTN_REMOVE_LL_RM );
		pnlRM.add( btnCompRM, BorderLayout.WEST );
		Fahrzeug initFahrzeug = new Fahrzeug();

		attCompRM =  createAttributeComponent( ATTCS, LBL_ATTC_RM, createAttributeElementsFor( initFahrzeug ) );
		pnlRM.add(attCompRM, BorderLayout.EAST);

		return pnlRM;
	}

	private AttributeComponent createAttributeComponent( String id, String title, AttributeElement[] attElements) {
		return AttributeComponent.builder( id )
				.attributeElements( attElements )
				.title( title )
				.build();
	}
	
	private ButtonComponent createButtonComponentForLeftList( SimpleListComponent slc, String ID1, String ID2 ) {
		ButtonElement[] beArr = new ButtonElement[] {
			ButtonElement.builder( ID1 )
			.buttonText( "Anlegen" )
			.build(),
			ButtonElement.builder( ID2 )
			.buttonText( "Löschen" )
			.build()
		};
		
		return ButtonComponent.builder(BTC).buttonElements(beArr)
				.embeddedComponent( slc )
				.observer( this )
				.componentSize( null )
				.build();
	}

	private ButtonComponent createButtonComponentForAttr( AttributeComponent attr ) {
		ButtonElement[] beArr = new ButtonElement[] {
				ButtonElement.builder(BTN_CHANGE_ATR)
						.buttonText( "Ändern" )
						.build()};

		return ButtonComponent.builder(BTC).buttonElements(beArr)
				.embeddedComponent( attr )
				.observer( this )
				.componentSize( null )
				.build();
	}


	private AttributeElement[] createAttributeElementsFor( IAttributed attObj ) {
		AttributeElement[] aeArr = null;
		if( attObj != null ) {
			Attribute[] attArr = attObj.getAttributeArray();
			aeArr = new AttributeElement[ attArr.length ];
			for (int i = 0; i < attArr.length; i++) {
				aeArr[i] = AttributeElement.builder( attArr[i].getName() )
						.attribute( attArr[i] )
						.colon( true )
						.toolTip( attArr[i].getName() )
						.modificationType( attArr[i].isModifiable() 
								? AttributeElement.ModificationType.DIRECT 
								: AttributeElement.ModificationType.NONE )
						.actionType(AttributeElement.ActionType.NONE)
						.labelSize( new Dimension(100,40) )
						.labelAlignment( javax.swing.SwingConstants.RIGHT )
						.build();
			}
		}
		return aeArr;
	}

	public void setElements( List<IDepictable> allElems ) {
//		this.allElements.clear();
//		this.allElements.addAll(allElems);
		this.allElements = allElems;
		slc.setListElements(allElems);
	}

	@Override
	public void processGUIEvent(GUIEvent ge) {
		System.out.println("Event: " + ge);
		System.out.println("Event.Command: " + ge.getCmdText() );
		System.out.println("Event.Data: " + ge.getData() );
		/**
		 * hier wird direkt der Event in der GUI-Komponente weitergeleitet von der SimpleListComponent
		 * zur AttributeComponent, da der Controller mit dem Event nichts anderes machen würde, als ihn
		 * wieder zurückzusenden (processUpdateEvent()).
		 */
		if( ge.getCmdText().equals( SimpleListComponent.Commands.ELEMENT_SELECTED.cmdText )) {
			if( ge.getData() instanceof Kunde ) {
				System.out.println("es ist ein Kunde");
				Kunde knd = (Kunde) ge.getData();
				System.out.println("Kunde: " + knd);
				Attribute[] atsKunde = knd.getAttributeArray();
				this.attComp.setAttributeElementValues(atsKunde);

				this.dcComp.updatePaths(
						new String[]{knd.getAttributeValueOf(Kunde.Attributes.PERSO),
								knd.getAttributeValueOf(Kunde.Attributes.FUERERSCHEIN)});

				System.out.println(atsKunde[0]);
				return;
			}

			if(ge.getData() instanceof Buchung){
				System.out.println("es ist eine Buchung");
				Buchung bch = (Buchung) ge.getData();
				Attribute[] atsBuchung = bch.getAttributeArray();
				this.attCompBuchung.setAttributeElementValues(atsBuchung);
				return;
			}

		}
		if( ge.getSource() == this.btnComp ) {
			ButtonElement be = (ButtonElement)ge.getData();
			/**
			 * Kunde hinzufügen:
			 * AttComp zur Eingabe mit Dummy-Kunde bauen
			 * Daten eingeben lassen und holen
			 * Daten an Controller zum Erzeugen des neuen Kunden weitergeben
			 * Der Controller schickt dann den neuen Kunden wieder per UpdateEvent an die GUI 
			 */
			if( be.getID().equals( BTN_ADD_LL ) ) {
				// build and show an AtributeComponent for input
				Kunde initKunde = new Kunde();
				initKunde.getAttributeArray();
				AttributeComponent attC = createAttributeComponent( "input", LBL_ATTC_KUNDE, createAttributeElementsFor( initKunde ) ); 
				attC.setPreferredSize(attCompSize);
				if( JOptionPane.showConfirmDialog(this, attC, "bitte die Kundendaten eintragen", JOptionPane.OK_CANCEL_OPTION )
						== JOptionPane.OK_OPTION ) 
				{
					String[] attVals = attC.getAttributeValuesAsArray();
					Arrays.asList(attVals).forEach( e -> logger.debug(e) );
					fireGUIEvent( new GUIEvent(this, Commands.ADD_KUNDE, attVals ));
				}
			}

		}

		if( ge.getSource() == this.btnCompB) {
			ButtonElement be = (ButtonElement) ge.getData();

			if (be.getID().equals(BTN_ADD_LL_BUCHUNG)) {
				// build and show an AtributeComponent for input
				Buchung initBuchung = new Buchung();
				initBuchung.getAttributeArray();
				AttributeComponent attC = createAttributeComponent("input", LBL_ATTC_BUCHUNG, createAttributeElementsFor(initBuchung));
				attC.setPreferredSize(attCompSize);
				if (JOptionPane.showConfirmDialog(this, attC, "bitte die Buchungsdaten eintragen", JOptionPane.OK_CANCEL_OPTION)
						== JOptionPane.OK_OPTION) {
					String[] attVals = attC.getAttributeValuesAsArray();
					Arrays.asList(attVals).forEach(e -> logger.debug(e));
					fireGUIEvent(new GUIEvent(this, Commands.ADD_BUCHUNG, attVals));
				}
			}
		}

		if( ge.getSource() == this.btnCompF ) {
			ButtonElement be = (ButtonElement) ge.getData();
			if (be.getID().equals(BTN_ADD_LL_FAHRZEUG)) {
				// build and show an AtributeComponent for input
				Fahrzeug initFahrzeug = new Fahrzeug();
				initFahrzeug.getAttributeArray();
				AttributeComponent attC = createAttributeComponent("input", LBL_ATTC_FAHRZEUG, createAttributeElementsFor(initFahrzeug));
				attC.setPreferredSize(attCompSize);
				if (JOptionPane.showConfirmDialog(this, attC, "bitte die Fahrzeugdaten eintragen", JOptionPane.OK_CANCEL_OPTION)
						== JOptionPane.OK_OPTION) {
					String[] attVals = attC.getAttributeValuesAsArray();
					Arrays.asList(attVals).forEach(e -> logger.debug(e));
					fireGUIEvent(new GUIEvent(this, Commands.ADD_FAHRZEUG, attVals));
				}
			}
		}

		if( ge.getSource() == this.btnCompRM ) {
			ButtonElement be = (ButtonElement) ge.getData();
			if (be.getID().equals(BTN_ADD_LL_RM)) {
				// build and show an AtributeComponent for input
				Rechnung initRechnung = new Rechnung();
				initRechnung.getAttributeArray();
				AttributeComponent attC = createAttributeComponent("input", LBL_ATTC_RM, createAttributeElementsFor(initRechnung));
				attC.setPreferredSize(attCompSize);
				if (JOptionPane.showConfirmDialog(this, attC, "bitte die Rechnungdaten eintragen", JOptionPane.OK_CANCEL_OPTION)
						== JOptionPane.OK_OPTION) {
					String[] attVals = attC.getAttributeValuesAsArray();
					Arrays.asList(attVals).forEach(e -> logger.debug(e));
					fireGUIEvent(new GUIEvent(this, Commands.ADD_RECHNUNG, attVals));
				}
			}
		}
		/**
		 * wenn nichts gemacht wird: an den Controller weiterleiten ...
		 */
		fireGUIEvent(ge);
	}

	@Override
	public void processUpdateEvent(UpdateEvent ue) {

		if( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_KUNDEN ) {
			List<Kunde> lstKunde = (List<Kunde>)ue.getData();
			this.slc.setListElements(lstKunde, true);
			if( lstKunde.size() > 0 ) {
				// wenn mind. 1 Element -> in AttComp darstellen (da sonst auto-generierte ID verwendet wird) 
				this.attComp.setAttributeElementValues( lstKunde.get(0).getAttributeArray() );

				this.dcComp.updatePaths(
						new String[] {lstKunde.get(0).getAttributeValueOf(Kunde.Attributes.PERSO),
								lstKunde.get(0).getAttributeValueOf(Kunde.Attributes.FUERERSCHEIN)});

			}



		}

		if(ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_BUCHUNG){
			List<Buchung> lstBuchung = (List<Buchung>)ue.getData();
			this.slc_buchung.setListElements(lstBuchung, true);

			if( lstBuchung.size() > 0 ) {
				// wenn mind. 1 Element -> in AttComp darstellen (da sonst auto-generierte ID verwendet wird
				System.out.println(lstBuchung.get(0).getAttributeArray()[7]);
				this.attCompBuchung.setAttributeElementValues( lstBuchung.get(0).getAttributeArray() );

			}

		}

		if(ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_FAHRZEUG){
			List<Fahrzeug> lstFahrzeuge = (List<Fahrzeug>)ue.getData();
			this.slc_fahrzeug.setListElements(lstFahrzeuge, true);

			if( lstFahrzeuge.size() > 0 ) {
				// wenn mind. 1 Element -> in AttComp darstellen (da sonst auto-generierte ID verwendet wird
				System.out.println(Arrays.toString(lstFahrzeuge.get(0).getAttributeArray()));
				this.attCompFahrzeug.setAttributeElementValues( lstFahrzeuge.get(0).getAttributeArray() );

			}

		}

		if(ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_RECHNUNG){
			List<Rechnung> lstRechnungen = (List<Rechnung>)ue.getData();
			this.slc_rm.setListElements(lstRechnungen, true);

			if( lstRechnungen.size() > 0 ) {
				// wenn mind. 1 Element -> in AttComp darstellen (da sonst auto-generierte ID verwendet wird
				System.out.println(Arrays.toString(lstRechnungen.get(0).getAttributeArray()));
				this.attCompRM.setAttributeElementValues( lstRechnungen.get(0).getAttributeArray() );

			}

		}

	}
	
}
