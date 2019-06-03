package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpdateRetroStatusDto;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroRepository;

@Service
public class RetroService {

  private final RetroRepository retroRepository;
  private final Converter converter;

  @Autowired
  public RetroService(RetroRepository retroRepository,
                      Converter converter) {

    this.retroRepository = retroRepository;
    this.converter = converter;
  }

  public RetroDTO saveRetro(UpsertRetroDTO upsertRetroDTO, String username) {
    Retro retro = converter.convertToObject(upsertRetroDTO, Retro.class);
    User createdBy = User.builder().userName(username).build();
    retro.setCreatedBy(createdBy);
    Retro savedRetro = retroRepository.save(retro);
    return converter.convertToObject(savedRetro, RetroDTO.class);
  }

  public List<RetroDTO> getRetros() {
    List<Retro> retros = retroRepository.findAll();
    return converter.convertToListOfObjects(retros, RetroDTO.class);
  }

  public RetroDTO updateRetro(Long retroId, UpsertRetroDTO upsertRetroDTO) {
    Optional<Retro> savedRetro = retroRepository.findById(retroId);
    if (!savedRetro.isPresent()) {
      throw new NotFoundException(format("Retro with id %d could not be found", retroId));
    }

    savedRetro.get().setName(upsertRetroDTO.getName());
    savedRetro.get().setMaximumVote(upsertRetroDTO.getMaximumVote());

    Retro updatedRetro = retroRepository.save(savedRetro.get());
    return converter.convertToObject(updatedRetro, RetroDTO.class);
  }

  public void changeRetroStatus(long retroId, UpdateRetroStatusDto updateRetroStatusDto) {
    Optional<Retro> savedRetro = retroRepository.findById(retroId);
    Retro retro = savedRetro.orElseThrow(
        () -> new NotFoundException(format("Retro %d could not be found", retroId)));

    retro.setStatus(updateRetroStatusDto.getStatus());
    retroRepository.save(retro);
  }
}
