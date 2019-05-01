package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.thenakliman.chupe.models.RetroPointType.NEED_IMPROVEMENT;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroPointType;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class RetroPointToRetroPointDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() {
    modelMapper.addMappings(new RetroPointToRetroPointDtoMapping().mapping());
  }

  @Test
  public void shouldMapRetroPointToRetroPointDto() {
    String userName = "user-name";
    User user = User
        .builder()
        .userName(userName)
        .build();

    RetroPointType retroPointType = NEED_IMPROVEMENT;
    long retroPointId = 10101L;
    String description = "my description";
    RetroPoint retroPoint = RetroPoint
        .builder()
        .type(retroPointType)
        .id(retroPointId)
        .description(description)
        .addedBy(user)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    RetroPointDTO retroPointDTO = modelMapper.map(retroPoint, RetroPointDTO.class);

    assertEquals(retroPointId, retroPointDTO.getId());
    assertEquals(description, retroPointDTO.getDescription());
    assertEquals(userName, retroPoint.getAddedBy().getUserName());
    assertEquals(retroPointType, retroPoint.getType());
  }
}