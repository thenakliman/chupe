package org.thenakliman.chupe.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterUtil<S, D> {
  private ModelMapper modelMapper;

  @Autowired
  public ConverterUtil(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public List<D> convertToListOfObjects(List<S> items, Class<D> destinationType) {
    return items
        .stream()
        .map(item -> modelMapper.map(item, destinationType))
        .collect(Collectors.toList());
  }
}
