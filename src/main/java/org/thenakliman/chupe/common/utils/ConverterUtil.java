package org.thenakliman.chupe.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConverterUtil {
  private ModelMapper modelMapper;

  public ConverterUtil(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <S, D> List<D> convertToListOfObjects(List<S> items, Class<D> destinationType) {
    return items
        .stream()
        .map(item -> modelMapper.map(item, destinationType))
        .collect(Collectors.toList());
  }

  public <S, D> D convertToObject(S item, Class<D> destinationType) {
    return modelMapper.map(item, destinationType);
  }
}
