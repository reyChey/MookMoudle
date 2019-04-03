package edu.nju.mook.app.model;

import edu.nju.mook.sys.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseModel {
    private String name;
    private String phone;
    private String gender;
    private String address;
    private Date createDate;
    private Date birthday;
    private int balance;
    private int total_deposit;
}
