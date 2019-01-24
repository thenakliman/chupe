package org.thenakliman.chupe.services;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.repositories.RetroPointRepository;

public class RetroPointServiceTest {
  @Mock
  private ModelMapper modelMapper;

  @Mock
  private RetroPointRepository retroPointRepository;

  @InjectMocks
  private RetroPointService retroPointService;

  @Test
  public void shouldSaveRetroPoint() {

  }
}