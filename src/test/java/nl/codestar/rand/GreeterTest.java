package nl.codestar.rand;

import org.junit.Test;

public class GreeterTest {

    private final Greeter greeter = new Greeter();

    @Test
    public void greeterSaysHello() {
        assert(greeter.sayHi().contains("Hello"));
    }

}
