package services;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public enum Theme { LIGHT, DARK }

    private static Theme currentTheme = Theme.LIGHT;

    private static final Color LIGHT_BG = new Color(240, 240, 240);
    private static final Color LIGHT_FG = new Color(33, 33, 33);
    private static final Color LIGHT_PANEL = Color.WHITE;

    private static final Color DARK_BG = new Color(45, 45, 45);
    private static final Color DARK_FG = new Color(230, 230, 230);
    private static final Color DARK_PANEL = new Color(60, 60, 60);

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static Color getBackground() {
        return currentTheme == Theme.LIGHT ? LIGHT_BG : DARK_BG;
    }

    public static Color getForeground() {
        return currentTheme == Theme.LIGHT ? LIGHT_FG : DARK_FG;
    }

    public static Color getPanelColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_PANEL : DARK_PANEL;
    }
}