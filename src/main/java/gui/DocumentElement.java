package gui;

import de.dhbwka.swe.utils.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DocumentElement implements IUpdateEventListener {
    private String path;
    private String title;

    private JLabel documentImage = new JLabel();

    private Label titleLabel;

    private JPanel base = new JPanel();



    public DocumentElement(String title, String path) {
        if (title != null) {
            this.title = title;
            titleLabel = new Label(title);
        }

        if (path != null){
            this.path = path;
        }
    }


    public JPanel initUI(){
        this.base.setLayout(new javax.swing.BoxLayout(this.base, BoxLayout.Y_AXIS));
        this.base.add(titleLabel);
        loadImage(null);
        this.base.add(this.documentImage);
        return this.base;
    }

    public void loadImage(String path){
        if (path != null){
            this.path = path;
        }

        try{
            BufferedImage imgBfrd = ImageIO.read(new File(this.path));
            ImageIcon icon = new ImageIcon(imgBfrd);
            this.documentImage.setIcon(icon);
        }
        catch (IOException e){
            System.out.println(e);
        }


    }

    public String  getPath(){
        return this.path;
    }

    public String getTitle(){
        return this.title;
    }


    public void setValues(String[] vals){
        this.setPath(vals[0]);
        this.setTitle(vals[1]);
    }
    public void setPath(String path){
        this.path = path;
    }

    public void setTitle(String title){
        this.title = title;
        this.titleLabel.setText(title);

    }

    private enum Commands  implements EventCommand {
        VALUE_CHANGED("AttributeComponent.valueChanged", DocumentElement.class),
        SELECTION_CHANGED("AttributeComponent.selectionChanged", DocumentElement.class);

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

    @Override
    public void processUpdateEvent(UpdateEvent updateEvent){

        if(updateEvent.getCmd() == Commands.SELECTION_CHANGED || updateEvent.getCmd() == Commands.VALUE_CHANGED ){

            this.setValues((String[])updateEvent.getData());

        }


    }

}