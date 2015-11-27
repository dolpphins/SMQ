package com.lym.stamp.generator;

import com.lym.stamp.bean.Square;

import java.util.List;

public interface ISquareGenerator {

    List<Square> generate(SquareGeneratorConfiguration config);
}
