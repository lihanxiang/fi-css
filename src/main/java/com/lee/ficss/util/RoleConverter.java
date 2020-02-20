package com.lee.ficss.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class RoleConverter {

    public Set<String> convertRolesIntoRoleSet(String roles){
        String[] roleArray = roles.split(";");
        return new HashSet<>(Arrays.asList(roleArray));
    }
}
