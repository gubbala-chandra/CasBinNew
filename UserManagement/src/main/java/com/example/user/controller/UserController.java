package com.example.user.controller;

import com.example.user.dto.RoleDto;
import com.example.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/api")
public class UserController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/saveOrUpdateRole", method = {RequestMethod.POST, RequestMethod.PUT}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrUpdateRole(@RequestBody RoleDto roleDto) {
        return roleService.saveOrUpdateRole(roleDto);
    }

    @RequestMapping(value = "/getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoles(@RequestParam(value= "roleId", required= false) Long roleId,
                                      @RequestParam(value="roleName", required = false) String roleName,
                                      @RequestParam(value="description", required= false) String description,
                                      @RequestParam(value="status", required = false) String status,
                                      Pageable pageable) {
        return roleService.getRoles(roleId, roleName, description, status, pageable);
    }

    @RequestMapping(value = "/getRoleById/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoleById(@PathVariable(value= "roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }
}
