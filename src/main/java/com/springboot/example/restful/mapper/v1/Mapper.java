package com.springboot.example.restful.mapper.v1;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.model.Person;

public class Mapper {

    private static final ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(Person.class, PersonDTO.class).addMapping(
            Person::getId, PersonDTO::setKey
        );

        mapper.createTypeMap(PersonDTO.class, Person.class).addMapping(
            PersonDTO::getKey, Person::setId
        );
    }

    public static <S, D> D map(final S source, final Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> List<D> mapList(final List<S> source, final Class<D> destinationClass) {
        
        List<D> destinationList = new ArrayList<>();

        for (S s : source) {
            destinationList.add(mapper.map(s, destinationClass));
        }

        return destinationList;
    }

}
