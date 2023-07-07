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

    //sprint7
    @Annotation(Url = "formulaire")
    public ModelView formulaire(){
        ModelView modelview = new ModelView("formulaire.jsp");
        modelview.addItem("emp", this);
        return modelview;
    }

    //sprint8
    @Annotation(Url = "fonctionArgumenter")
    public ModelView argument(Integer arg){
        ModelView modelview = new ModelView("data.jsp");
        modelview.addItem("argument", arg);
        return modelview;
    }




}
