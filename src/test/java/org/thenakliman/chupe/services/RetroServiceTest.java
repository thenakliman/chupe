package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RetroServiceTest {
  @Mock
  private RetroRepository retroRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private RetroService retroService;

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
    RetroDTO retroDTO = RetroDTO
        .builder()
        .createdBy(username)
        .maximumVote(maximumVote)
        .id(retroId)
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

    when(modelMapper.map(retroDTO, Retro.class)).thenReturn(retro);
    when(retroRepository.save(retro)).thenReturn(retro);
    when(modelMapper.map(retro, RetroDTO.class)).thenReturn(retroDTO);

    RetroDTO actualRetro = retroService.saveRetro(retroDTO);

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

    when(retroRepository.findAll()).thenReturn(asList(retro1, retro2));
    when(modelMapper.map(retro1, RetroDTO.class)).thenReturn(getRetroDTO(retro1));
    when(modelMapper.map(retro2, RetroDTO.class)).thenReturn(getRetroDTO(retro2));

    List<RetroDTO> retroDTOs = retroService.getRetros();

    verify(retroRepository).findAll();
    assertThat(retroDTOs, hasSize(2));
    assertThat(retroDTOs, hasItems(getRetroDTO(retro1), getRetroDTO(retro2)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionWhenRetroToBeUpdatedIsNotFound() throws NotFoundException {
    long retroId = 10L;
    when(retroRepository.findById(retroId)).thenReturn(Optional.empty());
    RetroDTO retroDTO = RetroDTO
        .builder()
        .id(retroId)
        .name("name")
        .createdBy("newCreatedBy")
        .maximumVote(3L)
        .build();

    retroService.updateRetro(retroId, retroDTO);
  }

  @Test
  public void shouldUpdateRetro() throws NotFoundException {
    long retroId = 10L;
    String retroName = "retro-name";
    String username = "username";
    Retro savedRetro = getRetro(retroId, retroName, username);

    when(retroRepository.findById(retroId)).thenReturn(Optional.of(savedRetro));

    String newName = "new-retro-name";
    RetroDTO retroDTO = RetroDTO
        .builder()
        .id(retroId)
        .name(newName)
        .createdBy("newCreatedBy")
        .maximumVote(3L)
        .build();

    Retro updatedRetro = getRetro(retroId, newName, username);
    when(modelMapper.map(updatedRetro, RetroDTO.class)).thenReturn(retroDTO);
    when(retroRepository.save(updatedRetro)).thenReturn(updatedRetro);
    RetroDTO actualRetroDTO = retroService.updateRetro(retroId, retroDTO);

    verify(retroRepository).save(updatedRetro);
    assertThat(actualRetroDTO, samePropertyValuesAs(retroDTO));
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