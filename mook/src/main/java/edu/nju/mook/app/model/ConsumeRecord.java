package edu.nju.mook.app.model;

import edu.nju.mook.sys.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeRecord extends BaseModel{
    private String memberId;
    private int consumeTotal;
    private int consumeDiscount;
    private Date consumeDate;
}
