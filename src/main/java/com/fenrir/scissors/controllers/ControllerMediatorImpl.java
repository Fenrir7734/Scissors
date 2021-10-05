package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.screenshot.Screenshot;

public class ControllerMediatorImpl implements ControllerMediator {
    private static ControllerMediatorImpl instance;

    private FavoriteInputWindowController favoriteInputWindowController;
    private SettingsWindowController settingsWindowController;
    private MainWindowController mainWindowController;
    private ScreenCaptureWindowController screenCaptureWindowController;

    @Override
    public void registerFavoriteInputWindowController(FavoriteInputWindowController controller) {
        favoriteInputWindowController = controller;
    }

    @Override
    public void registerSettingsWindowController(SettingsWindowController controller) {
        settingsWindowController = controller;
    }

    @Override
    public void registerMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    @Override
    public void registerScreenCaptureWindowController(ScreenCaptureWindowController controller) {
        screenCaptureWindowController = controller;
    }

    @Override
    public void receiveScreenshot(Screenshot screenshot) {
        mainWindowController.receiveScreenshot(screenshot);
    }

    @Override
    public void receiveFavoriteList() {
        settingsWindowController.refreshFavorite();
        mainWindowController.refreshFavorites();
    }

    private ControllerMediatorImpl() {}

    static ControllerMediatorImpl getInstance() {
        if(instance == null) {
            instance = new ControllerMediatorImpl();
        }
        return instance;
    }
}
