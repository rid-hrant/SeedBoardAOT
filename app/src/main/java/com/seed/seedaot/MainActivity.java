package com.seed.seedaot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private static final String GPIO_NAME = "BCM23";

    private Gpio mGpio;

    private GpioCallback mGpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            // Read the active low pin state
            try {
                if (gpio.getValue()) {
                    // Pin is HIGH
                    Log.e("MyLOG", gpio + ": GPIO is HIGH");
                } else {
                    // Pin is LOW
                    Log.e("MyLOG", gpio + ": GPIO is LOW");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Continue listening for more interrupts
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.e("MyLOG", gpio + ": Error event " + error);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PeripheralManager manager = PeripheralManager.getInstance();
            mGpio = manager.openGpio(GPIO_NAME);
        } catch (IOException e) {
            Log.e("MyLOG", "Unable to access GPIO", e);
        }

        try {
            mGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOutputHigh(mGpio);
    }

    public void configureInput(Gpio gpio) throws IOException {
        // Initialize the pin as an input
        gpio.setDirection(Gpio.DIRECTION_IN);
        // High voltage is considered active
        gpio.setActiveType(Gpio.ACTIVE_HIGH);

        // Register for all state changes
        gpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
        gpio.registerGpioCallback(mGpioCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Begin listening for interrupt events
        try {
            mGpio.registerGpioCallback(mGpioCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Interrupt events no longer necessary
        mGpio.unregisterGpioCallback(mGpioCallback);
    }

    public void InitializeThePinAsHighOutput(Gpio gpio) throws IOException {
        gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
    }

    public boolean setOutputHigh(Gpio gpio) {
        try {
            gpio.setActiveType(Gpio.ACTIVE_HIGH);
            gpio.setValue(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setOutputLow(Gpio gpio) {
        try {
            gpio.setActiveType(Gpio.ACTIVE_LOW);
            gpio.setValue(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mGpio != null) {
            try {
                mGpio.close();
                mGpio = null;
            } catch (IOException e) {
                Log.e("MyLOG", "Unable to close GPIO", e);
            }
        }
    }
}
