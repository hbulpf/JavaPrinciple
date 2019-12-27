package dev.lpf.demo.basic.enums;

public class EnumTest {
    public static void main(String[] args) {
        System.out.println(ErrorCategory.BusinessChange.id + "==>" +ErrorCategory.BusinessChange.content);
        System.out.println(ErrorCategory.BusinessChange.id + "==>" +ErrorCategory.BusinessChange.content);
        System.out.println(ErrorCategory.fromId(0).content);
    }
}
