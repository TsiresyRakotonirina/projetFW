package etu002015.model;

import etu002015.framework.annotation.*;

public class Emp {
    String nom;

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Emp(){}

    // @Annotation(Url = "listeemp")
    // public ModelView listeEmp(){
    //     return new ModelView("listeEmp.jsp");
    // }

    @Annotation(Url = "listeEmployer")
    public Emp listeEmp(){
        return new Emp();
    }


}
