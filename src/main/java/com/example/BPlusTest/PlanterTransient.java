package com.example.BPlusTest;

public class PlanterTransient {
    private int userId;
    private String temperature;
    private String planterName;//植物名称
    private String envHumidity;//环境湿度
    private String ph;
    private String light;
    private String soilHumidity;//土壤湿度
    private int date;
    private String type;

    private int planterId;
    private int planterUserId;
    private String plantertxID;//交易ID

    @Override
    public String toString(){
        return "{"
                +"planterId:"+planterId+","
                +"planterName:"+planterName+","
                +"planterUserId:"+planterUserId+","
                +"userId:"+userId+","
                +"date:"+date
                +"}";
    }

    public int getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = Integer.valueOf(date);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlanterId() {
        return planterId;
    }

    public void setPlanterId(int planterId) {
        this.planterId = planterId;
    }

    public int getPlanterUserId() {
        return planterUserId;
    }

    public void setPlanterUserId(int planterUserId) {
        this.planterUserId = planterUserId;
    }

    public String getPlantertxID() {
        return plantertxID;
    }

    public void setPlantertxID(String plantertxID) {
        this.plantertxID = plantertxID;
    }

    public String getPlanterName() {
        return planterName;
    }

    public void setPlanterName(String planterName) {
        this.planterName = planterName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getEnvHumidity() {
        return envHumidity;
    }

    public void setEnvHumidity(String envHumidity) {
        this.envHumidity = envHumidity;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(String soilHumidity) {
        this.soilHumidity = soilHumidity;
    }
}
