package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class RetroToRetroDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    modelMapper.addMappings(new RetroToRetroDtoMapping().mapping());
    now = new Date();
  }

  @Test
  public void shouldMapRetroDtoToRetro() {
    String userName = "user-name";
    long retroId = 1001L;
    long maximumVote = 101L;
    String name = "name";
    Retro retro = Retro
        .builder()
        .createdBy(User.builder().userName(userName).build())
        .id(retroId)
        .name(name)
        .maximumVote(maximumVote)
        .build();

    RetroDTO retroDTO = modelMapper.map(retro, RetroDTO.class);

    assertEquals(Long.valueOf(retroId), retroDTO.getId());
    assertEquals(Long.valueOf(maximumVote), retroDTO.getMaximumVote());
    assertEquals(name, retroDTO.getName());
    assertEquals(userName, retroDTO.getCreatedBy());
  }
}