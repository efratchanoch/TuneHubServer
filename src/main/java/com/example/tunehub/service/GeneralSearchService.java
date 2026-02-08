package com.example.tunehub.service;

import com.example.tunehub.dto.GlobalSearchResponseDTO;
import com.example.tunehub.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GeneralSearchService {

    private PostRepository postRepository;
    private SheetMusicRepository sheetMusicRepository;
    private UsersRepository usersRepository;
    private PostMapper postMapper;
    private SheetMusicMapper sheetMusicMapper;
    private UsersMapper usersMapper;

    @Autowired
    public GeneralSearchService(PostRepository postRepository, SheetMusicRepository sheetMusicRepository, UsersRepository usersRepository, PostMapper postMapper, SheetMusicMapper sheetMusicMapper, UsersMapper usersMapper) {
        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.postMapper = postMapper;
        this.sheetMusicMapper = sheetMusicMapper;
        this.usersMapper = usersMapper;
    }


    public GlobalSearchResponseDTO executeGlobalSearch(String searchTerm) {
        GlobalSearchResponseDTO results = new GlobalSearchResponseDTO();

        // Posts
        List<Post> posts = postRepository.findAllTop5ByTitleContainingIgnoreCase(searchTerm);
        if (!posts.isEmpty()) {
            results.setPosts(postMapper.postListToPostSearchDTOList(posts));
        }

        // Sheets
        List<SheetMusic> sheets = sheetMusicRepository.findAllTop5ByTitleContainingIgnoreCase(searchTerm);
        if (sheets != null && !sheets.isEmpty()) {
            results.setSheetMusic(sheetMusicMapper.sheetMusicListToSheetMusicSearchDTOList(sheets));
        }

        // Musicians
        List<Users> users = usersRepository.findTop5ByNameContainingIgnoreCaseAndUserTypesContaining(
                searchTerm,
                EUserType.MUSICIAN
        );
        if (users != null && !users.isEmpty()) {
            results.setMusicians(usersMapper.usersListToUsersSearchDTOList(users));
        }

        // Teachers
        List<Users> teachers = users.stream()
                .filter(u -> u.getUserTypes() != null &&
                        u.getUserTypes().contains(EUserType.TEACHER))
                .toList();
        if (teachers != null && !teachers.isEmpty()) {
            results.setTeachers(usersMapper.usersListToUsersSearchDTOList(teachers));
        }

        return results;
    }
}