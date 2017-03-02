package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stephen on 2017/2/26.
 */

public class JokeList implements Serializable {
    public List<JokeVo> infos;
    public int hasmore;

    public class JokeVo {
        public int id;
        public int seq;
        public String content;
        public int heart;
        public int bad;
        public boolean isLikeClick;
        public boolean isUnlikeClick;
    }
}
