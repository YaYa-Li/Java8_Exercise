

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

/**
 *  IStream.of(col) => op1 = new Operation()
 *          .map(x -> x * 2) => op1.map(x -> x * 2) => op2 = new Operation()
 *          .map(x -> new String(x)) => op2.map(..) => op3 = new Operation()
 *          .collect() => this = op3
 *
 *   op1(level = 0) <=> op2(level = 1) <=> op3(level = 2)
 */



public interface IStream<T> {

    static <T> IStream<T> of(Collection<T> col) {
        //Head
        return new Operation<T, T>(col) {
            //@Override
            ISink<T> onWrapSink(ISink<T> downstreamSink) {
                return val -> downstreamSink.accept(val);
            }
        };
    }

    <R> IStream<R> map(Function<T, R> mapper);

    IStream<T> filter(Predicate<T> predicate);

    <R> R collect(Supplier<R> supplier, BiConsumer<R, T> accumulator);

    <E> E reduce(E identify, BiFunction<E,T,E> accumulator);

    IStream<T> distinct();
    //<R> R collect2(Supplier<R> supplier, BiConsumer<R, T> accumulator);
    IStream<T>  sorted(Comparator<T> cpt);
}

class TestIStream {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(3);
        l.add(2);
        l.add(2);
        l.add(1);
        IStream<Integer> stream = IStream.of(l);

        l.add(2);
        l.add(5);
        //List<String> ans = stream.map(x -> x * 2).filter(x -> x < 5).map(x -> String.valueOf(x)).collect(() -> new ArrayList(), (res, val) -> res.add(val));

        // List<String> ans = stream.collect(() -> new ArrayList(), (res, val) -> res.add(val));

        //int ans = stream.reduce(0, (res,val)-> res + val);
        //List<Integer> ans = stream.distinct().collect(() -> new ArrayList(), (res, val) -> res.add(val));
        //System.out.println(ans);
        List<String> sortedAns = stream.sorted((v1, v2) -> v1.compareTo(v2)).collect(() -> new ArrayList(), (res, val) -> res.add(val));
        //System.out.println(sortedAns);

        int ReducedAns = stream.distinct().reduce(0, (res, val) -> res + val);
        System.out.println(sortedAns);
        System.out.println("---");
        System.out.println(ReducedAns);


    }
}



