package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroRepository;

@Service
public class RetroPointService {
  private ModelMapper modelMapper;
  private RetroPointRepository retroPointRepository;
  private RetroRepository retroRepository;
  private DateUtil dateUtil;

  @Autowired
  public RetroPointService(ModelMapper modelMapper,
                           RetroPointRepository retroPointRepository,
                           RetroRepository retroRepository,
                           DateUtil dateUtil) {

    this.modelMapper = modelMapper;
    this.retroPointRepository = retroPointRepository;
    this.retroRepository = retroRepository;
    this.dateUtil = dateUtil;
  }

  public RetroPointDTO saveRetroPoint(RetroPointDTO retroPointDTO) {
    RetroPoint retroPoint = modelMapper.map(retroPointDTO, RetroPoint.class);
    retroPoint.setId(null);
    RetroPoint savedRetroPoint = retroPointRepository.save(retroPoint);
    return modelMapper.map(savedRetroPoint, RetroPointDTO.class);
  }

  public List<RetroPointDTO> getRetroPoints(Long retroId) throws NotFoundException {
    Optional<Retro> retroOptional = retroRepository.findById(retroId);

    Retro retro = retroOptional.orElseThrow(
        () -> new NotFoundException(format("Retro not found for %d id", retroId)));

    List<RetroPoint> retroPoints = retroPointRepository.findAllByRetro(retro);
    return retroPoints
        .stream()
        .map(retroPoint -> modelMapper.map(retroPoint, RetroPointDTO.class))
        .collect(toList());
  }

  public RetroPointDTO updateRetroPoint(Long retroPointId,
                                        RetroPointDTO retroPointDTO) throws NotFoundException {
    Optional<RetroPoint> savedRetroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = savedRetroPointOptional.orElseThrow(
        () -> new NotFoundException(format("Retro point not found for %d id", retroPointId)));

    retroPoint.setDescription(retroPointDTO.getDescription());
    retroPoint.setType(retroPointDTO.getType());
    retroPoint.setUpdatedAt(dateUtil.getTime());
    RetroPoint updatedRetroPoint = retroPointRepository.save(retroPoint);
    return modelMapper.map(updatedRetroPoint, RetroPointDTO.class);
  }

  RetroPoint getRetroPoint(Long id) throws NotFoundException {
    Optional<RetroPoint> retroPoint = retroPointRepository.findById(id);
    return retroPoint.orElseThrow(
        () -> new NotFoundException("Retro poing not found for id " + id));
  }
}
