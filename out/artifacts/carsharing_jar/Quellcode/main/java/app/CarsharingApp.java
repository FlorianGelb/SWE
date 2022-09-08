package app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.IOUtilities;
import de.dhbwka.swe.utils.util.PropertyManager;
import gui.MainComponent;
import gui.MainComponentMitTabbedPane;
import model.Kunde;
import model.Standort;


public class CarsharingApp {

	private final static boolean startWithObserver = true;

	private static PropertyManager propertyManager;

	private static String propertyFilePath;
	private static String csvFilePath;
	
	public static void main(String[] args) throws Exception {

	if(args.length > 0){
		int index_d = -1;
		int index_p = -1;

		for(int i = 0; i < args.length; i++){
			String s = args[i];
			if(s.equals("-d")){
				index_d = i;
			} else if (s.equals("-p")) {
				index_p = i;
			}
		}

		if(args.length - 1 > (index_d > index_p? index_d : index_p)){

			propertyFilePath = index_p > -1 ? args[index_p + 1] : "";
			csvFilePath =  index_d > -1 ? args[index_d + 1] : "";

		}

	}

	propertyManager= new PropertyManager(args.length > 0? propertyFilePath :  "", CarsharingApp.class, "");




		new CarsharingApp( startWithObserver );
	}
	
	public CarsharingApp( boolean startWithObserver ) {

		String filePath = this.getClass()
				.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.getPath();
		String path = filePath.substring(0, filePath.lastIndexOf("/"));
		System.out.println(path);


		if( startWithObserver )
			initWithObserver();

	}

	
	
	private void initWithLinkToGUI() {
		MainComponent mainComp = new MainComponent(null);
		
		IOUtilities.openInJFrame(mainComp, 600, 500, 800, 300, "CarsharingApp", null, true);
	}

	private void initWithObserver() {
		MainComponentMitTabbedPane mainComp = new MainComponentMitTabbedPane(propertyManager);

		CSControllerReinerObserverUndSender controller = new CSControllerReinerObserverUndSender(csvFilePath);
		controller.addObserver( mainComp );
		mainComp.addObserver( controller );
		controller.init();
		IOUtilities.openInJFrame(mainComp, 600, 500, 800, 300, "CarsharingApp", null, true);
	}
	
	
	
	
	
	
	

	/**
	 * nur zu Testzwecken, Daten werden im Controller geladen
	 * @return
	 */
	private List<IDepictable> createKunden() {
		List<IDepictable> data = new ArrayList<>();

		Kunde kunde = new Kunde( "K-1", "Willi", "Bald", "Mein erster Kunde", false );
		data.add(kunde);
		data.add( new Kunde( "K-1", "Anna", "Conda", "Mein zweiter Kunde", true ) );
		
		return data;
	}
	
	/**
	 * nur zu Testzwecken, Daten werden im Controller geladen
	 * @return
	 */
	private List<IDepictable> createTestData() {
		List<IDepictable> data = new ArrayList<>();
		
		Standort standort = new Standort( "s-1", "Bahnhofstraße 4", 3);
		data.add(standort);
		standort = new Standort( "s-2", "Erzbergerstrasse 111", 15);
		data.add(standort);
		standort = new Standort( "s-3", "Beethovenstrasse 33", 3);
		data.add(standort);
		
		return data;
	}

}
