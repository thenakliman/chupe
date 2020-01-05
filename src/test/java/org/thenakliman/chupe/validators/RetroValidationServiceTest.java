package org.thenakliman.chupe.validators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.repositories.RetroActionItemRepository;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroRepository;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetroValidationServiceTest {
  @Mock
  private RetroRepository retroRepository;

  @Mock
  private RetroPointRepository retroPointRepository;

  @Mock
  private RetroActionItemRepository retroActionItemRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @InjectMocks
  private RetroValidationService retroValidationService;

  private Long retroId = 1000L;

  private String username = "user - name";

  @Before
  public void setUp() {
    Authentication authToken = new UsernamePasswordAuthenticationToken(
            User.builder().username(username).build(),
            null,
            null);

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  @Test
  public void canRetroBeUpdated_shouldReturnNotFound_whenRetroDoesNotExist() {
    when(retroRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.canRetroBeUpdated(retroId);
  }

  @Test
  public void canRetroBeUpdated_shouldReturnBadRequest_whenRetroStatusIsOpen() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CLOSED)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.canRetroBeUpdated(retroId);
  }

  @Test
  public void canRetroBeUpdated_shouldReturnBadRequest_whenRequestUserIsNotTheSameAsUpdated() {

    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CREATED)
            .createdBy(org.thenakliman.chupe.models.User.builder().userName("fake-user").build())
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    boolean canRetroBeUpdated = retroValidationService.canRetroBeUpdated(retroId);
    assertFalse(canRetroBeUpdated);
  }

  @Test
  public void canRetroBeUpdated_shouldReturnBadRequest_whenRequestUserIsTheSameAsUpdated() {

    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CREATED)
            .createdBy(org.thenakliman.chupe.models.User.builder().userName(username).build())
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    boolean canRetroBeUpdated = retroValidationService.canRetroBeUpdated(retroId);
    assertTrue(canRetroBeUpdated);
  }

  @Test
  public void isRetroOpen_shouldThrowNotFoundException_whenRetroDoesNotExist() {
    when(retroRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.isRetroOpen(retroId);
  }

  @Test
  public void isRetroOpen_shouldThrowBadRequest_whenStatusIsClosed() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CLOSED)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.isRetroOpen(retroId);
  }

  @Test
  public void isRetroOpen_shouldReturnTrue_whenStatusIsOpen() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CREATED)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    boolean retroOpen = retroValidationService.isRetroOpen(retroId);

    assertTrue(retroOpen);
  }

  @Test
  public void canBeUpdated_shouldReturnNotFound_whenRetroDoesNotExist() {
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.canBeUpdated(retroId);
  }

  @Test
  public void canBeUpdated_shouldReturnBadRequest_whenRetroStatusIsOpen() {
    Optional<RetroPoint> retroPointOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.CLOSED).build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroPointOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.canBeUpdated(retroId);
  }

  @Test
  public void canBeUpdated_shouldReturnBadRequest_whenRequestUserIsNotTheSameAsUpdated() {

    Optional<RetroPoint> retroOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.CREATED).build())
            .addedBy(org.thenakliman.chupe.models.User.builder().userName("fake-user").build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroOptional);

    boolean canBeUpdated = retroValidationService.canBeUpdated(retroId);
    assertFalse(canBeUpdated);
  }

  @Test
  public void canBeUpdated_shouldReturnBadRequest_whenRequestUserIsTheSameAsUpdated() {

    Optional<RetroPoint> retroOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.CREATED).build())
            .addedBy(org.thenakliman.chupe.models.User.builder().userName(username).build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroOptional);

    boolean canBeUpdated = retroValidationService.canBeUpdated(retroId);
    assertTrue(canBeUpdated);
  }

  @Test
  public void canBeVoted_shouldThrowNotFoundException_whenRetroDoesNotExist() {
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.canBeVoted(retroId);
  }

  @Test
  public void canBeVoted_shouldReturnFalse_whenStatusIsClosed() {
    Optional<RetroPoint> retroOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.CLOSED).build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.canBeVoted(retroId);
  }

  @Test
  public void canBeVoted_shouldReturnTrue_whenStatusIsOpen() {
    Optional<RetroPoint> retroOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.IN_PROGRESS).build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroOptional);

    boolean retroOpen = retroValidationService.canBeVoted(retroId);

    assertTrue(retroOpen);
  }

  @Test
  public void canBeVoted_shouldReturnFalse_whenStatusIsCreated() {
    Optional<RetroPoint> retroOptional = Optional.of(RetroPoint
            .builder()
            .id(retroId)
            .retro(Retro.builder().status(RetroStatus.CREATED).build())
            .build());

    when(retroPointRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.canBeVoted(retroId);
  }

  @Test
  public void isRetroInProgress_shouldReturnTrue_whenStatusIsInProgress() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.IN_PROGRESS)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    boolean isRetroInProgress = retroValidationService.isRetroInProgress(retroId);

    assertThat(isRetroInProgress, is(true));
  }

  @Test
  public void isRetroInProgress_shouldGiveNotFound_whenRetroDoesNotExist() {
    when(retroRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.isRetroInProgress(retroId);
  }

  @Test
  public void isRetroInProgress_shouldBadRequest_whenRetroIsInCreatedState() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CREATED)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.isRetroInProgress(retroId);
  }

  @Test
  public void isRetroInProgress_shouldBadRequest_whenRetroIsInClosedState() {
    Optional<Retro> retroOptional = Optional.of(Retro
            .builder()
            .id(retroId)
            .status(RetroStatus.CLOSED)
            .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.isRetroInProgress(retroId);
  }

  @Test
  public void canActionItemBeUpdated_shouldGiveNotFound_whenRetroActionItemIsNotFound() {
    when(retroActionItemRepository.findById(retroId)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.canActionItemBeUpdated(retroId);
  }

  @Test
  public void canActionItemBeUpdated_shouldGiveBadRequest_whenRetroIsInCloseState() {
    Optional<RetroActionItem> actionItemOptional = Optional.of(RetroActionItem
            .builder()
            .id(101L)
            .retro(Retro.builder().status(RetroStatus.CLOSED).build())
            .build());

    when(retroActionItemRepository.findById(101L)).thenReturn(actionItemOptional);

    expectedException.expect(BadRequestException.class);
    retroValidationService.canActionItemBeUpdated(101);
  }

  @Test
  public void canActionItemBeUpdated_shouldReturnTrue_whenRetroIsInCreatedState() {
    Optional<RetroActionItem> actionItemOptional = Optional.of(RetroActionItem
            .builder()
            .id(101L)
            .createdBy(org.thenakliman.chupe.models.User.builder().userName(username).build())
            .retro(Retro.builder().status(RetroStatus.CREATED).build())
            .build());

    when(retroActionItemRepository.findById(101L)).thenReturn(actionItemOptional);

    boolean canActionItemBeUpdated = retroValidationService.canActionItemBeUpdated(101);
    assertThat(canActionItemBeUpdated, is(true));
  }

  @Test
  public void canActionItemBeUpdated_shouldReturnTrue_whenRetroIsInProgressState() {
    Optional<RetroActionItem> actionItemOptional = Optional.of(RetroActionItem
            .builder()
            .id(101L)
            .createdBy(org.thenakliman.chupe.models.User.builder().userName(username).build())
            .retro(Retro.builder().status(RetroStatus.IN_PROGRESS).build())
            .build());

    when(retroActionItemRepository.findById(101L)).thenReturn(actionItemOptional);

    boolean canActionItemBeUpdated = retroValidationService.canActionItemBeUpdated(101);
    assertThat(canActionItemBeUpdated, is(true));
  }

  @Test
  public void canActionItemBeUpdated_shouldReturnFalse_whenRetroIsInProgressState() {
    Optional<RetroActionItem> actionItemOptional = Optional.of(RetroActionItem
            .builder()
            .id(101L)
            .createdBy(org.thenakliman.chupe.models.User.builder().userName("invalid user").build())
            .retro(Retro.builder().status(RetroStatus.IN_PROGRESS).build())
            .build());

    when(retroActionItemRepository.findById(101L)).thenReturn(actionItemOptional);

    boolean canActionItemBeUpdated = retroValidationService.canActionItemBeUpdated(101);
    assertThat(canActionItemBeUpdated, is(false));
  }

  @Test
  public void canPracticesBeAssessed_throwException_whenRetroDoesNotExist() {
    when(retroRepository.findById(101L)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroValidationService.canPracticesBeAssessed(101);
  }

  @Test
  public void canPracticesBeAssessed_returnTrue_whenRetroIsInCreatedState() {
    when(retroRepository.findById(101L)).thenReturn(Optional.of(
            Retro.builder()
                    .maximumVote(3L)
                    .status(RetroStatus.CREATED)
                    .build()));

    assertTrue(retroValidationService.canPracticesBeAssessed(101));
  }

  @Test
  public void canPracticesBeAssessed_returnFalse_whenRetroIsInProgressState() {
    when(retroRepository.findById(101L)).thenReturn(Optional.of(
            Retro.builder()
                    .id(101L)
                    .maximumVote(3L)
                    .status(RetroStatus.IN_PROGRESS)
                    .build()));

    expectedException.expect(BadRequestException.class);
    assertFalse(retroValidationService.canPracticesBeAssessed(101));
  }

  @Test
  public void canPracticesBeAssessed_returnFalse_whenRetroIsInClosedState() {
    when(retroRepository.findById(101L)).thenReturn(Optional.of(
            Retro.builder()
                    .id(101L)
                    .maximumVote(3L)
                    .status(RetroStatus.CLOSED)
                    .build()));

    expectedException.expect(BadRequestException.class);
    retroValidationService.canPracticesBeAssessed(101);
  }
}