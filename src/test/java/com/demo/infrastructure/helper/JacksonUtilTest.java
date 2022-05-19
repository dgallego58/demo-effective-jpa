package com.demo.infrastructure.helper;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class JacksonUtilTest {

    public static void main(String[] args) {
        class Cat {
            private String ultraNestedLocalProperty;
        }
    }

    @Test
    void name() {
        Consumer<Person> fp = person -> person.setName(person.getName() + "-added");
        Stream.of(new Person("1"), new Person("2"))
              .peek(fp)
              .forEach(System.out::println);

        var empty = Stream.of("one", "two", "three", "four")
                          .filter(e -> e.length() > 3)
                          .peek(e -> System.out.println("Filtered value: " + e))
                          .map(String::toUpperCase)
                          .peek(e -> System.out.println("Mapped value: " + e))
                          .collect(Collectors.toList());
        assertFalse(empty.isEmpty());
    }

    static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public Person setName(String name) {
            this.name = name;
            return this;
        }
    }

}
