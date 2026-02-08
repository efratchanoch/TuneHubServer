package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Users;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface PostMapper {
    @Mapping(target = "dateUploaded", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "hearts", constant = "0")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "comments", ignore = true)
    Post postUploadDTOtoPost(PostUploadDTO dto);


    @Mapping(target = "user", source = "user")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "imagesBase64", expression = "java(com.example.tunehub.service.FileUtils.imagesToBase64(p.getImagesPath()))")
    @Mapping(
            target = "liked",
            expression = "java(likeRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId,  com.example.tunehub.model.ETargetType.POST, p.getId()))")
    @Mapping(
            target = "favorite",
            expression = "java(favRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId, com.example.tunehub.model.ETargetType.POST, p.getId()))")
    @Mapping(
            target = "likes",
            expression = "java(likeRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.POST, p.getId()))")
    @Mapping(
            target = "hearts",
            expression = "java(favRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.POST, p.getId()))")
    PostResponseDTO postToPostResponseDTO(
            Post p,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);

    List<PostResponseDTO> postListToPostResponseDTOlist(
            List<Post> p,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);


    @Mapping(target = "userName", source = "user.name")
   // @Mapping(target = "dateUploaded", expression = "java(post.getDateUploaded() != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(post.getDateUploaded()) : null)")
    PostSearchDTO postToPostSearchDTO(Post post);

    List<PostSearchDTO> postListToPostSearchDTOList(List<Post> posts);
}
