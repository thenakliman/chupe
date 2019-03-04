package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroRepository;

@Service
public class RetroService {

  private ModelMapper modelMapper;
  private RetroRepository retroRepository;

  @Autowired
  public RetroService(ModelMapper modelMapper,
                      RetroRepository retroRepository) {

    this.modelMapper = modelMapper;
    this.retroRepository = retroRepository;
  }

  public RetroDTO saveRetro(UpsertRetroDTO upsertRetroDTO, String username) {
    Retro retro = modelMapper.map(upsertRetroDTO, Retro.class);
    User createdBy = User.builder().userName(username).build();
    retro.setCreatedBy(createdBy);
    Retro savedRetro = retroRepository.save(retro);
    return modelMapper.map(savedRetro, RetroDTO.class);
  }

  public List<RetroDTO> getRetros() {
    List<Retro> retros = retroRepository.findAll();
    return retros
        .stream()
        .map(retro -> modelMapper.map(retro, RetroDTO.class))
        .collect(toList());
  }

  public RetroDTO updateRetro(Long retroId, UpsertRetroDTO upsertRetroDTO) throws NotFoundException {
    Optional<Retro> savedRetro = retroRepository.findById(retroId);
    if (!savedRetro.isPresent()) {
      throw new NotFoundException(format("Retro with id %d could not be found", retroId));
    }

    savedRetro.get().setName(upsertRetroDTO.getName());
    savedRetro.get().setMaximumVote(upsertRetroDTO.getMaximumVote());

    Retro updatedRetro = retroRepository.save(savedRetro.get());
    return modelMapper.map(updatedRetro, RetroDTO.class);
  }
}
