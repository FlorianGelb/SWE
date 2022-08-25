package gui;

import de.dhbwka.swe.utils.gui.ObservableComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class DocumentElement extends ObservableComponent  {

    private String path;
    private String title;

    private JLabel documentImage = new JLabel();

    private Label titleLabel;


    public DocumentElement(String title, String path) {
        if (title != null) {
            this.title = title;
            titleLabel = new Label(title);
        }
        if (path != null){
            this.path = path;
        }
    }


    public DocumentElement initUI(){
        this.setLayout(new GridLayout(2, 1));
        this.add(titleLabel);
        loadImage(null);
        this.add(this.documentImage);
        return this;
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


    this.repaint();
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


}
