package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.BannedUserDTO;
import com.omarahmed42.ecommerce.model.BannedUser;
import com.omarahmed42.ecommerce.service.BannedUserService;

import io.swagger.v3.oas.annotations.Hidden;

/*
  No testing has been done for this controller yet
 */

@RestController
@RequestMapping("/v1")
@Hidden
public class BannedUserController {
    private final BannedUserService bannedUserService;
    private final ModelMapper modelMapper = new ModelMapper();

    public BannedUserController(BannedUserService bannedUserService) {
        this.bannedUserService = bannedUserService;
    }

    @PostMapping("/users/bans")
    public ResponseEntity<Object> addNewBannedUser(@RequestBody BannedUserDTO bannedUserDTO) {
        BannedUser bannedUser = modelMapper.map(bannedUserDTO, BannedUser.class);
        bannedUserService.addBannedUser(bannedUser);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/users/bans/{bannedUserId}")
    public ResponseEntity<String> deleteBannedUser(@PathVariable(name = "bannedUserId") UUID bannedUserId) {
        bannedUserService.deleteBannedUser(bannedUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/bans/{bannedUserId}")
    public ResponseEntity<BannedUserDTO> getBannedUser(@PathVariable(name = "bannedUserId") UUID bannedUserId) {
        BannedUser bannedUser = bannedUserService.getBannedUser(bannedUserId);
        BannedUserDTO bannedUserDTO = modelMapper.map(bannedUser, BannedUserDTO.class);
        return ResponseEntity.ok(bannedUserDTO);
    }

    @PutMapping("/users/bans/{bannedUserId}")
    public ResponseEntity<String> updateBannedUser(@PathVariable(name = "bannedUserId") UUID bannedUserId,
            BannedUserDTO bannedUserDTO) {
        BannedUser bannedUser = modelMapper.map(bannedUserDTO, BannedUser.class);
        bannedUser.setUserId(bannedUserId);
        bannedUserService.updateBannedUser(bannedUser);
        return ResponseEntity.noContent().build();
    }
}
