package com.cc;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	@Test
	public void testShowsHello() throws Exception {
		String hello = new MainActivity().getResources().getString(R.string.hello_world);
		assertThat(hello, equalTo("Hello world!"));
	}
}
