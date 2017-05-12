package com.usmanhussain.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WebDriverDiscovery extends EventFiringWebDriver {

    public static RemoteWebDriver remoteWebDriver = setDriver();

    public WebDriverDiscovery() {
        super(remoteWebDriver);
    }

    public static RemoteWebDriver getRemoteWebDriver() {
        return remoteWebDriver;
    }

    private static RemoteWebDriver setDriver() {
        switch (System.getProperty("driverType")) {
            case "firefox":
                return new FirefoxDriver();
            case "ie":
                return new InternetExplorerDriver();
            case "chrome":
                return new ChromeDriver();
            case "saucelabs":
                //SauceLabsDriver.startSauceConnect();
                if (getPlatform().contains("iOS") || getPlatform().contains("android")) {
                    return new SauceLabsDriver(getPlatform(), getBrowserName(), getAppiumVersion(), getDeviceName(), getDeviceOrientation(), getPlatformVersion());
                } else {
                    return new SauceLabsDriver(getPlatform(), getBrowserName(), getBrowserVersion());
                }
            default:
                ArrayList<String> cliArgsCap = new ArrayList<String>();
                DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
                cliArgsCap.add("--web-security=false");
                cliArgsCap.add("--ssl-protocol=any");
                cliArgsCap.add("--ignore-ssl-errors=true");
                capabilities.setCapability("takesScreenshot", true);
                capabilities.setCapability(
                        PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
                capabilities.setCapability(
                        PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                        new String[] { "--logLevel=2" });
                return new PhantomJSDriver(capabilities);
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