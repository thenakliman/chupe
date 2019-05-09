package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroRepository;

@Service
public class RetroService {

  private final RetroRepository retroRepository;
  private final ConverterUtil converterUtil;

  @Autowired
  public RetroService(RetroRepository retroRepository,
                      ConverterUtil converterUtil) {

    this.retroRepository = retroRepository;
    this.converterUtil = converterUtil;
  }

  public RetroDTO saveRetro(UpsertRetroDTO upsertRetroDTO, String username) {
    Retro retro = converterUtil.convertToObject(upsertRetroDTO, Retro.class);
    User createdBy = User.builder().userName(username).build();
    retro.setCreatedBy(createdBy);
    Retro savedRetro = retroRepository.save(retro);
    return converterUtil.convertToObject(savedRetro, RetroDTO.class);
  }

  public List<RetroDTO> getRetros() {
    List<Retro> retros = retroRepository.findAll();
    return converterUtil.convertToListOfObjects(retros, RetroDTO.class);
  }

  public RetroDTO updateRetro(Long retroId, UpsertRetroDTO upsertRetroDTO)
      throws NotFoundException {
    Optional<Retro> savedRetro = retroRepository.findById(retroId);
    if (!savedRetro.isPresent()) {
      throw new NotFoundException(format("Retro with id %d could not be found", retroId));
    }

    savedRetro.get().setName(upsertRetroDTO.getName());
    savedRetro.get().setMaximumVote(upsertRetroDTO.getMaximumVote());

    Retro updatedRetro = retroRepository.save(savedRetro.get());
    return converterUtil.convertToObject(updatedRetro, RetroDTO.class);
  }
}
