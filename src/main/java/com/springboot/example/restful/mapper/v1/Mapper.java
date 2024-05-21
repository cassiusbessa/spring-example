package com.springboot.example.restful.mapper.v1;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

public class Mapper {

    private static final ModelMapper mapper = new ModelMapper();

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
