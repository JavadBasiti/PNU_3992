package com.barshid.schematech.web.pagebean;

import org.omnifaces.util.Messages;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by basiti on 01/23/2021.
 */
@Named
@ViewScoped
public class InputMB implements Serializable {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void doAction() {
        Messages.create("Welcome to AdminBoot "+name+"!")
                .detail("<b>AdminFaces</b> and <b>SpringBoot</b> integration via <b>JoinFaces.</b>")
                .add();
    }
}
