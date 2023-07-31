package com.target.ready.library.system.entity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @JsonProperty("user_id")
    @JsonAlias("userId")
    private int userId;


    @JsonProperty("user_name")
    @JsonAlias("userName")
    private String userName;
}
