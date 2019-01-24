package org.thenakliman.chupe.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.repositories.RetroPointRepository;

@Service
public class RetroPointService {
  private ModelMapper modelMapper;
  private RetroPointRepository retroPointRepository;

  public RetroPointService(ModelMapper modelMapper, RetroPointRepository retroPointRepository) {
    this.modelMapper = modelMapper;
    this.retroPointRepository = retroPointRepository;
  }

  public saveRetroPoint()
}
