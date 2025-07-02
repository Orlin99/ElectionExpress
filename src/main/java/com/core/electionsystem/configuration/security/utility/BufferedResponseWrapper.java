package com.core.electionsystem.configuration.security.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {

  private static final String ERROR_MESSAGE_FOR_ALREADY_CALLED_PRINT_WRITER = "getWriter() Has Already Been Called For This Response";
  private static final String ERROR_MESSAGE_FOR_ALREADY_CALLED_GET_OUTPUT_STREAM = "getOutputStream() Has Already Been Called For This Response";

  private final ByteArrayOutputStream byteArrayOutputStream;
  private ServletOutputStream servletOutputStream;
  private PrintWriter printWriter;

  public BufferedResponseWrapper(HttpServletResponse response) {
    super(response);
    byteArrayOutputStream = new ByteArrayOutputStream();
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (printWriter != null) {
      throw new IllegalStateException(ERROR_MESSAGE_FOR_ALREADY_CALLED_PRINT_WRITER);
    }
    if (servletOutputStream == null) {
      servletOutputStream = new ServletOutputStream() {
        @Override
        public void write(int b) {
          byteArrayOutputStream.write(b);
        }

        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
          // Default Empty Implementation
        }
      };
    }
    return servletOutputStream;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (servletOutputStream != null) {
      throw new IllegalStateException(ERROR_MESSAGE_FOR_ALREADY_CALLED_GET_OUTPUT_STREAM);
    }
    if (printWriter == null) {
      printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, getCharacterEncoding()), true);
    }
    return printWriter;
  }

  public String getCapturedResponseBody() throws IOException {
    flushBuffer();
    return byteArrayOutputStream.toString(getCharacterEncoding());
  }

  @Override
  public void flushBuffer() throws IOException {
    if (printWriter != null) {
      printWriter.flush();
    } else if (servletOutputStream != null) {
      servletOutputStream.flush();
    }
  }

  public void copyBodyToResponse() throws IOException {
    HttpServletResponse response = (HttpServletResponse) getResponse();
    ServletOutputStream servletOutStream = response.getOutputStream();
    servletOutStream.write(byteArrayOutputStream.toByteArray());
    servletOutStream.flush();
  }
}
