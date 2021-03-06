package com.example.companion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @NonNull private String user;
    @NonNull private String login;
    private int rating;
}
