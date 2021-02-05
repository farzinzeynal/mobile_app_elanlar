package com.example.user.models;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdverModel
{
    private String id;
    private String adver_name;
    private String adver_price;
    private String adver_date;
    private String adver_number;
    private String adver_city;
    private String adver_detail;
    private boolean is_liked;
    private String adver_image;
    private String owner_name;
    private String owner_mail;
    private String owner_phone;
    private String updated_at;
    private String adver_type;
    private Long type_id;
}
