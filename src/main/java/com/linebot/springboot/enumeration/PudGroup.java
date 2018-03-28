package com.linebot.springboot.enumeration;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * Created by line play on 2017-11-08.
 */
public enum PudGroup {
    A("A", "0", "5"), B("B", "1", "6"), C("C", "2", "7"), D("D", "3", "8"), E("E", "4", "9");

    private String group;
    private String matchNo1;
    private String matchNo2;

    PudGroup(String group, String matchNo1, String matchNo2) {
        this.group = group;
        this.matchNo1 = matchNo1;
        this.matchNo2 = matchNo2;
    }

    public String getGroup() {
        return group;
    }

    public String getMatchNo1() {
        return matchNo1;
    }

    public String getMatchNo2() {
        return matchNo2;
    }

    public static String getGroup(String pudId) {
        for (PudGroup pudGroup : PudGroup.values()) {
            if (pudGroup.matchNo1.equals(pudId.substring(2, 3)) || pudGroup.matchNo2.equals(pudId.substring(2, 3))) {
                return pudGroup.group;
            }
        }

        throw new InvalidParameterException();
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
