package wireless.libs.bean.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carey on 2016/5/31 0031.
 */
public class BasePagerVo<T> implements Serializable {
    private long index;
    private int size;
    private long total;
    private long pages;
    private List<T> data;

    public List<T> getData() {
        if(data == null ){
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

}
