
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.*;
//import java.util.stream.*;

public abstract class AbstractPipeline<IN, OUT> implements IStream<OUT> {

    private Supplier<Iterator> sourceIterator;
    private AbstractPipeline upstream;
    private int level;

    public AbstractPipeline(Collection<OUT> source) {
        this.sourceIterator = () -> source.iterator();
    }

    public AbstractPipeline(AbstractPipeline<?, IN> upstream) {
        this.upstream = upstream;
        this.sourceIterator = upstream.sourceIterator;
        this.level = upstream.level + 1;
    }

    @Override
    public abstract <R> IStream<R> map(Function<OUT, R> mapper);

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, OUT> accumulator) {
        R collectionResult = supplier.get();
        ISink head = wrapSink(val -> accumulator.accept(collectionResult, val));
        head.begin(-1);
        Iterator<OUT> itr = this.sourceIterator.get();
        while(itr.hasNext()) {
            OUT val = itr.next();
            //System.out.println(val);
            head.accept(val);
        }
        head.end();
        return collectionResult;
    }

    @Override

    public <E> E reduce(E identify, BiFunction<E,OUT,E> accumulator){
        // E reduceResult = identify;
        // ISink head = wrapSink(val -> accumulator.accept(val) );
        ISink.Box<OUT,E> e = new ISink.Box<OUT, E>() {
            @Override
            public void begin(long size) {
                state = identify;
            }

            @Override
            public void accept(OUT out) {
                state = accumulator.apply(state, out);
            }


        };

        ISink head = wrapSink(e);

        Iterator<OUT> itr = this.sourceIterator.get();
        head.begin(-1);
        while(itr.hasNext()) {
            OUT val = itr.next();
            // System.out.println(val);
            head.accept(val);
        }
        head.end();

        return e.get();

    }



//    @Override
//    public <E> E reduce(Supplier<R> supplier, BiConsumer<OUT,E> accumulator){
//        E element = identify;
//        Isink head = wrapSink(val -> accumulator.accept(,val));
//        return element;
//    }



    /**
     *  IStream.of(col) => op1 = new Operation()
     *          .map(x -> x * 2) => op1.map(x -> x * 2) => op2 = new Operation()
     *          .map(x -> new String(x)) => op2.map(..) => op3 = new Operation()
     *          .collect() => this = op3
     *
     *   op1(level = 0) <=> op2(level = 1) <=> op3(level = 2)     -> collect()
     *                                           this = op3
     *      sink1      ->     sink2(mapper) ->    sink3(mapper)  ->   sink4(collect)
     *
     *      downstream = sink4
     *      return new sink = sink3 {sink4.accept(val)}
     *      ISink<OUT> onWrapSink(ISink<R> downstreamSink) {
     *                 return new ISink<OUT>() {
     *                     @Override
     *                     public void accept(OUT val) {
     *                         R r = mapper.apply(val);
     *                         downstreamSink.accept(r);
     *                     }
     *                 };
     *             }
     *      1. stream -> op1 -> op2 -> op3
     *      2. terminal op3 sink3 -> op2 sink2 -> op1 sink1
     *      3. iterator -> sink1 -> sink2 -> sink3
     */
    private ISink<?> wrapSink(ISink<OUT> sink) {
        for(AbstractPipeline p = this; p.level > 0; p = p.upstream) {
            //sink3 = op3.onWrapSink(sink4)
            sink = p.onWrapSink(sink);
        }
        return sink;
    }




    abstract ISink<IN> onWrapSink(ISink<OUT> sink);

}