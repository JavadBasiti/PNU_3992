package com.barshid.schematech.web.util;

import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class Utils implements Serializable {



    @PostConstruct
    public void init() {
//        cars = new ArrayList<>();
//        IntStream.rangeClosed(1, 50)
//                .forEach(i -> cars.add(create(i)));
    }

//    private static Patient create(int i) {
//        return new Patient(i).model("model " + i).name("name" + i).price(Double.valueOf(i));
//    }

    public static void addDetailMessage(String message) {
        addDetailMessage(message, null);
    }

    public static void addDetailMessage(String message, FacesMessage.Severity severity) {

        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
        Messages.add(null, facesMessage);
    }

//    @Produces
//    public List<Patient> getCars() {
//        return cars;
//    }
}
