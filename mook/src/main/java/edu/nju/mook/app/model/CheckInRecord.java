package edu.nju.mook.app.model;

import edu.nju.mook.sys.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRecord extends BaseModel{
    private String workerId;
    private String year;
    private String month;
    private int checkTimes;
}
