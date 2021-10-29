import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


@FunctionalInterface
public interface ISink<T> {
    default void begin(long size) { }
    default void end(){ };
    default boolean cancellation() { return false; };

    void accept(T t);


    abstract static class Box<T,E> implements ISink<T>{
        E state;
        public E get(){
            return state;
        }
    }

    abstract static class sortedSink<T,R> implements ISink<T>{
        private final ISink<R> downstream;

        public sortedSink(ISink<R> downstream){
            this.downstream = downstream;
        }

        @Override
        public void begin(long size) {
            downstream.begin(size);
        }

        @Override
        public void end() {
            downstream.end();
        }
    }


}