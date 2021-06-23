package com.barshid.schematech.web.pagebean;

import lombok.Data;
import org.springframework.web.context.annotation.SessionScope;

import javax.inject.Named;

@Named
@SessionScope
@Data
public class MyLayout {
    public final String template="/template.xhtml";
}
