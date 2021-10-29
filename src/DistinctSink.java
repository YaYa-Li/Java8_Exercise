import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DistinctSink<T> implements ISink<T>{
    private final ISink<T> downstream;
    private HashSet<T> set;
    //private List<T> list;

    public DistinctSink(ISink<T> downstream) {
        this.downstream = downstream;
    }

    @Override
    public void begin(long size) {
        set = new HashSet<T>();
        //  list = new ArrayList<>();
    }


    @Override
    public void end() {

        downstream.begin(-1);
        set.forEach(t -> downstream.accept(t));
        downstream.end();
    }



    @Override
    public void accept(T t) {
        set.add(t);
    }
}