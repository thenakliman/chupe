package org.thenakliman.chupe.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Converter {
  private ModelMapper modelMapper;

  public Converter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <S, D> List<D> convertToListOfObjects(List<S> sources, Class<D> destinationType) {
    return sources
        .stream()
        .map(item -> modelMapper.map(item, destinationType))
        .collect(Collectors.toList());
  }

  public <S, D> D convertToObject(S source, Class<D> destinationType) {
    return modelMapper.map(source, destinationType);
  }
}
