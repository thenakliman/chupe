package org.thenakliman.chupe.validators;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroRepository;

@RunWith(MockitoJUnitRunner.class)
public class RetroValidationServiceTest {
  @Mock
  private RetroRepository retroRepository;

  @Mock
  private RetroPointRepository retroPointRepository;

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
  public void isRetroOpen_shouldReturnFalse_whenStatusIsClosed() {
    Optional<Retro> retroOptional = Optional.of(Retro
        .builder()
        .id(retroId)
        .status(RetroStatus.CLOSED)
        .build());

    when(retroRepository.findById(retroId)).thenReturn(retroOptional);

    boolean retroOpen = retroValidationService.isRetroOpen(retroId);

    assertFalse(retroOpen);
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

    boolean retroOpen = retroValidationService.canBeVoted(retroId);

    assertFalse(retroOpen);
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

    boolean retroOpen = retroValidationService.canBeVoted(retroId);

    assertFalse(retroOpen);
  }
}