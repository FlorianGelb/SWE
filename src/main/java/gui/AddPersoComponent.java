package gui;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddPersoComponent extends ObservableComponent implements IGUIEventListener
    {

        private GridLayout layout = new GridLayout(9, 5);
        private ArrayList<JFrame> JF = new ArrayList<>();

        private final Label ID_LABEL = new Label("ID:");
        private final Label PATH_LABEL = new Label("Pfad:");
        private final Label NAME_LABEL = new Label("Name:");
        private final Label BIRTHDAY_LABEL = new Label("Geburtstag:");
        private final Label ISSUE_DATE_LABEL = new Label("Ausstellungsdatum");
        private final Label EXPIRATION_DATE_LABEL = new Label("Ablaufdatum");
        private final Label ISSUE_AUTHORITY_LABEL = new Label("Ausstellende Behörde:");
        private final Label ID_NUMBER_LABEL = new Label("Ausweisnummer:");
        private final Label CAN_LABEL = new Label("CAN:");
        private final Label SIZE_LABEL = new Label("Größe");
        private final Label EYE_COLOR_LABEL = new Label("Augenfarbe:");
        private final Label POST_CODE_LABEL = new Label("PLZ");
        private final Label PLACE_LABEL = new Label("Ort");
        private final Label STREET_LABEL = new Label("Straße:");
        private final Label HNR_LABEL = new Label("Hausnummer:");

        private Button OK = new Button("OK");
        private Button CANCEL = new Button("Abbrechen");
        private Button PICK = new Button("Auswählen");

        private ArrayList<TextField> textFields = new ArrayList();

        public AddPersoComponent(){
            for(int i = 0; i < 9; i++){
                JFrame J = new JFrame();
                J.setLayout(new GridLayout(1, 4));
                JF.add(J);
            }

            for(int i = 0; i < 9; i++){
                textFields.add(new TextField());
            }
        }

        public void initUI(){

        this.setLayout(layout);
        this.add(PATH_LABEL);



    }

    public APCBuilder builder(){
        return new APCBuilder();
    }

    @Override
    public void processGUIEvent(GUIEvent guiEvent) {

    }


        public static class APCBuilder{

        IGUIEventListener Observer;

        public APCBuilder addObserver(IGUIEventListener o){
            this.Observer = o;
            return this;
        }

        public AddPersoComponent build(){
            AddPersoComponent perso = new AddPersoComponent();
            return perso;

        }







    }


    }
