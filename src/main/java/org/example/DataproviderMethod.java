package org.example;

import java.util.stream.Stream;

public class DataproviderMethod {
     public static Stream<String> student(){
        return Stream.of("Anh", "Tho", "Thu");
    }
}
