package com.edgesdk.models;

public class TickerNotifications {
    String message;
    int timeForMessageToStay,timeForTickerToAppear;
    boolean isThisMsgAllowedToRepeat,isThisMsgAlreadyDisplayed,isTickerMustToAppear,isTempWalletForwarded;

    public TickerNotifications(String message, int timeForMessageToStay, int timeForTickerToAppear, boolean isThisMsgAllowedToRepeat, boolean isThisMsgAlreadyDisplayed,boolean isTickerMustToAppear,boolean isTempWalletForwarded) {
        this.message = message;
        this.timeForMessageToStay = timeForMessageToStay;
        this.timeForTickerToAppear = timeForTickerToAppear;

        this.isThisMsgAllowedToRepeat = isThisMsgAllowedToRepeat;
        this.isThisMsgAlreadyDisplayed = isThisMsgAlreadyDisplayed;
        this.isTickerMustToAppear=isTickerMustToAppear;
        this.isTempWalletForwarded=isTempWalletForwarded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimeForMessageToStay() {
        return timeForMessageToStay;
    }

    public void setTimeForMessageToStay(int timeForMessageToStay) {
        this.timeForMessageToStay = timeForMessageToStay;
    }

    public int getTimeForTickerToAppear() {
        return timeForTickerToAppear;
    }

    public void setTimeForTickerToAppear(int timeForTickerToAppear) {
        this.timeForTickerToAppear = timeForTickerToAppear;
    }


    public boolean isThisMsgAllowedToRepeat() {
        return isThisMsgAllowedToRepeat;
    }

    public void setThisMsgAllowedToRepeat(boolean thisMsgAllowedToRepeat) {
        isThisMsgAllowedToRepeat = thisMsgAllowedToRepeat;
    }

    public boolean isThisMsgAlreadyDisplayed() {
        return isThisMsgAlreadyDisplayed;
    }

    public void setThisMsgAlreadyDisplayed(boolean thisMsgAlreadyDisplayed) {
        isThisMsgAlreadyDisplayed = thisMsgAlreadyDisplayed;
    }

    public boolean isTickerMustToAppear() {
        return isTickerMustToAppear;
    }

    public void setTickerMustToAppear(boolean tickerMustToAppear) {
        isTickerMustToAppear = tickerMustToAppear;
    }

    public boolean isTempWalletForwarded() {
        return isTempWalletForwarded;
    }

    public void setTempWalletForwarded(boolean tempWalletForwarded) {
        isTempWalletForwarded = tempWalletForwarded;
    }
}
