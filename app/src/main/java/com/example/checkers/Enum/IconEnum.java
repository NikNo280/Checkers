package com.example.checkers.Enum;

import com.example.checkers.R;

public enum IconEnum {
    WHITE_MAP(R.drawable.white_square, 0),
    BLACK_MAP(R.drawable.black_square, 1),
    WHITE_CHECKER(R.drawable.white_checker,2),
    BLACK_CHECKER(R.drawable.black_checker, 3),
    WHITE_QUEEN(R.drawable.white_queen, 4),
    BLACK_QUEEN(R.drawable.black_queen, 5);

    private final int imageCode;
    private final int mapCode;

    IconEnum(int imageCode,int mapCode) {
        this.imageCode = imageCode;
        this.mapCode = mapCode;
    }

    public int getMapCode() {
        return mapCode;
    }
    public int getImageCode() {
        return imageCode;
    }
    public static int getImageCode(int mapCode){
        switch (mapCode)
        {
            case 0:
                return IconEnum.WHITE_MAP.getImageCode();
            case 1:
                return IconEnum.BLACK_MAP.getImageCode();
            case 2:
                return IconEnum.WHITE_CHECKER.getImageCode();
            case 3:
                return IconEnum.BLACK_CHECKER.getImageCode();
            case 4:
                return IconEnum.WHITE_QUEEN.getImageCode();
            case 5:
                return IconEnum.BLACK_QUEEN.getImageCode();
            default:
                return -13;
        }
    }
}
