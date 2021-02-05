package com.example.user.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdverRequest
{
    private String adver_name;
    private String adver_image;
    private String adver_price;
    private String adver_city;
    private String adver_detail;
    private String adver_owner_name;
    private String adver_owner_mail;
    private String adver_owner_phone;
    private String type_id;
}
