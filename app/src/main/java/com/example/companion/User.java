package com.example.companion;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
@Getter
@AllArgsConstructor
public class User {
    @NonNull private String login;
    @NonNull private Integer password;
}
