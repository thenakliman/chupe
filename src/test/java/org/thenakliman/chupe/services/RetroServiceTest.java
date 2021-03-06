package org.thenakliman.chupe.services;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpdateRetroStatusDto;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroRepository;


@RunWith(MockitoJUnitRunner.class)
public class RetroServiceTest {
  @Mock
  private RetroRepository retroRepository;

  @Mock
  private Converter converter;

  @InjectMocks
  private RetroService retroService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
  }

  @Test
  public void shouldSaveRetrospectionPoint() {
    String username = "lal_singh";
    long retroId = 1010L;
    long maximumVote = 3L;
    String name = "my-name";
    UpsertRetroDTO upsertRetroDTO = UpsertRetroDTO
        .builder()
        .maximumVote(maximumVote)
        .name(name)
        .build();

    User user = new User();
    user.setUserName(username);

    Retro retro = Retro
        .builder()
        .createdBy(user)
        .id(retroId)
        .maximumVote(maximumVote)
        .build();

    when(converter.convertToObject(upsertRetroDTO, Retro.class)).thenReturn(retro);
    when(retroRepository.save(retro)).thenReturn(retro);
    RetroDTO retroDTO = RetroDTO
        .builder()
        .id(retroId)
        .maximumVote(maximumVote)
        .name(name)
        .createdBy(name)
        .build();

    when(converter.convertToObject(retro, RetroDTO.class)).thenReturn(retroDTO);

    RetroDTO actualRetro = retroService.saveRetro(upsertRetroDTO, name);

    verify(retroRepository).save(retro);
    assertThat(actualRetro, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void shouldReturnEmptyRetros() {
    when(retroRepository.findAll()).thenReturn(emptyList());

    List<RetroDTO> retros = retroService.getRetros();

    verify(retroRepository).findAll();
    assertEquals(retros, emptyList());
  }

  @Test
  public void shouldReturnAllRetros() {
    Retro retro1 = getRetro(101L, "name - 1", "username - 1");
    Retro retro2 = getRetro(102L, "name - 2", "username - 2");

    List<Retro> retros = asList(retro1, retro2);
    when(retroRepository.findAll()).thenReturn(retros);
    when(converter.convertToListOfObjects(retros, RetroDTO.class))
        .thenReturn(asList(getRetroDTO(retro1), getRetroDTO(retro2)));

    List<RetroDTO> retroDTOs = retroService.getRetros();

    verify(retroRepository).findAll();
    assertThat(retroDTOs, hasSize(2));
    assertThat(retroDTOs, hasItems(getRetroDTO(retro1), getRetroDTO(retro2)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionWhenRetroToBeUpdatedIsNotFound() {
    long retroId = 10L;
    when(retroRepository.findById(retroId)).thenReturn(Optional.empty());
    UpsertRetroDTO upsertRetroDTO = UpsertRetroDTO
        .builder()
        .name("name")
        .maximumVote(3L)
        .build();

    retroService.updateRetro(retroId, upsertRetroDTO);
  }

  @Test
  public void shouldUpdateRetro() throws NotFoundException {
    long retroId = 10L;
    String retroName = "retro-name";
    String username = "username";
    Retro savedRetro = getRetro(retroId, retroName, username);

    when(retroRepository.findById(retroId)).thenReturn(Optional.of(savedRetro));
    String newName = "new-retro-name";
    UpsertRetroDTO upsertRetroDTO = UpsertRetroDTO
        .builder()
        .name(newName)
        .maximumVote(3L)
        .build();

    RetroDTO retroDTO = RetroDTO
        .builder()
        .id(retroId)
        .name(newName)
        .createdBy("newCreatedBy")
        .maximumVote(3L)
        .build();

    Retro updatedRetro = getRetro(retroId, newName, username);
    when(converter.convertToObject(updatedRetro, RetroDTO.class)).thenReturn(retroDTO);
    when(retroRepository.save(updatedRetro)).thenReturn(updatedRetro);
    RetroDTO actualRetroDTO = retroService.updateRetro(retroId, upsertRetroDTO);

    verify(retroRepository).save(updatedRetro);
    assertThat(actualRetroDTO, samePropertyValuesAs(retroDTO));
  }

  @Test
  public void changeRetroStatus_shouldThrowNotFoundException_whenRetroDoesNotExist() {
    when(retroRepository.findById(1029L)).thenReturn(Optional.empty());

    expectedException.expect(NotFoundException.class);
    retroService.changeRetroStatus(1029L, new UpdateRetroStatusDto(RetroStatus.CLOSED));
  }

  @Test
  public void changeRetroStatus_shouldCloseRetro() {
    Retro retro = mock(Retro.class);
    when(retroRepository.findById(1029L)).thenReturn(Optional.of(retro));
    retroService.changeRetroStatus(1029L, new UpdateRetroStatusDto(RetroStatus.CLOSED));
    verify(retroRepository).save(retro);
  }

  private RetroDTO getRetroDTO(Retro retro) {
    return RetroDTO
        .builder()
        .id(retro.getId())
        .maximumVote(retro.getMaximumVote())
        .createdBy(retro.getCreatedBy().getUserName())
        .name(retro.getName())
        .build();
  }

  private Retro getRetro(Long id, String name, String username) {
    User createdBy = User
        .builder()
        .userName(username)
        .build();

    return Retro
        .builder()
        .id(id)
        .name(name)
        .maximumVote(3L)
        .createdBy(createdBy)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}