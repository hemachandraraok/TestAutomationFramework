package com.usmanhussain.framework;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class WebDriverDiscovery extends EventFiringWebDriver {

    public static RemoteWebDriver remoteWebDriver = setDriver();
    public static BrowserMobProxy server;

    public WebDriverDiscovery() {
        super(remoteWebDriver);
    }

    public static RemoteWebDriver getRemoteWebDriver() {
        return remoteWebDriver;
    }

    private static RemoteWebDriver setDriver() {
        server = new BrowserMobProxyServer();
        server.enableHarCaptureTypes(CaptureType.getRequestCaptureTypes());
        server.enableHarCaptureTypes(CaptureType.getResponseCaptureTypes());
        server.start();
        switch (System.getProperty("driverType")) {
            case "firefox":
                return new FirefoxDriver();
            case "ie":
                return new InternetExplorerDriver();
            case "chrome":
                return new ChromeDriver();
            case "saucelabs":
                if (getPlatform().contains("iOS") || getPlatform().contains("android")) {
                    return new SauceLabsDriver(getPlatform(), getBrowserName(), getAppiumVersion(), getDeviceName(), getDeviceOrientation(), getPlatformVersion());
                } else {
                    return new SauceLabsDriver(getPlatform(), getBrowserName(), getBrowserVersion());
                }
            case "docker":
                try {
                    return new BrowserWebDriverContainer()
                            .withDesiredCapabilities(DesiredCapabilities.chrome())
                            .withRecordingMode(RECORD_ALL, new File("target")).getWebDriver();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            default:
                Proxy seleniumProxy = ClientUtil.createSeleniumProxy(server);
                ArrayList<String> cliArgsCap = new ArrayList<String>();
                cliArgsCap.add("--webdriver-loglevel=NONE");
                cliArgsCap.add("--web-security=false");
                cliArgsCap.add("--ssl-protocol=any");
                cliArgsCap.add("--ignore-ssl-errors=true");
                return new PhantomJSDriver();
        }
    }

    public static String getPlatform() {
        return System.getProperty("platform");
    }

    public static String getBrowserVersion() {
        return System.getProperty("browserVersion");
    }

    public static String getBrowserName() {
        return System.getProperty("browserName");
    }

    public static String getAppiumVersion() {
        return System.getProperty("appiumVersion");
    }

    public static String getDeviceName() {
        return System.getProperty("deviceName");
    }

    public static String getDeviceOrientation() {
        return System.getProperty("deviceOrientation");
    }

    public static String getPlatformVersion() {
        return System.getProperty("platformVersion");
    }

    public WebDriver getDriver() {
        return remoteWebDriver;
    }

    @Override
    public void get(String s) {
    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public void quit() {
    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }
}
