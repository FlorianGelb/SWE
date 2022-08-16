package gui;

import de.dhbwka.swe.utils.gui.ObservableComponent;

import javax.print.Doc;
import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DocumentComponent extends ObservableComponent {


    JPanel documentComponent = new JPanel();
    ArrayList<DocumentComponent> Documents = new ArrayList<>();

    public DocumentComponent(ArrayList<DocumentComponent> Documents)
    {
        this.Documents = Documents;
    }

    public JPanel initUI(){
        this.documentComponent.setLayout(new javax.swing.BoxLayout(this, BoxLayout.Y_AXIS));
        for(DocumentComponent doc : Documents){
            this.documentComponent.add(documentComponent);
        }

        return this.documentComponent;
    }


}
