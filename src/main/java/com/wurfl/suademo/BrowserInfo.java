package com.wurfl.suademo;

import java.util.List;

public class BrowserInfo {
    private List<Browser> browsers;
    private Platform platform;
    private String mobile;
    private String model;

    private String bitness;

    // Getters and Setters
    List<Browser> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(List<Browser> browsers) {
        this.browsers = browsers;
    }

    public void setBitness(String bitness) {
        this.bitness = bitness;
    }

    public String getBitness() {
        return bitness;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}