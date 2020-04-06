package com.example.companion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
@Getter
@AllArgsConstructor
public class Rating {
    @NonNull private String user;
    @NonNull private String login;
    @NonNull private int rating;
}
