package edu.nju.mook.app.model;

import edu.nju.mook.sys.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRecord extends BaseModel{
    private String workerId;
    private Date checkinDate;
}
