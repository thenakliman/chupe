package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroStatus;


@RunWith(MockitoJUnitRunner.class)
public class UpsertRetroDtoToRetroMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new UpsertRetroDtoToRetroMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapRetroDtoToRetro() {
    String retroName = "added-by-user";
    long maximumVote = 10L;
    UpsertRetroDTO upsertRetroDTO = UpsertRetroDTO
        .builder()
        .maximumVote(maximumVote)
        .name(retroName)
        .build();

    Retro mappedRetro = modelMapper.map(upsertRetroDTO, Retro.class);

    assertEquals(retroName, mappedRetro.getName());
    assertEquals(RetroStatus.CREATED, mappedRetro.getStatus());
    assertEquals(now, mappedRetro.getCreatedAt());
    assertEquals(now, mappedRetro.getUpdatedAt());
    assertNull(mappedRetro.getId());
    assertNull(mappedRetro.getCreatedBy());
    assertEquals(Long.valueOf(maximumVote), mappedRetro.getMaximumVote());
  }
}