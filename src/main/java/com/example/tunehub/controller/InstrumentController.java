package com.example.tunehub.controller;

import com.example.tunehub.dto.InstrumentResponseDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.service.InstrumentMapper;
import com.example.tunehub.service.InstrumentRepository;
import com.example.tunehub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrument")
public class InstrumentController {
    private final InstrumentRepository instrumentRepository;
    private final InstrumentMapper instrumentMapper;
    private final AuthService authService;

    @Autowired
    public InstrumentController(InstrumentRepository instrumentRepository,
                                InstrumentMapper instrumentMapper,
                                AuthService authService) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
        this.authService = authService;
    }

    //Get all instruments
    @GetMapping("/instruments")
    public ResponseEntity<List<InstrumentResponseDTO>> getInstruments() {
        try {
            List<Instrument> instrumentsList = instrumentRepository.findAll();
            if (instrumentsList == null || instrumentsList.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            List<InstrumentResponseDTO> dtoList =
                    instrumentMapper.instrumentListToInstrumentResponseDTOList(instrumentsList);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/instrumentsByUserId/{user_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsByUserId(@PathVariable Long user_id) {
        try {
            authService.getCurrentUserId(); // Ensure user is authenticated
            List<Instrument> instruments = instrumentRepository.findAllByUsers_Id(user_id);
            if (instruments == null || instruments.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(instruments, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/instrumentsByTeachersId/{teachers_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsByTeachersId(@PathVariable Long teachers_id) {
        try {
            authService.getCurrentUserId(); // Ensure user is authenticated
            List<Instrument> instruments = instrumentRepository.findAllByTeachers_Id(teachers_id);
            if (instruments == null || instruments.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(instruments, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/instrumentsBySheetMusicId/{sheet_music_id}")
    public ResponseEntity<List<Instrument>> getInstrumentsBySheetMusicId(@PathVariable Long sheet_music_id) {
        try {
            authService.getCurrentUserId(); // Ensure user is authenticated
            List<Instrument> instruments = instrumentRepository.findAllBySheetsMusic_Id(sheet_music_id);
            if (instruments == null || instruments.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(instruments, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
