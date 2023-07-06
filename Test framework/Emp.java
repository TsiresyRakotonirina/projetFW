package etu002015.model;

import etu002015.framework.ModelView;
import etu002015.framework.annotation.*;

public class Emp {
    
    String nom = "zah";

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Emp(){}

    @Annotation(Url = "listeemp")
    public ModelView listeEmp(){
        Object[] data = new Object[]{"olona"};
        ModelView modelview = new ModelView("listeEmp.jsp");
        modelview.addItem("liste", data);
        return modelview;
    }

    // @Annotation(Url = "listeEmployer")
    // public Emp listeEmp(){
    //     return new Emp();
    // }

    @Annotation(Url = "formulaire")
    public ModelView formulaire(){
        ModelView modelview = new ModelView("formulaire.jsp");
        modelview.addItem("emp", this);
        return modelview;
    }
}
