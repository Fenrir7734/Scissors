package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.screenshot.Screenshot;

public interface ControllerMediator {
    void registerFavoriteInputWindowController(FavoriteInputWindowController controller);
    void registerSettingsWindowController(SettingsWindowController controller);
    void registerMainWindowController(MainWindowController controller);
    void registerScreenCaptureWindowController(ScreenCaptureWindowController controller);
    void receiveScreenshot(Screenshot screenshot);
    void receiveFavoriteList();
}
