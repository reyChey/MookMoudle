package edu.nju.mook.app.model;

import edu.nju.mook.sys.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit extends BaseModel{
    private String memberId;
    private int depositMoney;
    private int grandMoney;
    private Date depositDate;
    private int remain_balance;
}
