package org;

import org.jparsec.Api;

public class Main {
    public static void main(String[] args) {
        System.out.println(Api.any().parseMaybe(Api.input("ab")).get());
    }
}