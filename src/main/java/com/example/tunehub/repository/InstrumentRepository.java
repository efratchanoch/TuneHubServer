package com.example.tunehub.repository;

import com.example.tunehub.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    List<Instrument> findAllByUsers_Id(Long usersId);

    List<Instrument> findAllByTeachers_Id(Long teachersId);

    List<Instrument> findAllBySheetsMusic_Id(Long sheetMusicId);

    Instrument findByName(String name);
}
