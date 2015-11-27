package com.lym.trample.generator.impl;

import android.graphics.Rect;

import com.lym.trample.bean.Square;
import com.lym.trample.generator.BaseSquareGenerator;
import com.lym.trample.generator.SquareGeneratorConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultSquareGenerator extends BaseSquareGenerator{

    @Override
    protected List<Square> generateSquareList(SquareGeneratorConfiguration config) {

        Rect rect = config.getRect();
        int width = config.getWidth();
        int height = config.getHeight();
        int num = config.getNum();
        int count = 0;
        long totalTryCount = 5 * num;
        List<Square> squareList = new ArrayList<Square>();
        int i = 0;
        while(count < num && i < totalTryCount) {
            Square square = generateOne(rect, width, height);
            if(square != null && !hasConflict(squareList, square, width)) {
                squareList.add(square);
                count++;
            }
            i++;
        }
        return squareList;
    }

    private Square generateOne(Rect r, int width, int height) {
        Random random = new Random();
        int y = random.nextInt(r.bottom - r.top) + r.top;
        int xNum = (r.right - r.left) / width;
        int x = random.nextInt(xNum) * width;

        Square square = new Square();
        square.setStartX(x);
        square.setStartY(y);
        square.setEndX(x + width);
        square.setEndY(y + height);

        return square;
    }

    private boolean hasConflict(List<Square> squareList, Square square, int width) {
        for(Square item : squareList) {
            if(item.isIntersection(square)) {
                return true;
            } else {
                float intervalWidth = Math.abs(item.getStartX() - square.getStartX());
                if(Math.abs(intervalWidth - width) < 10e-4) {
                    if(item.getEndY() < square.getStartY() && item.getStartY() > square.getEndY()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
