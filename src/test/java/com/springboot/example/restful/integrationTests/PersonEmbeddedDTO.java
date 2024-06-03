package com.springboot.example.restful.integrationTests;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JsonProperty("personDTOList")
    private List<PersonDTO> persons;

    public PersonEmbeddedDTO() {}

    public PersonEmbeddedDTO(List<PersonDTO> persons) {
        this.persons = persons;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((persons == null) ? 0 : persons.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonEmbeddedDTO other = (PersonEmbeddedDTO) obj;
        if (persons == null) {
            if (other.persons != null)
                return false;
        } else if (!persons.equals(other.persons))
            return false;
        return true;
    }

    

}
