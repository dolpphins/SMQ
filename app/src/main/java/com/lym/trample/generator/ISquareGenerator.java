package com.lym.trample.generator;

import com.lym.trample.bean.Square;

import java.util.List;

/**
 * Created by mao on 2015/11/8.
 *
 * 方块产生器接口
 *
 * @author 麦灿标
 */
public interface ISquareGenerator {

    /**
     * 产生方块集合
     *
     * @param config 要产生的方块的配置信息
     *
     * @return 产生成功返回方块集合，失败返回null.
     * */
    List<Square> generate(SquareGeneratorConfiguration config);
}
