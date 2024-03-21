package models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListUsersResponseModel {
    int page;
    List<Data> user;
    Support support;
}
