package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.models.Retro;


@RunWith(MockitoJUnitRunner.class)
public class RetroDtoToRetroMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new RetroDtoToRetroMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapRetroDtoToRetro() {
    long retroId = 7177L;
    String retroName = "added-by-user";
    String createdBy = "createdBy-user";
    long maximumVote = 10L;
    RetroDTO retroDTO = RetroDTO
        .builder()
        .id(retroId)
        .maximumVote(maximumVote)
        .createdBy(createdBy)
        .name(retroName)
        .build();

    Retro mappedRetro = modelMapper.map(retroDTO, Retro.class);

    assertEquals(retroName, mappedRetro.getName());
    assertEquals(now, mappedRetro.getCreatedAt());
    assertEquals(now, mappedRetro.getUpdatedAt());
    assertEquals(Long.valueOf(retroId), mappedRetro.getId());
    assertEquals(createdBy, mappedRetro.getCreatedBy().getUserName());
    assertEquals(Long.valueOf(maximumVote), mappedRetro.getMaximumVote());
  }
}