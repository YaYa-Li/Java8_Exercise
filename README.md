# Java_8Exercise

1. Self-defined stream:    Java8_Exercise/src/
2. AOP:    Java8_Exercise/src/AOP/

## Installation

1.Use the fork button to copy the project   
2.Down load zip file and open in IDE.


## Usage

```java

//Stream
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

// AOP
public class AOPExample {
    public static void main(String[] args) {
        EmployeeService es = (EmployeeService) Proxy.newProxyInstance(
                AOPExample.class.getClassLoader(),
                new Class[]{EmployeeService.class},
                new JdkAOPInvocationHandler(new EmployeeServiceImpl1(), new EmployeeAspect())
        );
        int val = es.get();
        //Object val = es.throwException();
        System.out.println(val);
    }
}
```
