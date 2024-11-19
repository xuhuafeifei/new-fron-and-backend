package com.fgbg.common.aspect.strategy;

/**
 * 限流策略
 */
public interface LimiterStrategy {
    /**
     * 判断是否限流
     * @return
     */
    boolean access();
}
