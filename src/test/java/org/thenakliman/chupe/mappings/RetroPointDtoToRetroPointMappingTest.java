package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroPointType;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.RetroPointType.NEED_IMPROVEMENT;

@RunWith(MockitoJUnitRunner.class)
public class RetroPointDtoToRetroPointMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new RetroPointDtoToRetroPointMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapRetroPointDtoToRetroPoint() {
    String addedBy = "added-by-user";
    String description = "test-description";
    long retroPointId = 10110L;
    RetroPointType retroPointType = NEED_IMPROVEMENT;
    long retroId = 1203L;
    RetroPointDTO retroPointDTO = RetroPointDTO
        .builder()
        .addedBy(addedBy)
        .description(description)
        .id(retroPointId)
        .type(retroPointType)
        .retroId(retroId)
        .build();

    RetroPoint retroPoint = modelMapper.map(retroPointDTO, RetroPoint.class);

    assertEquals(addedBy, retroPoint.getAddedBy().getUserName());
    assertEquals(description, retroPoint.getDescription());
    assertEquals(Long.valueOf(retroPointId), retroPoint.getId());
    assertEquals(retroPointType, retroPoint.getType());
    assertEquals(Long.valueOf(retroId), retroPoint.getRetro().getId());
    assertEquals(now, retroPoint.getCreatedAt());
    assertEquals(now, retroPoint.getUpdatedAt());
  }
}