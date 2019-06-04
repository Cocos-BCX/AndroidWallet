package com.cocos.library_base.widget.zloading;


import com.cocos.library_base.widget.zloading.ball.ElasticBallBuilder;
import com.cocos.library_base.widget.zloading.ball.InfectionBallBuilder;
import com.cocos.library_base.widget.zloading.ball.IntertwineBuilder;
import com.cocos.library_base.widget.zloading.circle.DoubleCircleBuilder;
import com.cocos.library_base.widget.zloading.circle.PacManBuilder;
import com.cocos.library_base.widget.zloading.circle.RotateCircleBuilder;
import com.cocos.library_base.widget.zloading.circle.SingleCircleBuilder;
import com.cocos.library_base.widget.zloading.circle.SnakeCircleBuilder;
import com.cocos.library_base.widget.zloading.clock.CircleBuilder;
import com.cocos.library_base.widget.zloading.clock.ClockBuilder;
import com.cocos.library_base.widget.zloading.path.MusicPathBuilder;
import com.cocos.library_base.widget.zloading.path.SearchPathBuilder;
import com.cocos.library_base.widget.zloading.path.StairsPathBuilder;
import com.cocos.library_base.widget.zloading.rect.ChartRectBuilder;
import com.cocos.library_base.widget.zloading.rect.StairsRectBuilder;
import com.cocos.library_base.widget.zloading.star.LeafBuilder;
import com.cocos.library_base.widget.zloading.star.StarBuilder;
import com.cocos.library_base.widget.zloading.text.TextBuilder;


public enum Z_TYPE
{
    CIRCLE(CircleBuilder.class),
    CIRCLE_CLOCK(ClockBuilder.class),
    STAR_LOADING(StarBuilder.class),
    LEAF_ROTATE(LeafBuilder.class),
    DOUBLE_CIRCLE(DoubleCircleBuilder.class),
    PAC_MAN(PacManBuilder.class),
    ELASTIC_BALL(ElasticBallBuilder.class),
    INFECTION_BALL(InfectionBallBuilder.class),
    INTERTWINE(IntertwineBuilder.class),
    TEXT(TextBuilder.class),
    SEARCH_PATH(SearchPathBuilder.class),
    ROTATE_CIRCLE(RotateCircleBuilder.class),
    SINGLE_CIRCLE(SingleCircleBuilder.class),
    SNAKE_CIRCLE(SnakeCircleBuilder.class),
    STAIRS_PATH(StairsPathBuilder.class),
    MUSIC_PATH(MusicPathBuilder.class),
    STAIRS_RECT(StairsRectBuilder.class),
    CHART_RECT(ChartRectBuilder.class),;

    private final Class<?> mBuilderClass;

    Z_TYPE(Class<?> builderClass)
    {
        this.mBuilderClass = builderClass;
    }

    <T extends ZLoadingBuilder> T newInstance()
    {
        try
        {
            return (T) mBuilderClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
