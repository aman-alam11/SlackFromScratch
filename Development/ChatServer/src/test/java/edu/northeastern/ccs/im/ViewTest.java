package edu.northeastern.ccs.im;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edu.northeastern.ccs.im.view.FrontEnd;

import static org.junit.Assert.assertEquals;

public class ViewTest {
  private ByteArrayOutputStream mByteArrayOutputStream;

  @Before
  public void init() {
    mByteArrayOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(mByteArrayOutputStream));
  }

  @After
  public void deInit() {
    System.setOut(System.out);
  }

  @Test
  public void testNull() {
    FrontEnd frontEnd = FrontEnd.getView();
    frontEnd.sendToView(null);
    assertEquals("", mByteArrayOutputStream.toString());
  }


  @Test
  public void testNewObject(){
    FrontEnd frontEnd = FrontEnd.getView();
    frontEnd.sendToView("");
    assertEquals(System.lineSeparator(), mByteArrayOutputStream.toString());
  }

  @Test
  public void testNormalObject(){
    FrontEnd frontEnd = FrontEnd.getView();
    frontEnd.sendToView("Test String");
    assertEquals("Test String" + System.lineSeparator(), mByteArrayOutputStream.toString());
  }


}
