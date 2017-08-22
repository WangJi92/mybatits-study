package com.common.utils.Hibernatevalidatedemo.spring;

/**
 * descrption: 男女
 * authohr: wangji
 * date: 2017-08-12 15:33
 */
public enum Gender {
    Woman("女人",1),
    Man("男人",2),
    Neutrality("中立",3);
    private String F;
    private Integer value;

    Gender(String f, Integer value) {
        F = f;
        this.value = value;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
