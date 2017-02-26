package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.DefHeadFemaleVo;
import wireless.libs.bean.vo.DefHeadMaleVo;

/**
 * Created by stephen on 2017/2/25.
 */

public class DefHeadList implements Serializable {
    public List<DefHeadMaleVo> male;
    public List<DefHeadFemaleVo> female;
}
