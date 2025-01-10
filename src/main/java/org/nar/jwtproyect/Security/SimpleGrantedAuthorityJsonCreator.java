package org.nar.jwtproyect.Security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  abstract class SimpleGrantedAuthorityJsonCreator {

    // constructor
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){

    }
}
