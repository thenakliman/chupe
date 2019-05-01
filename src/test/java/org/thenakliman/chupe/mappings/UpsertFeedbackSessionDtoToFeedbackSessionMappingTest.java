package org.thenakliman.chupe.mappings;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

@RunWith(MockitoJUnitRunner.class)
public class UpsertFeedbackSessionDtoToFeedbackSessionMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new UpsertFeedbackSessionDtoToFeedbackSessionMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapRetroDtoToRetro() {
    String description = "description - of";
    UpsertFeedbackSessionDTO upsertFeedbackSessionDTO = UpsertFeedbackSessionDTO
        .builder()
        .description(description)
        .build();

    FeedbackSession feedbackSession = modelMapper.map(upsertFeedbackSessionDTO, FeedbackSession.class);

    assertThat(feedbackSession.getDescription(), is(description));
    assertNull(feedbackSession.getId());
    assertNull(feedbackSession.getCreatedBy());
    assertThat(feedbackSession.getCreatedAt(), is(now));
    assertThat(feedbackSession.getUpdatedAt(), is(now));
  }
}