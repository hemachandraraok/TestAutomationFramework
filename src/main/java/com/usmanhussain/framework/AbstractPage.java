package com.usmanhussain.framework;

import com.google.common.base.Function;
import com.usmanhussain.exception.PageOperationException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPage {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPage.class);

    public static RemoteWebDriver getDriver;

    private static final int DRIVER_WAIT_TIME = 15;

    private static final int debugWait = 1000;

    public HashMap<String, WebElement> commonElements = new HashMap<>();

    public AbstractPage() {
        WebDriverDiscovery webDriverDiscovery = new WebDriverDiscovery();
        getDriver = WebDriverDiscovery.getRemoteWebDriver();
    }

    public void deleteCookies() {
        getDriver.manage().deleteAllCookies();
    }

    public WebElement findElement(By by) {
        return getDriver.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return getDriver.findElements(by);
    }

    public WebElement waitForUnstableElement(By by) {
        try {
            Thread.sleep(debugWait);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
        return waitForElementPresent(by);
    }

    public WebElement waitForElementPresent(final By by) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME);
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (UnhandledAlertException e) {
            getDriver.switchTo().alert().dismiss();
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (NoAlertPresentException e) {
            getDriver.switchTo().alert().dismiss();
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        }
    }

    public WebElement waitForElementPresent(final By by, final By sub_by) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME);
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(by, sub_by));
        } catch (UnhandledAlertException e) {
            getDriver.switchTo().alert().dismiss();
            return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(by, sub_by));
        } catch (NoAlertPresentException e) {
            getDriver.switchTo().alert().dismiss();
            return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(by, sub_by));
        }
    }

    public List<WebElement> waitForElementsPresent(final By by) {
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME, 100);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            return findElements(by);
        } catch (TimeoutException e) {
            return findElements(by);
        }
    }

    public WebElement waitForElementToBeClickableAndReturnElement(final By by) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME, 100);
        wait.until(ExpectedConditions.elementToBeClickable(by));
        return getDriver.findElement(by);
    }

    public WebElement waitForExpectedElement(final By by, int timeout) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        Wait<WebDriver> wait = new WebDriverWait(getDriver, timeout);
        wait.until(visibilityOfElementLocated(by));
        return getDriver.findElement(by);
    }

    public WebElement waitAndFindElement(By by) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        return waitForExpectedElement(by, 5);
    }

    public WebElement findEnabledElement(By by) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        return waitForElementPresent(by);
    }

    public void waitForElementEnabled(final By by) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementVisible(final By by) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    Thread.sleep(debugWait);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage());
                }
                ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
                WebElement element = getDriver.findElement(by);
                return element.isDisplayed() ? element : null;
            }
        };
    }

    private static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        return new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver getDriver) {
                return getDriver.findElement(locator);
            }
        };
    }

    /**
     * This method does the try and find the element for the 3 times repeating statements if element is not getting found at a time.
     * And based on the given locator element will be found and return as webelement.
     *
     * @param by
     * @return WebElement
     */
    protected WebElement retryingFindElement(By by) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                return waitForExpectedElement(by, 2);
            } catch (Exception te) {
                LOG.warn("Exceeding time to find element in retryingFindElement(): " + by, te.getMessage());
            }
            attempts++;
        }
        throw new PageOperationException("Unable to find the element:" + by);
    }

    /**
     * This method does the try and find the element for the 3 times repeating statements if element is not getting found at a time.
     * And based on the given locator element will be found and return as webelement.
     *
     * @param by
     * @return WebElement
     */
    protected List<WebElement> retryingFindElements(By by) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                return waitForElementsVisible(by, 10);
            } catch (Exception te) {
                LOG.warn("Exceeding time to find elements in retryingFindElements(): " + by, te.getMessage());
            }
            attempts++;
        }
        throw new PageOperationException("Unable to find the elements:" + by);
    }

    public List<WebElement> waitForElementsVisible(final By by, int timeout) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver, timeout);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public void waitForMoreTime() {
        try {
            Thread.sleep(debugWait);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }

    public static WebElement findElementUsingFluentWait(final By by, final int timeoutSeconds) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver)
                .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver webDriver) {
                return getDriver.findElement(by);
            }
        });
    }

    public Boolean waitForElementInVisible(final By by) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver, DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public void retryingClick(By by) {
        int attempts = 0;
        while (attempts < 30) {
            try {
                ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
                getDriver.findElement(by).click();
                break;
            } catch (Exception e) {
            }
            attempts++;
        }
    }

    public void retryingElementClick(WebElement element) {
        int attempts = 0;
        while (attempts < 50) {
            try {
                ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
                element.click();
                break;
            } catch (Exception e) {
            }
            attempts++;
        }
    }

    public Object executeScript(String string, WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver;
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            return jse.executeScript(string, element);
        } catch (StaleElementReferenceException e) {
            waitForPageLoad();
            return jse.executeScript(string, element);
        }
    }

    public Object executeScript(String script) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        return ((JavascriptExecutor) getDriver).executeScript(script);
    }

    public String executeReturnScript(String script) {
        ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
        String imgeJs = ((JavascriptExecutor) getDriver).executeScript(script).toString();
        return imgeJs;
    }

    public boolean elementDisplayed(By by) {
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            Wait<WebDriver> wait = new WebDriverWait(getDriver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean elementDisplayed(WebElement wb) {
        try {
            if (wb.isDisplayed()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean elementPresent(By by) {
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            Wait<WebDriver> wait = new WebDriverWait(getDriver, 1);
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean elementClickable(By by) {
        try {
            ((JavascriptExecutor) getDriver).executeScript("window.onbeforeunload = function(e){};");
            Wait<WebDriver> wait = new WebDriverWait(getDriver, 1);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeTabByIndex(int iWindowIndex) {
        Set<String> handles = getDriver.getWindowHandles();
        if (handles.size() > iWindowIndex) {
            String handle = handles.toArray()[iWindowIndex].toString();
            getDriver.switchTo().window(handle);
            getDriver.close();
        }
    }

    public void switchToWindowByIndex(int iWindowIndex) {
        Set<String> handles = getDriver.getWindowHandles();
        if (handles.size() > iWindowIndex) {
            String handle = handles.toArray()[iWindowIndex].toString();
            getDriver.switchTo().window(handle);
        }
    }

    public void switchToPopUpWindow() {
        String parentWindowHandler = getDriver.getWindowHandle();
        String subWindowHandler = null;
        Set<String> handles = getDriver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        getDriver.switchTo().window(subWindowHandler);
    }

    public void switchToFrameById(WebElement wbFrame) {
        getDriver.switchTo().frame(wbFrame);
    }

    public void switchToParentFrame() {
        getDriver.switchTo().parentFrame();
    }

    public void switchToFrameByIndex(int i) {
        getDriver.switchTo().frame(i);
    }

    public void switchToDefault() {
        getDriver.switchTo().defaultContent();
    }

    public void switchToLastOpenWindow() {
        Set<String> handles = getDriver.getWindowHandles();
        if (handles.size() > 0) {
            String handle = handles.toArray()[handles.size() - 1].toString();
            getDriver.switchTo().window(handle);
        }
    }

    public void btnClick(WebElement btn) {
        ((JavascriptExecutor) getDriver).executeScript("arguments[0].click();", btn);
    }

    public void selectDrop(WebElement webSel, String strText) {
        Select oSelect = new Select(webSel);
        oSelect.selectByVisibleText(strText);
    }

    public WebElement findParentNode(WebElement wbChild) {
        WebElement myParent = (WebElement) executeScript("return arguments[0].parentNode;", wbChild);
        return myParent;
    }

    public void navigateBackward() {
        getDriver.navigate().back();
    }

}
