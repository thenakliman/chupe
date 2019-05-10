package org.thenakliman.chupe.common.utils;

import static com.google.common.primitives.Ints.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class ConverterTest {
  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private Converter converter;

  @Test
  public void shouldMapListOfObjectToDestinationClass() {
    when(modelMapper.map(1, Integer.class)).thenReturn(11);
    when(modelMapper.map(2, Integer.class)).thenReturn(12);
    when(modelMapper.map(3, Integer.class)).thenReturn(13);
    when(modelMapper.map(4, Integer.class)).thenReturn(14);
    List<Integer> integers = converter.convertToListOfObjects(asList(1, 2, 3, 4), Integer.class);

    assertThat(integers, hasItems(11, 12, 13, 14));
    verify(modelMapper).map(1, Integer.class);
    verify(modelMapper).map(2, Integer.class);
    verify(modelMapper).map(3, Integer.class);
    verify(modelMapper).map(4, Integer.class);
  }

  @Test
  public void shouldMapAnObjectToDestinationClass() {
    when(modelMapper.map(4, Integer.class)).thenReturn(14);
    Integer integer = converter.convertToObject(4, Integer.class);

    assertThat(integer, is(14));
    verify(modelMapper).map(4, Integer.class);
  }

}