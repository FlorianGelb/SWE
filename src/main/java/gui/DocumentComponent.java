package gui;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.gui.AttributeElement;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import model.Kunde;

import java.awt.*;
import java.util.ArrayList;

public class DocumentComponent extends ObservableComponent implements IUpdateEventListener{
    private ArrayList<DocumentElement> documents;
    private ArrayList<IGUIEventListener> listOfAllObservers = new ArrayList();

    public DocumentComponent(ArrayList<DocumentElement> documents) {
       if(documents != null){
           this.documents = documents;
       }
    }

    public void initUI(){
        this.setLayout(new GridLayout(2,1));
        for(DocumentElement doc : documents)
        {
            this.add(doc);
        }
        this.repaint();
    }


    public void updatePaths(String[] paths){
        if (paths.length != this.documents.size()){
            throw new IllegalArgumentException("Anzahl der Pfade stimmt nicht mit der Anzahl der Elementet überein");
        }
        else{
            for(int i = 0; i < paths.length; i++){
                this.documents.get(i).loadImage(paths[i]);
            }
            this.repaint();
        }

    }


    public static DCBuilder builder(){
        DCBuilder builder = new DCBuilder();
        return builder;
    }

    public void setListener(IGUIEventListener el){
        this.listOfAllObservers.add(el);
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {
    }


    public static class DCBuilder {

        private ArrayList<DocumentElement> documents;
        private IGUIEventListener listener;

        public DocumentComponent build(){

            DocumentComponent element = new DocumentComponent(this.documents);
            element.setListener(this.listener);
            element.initUI();
            return element;

        }

        public DCBuilder documents(ArrayList<DocumentElement> documents){
            this.documents = documents;
            return this;
        }


        public DCBuilder observer(IGUIEventListener observer){
                this.listener = observer;
                return this;
        }




    }

    public static enum Commands implements EventCommand {
        VALUE_CHANGED("AttributeComponent.valueChanged", AttributeElement.class);

        public final Class<?> payloadType;
        public final String cmdText;

        private Commands(String cmdText, Class payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        public String getCmdText() {
            return this.cmdText;
        }

        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

}

