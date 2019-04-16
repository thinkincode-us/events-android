package com.thinkincode.events_android;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.events_android.view.LoginActivity;
import com.thinkincode.events_android.view.Register;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewMode;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public  class ExampleInstrumentedTest {
    @Test
    public void checkEmail() {
        Register test = mock(Register.class);

        when(test.isValidEmail("isgori2@hotmail.com")).thenReturn(true);

        assertEquals(test.isValidEmail("isgori2"), false);
    }

    @Test
    public void checkPassword() {
        Register.PasswordValidator test = mock(Register.PasswordValidator.class);
        //mock(Register.PasswordValidator.class);
        when(test.validate2("Abc123$%")).thenReturn(true);

        assertEquals(test.validate2("Abc123asss"), false);
    }

    @Test
    public void checkLoginBackground() {

    }
}
