package models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListUsersResponseModel {
    int page, per_page, total, total_pages;
    List<Data> data;
    Support support;
}
