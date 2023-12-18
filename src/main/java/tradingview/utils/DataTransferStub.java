package tradingview.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTransferStub {


    @Getter
    private static String string;
    @Getter
    private static Integer integer;
    @Getter
    private static Object object;


    public static void setString(String string) {
        DataTransferStub.string = string;
    }


    public static void setInteger(Integer integer) {
        DataTransferStub.integer = integer;
    }


    public static void setObject(Object object) {
        DataTransferStub.object = object;
    }


}