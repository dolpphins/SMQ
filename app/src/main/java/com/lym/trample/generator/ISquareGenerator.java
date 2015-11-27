package com.lym.trample.generator;

import com.lym.trample.bean.Square;

import java.util.List;

public interface ISquareGenerator {

    List<Square> generate(SquareGeneratorConfiguration config);
}
