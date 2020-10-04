import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Main {

  public static void main(String... args) {
    Main main = new Main();

    main.infiniteStream();

    // FileInputStream
    main.readByFileInputStream();
    main.readByFileInputStreamToWriteByFileOutputStream();
    main.readByBufferedFileInputStreamToWriteByBufferedFileOutputStream();

    // ZipInputStream
    main.readByZipInputStream();

    // InputStreamReader
    main.readByInputStreamReader();
    main.readByInputStreamReaderToWriteByOutputStreamWriter();

    // FileReader
    main.readByFileReader();

    // StringReader
    main.readByStringReader1();
    main.readByStringReader2();
    main.readByStringReader3();

    // BufferedReader
    main.readByBufferedReader();

    // BufferedReader with Stream
    main.readByBufferedReaderWithStream();
  }

  private void infiniteStream() {
    Stream<Integer> stream = Stream.iterate(10, i -> i * 2);
    stream.limit(5).forEach(System.out::println);
  }

  private void readByFileInputStream() {
    byte[] readData = new byte[1024];
    try (FileInputStream fis = new FileInputStream("./src/japanese.txt")) {
      int readBytes = fis.read(readData, 0, 5); // 第三引数で指定した長さ（バイト数）分読み込む
      System.out.println(readBytes); // 5
      System.out.println(new String(readData)); // 私�  ←日本語を5バイトまで読み込んだことで途中までしか読めなかったので最後の文字は文字化けしている
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByFileInputStreamToWriteByFileOutputStream() {
    byte[] readData = new byte[1024];
    try (
        FileInputStream fis = new FileInputStream("./src/coin.png");
        FileOutputStream fos = new FileOutputStream("./out/written.png")) {
      int readBytes;
      while ((readBytes = fis.read(readData)) != -1) {
        fos.write(readData, 0, readBytes);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByBufferedFileInputStreamToWriteByBufferedFileOutputStream() {
    byte[] readData = new byte[1024];
    try (
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("./src/coin.png"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("./out/written2.png"))) {
      int readBytes;
      while ((readBytes = bis.read(readData)) != -1) {
        bos.write(readData, 0, readBytes);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByZipInputStream() {
    ZipEntry fileZipEntry = new ZipEntry("nozip");
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream("./src/samplezip.zip"))) {
      ZipEntry zipEntry;
      while ((zipEntry = zis.getNextEntry()) != null) {
        if (!zipEntry.isDirectory()) {
          fileZipEntry = zipEntry;
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(fileZipEntry.getName()); // zip-dir/file1.txt

    byte[] buf = new byte[1024];
    try (InputStream is = new ZipFile("./src/samplezip.zip").getInputStream(fileZipEntry)) {
      // InputStreamはzip-dir/file1.txtのファイルに関するストリーム
      int readBytes = is.read(buf, 0, 1024);
      System.out.println(readBytes); // 78
      System.out.println(new String(buf)); // zip-dir/file1.txt のファイルの中身
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByStringReader1() {
    String data = "あいうえお";
    try (StringReader reader = new StringReader(data)) {
      int readChar = reader.read(); // 1文字ずつ読み込む 返り値は読み込んだ文字のコード値を返す
      System.out.println(readChar); // あに対応するコード値12354が表示される
      readChar = reader.read();
      System.out.println(readChar); // いに対応するコード値12356が表示される
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByStringReader2() {
    String data = "あいうえお";
    char[] readData = new char[32];
    try (StringReader reader = new StringReader(data)) {
      int readChars = reader.read(readData); // 全文字を読み込む 返り値は読み込んだ文字数を返す
      System.out.println(readChars); // 5
      System.out.println(new String(readData)); // あいうえお
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByStringReader3() {
    String data = "あいうえお";
    char[] readData = new char[32];
    try (StringReader reader = new StringReader(data)) {
      int readChars = reader.read(readData, 0, 3); // 第三引数で指定した長さ（文字数分）まで読み込む 返り値は読み込んだ文字数を返す
      System.out.println(readChars); // 3
      System.out.println(new String(readData)); // あいう
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByInputStreamReader() {
    char[] readData = new char[32];
    try (InputStreamReader isr = new InputStreamReader(new FileInputStream("./src/japanese.txt"))) {
      int readChars = isr.read(readData, 0, 5); // 第三引数で指定した長さ（文字数）分読み込む
      System.out.println(readChars); // 5
      System.out.println(new String(readData)); // 私は日本人
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByInputStreamReaderToWriteByOutputStreamWriter() {
    char[] readData = new char[32];
    try (
        InputStreamReader isr = new InputStreamReader(new FileInputStream("./src/japanese.txt"));
        OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream("./out/japanese.txt"))
    ) {
      int readChars;
      while ((readChars = isr.read(readData)) != -1) {
        osr.write(readData, 0, readChars);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByFileReader() {
    char[] readData = new char[32];
    try (FileReader reader = new FileReader("./src/japanese.txt")) {
      int readChars = reader.read(readData, 0, 5);
      System.out.println(readChars); // 5
      System.out.println(new String(readData)); // 私は日本人
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByBufferedReader() {
    char[] readData = new char[32];
    try (BufferedReader br = new BufferedReader(new FileReader("./src/japanese.txt"), 4)) {
      // まずバッファに指定されたバッファサイズ分（4文字分）読み込まれる（「私は日本」までバッファに読み込まれる）
      // その後バッファから指定文字数（2文字分）読み込む
      int readChars = br.read(readData, 0, 2);
      System.out.println(readChars); // 2
      System.out.println(new String(readData)); // 私は

      // まずバッファ内に格納されている分（「日本」）まで読み込む
      // 残り文字があればまた指定されたバッファサイズ分（4文字分）バッファに読み込んだ（「人です。」までバッファに読み込む）あと、バッファから残りの文字数分（1文字分）読み込む（「人」まで）
      readChars = br.read(readData, readChars, 3);
      System.out.println(readChars); // 3
      System.out.println(new String(readData)); // 私は日本人
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readByBufferedReaderWithStream() {
    try (
        BufferedReader br = new BufferedReader(new FileReader("./src/sample.txt"));
        Stream<String> lines = br.lines()) { // Streamのため、この段階ではファイルは読み込まれない
      System.out.println(lines.findFirst().orElse("no data")); // 終端操作のfindFirstにより、バッファからファイル1行分だけ読み込まれて終わり（バッファにはどれくらい読み込まれる？）
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
