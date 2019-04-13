package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpsertFeedbackPointDtoToFeedbackPointMappingTest {
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    modelMapper.addConverter(new UpsertFeedbackPointDtoToFeedbackPointMapping(dateUtil).converter());
  }

  @Test
  public void shouldMapUpsertFeedbackPointToFeedbackPoint() {
    String description = "description";
    String username = "user name";
    long sessionId = 101L;
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO
        .builder()
        .description(description)
        .givenTo(username)
        .sessionId(sessionId)
        .build();

    FeedbackPoint feedbackPoint = modelMapper.map(upsertFeedbackPointDTO, FeedbackPoint.class);

    assertThat(feedbackPoint.getDescription(), is(description));
    assertThat(feedbackPoint.getGivenTo().getUserName(), is(username));
    assertThat(feedbackPoint.getFeedbackSession().getId(), is(sessionId));
    assertThat(feedbackPoint.getCreatedAt(), is(now));
    assertThat(feedbackPoint.getUpdatedAt(), is(now));
  }
}