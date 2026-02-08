package com.example.tunehub.service;

import com.example.tunehub.dto.InstrumentResponseDTO;
import com.example.tunehub.model.Instrument;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {

    Instrument instrumentResponseDTOtoInstrument(InstrumentResponseDTO i);

    List<InstrumentResponseDTO> instrumentListToInstrumentResponseDTOList(List<Instrument> list);

    @Named("mapInstrumentListWithoutCreate")
    @IterableMapping(qualifiedByName = "mapInstrument")
    List<Instrument> mapInstrumentListWithoutCreate(List<InstrumentResponseDTO> list);

    @Named("mapInstrument")
    Instrument mapInstrument(InstrumentResponseDTO i);


    InstrumentResponseDTO instrumentToInstrumentResponseDTO(Instrument instrument);
}
