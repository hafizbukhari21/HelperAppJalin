package com.jalin.helper.util;

import java.io.InputStream;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;

public class StringToBinaryInputStreamUtil extends BaseNCodecInputStream{

  public StringToBinaryInputStreamUtil(InputStream paramInputStream) {
	    super(paramInputStream, (BaseNCodec)new Base64(true), false);
  }
  
}

