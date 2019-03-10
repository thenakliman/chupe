package org.thenakliman.chupe.mappings;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroPointType;


@RunWith(MockitoJUnitRunner.class)
public class UpsertRetroPointDtoToRetroPointMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new UpsertRetroPointDtoToRetroPointMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapUpsertRetroPointDtoToRetroPoint() {
    long retroId = 7177L;
    String description = "added-by-user";
    RetroPointType retroPointType = RetroPointType.NEED_IMPROVEMENT;
    UpsertRetroPointDTO upsertRetroPointDTO = UpsertRetroPointDTO
        .builder()
        .retroId(retroId)
        .type(retroPointType)
        .description(description)
        .build();

    RetroPoint mappedRetro = modelMapper.map(upsertRetroPointDTO, RetroPoint.class);

    assertEquals(description, mappedRetro.getDescription());
    assertEquals(now, mappedRetro.getCreatedAt());
    assertEquals(now, mappedRetro.getUpdatedAt());
    assertNull(mappedRetro.getId());
    assertEquals(Long.valueOf(retroId), mappedRetro.getRetro().getId());
    assertEquals(retroPointType, mappedRetro.getType());
  }
}