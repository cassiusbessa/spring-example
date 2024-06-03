package com.springboot.example.restful.integrationTests;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonDTO> content;

    public PagedModelPerson() {
    }

    public PagedModelPerson(List<PersonDTO> content) {
        this.content = content;
    }

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }

}
