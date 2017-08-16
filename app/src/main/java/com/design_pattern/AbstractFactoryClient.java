package com.design_pattern;

/**
 * Created by cwj on 17/1/11.
 * 抽象工厂模式
 */
public class AbstractFactoryClient {

    public static void main(String[] args) {
        //首都球队比赛
        CountryMatchFactory factory1 = new CapitalMatchFactory();
        introduceBeforeMatch(factory1.createSpainClub(), factory1.createEnglandClub());
        //冠军球队比赛
        System.out.println("------");
        CountryMatchFactory factory2 = new ChampionMatchFactory();
        introduceBeforeMatch(factory2.createSpainClub(), factory2.createEnglandClub());
    }

    public static void introduceBeforeMatch(Club club1, Club club2) {
        club1.introduce();
        club2.introduce();
    }
}

//两个国家间俱乐部比赛的工厂
abstract class CountryMatchFactory {

    protected abstract SpainClub createSpainClub();

    protected abstract EnglandClub createEnglandClub();
}

//首都球队之间比赛
class CapitalMatchFactory extends CountryMatchFactory {

    @Override
    protected SpainClub createSpainClub() {
        return new RealMardrid();
    }

    @Override
    protected EnglandClub createEnglandClub() {
        return new Arsenal();
    }
}

//冠军之间比赛
class ChampionMatchFactory extends CountryMatchFactory {

    @Override
    protected SpainClub createSpainClub() {
        return new Barcelona();
    }

    @Override
    protected EnglandClub createEnglandClub() {
        return new Chelsea();
    }
}

abstract class Club {

    final public void introduce() {
        System.out.println("我们是来自" + getCountry() + "的" + getName() + "队");
    }

    protected abstract String getCountry();

    protected abstract String getName();
}

abstract class SpainClub extends Club {

    @Override
    protected String getCountry() {
        return "西班牙";
    }
}

abstract class EnglandClub extends Club {

    @Override
    protected String getCountry() {
        return "英格兰";
    }
}

class Barcelona extends SpainClub {

    @Override
    protected String getName() {
        return "巴塞罗那";
    }
}

class RealMardrid extends SpainClub {

    @Override
    protected String getName() {
        return "皇家马德里";
    }
}

class Chelsea extends EnglandClub {

    @Override
    protected String getName() {
        return "切尔西";
    }
}

class Arsenal extends EnglandClub {

    @Override
    protected String getName() {
        return "阿森纳";
    }
}